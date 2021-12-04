package prog8.compiler.astprocessing

import prog8.ast.Node
import prog8.ast.Program
import prog8.ast.base.DataType
import prog8.ast.expressions.CharLiteral
import prog8.ast.expressions.IdentifierReference
import prog8.ast.expressions.NumericLiteralValue
import prog8.ast.statements.Directive
import prog8.ast.walk.AstWalker
import prog8.ast.walk.IAstModification
import prog8.compiler.BeforeAsmGenerationAstChanger
import prog8.compilerinterface.CompilationOptions
import prog8.compilerinterface.IErrorReporter
import prog8.compilerinterface.IStringEncoding


internal fun Program.checkValid(errors: IErrorReporter, compilerOptions: CompilationOptions) {
    val checker = AstChecker(this, errors, compilerOptions)
    checker.visit(this)
}

internal fun Program.processAstBeforeAsmGeneration(compilerOptions: CompilationOptions, errors: IErrorReporter) {
    val fixer = BeforeAsmGenerationAstChanger(this, compilerOptions, errors)
    fixer.visit(this)
    while(errors.noErrors() && fixer.applyModifications()>0) {
        fixer.visit(this)
    }
}

internal fun Program.reorderStatements(errors: IErrorReporter, options: CompilationOptions) {
    val reorder = StatementReorderer(this, errors, options)
    reorder.visit(this)
    if(errors.noErrors()) {
        reorder.applyModifications()
        reorder.visit(this)
        if(errors.noErrors())
            reorder.applyModifications()
    }
}

internal fun Program.charLiteralsToUByteLiterals(enc: IStringEncoding) {
    val walker = object : AstWalker() {
        override fun after(char: CharLiteral, parent: Node): Iterable<IAstModification> {
            return listOf(IAstModification.ReplaceNode(
                char,
                NumericLiteralValue(DataType.UBYTE, enc.encodeString(char.value.toString(), char.altEncoding)[0].toDouble(), char.position),
                parent
            ))
        }
    }
    walker.visit(this)
    walker.applyModifications()
}

internal fun Program.addTypecasts(errors: IErrorReporter) {
    val caster = TypecastsAdder(this, errors)
    caster.visit(this)
    caster.applyModifications()
}

internal fun Program.verifyFunctionArgTypes() {
    val fixer = VerifyFunctionArgTypes(this)
    fixer.visit(this)
}

internal fun Program.preprocessAst(program: Program) {
    val transforms = AstPreprocessor(program)
    transforms.visit(this)
    var mods = transforms.applyModifications()
    while(mods>0)
        mods = transforms.applyModifications()
}

internal fun Program.checkIdentifiers(errors: IErrorReporter, program: Program, options: CompilationOptions) {

    val checker2 = AstIdentifiersChecker(errors, program, options.compTarget)
    checker2.visit(this)

    if(errors.noErrors()) {
        val transforms = AstVariousTransforms(this)
        transforms.visit(this)
        transforms.applyModifications()
        val lit2decl = LiteralsToAutoVars(this)
        lit2decl.visit(this)
        lit2decl.applyModifications()
    }
}

internal fun Program.variousCleanups(program: Program, errors: IErrorReporter) {
    val process = VariousCleanups(program, errors)
    process.visit(this)
    if(errors.noErrors())
        process.applyModifications()
}

internal fun Program.moveMainAndStartToFirst() {
    // the module containing the program entrypoint is moved to the first in the sequence.
    // the "main" block containing the entrypoint is moved to the top in there,
    // and finally the entrypoint subroutine "start" itself is moved to the top in that block.

    val directives = modules[0].statements.filterIsInstance<Directive>()
    val start = this.entrypoint
    val mod = start.definingModule
    val block = start.definingBlock
    moveModuleToFront(mod)
    mod.remove(block)
    var afterDirective = mod.statements.indexOfFirst { it !is Directive }
    if(afterDirective<0)
        mod.statements.add(block)
    else
        mod.statements.add(afterDirective, block)
    block.remove(start)
    afterDirective = block.statements.indexOfFirst { it !is Directive }
    if(afterDirective<0)
        block.statements.add(start)
    else
        block.statements.add(afterDirective, start)

    // overwrite the directives in the module containing the entrypoint
    for(directive in directives) {
        modules[0].statements.removeAll { it is Directive && it.directive == directive.directive }
        modules[0].statements.add(0, directive)
    }
}

internal fun IdentifierReference.isSubroutineParameter(program: Program): Boolean {
    val vardecl = this.targetVarDecl(program)
    if(vardecl!=null && vardecl.autogeneratedDontRemove) {
        return vardecl.definingSubroutine?.parameters?.any { it.name==vardecl.name } == true
    }
    return false
}
