package prog8.compiler.astprocessing

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.getOrElse
import com.github.michaelbull.result.mapError
import prog8.ast.Program
import prog8.ast.base.FatalAstException
import prog8.ast.expressions.*
import prog8.ast.statements.*
import prog8.code.ast.*
import prog8.code.core.*
import prog8.compiler.builtinFunctionReturnType
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.isRegularFile


/**
 *  Convert 'old' compiler-AST into the 'new' simplified AST with baked types.
 */
class IntermediateAstMaker(private val program: Program, private val errors: IErrorReporter) {
    fun transform(): PtProgram {
        val ptProgram = PtProgram(
            program.name,
            program.memsizer,
            program.encoding
        )

        // note: modules are not represented any longer in this Ast. All blocks have been moved into the top scope.
        for (block in program.allBlocks)
            ptProgram.add(transform(block))

        return ptProgram
    }

    private fun transformStatement(statement: Statement): PtNode {
        return when (statement) {
            is AnonymousScope -> throw FatalAstException("AnonymousScopes should have been flattened")
            is Assignment -> transform(statement)
            is Block -> transform(statement)
            is Break -> throw FatalAstException("break should have been replaced by Goto")
            is Continue -> throw FatalAstException("continue should have been replaced by Goto")
            is BuiltinFunctionCallStatement -> transform(statement)
            is BuiltinFunctionPlaceholder -> throw FatalAstException("BuiltinFunctionPlaceholder should not occur in Ast here")
            is ConditionalBranch -> transform(statement)
            is Directive -> transform(statement)
            is ForLoop -> transform(statement)
            is FunctionCallStatement -> transform(statement)
            is IfElse -> transform(statement)
            is InlineAssembly -> transform(statement)
            is Jump -> transform(statement)
            is Label -> transform(statement)
            is PostIncrDecr -> transform(statement)
            is RepeatLoop -> transform(statement)
            is UnrollLoop -> transform(statement)
            is Return -> transform(statement)
            is Subroutine -> {
                if(statement.isAsmSubroutine)
                    transformAsmSub(statement)
                else
                    transformSub(statement)
            }
            is UntilLoop -> throw FatalAstException("until loops must have been converted to jumps")
            is VarDecl -> transform(statement)
            is When -> transform(statement)
            is WhileLoop -> throw FatalAstException("while loops must have been converted to jumps")
        }
    }

    private fun transformExpression(expr: Expression): PtExpression {
        return when(expr) {
            is AddressOf -> transform(expr)
            is ArrayIndexedExpression -> transform(expr)
            is ArrayLiteral -> transform(expr)
            is BinaryExpression -> transform(expr)
            is BuiltinFunctionCall -> transform(expr)
            is CharLiteral -> throw FatalAstException("char literals should have been converted into bytes")
            is ContainmentCheck -> transform(expr)
            is DirectMemoryRead -> transform(expr)
            is FunctionCallExpression -> transform(expr)
            is IdentifierReference -> transform(expr)
            is NumericLiteral -> transform(expr)
            is PrefixExpression -> transform(expr)
            is RangeExpression -> transform(expr)
            is StringLiteral -> transform(expr)
            is TypecastExpression -> transform(expr)
        }
    }

