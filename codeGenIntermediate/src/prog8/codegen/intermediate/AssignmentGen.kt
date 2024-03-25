package prog8.codegen.intermediate

import prog8.code.StRomSub
import prog8.code.StRomSubParameter
import prog8.code.ast.*
import prog8.code.core.*
import prog8.intermediate.*


internal class AssignmentGen(private val codeGen: IRCodeGen, private val expressionEval: ExpressionGen) {

    internal fun translate(assignment: PtAssignment): IRCodeChunks {
        if(assignment.multiTarget) {
            val values = assignment.value as? PtFunctionCall
                ?: throw AssemblyError("only function calls can return multiple values in a multi-assign")

            val sub = codeGen.symbolTable.lookup(values.name) as? StRomSub
                ?: throw AssemblyError("only asmsubs can return multiple values")

            val result = mutableListOf<IRCodeChunkBase>()
            val funcCall = this.expressionEval.translate(values)
            require(funcCall.multipleResultRegs.size + funcCall.multipleResultFpRegs.size >= 2)
            if(funcCall.multipleResultFpRegs.isNotEmpty())
                TODO("deal with (multiple?) FP return registers")

            // because we can only handle integer results right now we can just zip() it all up
            addToResult(result, funcCall, funcCall.resultReg, funcCall.resultFpReg)
            sub.returns.zip(assignment.children).zip(funcCall.multipleResultRegs).forEach {
                val regNumber = it.second
                val returns = it.first.first
                val target = it.first.second as PtAssignTarget
                result += assignCpuRegister(returns, regNumber, target)
            }
            return result
        } else {
            if (assignment.target.children.single() is PtIrRegister)
                throw AssemblyError("assigning to a register should be done by just evaluating the expression into resultregister")

            return translateRegularAssign(assignment)
        }
    }

    private fun assignCpuRegister(returns: StRomSubParameter, regNum: Int, target: PtAssignTarget): IRCodeChunks {
        val result = mutableListOf<IRCodeChunkBase>()
        val loadCpuRegInstr = when(returns.register.registerOrPair) {
            RegisterOrPair.A -> IRInstruction(Opcode.LOADHA, IRDataType.BYTE, reg1=regNum)
            RegisterOrPair.X -> IRInstruction(Opcode.LOADHX, IRDataType.BYTE, reg1=regNum)
            RegisterOrPair.Y -> IRInstruction(Opcode.LOADHY, IRDataType.BYTE, reg1=regNum)
            RegisterOrPair.AX -> IRInstruction(Opcode.LOADHAX, IRDataType.WORD, reg1=regNum)
            RegisterOrPair.AY -> IRInstruction(Opcode.LOADHAY, IRDataType.WORD, reg1=regNum)
            RegisterOrPair.XY -> IRInstruction(Opcode.LOADHXY, IRDataType.WORD, reg1=regNum)
            null -> {
                when(returns.register.statusflag) {
                    Statusflag.Pc -> IRInstruction(Opcode.LOADHA, IRDataType.BYTE, reg1=regNum)
                    else -> throw AssemblyError("weird statusflag as returnvalue")
                }
            }
            else -> throw AssemblyError("cannot load register")
        }
        addInstr(result, loadCpuRegInstr, null)

        // build an assignment to store the value in the actual target.
        val assign = PtAssignment(target.position)
        assign.add(target)
        assign.add(PtIrRegister(regNum, target.type, target.position))
        result += translate(assign)
        return result
    }

    internal fun translate(augAssign: PtAugmentedAssign): IRCodeChunks {
        // augmented assignment always has just a single target
        if(augAssign.target.children.single() is PtIrRegister)
            throw AssemblyError("assigning to a register should be done by just evaluating the expression into resultregister")

        val target = augAssign.target
        val targetDt = irType(target.type)
        val memTarget = target.memory
        val constAddress = (memTarget?.address as? PtNumber)?.number?.toInt()
        val symbol = target.identifier?.name
        val array = target.array
        val value = augAssign.value
        val signed = target.type in SignedDatatypes
        val result = when(augAssign.operator) {
            "+=" -> operatorPlusInplace(symbol, array, constAddress, memTarget, targetDt, value)
            "-=" -> operatorMinusInplace(symbol, array, constAddress, memTarget, targetDt, value)
            "*=" -> operatorMultiplyInplace(symbol, array, constAddress, memTarget, targetDt, value)
            "/=" -> operatorDivideInplace(symbol, array, constAddress, memTarget, targetDt, value, signed)
            "|=" -> operatorOrInplace(symbol, array, constAddress, memTarget, targetDt, value)
            "or=" -> operatorLogicalOrInplace(symbol, array, constAddress, memTarget, targetDt, value)
            "&=" -> operatorAndInplace(symbol, array, constAddress, memTarget, targetDt, value)
            "and=" -> operatorLogicalAndInplace(symbol, array, constAddress, memTarget, targetDt, value)
            "^=", "xor=" -> operatorXorInplace(symbol, array, constAddress, memTarget, targetDt, value)
            "<<=" -> operatorShiftLeftInplace(symbol, array, constAddress, memTarget, targetDt, value)
            ">>=" -> operatorShiftRightInplace(symbol, array, constAddress, memTarget, targetDt, value, signed)
            "%=" -> operatorModuloInplace(symbol, array, constAddress, memTarget, targetDt, value)
            "==" -> operatorEqualsInplace(symbol, array, constAddress, memTarget, targetDt, value)
            "!=" -> operatorNotEqualsInplace(symbol, array, constAddress, memTarget, targetDt, value)
            "<" -> operatorLessInplace(symbol, array, constAddress, memTarget, targetDt, value, signed)
            ">" -> operatorGreaterInplace(symbol, array, constAddress, memTarget, targetDt, value, signed)
            "<=" -> operatorLessEqualInplace(symbol, array, constAddress, memTarget, targetDt, value, signed)
            ">=" -> operatorGreaterEqualInplace(symbol, array, constAddress, memTarget, targetDt, value, signed)
            in PrefixOperators -> inplacePrefix(augAssign.operator, symbol, array, constAddress, memTarget, targetDt)

            else -> throw AssemblyError("invalid augmented assign operator ${augAssign.operator}")
        }

        val chunks = if(result!=null) result else fallbackAssign(augAssign)
        chunks.filterIsInstance<IRCodeChunk>().firstOrNull()?.appendSrcPosition(augAssign.position)
        return chunks
    }

    private fun fallbackAssign(origAssign: PtAugmentedAssign): IRCodeChunks {
        val value: PtExpression
        if(origAssign.operator in PrefixOperators) {
            value = PtPrefix(origAssign.operator, origAssign.value.type, origAssign.value.position)
            value.add(origAssign.value)
        } else {
            val operator = when(origAssign.operator) {
                in ComparisonOperators -> origAssign.operator
                else -> {
                    require(origAssign.operator.endsWith('='))
                    origAssign.operator.dropLast(1)
                }
            }
            value = PtBinaryExpression(operator, origAssign.value.type, origAssign.value.position)
            val left: PtExpression = origAssign.target.children.single() as PtExpression
            value.add(left)
            value.add(origAssign.value)
        }
        val normalAssign = PtAssignment(origAssign.position)
        normalAssign.add(origAssign.target)
        normalAssign.add(value)
        return translateRegularAssign(normalAssign)
    }

