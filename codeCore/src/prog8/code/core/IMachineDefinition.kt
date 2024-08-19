package prog8.code.core

import java.nio.file.Path


enum class CpuType {
    CPU6502,
    CPU65c02,
    VIRTUAL
}

interface IMachineDefinition {
    val FLOAT_MAX_NEGATIVE: Double
    val FLOAT_MAX_POSITIVE: Double
    val FLOAT_MEM_SIZE: Int
    val PROGRAM_LOAD_ADDRESS : UInt
    val BSSHIGHRAM_START: UInt
    val BSSHIGHRAM_END: UInt
    val BSSGOLDENRAM_START: UInt
    val BSSGOLDENRAM_END: UInt

    val cpu: CpuType
    var zeropage: Zeropage
    var golden: GoldenRam

    fun initializeMemoryAreas(compilerOptions: CompilationOptions)
    fun getFloatAsmBytes(num: Number): String

    fun convertFloatToBytes(num: Double): List<UByte>
    fun convertBytesToFloat(bytes: List<UByte>): Double

    fun launchEmulator(selectedEmulator: Int, programNameWithPath: Path)
    fun isIOAddress(address: UInt): Boolean
}