    private fun transform(srcAssign: Assignment): PtNode {
        if(srcAssign.isAugmentable) {
            val srcExpr = srcAssign.value
            val (operator: String, augmentedValue: Expression?) = when(srcExpr) {
                is BinaryExpression -> {
                    if(srcExpr.left isSameAs srcAssign.target) {
                        val oper = if(srcExpr.operator in ComparisonOperators) srcExpr.operator else srcExpr.operator+'='
                        Pair(oper, srcExpr.right)
                    } else if(srcExpr.right isSameAs srcAssign.target) {
                        val oper = if(srcExpr.operator in ComparisonOperators) srcExpr.operator else srcExpr.operator+'='
                        Pair(oper, srcExpr.left)
                    } else {
                        // either left or right is same as target, other combinations are not supported here
                        Pair("", null)
                    }
                }
                is PrefixExpression -> {
                    require(srcExpr.expression isSameAs srcAssign.target)
                    Pair(srcExpr.operator, srcExpr.expression)
                }
                is TypecastExpression -> {
                    // At this time, there are no special optimized instructions to do an in-place type conversion.
                    // so we simply revert to a regular type converting assignment.
                    // Also, an in-place type cast is very uncommon so probably not worth optimizing anyway.
                    Pair("", null)
                    // the following is what *could* be used here if such instructions *were* available:
//                    if(srcExpr.expression isSameAs srcAssign.target)
//                        Pair("cast", srcExpr.expression)
//                    else {
//                        val subTypeCast = srcExpr.expression as? TypecastExpression
//                        val targetDt = srcAssign.target.inferType(program).getOrElse { DataType.UNDEFINED }
//                        if (subTypeCast!=null && srcExpr.type==targetDt && subTypeCast.expression isSameAs srcAssign.target) {
//                            Pair("cast", subTypeCast)
//                        } else
//                            Pair("", null)
//                    }
                }
                else -> Pair("", null)
            }
            if(augmentedValue!=null) {
                val assign = PtAugmentedAssign(operator, srcAssign.position)
                assign.add(transform(srcAssign.target))
                assign.add(transformExpression(augmentedValue))
                return assign
            }
        }

        val assign = PtAssignment(srcAssign.position)
        assign.add(transform(srcAssign.target))
        assign.add(transformExpression(srcAssign.value))
        return assign
    }

    private fun transform(srcTarget: AssignTarget): PtAssignTarget {
        val target = PtAssignTarget(srcTarget.position)
        if(srcTarget.identifier!=null)
            target.add(transform(srcTarget.identifier!!))
        else if(srcTarget.arrayindexed!=null)
            target.add(transform(srcTarget.arrayindexed!!))
        else if(srcTarget.memoryAddress!=null)
            target.add(transform(srcTarget.memoryAddress!!))
        else
            throw FatalAstException("invalid AssignTarget")
        return target
    }

    private fun transform(identifier: IdentifierReference): PtIdentifier {
        val (target, type) = identifier.targetNameAndType(program)
        return PtIdentifier(target, type, identifier.position)
    }

    private fun transform(srcBlock: Block): PtBlock {
        var alignment = PtBlock.BlockAlignment.NONE
        var forceOutput = false
        var veraFxMuls = false
        var noSymbolPrefixing = false
        val directives = srcBlock.statements.filterIsInstance<Directive>()
        for (directive in directives.filter { it.directive == "%option" }) {
            for (arg in directive.args) {
                when (arg.name) {
                    "align_word" -> alignment = PtBlock.BlockAlignment.WORD
                    "align_page" -> alignment = PtBlock.BlockAlignment.PAGE
                    "no_symbol_prefixing" -> noSymbolPrefixing = true
                    "force_output" -> forceOutput = true
                    "merge", "splitarrays"  -> { /* ignore this one */ }
                    "verafxmuls" -> veraFxMuls = true
                    else -> throw FatalAstException("weird directive option: ${arg.name}")
                }
            }
        }
        val (vardecls, statements) = srcBlock.statements.partition { it is VarDecl }
        val src = srcBlock.definingModule.source
        val block = PtBlock(srcBlock.name, srcBlock.address, srcBlock.isInLibrary, forceOutput, noSymbolPrefixing, veraFxMuls, alignment, src, srcBlock.position)
        makeScopeVarsDecls(vardecls).forEach { block.add(it) }
        for (stmt in statements)
            block.add(transformStatement(stmt))
        return block
    }

    private fun makeScopeVarsDecls(vardecls: Iterable<Statement>): Iterable<PtNamedNode> {
        val decls = mutableListOf<PtNamedNode>()
        vardecls.forEach {
            decls.add(transformStatement(it as VarDecl) as PtNamedNode)
        }
        return decls
    }

