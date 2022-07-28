package prog8.code.target.cx16

import prog8.code.core.*


class CX16Zeropage(options: CompilationOptions) : Zeropage(options) {

    override val SCRATCH_B1 = 0x7au      // temp storage for a single byte
    override val SCRATCH_REG = 0x7bu     // temp storage for a register, must be B1+1
    override val SCRATCH_W1 = 0x7cu      // temp storage 1 for a word  $7c+$7d
    override val SCRATCH_W2 = 0x7eu      // temp storage 2 for a word  $7e+$7f


    init {
        if (options.floats && options.zeropage !in arrayOf(
                ZeropageType.FLOATSAFE,
                ZeropageType.BASICSAFE,
                ZeropageType.DONTUSE
            ))
            throw InternalCompilerException("when floats are enabled, zero page type should be 'floatsafe' or 'basicsafe' or 'dontuse'")

        // the addresses 0x02 to 0x21 (inclusive) are taken for sixteen virtual 16-bit api registers.

        synchronized(this) {
            when (options.zeropage) {
                ZeropageType.FULL -> {
                    free.addAll(0x22u..0xffu)
                }
                ZeropageType.KERNALSAFE -> {
                    free.addAll(0x22u..0x7fu)
                    free.addAll(0xa9u..0xffu)
                }
                ZeropageType.FLOATSAFE -> {
                    free.addAll(0x22u..0x7fu)
                    free.addAll(0xd4u..0xffu)
                }
                ZeropageType.BASICSAFE -> {
                    free.addAll(0x22u..0x7fu)
                }
                ZeropageType.DONTUSE -> {
                    free.clear() // don't use zeropage at all
                }
                else -> throw InternalCompilerException("for this machine target, zero page type 'floatsafe' is not available. ${options.zeropage}")
            }

            removeReservedFromFreePool()

            allocateCx16VirtualRegisters()
        }
    }

    override fun allocateCx16VirtualRegisters() {
        // Note: the 16 virtual registers R0-R15 are not regular allocated variables, they're *memory mapped* elsewhere to fixed addresses.
        // However, to be able for the compiler to "see" them as zero page variables, we have to register them here as well.
        // This is important because the compiler sometimes treats ZP variables more efficiently (for example if it's a pointer)
        for(reg in 0..15) {
            allocatedVariables[listOf("cx16", "r${reg}")]   = ZpAllocation((2+reg*2).toUInt(), DataType.UWORD, 2)       // cx16.r0 .. cx16.r15
            allocatedVariables[listOf("cx16", "r${reg}s")]  = ZpAllocation((2+reg*2).toUInt(), DataType.WORD, 2)        // cx16.r0s .. cx16.r15s
            allocatedVariables[listOf("cx16", "r${reg}L")]  = ZpAllocation((2+reg*2).toUInt(), DataType.UBYTE, 1)       // cx16.r0L .. cx16.r15L
            allocatedVariables[listOf("cx16", "r${reg}H")]  = ZpAllocation((3+reg*2).toUInt(), DataType.UBYTE, 1)       // cx16.r0H .. cx16.r15H
            allocatedVariables[listOf("cx16", "r${reg}sL")] = ZpAllocation((2+reg*2).toUInt(), DataType.BYTE, 1)        // cx16.r0sL .. cx16.r15sL
            allocatedVariables[listOf("cx16", "r${reg}sH")] = ZpAllocation((3+reg*2).toUInt(), DataType.BYTE, 1)        // cx16.r0sH .. cx16.r15sH
        }
    }
}