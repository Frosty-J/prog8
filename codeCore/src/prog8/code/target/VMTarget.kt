package prog8.code.target

import prog8.code.core.*
import prog8.code.target.virtual.VirtualMachineDefinition

class VMTarget: ICompilationTarget, IStringEncoding by Encoder, IMemSizer {
    override val name = NAME
    override val machine = VirtualMachineDefinition()
    override val supportedEncodings = setOf(Encoding.ISO)
    override val defaultEncoding = Encoding.ISO

    companion object {
        const val NAME = "virtual"
    }

    override fun memorySize(dt: DataType): Int {
        return when(dt) {
            in ByteDatatypes -> 1
            in WordDatatypes, in PassByReferenceDatatypes -> 2
            DataType.FLOAT -> machine.FLOAT_MEM_SIZE
            else -> Int.MIN_VALUE
        }
    }

    override fun memorySize(arrayDt: DataType, numElements: Int) =
        memorySize(ArrayToElementTypes.getValue(arrayDt)) * numElements
}