    private fun transform(srcNode: BuiltinFunctionCallStatement): PtBuiltinFunctionCall {
        val type = builtinFunctionReturnType(srcNode.name).getOr(DataType.UNDEFINED)
        val noSideFx = BuiltinFunctions.getValue(srcNode.name).pure
        val call = PtBuiltinFunctionCall(srcNode.name, true, noSideFx, type, srcNode.position)
        for (arg in srcNode.args)
            call.add(transformExpression(arg))
        return call
    }

    private fun transform(srcBranch: ConditionalBranch): PtConditionalBranch {
        val branch = PtConditionalBranch(srcBranch.condition, srcBranch.position)
        val trueScope = PtNodeGroup()
        val falseScope = PtNodeGroup()
        for (stmt in srcBranch.truepart.statements)
            trueScope.add(transformStatement(stmt))
        for (stmt in srcBranch.elsepart.statements)
            falseScope.add(transformStatement(stmt))
        branch.add(trueScope)
        branch.add(falseScope)
        return branch
    }

    private fun transform(directive: Directive): PtNode {
        return when(directive.directive) {
            "%breakpoint" -> PtBreakpoint(directive.position)
            "%asmbinary" -> {
                val filename = directive.args[0].str!!
                val offset: UInt? = if(directive.args.size>=2) directive.args[1].int!! else null
                val length: UInt? = if(directive.args.size>=3) directive.args[2].int!! else null
                val abspath = if(File(filename).isFile) {
                    Path(filename).toAbsolutePath()
                } else {
                    val sourcePath = Path(directive.definingModule.source.origin)
                    sourcePath.resolveSibling(filename).toAbsolutePath()
                }
                if(abspath.toFile().isFile)
                    PtIncludeBinary(abspath, offset, length, directive.position)
                else
                    throw FatalAstException("included file doesn't exist")
            }
            "%asminclude" -> {
                val result = loadAsmIncludeFile(directive.args[0].str!!, directive.definingModule.source)
                val assembly = result.getOrElse { throw it }
                PtInlineAssembly(assembly.trimEnd().trimStart('\r', '\n'), false, directive.position)
            }
            else -> {
                // other directives don't output any code (but could end up in option flags somewhere else)
                PtNop(directive.position)
            }
        }
    }

    private fun transform(srcFor: ForLoop): PtForLoop {
        val forloop = PtForLoop(srcFor.position)
        forloop.add(transform(srcFor.loopVar))
        forloop.add(transformExpression(srcFor.iterable))
        val statements = PtNodeGroup()
        for (stmt in srcFor.body.statements)
            statements.add(transformStatement(stmt))
        forloop.add(statements)
        return forloop
    }

    private fun transform(srcCall: FunctionCallStatement): PtFunctionCall {
        val (target, type) = srcCall.target.targetNameAndType(program)
        val call = PtFunctionCall(target,true, type, srcCall.position)
        for (arg in srcCall.args)
            call.add(transformExpression(arg))
        return call
    }

    private fun transform(srcCall: FunctionCallExpression): PtFunctionCall {
        val (target, _) = srcCall.target.targetNameAndType(program)
        val type = srcCall.inferType(program).getOrElse {
            throw FatalAstException("unknown dt $srcCall")
        }
        val isVoid = type==DataType.UNDEFINED
        val call = PtFunctionCall(target, isVoid, type, srcCall.position)
        for (arg in srcCall.args)
            call.add(transformExpression(arg))
        return call
    }

    private fun transform(srcIf: IfElse): PtIfElse {
        val ifelse = PtIfElse(srcIf.position)
        ifelse.add(transformExpression(srcIf.condition))
        val ifScope = PtNodeGroup()
        val elseScope = PtNodeGroup()
        for (stmt in srcIf.truepart.statements)
            ifScope.add(transformStatement(stmt))
        for (stmt in srcIf.elsepart.statements)
            elseScope.add(transformStatement(stmt))
        ifelse.add(ifScope)
        ifelse.add(elseScope)
        return ifelse
    }