    private fun inplacePrefix(operator: String, symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType): IRCodeChunks? {
        if(operator=="+")
            return emptyList()

        if(array!=null)
            return inplacePrefixArray(operator, array)

        val result = mutableListOf<IRCodeChunkBase>()
        if(constAddress==null && memory!=null) {
            val register = codeGen.registers.nextFree()
            val tr = expressionEval.translateExpression(memory.address)
            addToResult(result, tr, tr.resultReg, -1)
            when(operator) {
                "-" -> {
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOADI, vmDt, reg1=register, reg2=tr.resultReg)
                        it += IRInstruction(Opcode.NEG, vmDt, reg1=register)
                        it += IRInstruction(Opcode.STOREI, vmDt, reg1=register, reg2=tr.resultReg)
                    }
                }
                "~" -> {
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOADI, vmDt, reg1=register, reg2=tr.resultReg)
                        it += IRInstruction(Opcode.INV, vmDt, reg1=register)
                        it += IRInstruction(Opcode.STOREI, vmDt, reg1=register, reg2=tr.resultReg)
                    }
                }
            }
        } else {
            when (operator) {
                "-" -> addInstr(result, IRInstruction(Opcode.NEGM, vmDt, address = constAddress, labelSymbol = symbol), null)
                "~" -> addInstr(result, IRInstruction(Opcode.INVM, vmDt, address = constAddress, labelSymbol = symbol), null)
                "not" -> {
                    val regMask = codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=regMask, immediate = 1)
                        it += IRInstruction(Opcode.XORM, vmDt, reg1=regMask, address = constAddress, labelSymbol = symbol)
                    }
                }
                else -> throw AssemblyError("weird prefix operator")
            }
        }
        return result
    }

    private fun inplacePrefixArray(operator: String, array: PtArrayIndexer): IRCodeChunks {
        val eltSize = codeGen.program.memsizer.memorySize(array.type)
        val result = mutableListOf<IRCodeChunkBase>()
        val vmDt = irType(array.type)
        val constIndex = array.index.asConstInteger()

        fun loadIndex(): Int {
            val tr = expressionEval.translateExpression(array.index)
            addToResult(result, tr, tr.resultReg, -1)
            if(!array.splitWords && eltSize>1)
                result += codeGen.multiplyByConst(IRDataType.BYTE, tr.resultReg, eltSize)
            return tr.resultReg
        }

        if(array.splitWords) {
            // handle split LSB/MSB arrays
            when(operator) {
                "+" -> { }
                "-" -> {
                    val skipCarryLabel = codeGen.createLabelName()
                    if(constIndex!=null) {
                        addInstr(result, IRInstruction(Opcode.NEGM, IRDataType.BYTE, labelSymbol = array.variable.name+"_lsb", symbolOffset = constIndex), null)
                        addInstr(result, IRInstruction(Opcode.BSTEQ, labelSymbol = skipCarryLabel), null)
                        addInstr(result, IRInstruction(Opcode.INCM, IRDataType.BYTE, labelSymbol = array.variable.name+"_msb", symbolOffset = constIndex), null)
                        addInstr(result, IRInstruction(Opcode.NEGM, IRDataType.BYTE, labelSymbol = array.variable.name+"_msb", symbolOffset = constIndex), skipCarryLabel)
                    } else {
                        val indexReg = loadIndex()
                        val registerLsb = codeGen.registers.nextFree()
                        val registerMsb = codeGen.registers.nextFree()
                        result += IRCodeChunk(null, null).also {
                            it += IRInstruction(Opcode.LOADX, IRDataType.BYTE, reg1 = registerLsb, reg2 = indexReg, labelSymbol = array.variable.name+"_lsb")
                            it += IRInstruction(Opcode.NEG, IRDataType.BYTE, reg1 = registerLsb)
                            it += IRInstruction(Opcode.STOREX, IRDataType.BYTE, reg1 = registerLsb, reg2 = indexReg, labelSymbol = array.variable.name+"_lsb")
                            it += IRInstruction(Opcode.LOADX, IRDataType.BYTE, reg1 = registerMsb, reg2 = indexReg, labelSymbol = array.variable.name+"_msb")
                            it += IRInstruction(Opcode.NEG, IRDataType.BYTE, reg1 = registerMsb)
                            it += IRInstruction(Opcode.CMPI, IRDataType.BYTE, reg1 = registerLsb, immediate = 0)
                            it += IRInstruction(Opcode.BSTEQ, labelSymbol = skipCarryLabel)
                            it += IRInstruction(Opcode.DEC, IRDataType.BYTE, reg1 = registerMsb)
                        }
                        result += IRCodeChunk(skipCarryLabel, null).also {
                            it += IRInstruction(Opcode.STOREX, IRDataType.BYTE, reg1 = registerMsb, reg2 = indexReg, labelSymbol = array.variable.name+"_msb")
                        }
                    }
                }
                "~" -> {
                    if(constIndex!=null) {
                        addInstr(result, IRInstruction(Opcode.INVM, IRDataType.BYTE, labelSymbol = array.variable.name+"_lsb", symbolOffset = constIndex), null)
                        addInstr(result, IRInstruction(Opcode.INVM, IRDataType.BYTE, labelSymbol = array.variable.name+"_msb", symbolOffset = constIndex), null)
                    } else {
                        val indexReg = loadIndex()
                        val register = codeGen.registers.nextFree()
                        result += IRCodeChunk(null, null).also {
                            it += IRInstruction(Opcode.LOADX, IRDataType.BYTE, reg1 = register, reg2 = indexReg, labelSymbol = array.variable.name+"_lsb")
                            it += IRInstruction(Opcode.INV, IRDataType.BYTE, reg1 = register)
                            it += IRInstruction(Opcode.STOREX, IRDataType.BYTE, reg1 = register, reg2 = indexReg, labelSymbol = array.variable.name+"_lsb")
                            it += IRInstruction(Opcode.LOADX, IRDataType.BYTE, reg1 = register, reg2 = indexReg, labelSymbol = array.variable.name+"_msb")
                            it += IRInstruction(Opcode.INV, IRDataType.BYTE, reg1 = register)
                            it += IRInstruction(Opcode.STOREX, IRDataType.BYTE, reg1 = register, reg2 = indexReg, labelSymbol = array.variable.name+"_msb")
                        }
                    }
                }
                else -> throw AssemblyError("weird prefix operator")
            }
            return result
        }

        // normal array.

        when(operator) {
            "+" -> { }
            "-" -> {
                if(constIndex!=null) {
                    addInstr(result, IRInstruction(Opcode.NEGM, vmDt, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize), null)
                } else {
                    val indexReg = loadIndex()
                    val register = codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOADX, vmDt, reg1 = register, reg2 = indexReg, labelSymbol = array.variable.name)
                        it += IRInstruction(Opcode.NEG, vmDt, reg1 = register)
                        it += IRInstruction(Opcode.STOREX, vmDt, reg1 = register, reg2 = indexReg, labelSymbol = array.variable.name)
                    }
                }
            }
            "~" -> {
                if(constIndex!=null) {
                    addInstr(result, IRInstruction(Opcode.INVM, vmDt, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize), null)
                } else {
                    val indexReg = loadIndex()
                    val register = codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOADX, vmDt, reg1 = register, reg2 = indexReg, labelSymbol = array.variable.name)
                        it += IRInstruction(Opcode.INV, vmDt, reg1 = register)
                        it += IRInstruction(Opcode.STOREX, vmDt, reg1 = register, reg2 = indexReg, labelSymbol = array.variable.name)
                    }
                }
            }
            "not" -> {
                // TODO: in boolean branch, is 'not' handled ok like this?
                val register = codeGen.registers.nextFree()
                if(constIndex!=null) {
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=register, immediate = 1)
                        it += IRInstruction(Opcode.XORM, vmDt, reg1=register, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize)
                    }
                } else {
                    val indexReg = loadIndex()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOADX, vmDt, reg1 = register, reg2 = indexReg, labelSymbol = array.variable.name)
                        it += IRInstruction(Opcode.XOR, vmDt, reg1 = register, immediate = 1)
                        it += IRInstruction(Opcode.STOREX, vmDt, reg1 = register, reg2 = indexReg, labelSymbol = array.variable.name)
                    }
                }
            }
            else -> throw AssemblyError("weird prefix operator")
        }
        return result
    }

    private fun translateRegularAssign(assignment: PtAssignment): IRCodeChunks {
        // note: assigning array and string values is done via an explicit memcopy/stringcopy function call.
        val targetIdent = assignment.target.identifier
        val targetMemory = assignment.target.memory
        val targetArray = assignment.target.array
        val valueDt = irType(assignment.value.type)
        val targetDt = irType(assignment.target.type)
        val result = mutableListOf<IRCodeChunkBase>()

        var valueRegister = -1
        var valueFpRegister = -1
        val zero = codeGen.isZero(assignment.value)
        if(!zero) {
            // calculate the assignment value
            if (valueDt == IRDataType.FLOAT) {
                val tr = expressionEval.translateExpression(assignment.value)
                valueFpRegister = tr.resultFpReg
                addToResult(result, tr, -1, valueFpRegister)
            } else {
                val extendByteToWord = if(targetDt != valueDt) {
                    // usually an error EXCEPT when a byte is assigned to a word.
                    if(targetDt==IRDataType.WORD && valueDt==IRDataType.BYTE)
                        true
                    else
                        throw AssemblyError("assignment value and target dt mismatch")
                } else false
                if (assignment.value is PtIrRegister) {
                    valueRegister = (assignment.value as PtIrRegister).register
                    if(extendByteToWord) {
                        valueRegister = codeGen.registers.nextFree()
                        addInstr(result, IRInstruction(Opcode.EXT, IRDataType.BYTE, reg1=valueRegister, reg2=(assignment.value as PtIrRegister).register), null)
                    }
                } else {
                    val tr = expressionEval.translateExpression(assignment.value)
                    valueRegister = tr.resultReg
                    addToResult(result, tr, valueRegister, -1)
                    if(extendByteToWord) {
                        valueRegister = codeGen.registers.nextFree()
                        val opcode = if(assignment.value.type in SignedDatatypes) Opcode.EXTS else Opcode.EXT
                        addInstr(result, IRInstruction(opcode, IRDataType.BYTE, reg1=valueRegister, reg2=tr.resultReg), null)
                    }
                }
            }
        }

        if(targetIdent!=null) {
            val instruction = if(zero) {
                IRInstruction(Opcode.STOREZM, targetDt, labelSymbol = targetIdent.name)
            } else {
                if (targetDt == IRDataType.FLOAT)
                    IRInstruction(Opcode.STOREM, targetDt, fpReg1 = valueFpRegister, labelSymbol = targetIdent.name)
                else
                    IRInstruction(Opcode.STOREM, targetDt, reg1 = valueRegister, labelSymbol = targetIdent.name)
            }
            result += IRCodeChunk(null, null).also { it += instruction }
            return result
        }
        else if(targetArray!=null) {
            val variable = targetArray.variable.name
            val itemsize = codeGen.program.memsizer.memorySize(targetArray.type)

            val fixedIndex = constIntValue(targetArray.index)
            val arrayLength = codeGen.symbolTable.getLength(targetArray.variable.name)
            if(zero) {
                if(fixedIndex!=null) {
                    val chunk = IRCodeChunk(null, null).also {
                        if(targetArray.splitWords) {
                            it += IRInstruction(Opcode.STOREZM, IRDataType.BYTE, immediate = arrayLength, labelSymbol = "${variable}_lsb", symbolOffset = fixedIndex)
                            it += IRInstruction(Opcode.STOREZM, IRDataType.BYTE, immediate = arrayLength, labelSymbol = "${variable}_msb", symbolOffset = fixedIndex)
                        }
                        else
                            it += IRInstruction(Opcode.STOREZM, targetDt, labelSymbol = variable, symbolOffset = fixedIndex*itemsize)
                    }
                    result += chunk
                } else {
                    val (code, indexReg) = loadIndexReg(targetArray, itemsize)
                    result += code
                    result += IRCodeChunk(null, null).also {
                        if(targetArray.splitWords) {
                            it += IRInstruction(Opcode.STOREZX, IRDataType.BYTE, reg1 = indexReg, immediate = arrayLength, labelSymbol = variable+"_lsb")
                            it += IRInstruction(Opcode.STOREZX, IRDataType.BYTE, reg1 = indexReg, immediate = arrayLength, labelSymbol = variable+"_msb")
                        }
                        else
                            it += IRInstruction(Opcode.STOREZX, targetDt, reg1=indexReg, labelSymbol = variable)
                    }
                }
            } else {
                if(targetDt== IRDataType.FLOAT) {
                    if(fixedIndex!=null) {
                        val offset = fixedIndex*itemsize
                        val chunk = IRCodeChunk(null, null).also {
                            it += IRInstruction(Opcode.STOREM, targetDt, fpReg1 = valueFpRegister, labelSymbol = variable, symbolOffset = offset)
                        }
                        result += chunk
                    } else {
                        val (code, indexReg) = loadIndexReg(targetArray, itemsize)
                        result += code
                        result += IRCodeChunk(null, null).also {
                            it += IRInstruction(Opcode.STOREX, targetDt, reg1 = indexReg, fpReg1 = valueFpRegister, labelSymbol = variable)
                        }
                    }
                } else {
                    if(fixedIndex!=null) {
                        val chunk = IRCodeChunk(null, null).also {
                            if(targetArray.splitWords) {
                                val msbReg = codeGen.registers.nextFree()
                                it += IRInstruction(Opcode.STOREM, IRDataType.BYTE, reg1 = valueRegister, immediate = arrayLength, labelSymbol = "${variable}_lsb", symbolOffset = fixedIndex)
                                it += IRInstruction(Opcode.MSIG, IRDataType.BYTE, reg1 = msbReg, reg2 = valueRegister)
                                it += IRInstruction(Opcode.STOREM, IRDataType.BYTE, reg1 = msbReg, immediate = arrayLength, labelSymbol = "${variable}_msb", symbolOffset = fixedIndex)
                            }
                            else
                                it += IRInstruction(Opcode.STOREM, targetDt, reg1 = valueRegister, labelSymbol = variable, symbolOffset = fixedIndex*itemsize)
                        }
                        result += chunk
                    } else {
                        val (code, indexReg) = loadIndexReg(targetArray, itemsize)
                        result += code
                        result += IRCodeChunk(null, null).also {
                            if(targetArray.splitWords) {
                                val msbReg = codeGen.registers.nextFree()
                                it += IRInstruction(Opcode.STOREX, IRDataType.BYTE, reg1 = valueRegister, reg2=indexReg, immediate = arrayLength, labelSymbol = "${variable}_lsb")
                                it += IRInstruction(Opcode.MSIG, IRDataType.BYTE, reg1 = msbReg, reg2 = valueRegister)
                                it += IRInstruction(Opcode.STOREX, IRDataType.BYTE, reg1 = msbReg, reg2=indexReg, immediate = arrayLength, labelSymbol = "${variable}_msb")
                            }
                            else
                                it += IRInstruction(Opcode.STOREX, targetDt, reg1 = valueRegister, reg2=indexReg, labelSymbol = variable)
                        }
                    }
                }
            }
            return result
        }
        else if(targetMemory!=null) {
            require(targetDt == IRDataType.BYTE) { "must be byte type ${targetMemory.position}"}
            if(zero) {
                if(targetMemory.address is PtNumber) {
                    val chunk = IRCodeChunk(null, null).also { it += IRInstruction(Opcode.STOREZM, targetDt, address = (targetMemory.address as PtNumber).number.toInt()) }
                    result += chunk
                } else {
                    val tr = expressionEval.translateExpression(targetMemory.address)
                    val addressReg = tr.resultReg
                    addToResult(result, tr, tr.resultReg, -1)
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.STOREZI, targetDt, reg1=addressReg)
                    }
                }
            } else {
                val constAddress = targetMemory.address as? PtNumber
                if(constAddress!=null) {
                    addInstr(result, IRInstruction(Opcode.STOREM, targetDt, reg1=valueRegister, address=constAddress.number.toInt()), null)
                    return result
                }
                val ptrWithOffset = targetMemory.address as? PtBinaryExpression
                if(ptrWithOffset!=null && ptrWithOffset.operator=="+" && ptrWithOffset.left is PtIdentifier) {
                    if((ptrWithOffset.right as? PtNumber)?.number?.toInt() in 0..255) {
                        // STOREIX only works with byte index.
                        val ptrName = (ptrWithOffset.left as PtIdentifier).name
                        val offsetReg = codeGen.registers.nextFree()
                        result += IRCodeChunk(null, null).also {
                            it += IRInstruction(Opcode.LOAD, IRDataType.BYTE, reg1=offsetReg, immediate = ptrWithOffset.right.asConstInteger())
                            it += IRInstruction(Opcode.STOREIX, IRDataType.BYTE, reg1=valueRegister, reg2=offsetReg, labelSymbol = ptrName)
                        }
                        return result
                    }
                }
                val offsetTypecast = ptrWithOffset?.right as? PtTypeCast
                if(ptrWithOffset!=null && ptrWithOffset.operator=="+" && ptrWithOffset.left is PtIdentifier
                    && (ptrWithOffset.right.type in ByteDatatypes || offsetTypecast?.value?.type in ByteDatatypes)) {
                    // STOREIX only works with byte index.
                    val tr = if(offsetTypecast?.value?.type in ByteDatatypes)
                        expressionEval.translateExpression(offsetTypecast!!.value)
                    else
                        expressionEval.translateExpression(ptrWithOffset.right)
                    addToResult(result, tr, tr.resultReg, -1)
                    val ptrName = (ptrWithOffset.left as PtIdentifier).name
                    addInstr(result, IRInstruction(Opcode.STOREIX, IRDataType.BYTE, reg1=valueRegister, reg2=tr.resultReg, labelSymbol = ptrName), null)
                    return result
                }
                val tr = expressionEval.translateExpression(targetMemory.address)
                val addressReg = tr.resultReg
                addToResult(result, tr, tr.resultReg, -1)
                addInstr(result, IRInstruction(Opcode.STOREI, targetDt, reg1=valueRegister, reg2=addressReg), null)
                return result
            }

            return result
        }
        else
            throw AssemblyError("weird assigntarget")
    }

    private fun loadIndexReg(array: PtArrayIndexer, itemsize: Int): Pair<IRCodeChunks, Int> {
        // returns the code to load the Index into the register, which is also returned.

        val result = mutableListOf<IRCodeChunkBase>()
        if(itemsize==1 || array.splitWords) {
            val tr = expressionEval.translateExpression(array.index)
            addToResult(result, tr, tr.resultReg, -1)
            return Pair(result, tr.resultReg)
        }

        val mult: PtExpression
        mult = PtBinaryExpression("*", DataType.UBYTE, array.position)
        mult.children += array.index
        mult.children += PtNumber(DataType.UBYTE, itemsize.toDouble(), array.position)
        val tr = expressionEval.translateExpression(mult)
        addToResult(result, tr, tr.resultReg, -1)
        return Pair(result, tr.resultReg)
    }

    private fun operatorAndInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression): IRCodeChunks? {
        if(array!=null) {
            val result = mutableListOf<IRCodeChunkBase>()
            val constIndex = array.index.asConstInteger()
            val constValue = operand.asConstInteger()
            val eltSize = codeGen.program.memsizer.memorySize(array.type)
            if(constIndex!=null && constValue!=null) {
                if(array.splitWords) {
                    val valueRegLsb = codeGen.registers.nextFree()
                    val valueRegMsb = codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueRegLsb, immediate=constValue and 255)
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueRegMsb, immediate=constValue shr 8)
                        it += IRInstruction(Opcode.ANDM, vmDt, reg1=valueRegLsb, labelSymbol = array.variable.name+"_lsb", symbolOffset = constIndex)
                        it += IRInstruction(Opcode.ANDM, vmDt, reg1=valueRegMsb, labelSymbol = array.variable.name+"_msb", symbolOffset = constIndex)
                    }
                } else {
                    val valueReg = codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueReg, immediate=constValue)
                        it += IRInstruction(Opcode.ANDM, vmDt, reg1=valueReg, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize)
                    }
                }
                return result
            }
            return null // TODO("inplace word array &")
        }
        if(constAddress==null && memory!=null)
            return null // TODO("optimized memory in-place &")

        val result = mutableListOf<IRCodeChunkBase>()
        val tr = expressionEval.translateExpression(operand)
        addToResult(result, tr, tr.resultReg, -1)
        addInstr(result, if(constAddress!=null)
            IRInstruction(Opcode.ANDM, vmDt, reg1=tr.resultReg, address = constAddress)
        else
            IRInstruction(Opcode.ANDM, vmDt, reg1=tr.resultReg, labelSymbol = symbol),null)
        return result
    }

    private fun operatorLogicalAndInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression): IRCodeChunks? {
        if(array!=null) {
            val result = mutableListOf<IRCodeChunkBase>()
            val constIndex = array.index.asConstInteger()
            val constValue = operand.asConstInteger()
            val eltSize = codeGen.program.memsizer.memorySize(array.type)
            if(constIndex!=null && constValue!=null) {
                if(array.splitWords) {
                    val valueRegLsb = codeGen.registers.nextFree()
                    val valueRegMsb = codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueRegLsb, immediate=constValue and 255)
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueRegMsb, immediate=constValue shr 8)
                        it += IRInstruction(Opcode.ANDM, vmDt, reg1=valueRegLsb, labelSymbol = array.variable.name+"_lsb", symbolOffset = constIndex)
                        it += IRInstruction(Opcode.ANDM, vmDt, reg1=valueRegMsb, labelSymbol = array.variable.name+"_msb", symbolOffset = constIndex)
                    }
                } else {
                    val valueReg = codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueReg, immediate=constValue)
                        it += IRInstruction(Opcode.ANDM, vmDt, reg1=valueReg, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize)
                    }
                }
                return result
            }
            return null // TODO("inplace word array and")
        }
        if(constAddress==null && memory!=null)
            return null // TODO("optimized memory in-place and")

        val result = mutableListOf<IRCodeChunkBase>()
        val tr = expressionEval.translateExpression(operand)
        if(!operand.isSimple()) {
            // short-circuit  LEFT and RIGHT  -->  if LEFT then RIGHT else LEFT   (== if !LEFT then LEFT else RIGHT)
            val inplaceReg = codeGen.registers.nextFree()
            val shortcutLabel = codeGen.createLabelName()
            result += IRCodeChunk(null, null).also {
                it += if(constAddress!=null)
                    IRInstruction(Opcode.LOADM, vmDt, reg1=inplaceReg, address = constAddress)
                else
                    IRInstruction(Opcode.LOADM, vmDt, reg1=inplaceReg, labelSymbol = symbol)
                it += IRInstruction(Opcode.BSTEQ, labelSymbol = shortcutLabel)
            }
            addToResult(result, tr, tr.resultReg, -1)
            addInstr(result, if(constAddress!=null)
                IRInstruction(Opcode.STOREM, vmDt, reg1=tr.resultReg, address = constAddress)
            else
                IRInstruction(Opcode.STOREM, vmDt, reg1=tr.resultReg, labelSymbol = symbol), null)
            result += IRCodeChunk(shortcutLabel, null)
        } else {
            // normal evaluation, it is *likely* shorter and faster because of the simple operands.
            addToResult(result, tr, tr.resultReg, -1)
            addInstr(result, if(constAddress!=null)
                IRInstruction(Opcode.ANDM, vmDt, reg1=tr.resultReg, address = constAddress)
            else
                IRInstruction(Opcode.ANDM, vmDt, reg1=tr.resultReg, labelSymbol = symbol),null)
        }
        return result
    }

    private fun operatorOrInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression): IRCodeChunks? {
        if(array!=null) {
            val result = mutableListOf<IRCodeChunkBase>()
            val constIndex = array.index.asConstInteger()
            val constValue = operand.asConstInteger()
            val eltSize = codeGen.program.memsizer.memorySize(array.type)
            if(constIndex!=null && constValue!=null) {
                if(array.splitWords) {
                    val valueRegLsb = codeGen.registers.nextFree()
                    val valueRegMsb = codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueRegLsb, immediate=constValue and 255)
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueRegMsb, immediate=constValue shr 8)
                        it += IRInstruction(Opcode.ORM, vmDt, reg1=valueRegLsb, labelSymbol = array.variable.name+"_lsb", symbolOffset = constIndex)
                        it += IRInstruction(Opcode.ORM, vmDt, reg1=valueRegMsb, labelSymbol = array.variable.name+"_msb", symbolOffset = constIndex)
                    }
                } else {
                    val valueReg = codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueReg, immediate=constValue)
                        it += IRInstruction(Opcode.ORM, vmDt, reg1=valueReg, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize)
                    }
                }
                return result
            }
            return null // TODO("inplace word array |")
        }
        if(constAddress==null && memory!=null)
            return null // TODO("optimized memory in-place |")

        val result = mutableListOf<IRCodeChunkBase>()
        val tr = expressionEval.translateExpression(operand)
        addToResult(result, tr, tr.resultReg, -1)
        addInstr(result, if(constAddress!=null)
            IRInstruction(Opcode.ORM, vmDt, reg1=tr.resultReg, address = constAddress)
        else
            IRInstruction(Opcode.ORM, vmDt, reg1=tr.resultReg, labelSymbol = symbol), null)
        return result
    }

    private fun operatorLogicalOrInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression): IRCodeChunks? {
        if(array!=null) {
            val result = mutableListOf<IRCodeChunkBase>()
            val constIndex = array.index.asConstInteger()
            val constValue = operand.asConstInteger()
            val eltSize = codeGen.program.memsizer.memorySize(array.type)
            if(constIndex!=null && constValue!=null) {
                if(array.splitWords) {
                    val valueRegLsb = codeGen.registers.nextFree()
                    val valueRegMsb = codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueRegLsb, immediate=constValue and 255)
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueRegMsb, immediate=constValue shr 8)
                        it += IRInstruction(Opcode.ORM, vmDt, reg1=valueRegLsb, labelSymbol = array.variable.name+"_lsb", symbolOffset = constIndex)
                        it += IRInstruction(Opcode.ORM, vmDt, reg1=valueRegMsb, labelSymbol = array.variable.name+"_msb", symbolOffset = constIndex)
                    }
                } else {
                    val valueReg = codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueReg, immediate=constValue)
                        it += IRInstruction(Opcode.ORM, vmDt, reg1=valueReg, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize)
                    }
                }
                return result
            }
            return null  // TODO("inplace word array or")
        }
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place or"")

        val result = mutableListOf<IRCodeChunkBase>()
        val tr = expressionEval.translateExpression(operand)
        if(!operand.isSimple()) {
            // short-circuit  LEFT or RIGHT  -->  if LEFT then LEFT else RIGHT
            val inplaceReg = codeGen.registers.nextFree()
            val shortcutLabel = codeGen.createLabelName()
            result += IRCodeChunk(null, null).also {
                it += if(constAddress!=null)
                    IRInstruction(Opcode.LOADM, vmDt, reg1=inplaceReg, address = constAddress)
                else
                    IRInstruction(Opcode.LOADM, vmDt, reg1=inplaceReg, labelSymbol = symbol)
                it += IRInstruction(Opcode.BSTNE, labelSymbol = shortcutLabel)
            }
            addToResult(result, tr, tr.resultReg, -1)
            addInstr(result, if(constAddress!=null)
                IRInstruction(Opcode.STOREM, vmDt, reg1=tr.resultReg, address = constAddress)
            else
                IRInstruction(Opcode.STOREM, vmDt, reg1=tr.resultReg, labelSymbol = symbol), null)
            result += IRCodeChunk(shortcutLabel, null)
        } else {
            // normal evaluation, it is *likely* shorter and faster because of the simple operands.
            addToResult(result, tr, tr.resultReg, -1)
            addInstr(result, if(constAddress!=null)
                IRInstruction(Opcode.ORM, vmDt, reg1=tr.resultReg, address = constAddress)
            else
                IRInstruction(Opcode.ORM, vmDt, reg1=tr.resultReg, labelSymbol = symbol), null)
        }
        return result
    }

    private fun operatorDivideInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression, signed: Boolean): IRCodeChunks? {
        if(array!=null) {
            TODO("/ in array")
        }
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place /"")

        val result = mutableListOf<IRCodeChunkBase>()
        val constFactorRight = operand as? PtNumber
        if(vmDt==IRDataType.FLOAT) {
            if(constFactorRight!=null && constFactorRight.type!=DataType.FLOAT) {
                val factor = constFactorRight.number
                result += codeGen.divideByConstFloatInplace(constAddress, symbol, factor)
            } else {
                val tr = expressionEval.translateExpression(operand)
                addToResult(result, tr, -1, tr.resultFpReg)
                val ins = if(signed) {
                    if(constAddress!=null)
                        IRInstruction(Opcode.DIVSM, vmDt, fpReg1 = tr.resultFpReg, address = constAddress)
                    else
                        IRInstruction(Opcode.DIVSM, vmDt, fpReg1 = tr.resultFpReg, labelSymbol = symbol)
                }
                else {
                    if(constAddress!=null)
                        IRInstruction(Opcode.DIVM, vmDt, fpReg1 = tr.resultFpReg, address = constAddress)
                    else
                        IRInstruction(Opcode.DIVM, vmDt, fpReg1 = tr.resultFpReg, labelSymbol = symbol)
                }
                addInstr(result, ins, null)
            }
        } else {
            if(constFactorRight!=null && constFactorRight.type!=DataType.FLOAT) {
                val factor = constFactorRight.number.toInt()
                result += codeGen.divideByConstInplace(vmDt, constAddress, symbol, factor, signed)
            } else {
                val tr = expressionEval.translateExpression(operand)
                addToResult(result, tr, tr.resultReg, -1)
                val ins = if(signed) {
                    if(constAddress!=null)
                        IRInstruction(Opcode.DIVSM, vmDt, reg1 = tr.resultReg, address = constAddress)
                    else
                        IRInstruction(Opcode.DIVSM, vmDt, reg1 = tr.resultReg, labelSymbol = symbol)
                }
                else {
                    if(constAddress!=null)
                        IRInstruction(Opcode.DIVM, vmDt, reg1 = tr.resultReg, address = constAddress)
                    else
                        IRInstruction(Opcode.DIVM, vmDt, reg1 = tr.resultReg, labelSymbol = symbol)
                }
                addInstr(result, ins, null)
            }
        }
        return result
    }

    private fun operatorMultiplyInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression): IRCodeChunks? {
        if(array!=null) {
            val eltSize = codeGen.program.memsizer.memorySize(array.type)
            val result = mutableListOf<IRCodeChunkBase>()
            if(array.splitWords)
                return operatorMultiplyInplaceSplitArray(array, operand)
            val eltDt = irType(array.type)
            val constIndex = array.index.asConstInteger()
            val constValue = operand.asConstInteger()
            if(constIndex!=null && constValue!=null) {
                if(constValue!=1) {
                    val valueReg=codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, eltDt, reg1=valueReg, immediate = constValue)
                        it += IRInstruction(Opcode.MULM, eltDt, reg1=valueReg, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize)
                    }
                }
                return result
            }
            return null  // TODO("inplace array * non-const")
        }
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place *"")

        val result = mutableListOf<IRCodeChunkBase>()
        val constFactorRight = operand as? PtNumber
        if(vmDt==IRDataType.FLOAT) {
            if(constFactorRight!=null) {
                val factor = constFactorRight.number
                result += codeGen.multiplyByConstFloatInplace(constAddress, symbol, factor)
            } else {
                val tr = expressionEval.translateExpression(operand)
                addToResult(result, tr, -1, tr.resultFpReg)
                addInstr(result, if(constAddress!=null)
                    IRInstruction(Opcode.MULM, vmDt, fpReg1 = tr.resultFpReg, address = constAddress)
                else
                    IRInstruction(Opcode.MULM, vmDt, fpReg1 = tr.resultFpReg, labelSymbol = symbol)
                    , null)
            }
        } else {
            if(constFactorRight!=null && constFactorRight.type!=DataType.FLOAT) {
                val factor = constFactorRight.number.toInt()
                result += codeGen.multiplyByConstInplace(vmDt, constAddress, symbol, factor)
            } else {
                val tr = expressionEval.translateExpression(operand)
                addToResult(result, tr, tr.resultReg, -1)
                addInstr(result, if(constAddress!=null)
                    IRInstruction(Opcode.MULM, vmDt, reg1=tr.resultReg, address = constAddress)
                else
                    IRInstruction(Opcode.MULM, vmDt, reg1=tr.resultReg, labelSymbol = symbol)
                    , null)
            }
        }
        return result
    }

    private fun operatorMinusInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression): IRCodeChunks? {
        if(array!=null) {
            val eltSize = codeGen.program.memsizer.memorySize(array.type)
            val result = mutableListOf<IRCodeChunkBase>()
            if(array.splitWords)
                return operatorMinusInplaceSplitArray(array, operand)
            val eltDt = irType(array.type)
            val constIndex = array.index.asConstInteger()
            val constValue = operand.asConstInteger()
            if(constIndex!=null && constValue!=null) {
                if(constValue==1) {
                    addInstr(result, IRInstruction(Opcode.DECM, eltDt, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize), null)
                } else {
                    val valueReg=codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, eltDt, reg1=valueReg, immediate = constValue)
                        it += IRInstruction(Opcode.SUBM, eltDt, reg1=valueReg, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize)
                    }
                }
                return result
            }
            return null  // TODO("inplace array -")
        }
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place -"")

        val result = mutableListOf<IRCodeChunkBase>()
        if(vmDt==IRDataType.FLOAT) {
            if((operand as? PtNumber)?.number==1.0) {
                addInstr(result, if(constAddress!=null)
                    IRInstruction(Opcode.DECM, vmDt, address = constAddress)
                else
                    IRInstruction(Opcode.DECM, vmDt, labelSymbol = symbol)
                    , null)
            }
            else {
                val tr = expressionEval.translateExpression(operand)
                addToResult(result, tr, -1, tr.resultFpReg)
                addInstr(result, if(constAddress!=null)
                    IRInstruction(Opcode.SUBM, vmDt, fpReg1=tr.resultFpReg, address = constAddress)
                else
                    IRInstruction(Opcode.SUBM, vmDt, fpReg1=tr.resultFpReg, labelSymbol = symbol)
                    , null)
            }
        } else {
            if((operand as? PtNumber)?.number==1.0) {
                addInstr(result, if(constAddress!=null)
                    IRInstruction(Opcode.DECM, vmDt, address = constAddress)
                else
                    IRInstruction(Opcode.DECM, vmDt, labelSymbol = symbol)
                    , null)
            }
            else {
                val tr = expressionEval.translateExpression(operand)
                addToResult(result, tr, tr.resultReg, -1)
                addInstr(result, if(constAddress!=null)
                    IRInstruction(Opcode.SUBM, vmDt, reg1=tr.resultReg, address = constAddress)
                else
                    IRInstruction(Opcode.SUBM, vmDt, reg1=tr.resultReg, labelSymbol = symbol)
                    , null)
            }
        }
        return result
    }

    private fun operatorMultiplyInplaceSplitArray(array: PtArrayIndexer, operand: PtExpression): IRCodeChunks? {
        return null //    TODO("inplace split word array *")
    }

    private fun operatorMinusInplaceSplitArray(array: PtArrayIndexer, operand: PtExpression): IRCodeChunks? {
        val result = mutableListOf<IRCodeChunkBase>()
        val constIndex = array.index.asConstInteger()
        val constValue = operand.asConstInteger()
        if(constIndex!=null) {
            val skip = codeGen.createLabelName()
            if(constValue==1) {
                val lsbReg = codeGen.registers.nextFree()
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOAD, IRDataType.BYTE, reg1 = lsbReg, labelSymbol = array.variable.name+"_lsb", symbolOffset = constIndex)
                    it += IRInstruction(Opcode.BSTNE, labelSymbol = skip)
                    it += IRInstruction(Opcode.DECM, IRDataType.BYTE, labelSymbol = array.variable.name+"_msb", symbolOffset = constIndex)
                }
                result += IRCodeChunk(skip, null).also {
                    it += IRInstruction(Opcode.DECM, IRDataType.BYTE, labelSymbol = array.variable.name+"_lsb", symbolOffset = constIndex)
                }
                return result
            } else {
                return null  // TODO("inplace split word array +")
            }
        }
        return null  // TODO("inplace split word array +")
    }

    private fun operatorPlusInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression): IRCodeChunks? {
        if(array!=null) {
            val result = mutableListOf<IRCodeChunkBase>()
            if(array.splitWords)
                return operatorPlusInplaceSplitArray(array, operand)
            val eltSize = codeGen.program.memsizer.memorySize(array.type)
            val elementDt = irType(array.type)
            val constIndex = array.index.asConstInteger()
            val constValue = operand.asConstInteger()
            if(constIndex!=null && constValue!=null) {
                if(constValue==1) {
                    addInstr(result, IRInstruction(Opcode.INCM, elementDt, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize), null)
                } else {
                    val valueReg=codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, elementDt, reg1=valueReg, immediate = constValue)
                        it += IRInstruction(Opcode.ADDM, elementDt, reg1=valueReg, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize)
                    }
                }
                return result
            }
            return null  // TODO("inplace array +")
        }
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place +"")

        val result = mutableListOf<IRCodeChunkBase>()
        if(vmDt==IRDataType.FLOAT) {
            if((operand as? PtNumber)?.number==1.0) {
                addInstr(result, if(constAddress!=null)
                    IRInstruction(Opcode.INCM, vmDt, address = constAddress)
                else
                    IRInstruction(Opcode.INCM, vmDt, labelSymbol = symbol)
                    , null)
            }
            else {
                val tr = expressionEval.translateExpression(operand)
                addToResult(result, tr, -1, tr.resultFpReg)
                addInstr(result, if(constAddress!=null)
                    IRInstruction(Opcode.ADDM, vmDt, fpReg1=tr.resultFpReg, address = constAddress)
                else
                    IRInstruction(Opcode.ADDM, vmDt, fpReg1=tr.resultFpReg, labelSymbol = symbol)
                    , null)
            }
        } else {
            if((operand as? PtNumber)?.number==1.0) {
                addInstr(result, if(constAddress!=null)
                    IRInstruction(Opcode.INCM, vmDt, address = constAddress)
                else
                    IRInstruction(Opcode.INCM, vmDt, labelSymbol = symbol)
                    , null)
            }
            else {
                val tr = expressionEval.translateExpression(operand)
                addToResult(result, tr, tr.resultReg, -1)
                addInstr(result, if(constAddress!=null)
                    IRInstruction(Opcode.ADDM, vmDt, reg1=tr.resultReg, address = constAddress)
                else
                    IRInstruction(Opcode.ADDM, vmDt, reg1=tr.resultReg, labelSymbol = symbol)
                    , null)
            }
        }
        return result
    }

    private fun operatorPlusInplaceSplitArray(array: PtArrayIndexer, operand: PtExpression): IRCodeChunks? {
        val result = mutableListOf<IRCodeChunkBase>()
        val constIndex = array.index.asConstInteger()
        val constValue = operand.asConstInteger()
        if(constIndex!=null) {
            val skip = codeGen.createLabelName()
            if(constValue==1) {
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.INCM, IRDataType.BYTE, labelSymbol = array.variable.name+"_lsb", symbolOffset = constIndex)
                    it += IRInstruction(Opcode.BSTNE, labelSymbol = skip)
                    it += IRInstruction(Opcode.INCM, IRDataType.BYTE, labelSymbol = array.variable.name+"_msb", symbolOffset = constIndex)
                }
                result += IRCodeChunk(skip, null)
                return result
            } else {
                return null  // TODO("inplace split word array +")
            }
        }
        return null  // TODO("inplace split word array +")
    }

    private fun operatorShiftRightInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression, signed: Boolean): IRCodeChunks? {
        if(array!=null) {
            TODO(">> in array")
        }
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place >>"")

        val result = mutableListOf<IRCodeChunkBase>()
        if(codeGen.isOne(operand)) {
            val opc = if (signed) Opcode.ASRM else Opcode.LSRM
            val ins = if(constAddress!=null)
                IRInstruction(opc, vmDt, address = constAddress)
            else
                IRInstruction(opc, vmDt, labelSymbol = symbol)
            addInstr(result, ins, null)
        } else {
            val tr = expressionEval.translateExpression(operand)
            addToResult(result, tr, tr.resultReg, -1)
            val opc = if (signed) Opcode.ASRNM else Opcode.LSRNM
            val ins = if(constAddress!=null)
                IRInstruction(opc, vmDt, reg1 = tr.resultReg, address = constAddress)
            else
                IRInstruction(opc, vmDt, reg1 = tr.resultReg, labelSymbol = symbol)
            addInstr(result, ins, null)
        }
        return result
    }

    private fun operatorShiftLeftInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression): IRCodeChunks? {
        if(array!=null) {
            TODO("<< in array")
        }
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place <<"")

        val result = mutableListOf<IRCodeChunkBase>()
        if(codeGen.isOne(operand)){
            addInstr(result, if(constAddress!=null)
                IRInstruction(Opcode.LSLM, vmDt, address = constAddress)
            else
                IRInstruction(Opcode.LSLM, vmDt, labelSymbol = symbol)
                , null)
        } else {
            val tr = expressionEval.translateExpression(operand)
            addToResult(result, tr, tr.resultReg, -1)
            addInstr(result, if(constAddress!=null)
                IRInstruction(Opcode.LSLNM, vmDt, reg1=tr.resultReg, address = constAddress)
            else
                IRInstruction(Opcode.LSLNM, vmDt, reg1=tr.resultReg, labelSymbol = symbol)
                ,null)
        }
        return result
    }

    private fun operatorXorInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression): IRCodeChunks? {
        if(array!=null) {
            val result = mutableListOf<IRCodeChunkBase>()
            val constIndex = array.index.asConstInteger()
            val constValue = operand.asConstInteger()
            val eltSize = codeGen.program.memsizer.memorySize(array.type)
            if(constIndex!=null && constValue!=null) {
                if(array.splitWords) {
                    val valueRegLsb = codeGen.registers.nextFree()
                    val valueRegMsb = codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueRegLsb, immediate=constValue and 255)
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueRegMsb, immediate=constValue shr 8)
                        it += IRInstruction(Opcode.XORM, vmDt, reg1=valueRegLsb, labelSymbol = array.variable.name+"_lsb", symbolOffset = constIndex)
                        it += IRInstruction(Opcode.XORM, vmDt, reg1=valueRegMsb, labelSymbol = array.variable.name+"_msb", symbolOffset = constIndex)
                    }
                } else {
                    val valueReg = codeGen.registers.nextFree()
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOAD, vmDt, reg1=valueReg, immediate=constValue)
                        it += IRInstruction(Opcode.XORM, vmDt, reg1=valueReg, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize)
                    }
                }
                return result
            }
            return null  // TODO("inplace word array xor")
        }
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place xor"")

        val result = mutableListOf<IRCodeChunkBase>()
        val tr = expressionEval.translateExpression(operand)
        addToResult(result, tr, tr.resultReg, -1)
        addInstr(result, if(constAddress!=null)
            IRInstruction(Opcode.XORM, vmDt, reg1=tr.resultReg, address = constAddress)
        else
            IRInstruction(Opcode.XORM, vmDt, reg1=tr.resultReg, labelSymbol = symbol)
            ,null)
        return result
    }

    private fun operatorModuloInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression): IRCodeChunks? {
        if(array!=null) {
            TODO("% in array")
        }
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place %"")

        val result = mutableListOf<IRCodeChunkBase>()
        val resultReg = codeGen.registers.nextFree()
        if(operand is PtNumber) {
            val number = operand.number.toInt()
            if (constAddress != null) {
                // @(address) = @(address) %= operand
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOADM, vmDt, reg1 = resultReg, address = constAddress)
                    it += IRInstruction(Opcode.MOD, vmDt, reg1 = resultReg, immediate = number)
                    it += IRInstruction(Opcode.STOREM, vmDt, reg1 = resultReg, address = constAddress)
                }
            } else {
                // symbol = symbol %= operand
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOADM, vmDt, reg1 = resultReg, labelSymbol = symbol)
                    it += IRInstruction(Opcode.MOD, vmDt, reg1 = resultReg, immediate = number)
                    it += IRInstruction(Opcode.STOREM, vmDt, reg1 = resultReg, labelSymbol = symbol)
                }
            }
        } else {
            val tr = expressionEval.translateExpression(operand)
            result += tr.chunks
            if (constAddress != null) {
                // @(address) = @(address) %= operand
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOADM, vmDt, reg1 = resultReg, address = constAddress)
                    it += IRInstruction(Opcode.MODR, vmDt, reg1 = resultReg, reg2 = tr.resultReg)
                    it += IRInstruction(Opcode.STOREM, vmDt, reg1 = resultReg, address = constAddress)
                }
            } else {
                // symbol = symbol %= operand
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOADM, vmDt, reg1 = resultReg, labelSymbol = symbol)
                    it += IRInstruction(Opcode.MODR, vmDt, reg1 = resultReg, reg2 = tr.resultReg)
                    it += IRInstruction(Opcode.STOREM, vmDt, reg1 = resultReg, labelSymbol = symbol)
                }
            }
        }
        return result
    }

    private fun operatorEqualsInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression): IRCodeChunks? {
        if(array!=null)
            return createInplaceArrayComparison(array, operand, "==")
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place compare"")

        val chunks = if(vmDt==IRDataType.FLOAT) {
            createInplaceFloatComparison(constAddress, symbol, operand, Opcode.SEQ)
        } else {
            createInplaceComparison(constAddress, symbol, vmDt, operand, Opcode.SEQ)
        }
        return chunks
    }

    private fun operatorNotEqualsInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression): IRCodeChunks? {
        if(array!=null)
            return createInplaceArrayComparison(array, operand, "!=")
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place compare"")

        val chunks = if(vmDt==IRDataType.FLOAT) {
            createInplaceFloatComparison(constAddress, symbol, operand, Opcode.SNE)
        } else {
            createInplaceComparison(constAddress, symbol, vmDt, operand, Opcode.SNE)
        }
        return chunks
    }

    private fun operatorGreaterInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression, signed: Boolean): IRCodeChunks? {
        if(array!=null)
            return createInplaceArrayComparison(array, operand, ">")
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place compare"")

        val opcode = if(signed) Opcode.SGTS else Opcode.SGT
        val chunks = if(vmDt==IRDataType.FLOAT) {
            createInplaceFloatComparison(constAddress, symbol, operand, opcode)
        } else {
            createInplaceComparison(constAddress, symbol, vmDt, operand, opcode)
        }
        return chunks
    }

    private fun operatorLessInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression, signed: Boolean): IRCodeChunks? {
        if(array!=null)
            return createInplaceArrayComparison(array, operand, "<")
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place compare"")

        val opcode = if(signed) Opcode.SLTS else Opcode.SLT
        val chunks = if(vmDt==IRDataType.FLOAT) {
            createInplaceFloatComparison(constAddress, symbol, operand, opcode)
        } else {
            createInplaceComparison(constAddress, symbol, vmDt, operand, opcode)
        }
        return chunks
    }

    private fun operatorGreaterEqualInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression, signed: Boolean): IRCodeChunks? {
        if(array!=null)
            return createInplaceArrayComparison(array, operand, ">=")
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place compare"")

        val opcode = if(signed) Opcode.SGES else Opcode.SGE
        val chunks = if(vmDt==IRDataType.FLOAT) {
            createInplaceFloatComparison(constAddress, symbol, operand, opcode)
        } else {
            createInplaceComparison(constAddress, symbol, vmDt, operand, opcode)
        }
        return chunks
    }

    private fun operatorLessEqualInplace(symbol: String?, array: PtArrayIndexer?, constAddress: Int?, memory: PtMemoryByte?, vmDt: IRDataType, operand: PtExpression, signed: Boolean): IRCodeChunks? {
        if(array!=null)
            return createInplaceArrayComparison(array, operand, "<=")
        if(constAddress==null && memory!=null)
            return null  // TODO("optimized memory in-place compare"")

        val opcode = if(signed) Opcode.SLES else Opcode.SLE
        val chunks = if(vmDt==IRDataType.FLOAT) {
            createInplaceFloatComparison(constAddress, symbol, operand, opcode)
        } else {
            createInplaceComparison(constAddress, symbol, vmDt, operand, opcode)
        }
        return chunks
    }

    private fun createInplaceComparison(
        constAddress: Int?,
        symbol: String?,
        vmDt: IRDataType,
        operand: PtExpression,
        compareAndSetOpcode: Opcode
    ): MutableList<IRCodeChunkBase> {
        val result = mutableListOf<IRCodeChunkBase>()
        val valueReg = codeGen.registers.nextFree()
        val cmpResultReg = codeGen.registers.nextFree()
        if(operand is PtNumber || operand is PtBool) {

            if(operand is PtNumber && operand.number==0.0 && compareAndSetOpcode in arrayOf(Opcode.SEQ, Opcode.SNE)) {
                // ==0 or !=0 optimized case
                val compareAndSetOpcodeZero = if(compareAndSetOpcode==Opcode.SEQ) Opcode.SZ else Opcode.SNZ
                if (constAddress != null) {
                    // in-place modify a memory location
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOADM, vmDt, reg1 = valueReg, address = constAddress)
                        it += IRInstruction(compareAndSetOpcodeZero, vmDt, reg1 = cmpResultReg, reg2 = valueReg)
                        it += IRInstruction(Opcode.STOREM, vmDt, reg1 = cmpResultReg, address = constAddress)
                    }
                } else {
                    // in-place modify a symbol (variable)
                    result += IRCodeChunk(null, null).also {
                        it += IRInstruction(Opcode.LOADM, vmDt, reg1 = valueReg, labelSymbol = symbol)
                        it += IRInstruction(compareAndSetOpcodeZero, vmDt, reg1=cmpResultReg, reg2 = valueReg)
                        it += IRInstruction(Opcode.STOREM, vmDt, reg1 = cmpResultReg, labelSymbol = symbol)
                    }
                }
                return result
            }

            // compare against number that is not 0
            val numberReg = codeGen.registers.nextFree()
            val value = if(operand is PtNumber) operand.number.toInt() else if(operand is PtBool) operand.asInt() else throw AssemblyError("wrong operand type")
            if (constAddress != null) {
                // in-place modify a memory location
                val innervalue = if(operand is PtNumber) operand.number.toInt() else if(operand is PtBool) operand.asInt() else throw AssemblyError("wrong operand type")
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOADM, vmDt, reg1 = valueReg, address = constAddress)
                    it += IRInstruction(Opcode.LOAD, vmDt, reg1=numberReg, immediate = innervalue)
                    it += IRInstruction(compareAndSetOpcode, vmDt, reg1 = cmpResultReg, reg2 = valueReg, reg3 = numberReg)
                    it += IRInstruction(Opcode.STOREM, vmDt, reg1 = cmpResultReg, address = constAddress)
                }
            } else {
                // in-place modify a symbol (variable)
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOADM, vmDt, reg1 = valueReg, labelSymbol = symbol)
                    it += IRInstruction(Opcode.LOAD, vmDt, reg1=numberReg, immediate = value)
                    it += IRInstruction(compareAndSetOpcode, vmDt, reg1=cmpResultReg, reg2 = valueReg, reg3 = numberReg)
                    it += IRInstruction(Opcode.STOREM, vmDt, reg1 = cmpResultReg, labelSymbol = symbol)
                }
            }
        } else {
            val tr = expressionEval.translateExpression(operand)
            addToResult(result, tr, tr.resultReg, -1)
            if (constAddress != null) {
                // in-place modify a memory location
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOADM, vmDt, reg1 = valueReg, address = constAddress)
                    it += IRInstruction(compareAndSetOpcode, vmDt, reg1=cmpResultReg, reg2 = valueReg, reg3 = tr.resultReg)
                    it += IRInstruction(Opcode.STOREM, vmDt, reg1 = cmpResultReg, address = constAddress)
                }
            } else {
                // in-place modify a symbol (variable)
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOADM, vmDt, reg1 = valueReg, labelSymbol = symbol)
                    it += IRInstruction(compareAndSetOpcode, vmDt, reg1=cmpResultReg, reg2 = valueReg, reg3 = tr.resultReg)
                    it += IRInstruction(Opcode.STOREM, vmDt, reg1 = cmpResultReg, labelSymbol = symbol)
                }
            }
        }
        return result
    }

    private fun createInplaceFloatComparison(
        constAddress: Int?,
        symbol: String?,
        operand: PtExpression,
        compareAndSetOpcode: Opcode
    ): MutableList<IRCodeChunkBase> {
        val result = mutableListOf<IRCodeChunkBase>()
        val valueReg = codeGen.registers.nextFreeFloat()
        val cmpReg = codeGen.registers.nextFree()
        val zeroReg = codeGen.registers.nextFree()
        if(operand is PtNumber) {
            val numberReg = codeGen.registers.nextFreeFloat()
            val cmpResultReg = codeGen.registers.nextFree()
            if (constAddress != null) {
                // in-place modify a memory location
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOADM, IRDataType.FLOAT, fpReg1 = valueReg, address = constAddress)
                    it += IRInstruction(Opcode.LOAD, IRDataType.FLOAT, fpReg1 = numberReg, immediateFp = operand.number)
                    it += IRInstruction(Opcode.FCOMP, IRDataType.FLOAT, reg1=cmpReg, fpReg1 = valueReg, fpReg2 = numberReg)
                    it += IRInstruction(Opcode.LOAD, IRDataType.BYTE, reg1=zeroReg, immediate = 0)
                    it += IRInstruction(compareAndSetOpcode, IRDataType.BYTE, reg1=cmpResultReg, reg2=cmpReg, reg3=zeroReg)
                    it += IRInstruction(Opcode.FFROMUB, IRDataType.FLOAT, reg1=cmpResultReg, fpReg1 = valueReg)
                    it += IRInstruction(Opcode.STOREM, IRDataType.FLOAT, fpReg1 = valueReg, address = constAddress)
                }
            } else {
                // in-place modify a symbol (variable)
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOADM, IRDataType.FLOAT, fpReg1 = valueReg, labelSymbol = symbol)
                    it += IRInstruction(Opcode.LOAD, IRDataType.FLOAT, fpReg1 = numberReg, immediateFp = operand.number)
                    it += IRInstruction(Opcode.FCOMP, IRDataType.FLOAT, reg1=cmpReg, fpReg1 = valueReg, fpReg2 = numberReg)
                    it += IRInstruction(Opcode.LOAD, IRDataType.BYTE, reg1=zeroReg, immediate = 0)
                    it += IRInstruction(compareAndSetOpcode, IRDataType.BYTE, reg1=cmpResultReg, reg2=cmpReg, reg3=zeroReg)
                    it += IRInstruction(Opcode.FFROMUB, IRDataType.FLOAT, reg1=cmpResultReg, fpReg1 = valueReg)
                    it += IRInstruction(Opcode.STOREM, IRDataType.FLOAT, fpReg1 = valueReg, labelSymbol = symbol)
                }
            }
        } else {
            val tr = expressionEval.translateExpression(operand)
            val cmpResultReg = codeGen.registers.nextFree()
            addToResult(result, tr, -1, tr.resultFpReg)
            if (constAddress != null) {
                // in-place modify a memory location
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOADM, IRDataType.FLOAT, fpReg1 = valueReg, address = constAddress)
                    it += IRInstruction(Opcode.FCOMP, IRDataType.FLOAT, reg1=cmpReg, fpReg1 = valueReg, fpReg2 = tr.resultFpReg)
                    it += IRInstruction(Opcode.LOAD, IRDataType.BYTE, reg1=zeroReg, immediate = 0)
                    it += IRInstruction(compareAndSetOpcode, IRDataType.BYTE, reg1=cmpResultReg, reg2=cmpReg, reg3=zeroReg)
                    it += IRInstruction(Opcode.FFROMUB, IRDataType.FLOAT, reg1=cmpResultReg, fpReg1 = valueReg)
                    it += IRInstruction(Opcode.STOREM, IRDataType.FLOAT, fpReg1 = valueReg, address = constAddress)
                }
            } else {
                // in-place modify a symbol (variable)
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOADM, IRDataType.FLOAT, fpReg1 = valueReg, labelSymbol = symbol)
                    it += IRInstruction(Opcode.FCOMP, IRDataType.FLOAT, reg1=cmpReg, fpReg1 = valueReg, fpReg2 = tr.resultFpReg)
                    it += IRInstruction(Opcode.LOAD, IRDataType.BYTE, reg1=zeroReg, immediate = 0)
                    it += IRInstruction(compareAndSetOpcode, IRDataType.BYTE, reg1=cmpResultReg, reg2=cmpReg, reg3=zeroReg)
                    it += IRInstruction(Opcode.FFROMUB, IRDataType.FLOAT, reg1=cmpResultReg, fpReg1 = valueReg)
                    it += IRInstruction(Opcode.STOREM, IRDataType.FLOAT, fpReg1 = valueReg, labelSymbol = symbol)
                }
            }
        }
        return result
    }

    private fun createInplaceArrayComparison(array: PtArrayIndexer, value: PtExpression, comparisonOperator: String): IRCodeChunks? {
        if(array.type==DataType.FLOAT)
            return null  // TODO("optimized in-place compare on float arrays"))   // TODO?

        val eltSize = codeGen.program.memsizer.memorySize(array.type)
        val result = mutableListOf<IRCodeChunkBase>()
        if(array.splitWords)
            TODO("inplace compare for split word array")
        val vmDt = irType(array.type)
        val constIndex = array.index.asConstInteger()
        val constValue = value.asConstInteger()
        val cmpResultReg = codeGen.registers.nextFree()
        if(constIndex!=null) {
            if(constValue==0) {
                // comparison against zero.
                val valueReg = codeGen.registers.nextFree()
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOADM, vmDt, reg1=valueReg, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize)
                    when(comparisonOperator) {
                        "==" -> it += IRInstruction(Opcode.SZ, vmDt, reg1=cmpResultReg, reg2=valueReg)
                        "!=" -> it += IRInstruction(Opcode.SNZ, vmDt, reg1=cmpResultReg, reg2=valueReg)
                        "<" -> return null  // TODO("array <0 inplace")) // TODO?
                        "<=" -> return null  // TODO("array <=0 inplace")) // TODO?
                        ">" -> return null  // TODO("array >0 inplace")) // TODO?
                        ">=" -> return null  // TODO("array >=0 inplace")) // TODO?
                        else -> throw AssemblyError("invalid operator")
                    }
                    it += IRInstruction(Opcode.STOREM, vmDt, reg1 = cmpResultReg, labelSymbol = array.variable.name, symbolOffset = constIndex*eltSize)
                }
                return result
            } else {
                return null  // TODO("compare against non-zero value"))
            }
        } else {
            if(constValue==0) {
                // comparison against zero.
                val valueReg = codeGen.registers.nextFree()
                val indexTr = expressionEval.translateExpression(array.index)
                addToResult(result, indexTr, indexTr.resultReg, -1)
                result += IRCodeChunk(null, null).also {
                    it += IRInstruction(Opcode.LOADX, vmDt, reg1=valueReg, reg2=indexTr.resultReg, labelSymbol = array.variable.name)
                    when(comparisonOperator) {
                        "==" -> it += IRInstruction(Opcode.SZ, vmDt, reg1=cmpResultReg, reg2=valueReg)
                        "!=" -> it += IRInstruction(Opcode.SNZ, vmDt, reg1=cmpResultReg, reg2=valueReg)
                        "<" -> return null  // TODO("array <0 inplace")) // TODO?
                        "<=" -> return null  // TODO("array <=0 inplace")) // TODO?
                        ">" -> return null  // TODO("array >0 inplace")) // TODO?
                        ">=" -> return null  // TODO("array >=0 inplace")) // TODO?
                        else -> throw AssemblyError("invalid operator")
                    }
                    it += IRInstruction(Opcode.STOREX, vmDt, reg1=valueReg, reg2=indexTr.resultReg, labelSymbol = array.variable.name)
                }
                return result
            }
            else
                return null  // TODO("compare against non-zero value"))
        }
    }

}