    private fun transform(srcNode: InlineAssembly): PtInlineAssembly {
        val assembly = srcNode.assembly.trimEnd().trimStart('\r', '\n')
        return PtInlineAssembly(assembly, srcNode.isIR, srcNode.position)
    }

    private fun transform(srcJump: Jump): PtJump {
        val identifier = if(srcJump.identifier!=null) transform(srcJump.identifier!!) else null
        return PtJump(identifier,
            srcJump.address,
            srcJump.generatedLabel,
            srcJump.position)
    }

    private fun transform(label: Label): PtLabel =
        PtLabel(label.name, label.position)

    private fun transform(src: PostIncrDecr): PtPostIncrDecr {
        val post = PtPostIncrDecr(src.operator, src.position)
        post.add(transform(src.target))
        return post
    }

    private fun transform(srcRepeat: RepeatLoop): PtRepeatLoop {
        if(srcRepeat.iterations==null)
            throw FatalAstException("repeat-forever loop should have been replaced with label+jump")
        val repeat = PtRepeatLoop(srcRepeat.position)
        repeat.add(transformExpression(srcRepeat.iterations!!))
        val scope = PtNodeGroup()
        for (statement in srcRepeat.body.statements) {
            scope.add(transformStatement(statement))
        }
        repeat.add(scope)
        return repeat
    }

    private fun transform(srcUnroll: UnrollLoop): PtNodeGroup {
        val result = PtNodeGroup()
        repeat(srcUnroll.iterations.constValue(program)!!.number.toInt()) {
            srcUnroll.body.statements.forEach {
                result.add(transformStatement(it))
            }
        }
        return result
    }

    private fun transform(srcNode: Return): PtReturn {
        val ret = PtReturn(srcNode.position)
        if(srcNode.value!=null)
            ret.add(transformExpression(srcNode.value!!))
        return ret
    }

    private fun transformAsmSub(srcSub: Subroutine): PtAsmSub {
        val params = srcSub.asmParameterRegisters.zip(srcSub.parameters.map { PtSubroutineParameter(it.name, it.type, it.position) })
        val sub = PtAsmSub(srcSub.name,
            srcSub.asmAddress,
            srcSub.asmClobbers,
            params,
            srcSub.asmReturnvaluesRegisters.zip(srcSub.returntypes),
            srcSub.inline,
            srcSub.position)
        sub.parameters.forEach { it.second.parent=sub }

        if(srcSub.asmAddress==null) {
            var combinedTrueAsm = ""
            var combinedIrAsm = ""
            for (asm in srcSub.statements) {
                asm as InlineAssembly
                if(asm.isIR)
                    combinedIrAsm += asm.assembly.trimEnd() + "\n"
                else
                    combinedTrueAsm += asm.assembly.trimEnd() + "\n"
            }

            if(combinedTrueAsm.isNotEmpty()) {
                combinedTrueAsm = combinedTrueAsm.trimEnd().trimStart('\r', '\n')
                sub.add(PtInlineAssembly(combinedTrueAsm, false, srcSub.statements[0].position))
            }
            if(combinedIrAsm.isNotEmpty()) {
                combinedIrAsm = combinedIrAsm.trimEnd().trimStart('\r', '\n')
                sub.add(PtInlineAssembly(combinedIrAsm, true, srcSub.statements[0].position))
            }
            if(combinedIrAsm.isEmpty() && combinedTrueAsm.isEmpty())
                sub.add(PtInlineAssembly("", true, srcSub.position))
        }

        return sub
    }

    private fun transformSub(srcSub: Subroutine): PtSub {
        val (vardecls, statements) = srcSub.statements.partition { it is VarDecl }
        var returntype = srcSub.returntypes.singleOrNull()
        if(returntype==DataType.STR)
            returntype=DataType.UWORD   // if a sub returns 'str', replace with uword.  Intermediate AST and I.R. don't contain 'str' datatype anymore.

        // do not bother about the 'inline' hint of the source subroutine.
        val sub = PtSub(srcSub.name,
            srcSub.parameters.map { PtSubroutineParameter(it.name, it.type, it.position) },
            returntype,
            srcSub.position)
        sub.parameters.forEach { it.parent=sub }
        makeScopeVarsDecls(vardecls).forEach { sub.add(it) }
        for (statement in statements)
            sub.add(transformStatement(statement))

        return sub
    }

    private fun transform(srcVar: VarDecl): PtNode {
        when(srcVar.type) {
            VarDeclType.VAR -> {
                val value = if(srcVar.value!=null) transformExpression(srcVar.value!!) else null
                if(srcVar.datatype in ArrayDatatypes) {
                    if(value==null) {
                        val blockOptions = srcVar.definingBlock.options()
                        if("align_page" in blockOptions || "align_word" in blockOptions) {
                            errors.warn("converting uninitialized array to explicit zeros because of block alignment option", srcVar.position)
                            val zeros = PtArray(srcVar.datatype, srcVar.position)
                            repeat(srcVar.arraysize!!.constIndex()!!) {
                                zeros.children.add(PtNumber(ArrayToElementTypes.getValue(srcVar.datatype), 0.0, srcVar.position))
                            }
                            return PtVariable(srcVar.name, srcVar.datatype, srcVar.zeropage, zeros, srcVar.arraysize?.constIndex()?.toUInt(), srcVar.position)
                        }
                    }
                }
                return PtVariable(srcVar.name, srcVar.datatype, srcVar.zeropage, value, srcVar.arraysize?.constIndex()?.toUInt(), srcVar.position)
            }
            VarDeclType.CONST -> return PtConstant(srcVar.name, srcVar.datatype, (srcVar.value as NumericLiteral).number, srcVar.position)
            VarDeclType.MEMORY -> return PtMemMapped(srcVar.name, srcVar.datatype, (srcVar.value as NumericLiteral).number.toUInt(), srcVar.arraysize?.constIndex()?.toUInt(), srcVar.position)
        }
    }

    private fun transform(srcWhen: When): PtWhen {
        val w = PtWhen(srcWhen.position)
        w.add(transformExpression(srcWhen.condition))
        val choices = PtNodeGroup()
        for (choice in srcWhen.choices)
            choices.add(transform(choice))
        w.add(choices)
        return w
    }

    private fun transform(srcChoice: WhenChoice): PtWhenChoice {
        val choice = PtWhenChoice(srcChoice.values==null, srcChoice.position)
        val values = PtNodeGroup()
        val statements = PtNodeGroup()
        if(!choice.isElse) {
            for (value in srcChoice.values!!)
                values.add(transformExpression(value))
        }
        for (stmt in srcChoice.statements.statements)
            statements.add(transformStatement(stmt))
        choice.add(values)
        choice.add(statements)
        return choice
    }

    private fun transform(src: AddressOf): PtAddressOf {
        val addr = PtAddressOf(src.position)
        val (name, dt) = src.identifier.targetNameAndType(program)
        if(dt in SplitWordArrayTypes)
            addr.add(PtIdentifier(name+"_lsb", dt, src.identifier.position))        // NOTE: assumes _lsb is first in memory! (immediately followed by _msb)
        else
            addr.add(transform(src.identifier))
        if(src.arrayIndex!=null)
            addr.add(transformExpression(src.arrayIndex!!.indexExpr))
        return addr
    }

    private fun transform(srcArr: ArrayIndexedExpression): PtArrayIndexer {
        val arrayVarType = srcArr.inferType(program).getOrElse { throw FatalAstException("unknown dt") }
        val array = PtArrayIndexer(arrayVarType, srcArr.position)
        array.add(transform(srcArr.arrayvar))
        array.add(transformExpression(srcArr.indexer.indexExpr))
        return array
    }

    private fun transform(srcArr: ArrayLiteral): PtArray {
        val arr = PtArray(srcArr.inferType(program).getOrElse { throw FatalAstException("array must know its type") }, srcArr.position)
        for (elt in srcArr.value)
            arr.add(transformExpression(elt))
        return arr
    }

    private fun transform(srcExpr: BinaryExpression): PtBinaryExpression {
        val type = srcExpr.inferType(program).getOrElse { throw FatalAstException("unknown dt") }
        val actualType = if(type==DataType.BOOL) DataType.UBYTE else type
        val expr = PtBinaryExpression(srcExpr.operator, actualType, srcExpr.position)
        expr.add(transformExpression(srcExpr.left))
        expr.add(transformExpression(srcExpr.right))
        return expr
    }

    private fun transform(srcCall: BuiltinFunctionCall): PtBuiltinFunctionCall {
        val type = srcCall.inferType(program).getOrElse { throw FatalAstException("unknown dt") }
        val noSideFx = BuiltinFunctions.getValue(srcCall.name).pure
        val call = PtBuiltinFunctionCall(srcCall.name, false, noSideFx, type, srcCall.position)
        for (arg in srcCall.args)
            call.add(transformExpression(arg))
        return call
    }

    private fun transform(srcCheck: ContainmentCheck): PtContainmentCheck {
        val check = PtContainmentCheck(srcCheck.position)
        check.add(transformExpression(srcCheck.element))
        if(srcCheck.iterable !is IdentifierReference)
            throw FatalAstException("iterable in containmentcheck must always be an identifier (referencing string or array) $srcCheck")
        val iterable = transformExpression(srcCheck.iterable)
        check.add(iterable)
        return check
    }

    private fun transform(memory: DirectMemoryWrite): PtMemoryByte {
        val mem = PtMemoryByte(memory.position)
        mem.add(transformExpression(memory.addressExpression))
        return mem
    }

    private fun transform(memory: DirectMemoryRead): PtMemoryByte {
        val mem = PtMemoryByte(memory.position)
        mem.add(transformExpression(memory.addressExpression))
        return mem
    }

    private fun transform(number: NumericLiteral): PtNumber =
        PtNumber(number.type, number.number, number.position)

    private fun transform(srcPrefix: PrefixExpression): PtPrefix {
        val type = srcPrefix.inferType(program).getOrElse { throw FatalAstException("unknown dt") }
        val prefix = PtPrefix(srcPrefix.operator, type, srcPrefix.position)
        prefix.add(transformExpression(srcPrefix.expression))
        return prefix
    }

    private fun transform(srcRange: RangeExpression): PtRange {
        val type = srcRange.inferType(program).getOrElse { throw FatalAstException("unknown dt") }
        val range=PtRange(type, srcRange.position)
        range.add(transformExpression(srcRange.from))
        range.add(transformExpression(srcRange.to))
        range.add(transformExpression(srcRange.step) as PtNumber)
        return range
    }

    private fun transform(srcString: StringLiteral): PtString =
        PtString(srcString.value, srcString.encoding, srcString.position)

    private fun transform(srcCast: TypecastExpression): PtTypeCast {
        val cast = PtTypeCast(srcCast.type, srcCast.position)
        cast.add(transformExpression(srcCast.expression))
        require(cast.type!=cast.value.type)
        return cast
    }


    private fun loadAsmIncludeFile(filename: String, source: SourceCode): Result<String, NoSuchFileException> {
        return if (filename.startsWith(SourceCode.LIBRARYFILEPREFIX)) {
            return com.github.michaelbull.result.runCatching {
                SourceCode.Resource("/prog8lib/${filename.substring(SourceCode.LIBRARYFILEPREFIX.length)}").text
            }.mapError { NoSuchFileException(File(filename)) }
        } else {
            val sib = Path(source.origin).resolveSibling(filename)
            if (sib.isRegularFile())
                Ok(SourceCode.File(sib).text)
            else
                Ok(SourceCode.File(Path(filename)).text)
        }
    }

}