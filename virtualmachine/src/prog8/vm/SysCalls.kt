package prog8.vm

import prog8.intermediate.FunctionCallArgs
import prog8.intermediate.IRDataType
import kotlin.math.*

/*
SYSCALLS:

0 = reset ; resets system
1 = exit ; stops program and returns statuscode from r0.w
2 = print_c ; print single character
3 = print_s ; print 0-terminated string from memory
4 = print_u8 ; print unsigned int byte
5 = print_u16 ; print unsigned int word
6 = input ; reads a line of text entered by the user, r0.w = memory buffer, r1.b = maxlength (0-255, 0=unlimited).  Zero-terminates the string. Returns length in r0.w
7 = sleep ; sleep amount of milliseconds
8 = gfx_enable  ; enable graphics window  r0.b = 0 -> lores 320x240,  r0.b = 1 -> hires 640x480
9 = gfx_clear   ; clear graphics window with shade in r0.b
10 = gfx_plot   ; plot pixel in graphics window, r0.w/r1.w contain X and Y coordinates, r2.b contains brightness
11 = decimal string to word (unsigned)
12 = decimal string to word (signed)
13 = wait       ; wait certain amount of jiffies (1/60 sec)
14 = waitvsync  ; wait on vsync
15 = sort_ubyte array
16 = sort_byte array
17 = sort_uword array
18 = sort_word array
19 = any_byte array
20 = any_word array
21 = any_float array
22 = all_byte array
23 = all_word array
24 = all_float array
25 = print_f  (floating point value in fp reg 0)
26 = reverse_bytes array
27 = reverse_words array
28 = reverse_floats array
29 = compare strings
30 = gfx_getpixel      ; get byte pixel value at coordinates r0.w/r1.w
31 = rndseed
32 = rndfseed
33 = RND
34 = RNDW
35 = RNDF
36 = STRING_CONTAINS
37 = BYTEARRAY_CONTAINS
38 = WORDARRAY_CONTAINS
39 = CLAMP_BYTE
40 = CLAMP_UBYTE
41 = CLAMP_WORD
42 = CLAMP_UWORD
43 = CLAMP_FLOAT
44 = ATAN
45 = STR_TO_FLOAT
46 = MUL16_LAST_UPPER
*/

enum class Syscall {
    RESET,
    EXIT,
    PRINT_C,
    PRINT_S,
    PRINT_U8,
    PRINT_U16,
    INPUT,
    SLEEP,
    GFX_ENABLE,
    GFX_CLEAR,
    GFX_PLOT,
    STR_TO_UWORD,
    STR_TO_WORD,
    WAIT,
    WAITVSYNC,
    SORT_UBYTE,
    SORT_BYTE,
    SORT_UWORD,
    SORT_WORD,
    ANY_BYTE,
    ANY_WORD,
    ANY_FLOAT,
    ALL_BYTE,
    ALL_WORD,
    ALL_FLOAT,
    PRINT_F,
    REVERSE_BYTES,
    REVERSE_WORDS,
    REVERSE_FLOATS,
    COMPARE_STRINGS,
    GFX_GETPIXEL,
    RNDSEED,
    RNDFSEED,
    RND,
    RNDW,
    RNDF,
    STRING_CONTAINS,
    BYTEARRAY_CONTAINS,
    WORDARRAY_CONTAINS,
    CLAMP_BYTE,
    CLAMP_UBYTE,
    CLAMP_WORD,
    CLAMP_UWORD,
    CLAMP_FLOAT,
    ATAN,
    STR_TO_FLOAT,
    MUL16_LAST_UPPER
    ;

    companion object {
        private val VALUES = values()
        fun fromInt(value: Int) = VALUES[value]
    }
}

object SysCalls {
    private fun getArgValues(argspec: List<FunctionCallArgs.ArgumentSpec>, vm: VirtualMachine): List<Comparable<Nothing>> {
        return argspec.map {
            when(it.reg.dt) {
                IRDataType.BYTE -> vm.registers.getUB(it.reg.registerNum)
                IRDataType.WORD -> vm.registers.getUW(it.reg.registerNum)
                IRDataType.FLOAT -> vm.registers.getFloat(it.reg.registerNum)
            }
        }
    }

    private fun returnValue(returns: FunctionCallArgs.RegSpec, value: Comparable<Nothing>, vm: VirtualMachine) {
        val vv: Double = when(value) {
            is UByte -> value.toDouble()
            is UShort -> value.toDouble()
            is UInt -> value.toDouble()
            is Byte -> value.toDouble()
            is Short -> value.toDouble()
            is Int -> value.toDouble()
            is Float -> value.toDouble()
            is Double -> value
            else -> (value as Number).toDouble()
        }
        when(returns.dt) {
            IRDataType.BYTE -> vm.registers.setUB(returns.registerNum, vv.toInt().toUByte())
            IRDataType.WORD -> vm.registers.setUW(returns.registerNum, vv.toInt().toUShort())
            IRDataType.FLOAT -> vm.registers.setFloat(returns.registerNum, vv)
        }
    }

    fun call(call: Syscall, callspec: FunctionCallArgs, vm: VirtualMachine) {

        when(call) {
            Syscall.RESET -> {
                vm.reset(false)
            }
            Syscall.EXIT ->{
                val exitValue = getArgValues(callspec.arguments, vm).single() as UByte
                vm.exit(exitValue.toInt())
            }
            Syscall.PRINT_C -> {
                val char = getArgValues(callspec.arguments, vm).single() as UByte
                print(Char(char.toInt()))
            }
            Syscall.PRINT_S -> {
                var addr = (getArgValues(callspec.arguments, vm).single() as UShort).toInt()
                while(true) {
                    val char = vm.memory.getUB(addr).toInt()
                    if(char==0)
                        break
                    print(Char(char))
                    addr++
                }
            }
            Syscall.PRINT_U8 -> {
                val value = getArgValues(callspec.arguments, vm).single()
                print(value)
            }
            Syscall.PRINT_U16 -> {
                val value = getArgValues(callspec.arguments, vm).single()
                print(value)
            }
            Syscall.INPUT -> {
                val (address, maxlen) = getArgValues(callspec.arguments, vm)
                var input = readln()
                val maxlenvalue = (maxlen as UByte).toInt()
                if(maxlenvalue>0)
                    input = input.substring(0, min(input.length, maxlenvalue))
                vm.memory.setString((address as UShort).toInt(), input, true)
                returnValue(callspec.returns!!, input.length, vm)
            }
            Syscall.SLEEP -> {
                val duration = getArgValues(callspec.arguments, vm).single() as UShort
                Thread.sleep(duration.toLong())
            }
            Syscall.GFX_ENABLE -> {
                val mode = getArgValues(callspec.arguments, vm).single() as UByte
                vm.gfx_enable(mode)
            }
            Syscall.GFX_CLEAR -> {
                val color = getArgValues(callspec.arguments, vm).single() as UByte
                vm.gfx_clear(color)
            }
            Syscall.GFX_PLOT -> {
                val (x,y,color) = getArgValues(callspec.arguments, vm)
                vm.gfx_plot(x as UShort, y as UShort, color as UByte)
            }
            Syscall.GFX_GETPIXEL -> {
                val (x,y) = getArgValues(callspec.arguments, vm)
                val color = vm.gfx_getpixel(x as UShort, y as UShort)
                returnValue(callspec.returns!!, color, vm)
            }
            Syscall.WAIT -> {
                val time = getArgValues(callspec.arguments, vm).single() as UShort
                Thread.sleep(time.toLong() * 1000/60)
            }
            Syscall.WAITVSYNC -> vm.waitvsync()
            Syscall.SORT_UBYTE -> {
                val (addressV, lengthV) = getArgValues(callspec.arguments, vm)
                val address = (addressV as UShort).toInt()
                val length = (lengthV as UByte).toInt()
                val array = IntProgression.fromClosedRange(address, address+length-1, 1).map {
                    vm.memory.getUB(it)
                }.sorted()
                array.withIndex().forEach { (index, value)->
                    vm.memory.setUB(address+index, value)
                }
            }
            Syscall.SORT_BYTE -> {
                val (addressV, lengthV) = getArgValues(callspec.arguments, vm)
                val address = (addressV as UShort).toInt()
                val length = (lengthV as UByte).toInt()
                val array = IntProgression.fromClosedRange(address, address+length-1, 1).map {
                    vm.memory.getSB(it)
                }.sorted()
                array.withIndex().forEach { (index, value)->
                    vm.memory.setSB(address+index, value)
                }
            }
            Syscall.SORT_UWORD -> {
                val (addressV, lengthV) = getArgValues(callspec.arguments, vm)
                val address = (addressV as UShort).toInt()
                val length = (lengthV as UByte).toInt()
                val array = IntProgression.fromClosedRange(address, address+length*2-2, 2).map {
                    vm.memory.getUW(it)
                }.sorted()
                array.withIndex().forEach { (index, value)->
                    vm.memory.setUW(address+index*2, value)
                }
            }
            Syscall.SORT_WORD -> {
                val (addressV, lengthV) = getArgValues(callspec.arguments, vm)
                val address = (addressV as UShort).toInt()
                val length = (lengthV as UByte).toInt()
                val array = IntProgression.fromClosedRange(address, address+length*2-2, 2).map {
                    vm.memory.getSW(it)
                }.sorted()
                array.withIndex().forEach { (index, value)->
                    vm.memory.setSW(address+index*2, value)
                }
            }
            Syscall.REVERSE_BYTES -> {
                val (addressV, lengthV) = getArgValues(callspec.arguments, vm)
                val address = (addressV as UShort).toInt()
                val length = (lengthV as UByte).toInt()
                val array = IntProgression.fromClosedRange(address, address+length-1, 1).map {
                    vm.memory.getUB(it)
                }.reversed()
                array.withIndex().forEach { (index, value)->
                    vm.memory.setUB(address+index, value)
                }
            }
            Syscall.REVERSE_WORDS -> {
                val (addressV, lengthV) = getArgValues(callspec.arguments, vm)
                val address = (addressV as UShort).toInt()
                val length = (lengthV as UByte).toInt()
                val array = IntProgression.fromClosedRange(address, address+length*2-2, 2).map {
                    vm.memory.getUW(it)
                }.reversed()
                array.withIndex().forEach { (index, value)->
                    vm.memory.setUW(address+index*2, value)
                }
            }
            Syscall.REVERSE_FLOATS -> {
                val (addressV, lengthV) = getArgValues(callspec.arguments, vm)
                val address = (addressV as UShort).toInt()
                val length = (lengthV as UByte).toInt()
                val array = IntProgression.fromClosedRange(address, address+length*4-2, 4).map {
                    vm.memory.getFloat(it)
                }.reversed()
                array.withIndex().forEach { (index, value)->
                    vm.memory.setFloat(address+index*4, value)
                }
            }
            Syscall.ANY_BYTE -> {
                val (addressV, lengthV) = getArgValues(callspec.arguments, vm)
                val address = (addressV as UShort).toInt()
                val length = (lengthV as UByte).toInt()
                val addresses = IntProgression.fromClosedRange(address, address+length-1, 1)
                if(addresses.any { vm.memory.getUB(it).toInt()!=0 })
                    returnValue(callspec.returns!!, 1, vm)
                else
                    returnValue(callspec.returns!!, 0, vm)
            }
            Syscall.ANY_WORD -> {
                val (addressV, lengthV) = getArgValues(callspec.arguments, vm)
                val address = (addressV as UShort).toInt()
                val length = (lengthV as UByte).toInt()
                val addresses = IntProgression.fromClosedRange(address, address+length*2-2, 2)
                if(addresses.any { vm.memory.getUW(it).toInt()!=0 })
                    returnValue(callspec.returns!!, 1, vm)
                else
                    returnValue(callspec.returns!!, 0, vm)
            }
            Syscall.ANY_FLOAT -> {
                val (addressV, lengthV) = getArgValues(callspec.arguments, vm)
                val address = (addressV as UShort).toInt()
                val length = (lengthV as UByte).toInt()
                val addresses = IntProgression.fromClosedRange(address, address+length*4-2, 4)
                if(addresses.any { vm.memory.getFloat(it).toInt()!=0 })
                    returnValue(callspec.returns!!, 1, vm)
                else
                    returnValue(callspec.returns!!, 0, vm)
            }
            Syscall.ALL_BYTE -> {
                val (addressV, lengthV) = getArgValues(callspec.arguments, vm)
                val address = (addressV as UShort).toInt()
                val length = (lengthV as UByte).toInt()
                val addresses = IntProgression.fromClosedRange(address, address+length-1, 1)
                if(addresses.all { vm.memory.getUB(it).toInt()!=0 })
                    returnValue(callspec.returns!!, 1, vm)
                else
                    returnValue(callspec.returns!!, 0, vm)
            }
            Syscall.ALL_WORD -> {
                val (addressV, lengthV) = getArgValues(callspec.arguments, vm)
                val address = (addressV as UShort).toInt()
                val length = (lengthV as UByte).toInt()
                val addresses = IntProgression.fromClosedRange(address, address+length*2-2, 2)
                if(addresses.all { vm.memory.getUW(it).toInt()!=0 })
                    returnValue(callspec.returns!!, 1, vm)
                else
                    returnValue(callspec.returns!!, 0, vm)
            }
            Syscall.ALL_FLOAT -> {
                val (addressV, lengthV) = getArgValues(callspec.arguments, vm)
                val address = (addressV as UShort).toInt()
                val length = (lengthV as UByte).toInt()
                val addresses = IntProgression.fromClosedRange(address, address+length*4-2, 4)
                if(addresses.all { vm.memory.getFloat(it).toInt()!=0 })
                    returnValue(callspec.returns!!, 1, vm)
                else
                    returnValue(callspec.returns!!, 0, vm)
            }
            Syscall.PRINT_F -> {
                val value = getArgValues(callspec.arguments, vm).single() as Double
                if(value==0.0)
                    print("0")
                else
                    print(value)
            }
            Syscall.STR_TO_UWORD -> {
                val stringAddr = getArgValues(callspec.arguments, vm).single() as UShort
                val string = vm.memory.getString(stringAddr.toInt()).takeWhile { it.isDigit() }
                val value = try {
                    string.toUShort()
                } catch(_: NumberFormatException) {
                    0u
                }
                returnValue(callspec.returns!!, value, vm)
            }
            Syscall.STR_TO_WORD -> {
                val stringAddr = getArgValues(callspec.arguments, vm).single() as UShort
                val memstring = vm.memory.getString(stringAddr.toInt())
                val match = Regex("^[+-]?\\d+").find(memstring) ?: return returnValue(callspec.returns!!, 0, vm)
                val value = try {
                    match.value.toShort()
                } catch(_: NumberFormatException) {
                    0
                }
                return returnValue(callspec.returns!!, value, vm)
            }
            Syscall.STR_TO_FLOAT -> {
                val stringAddr = getArgValues(callspec.arguments, vm).single() as UShort
                val memstring = vm.memory.getString(stringAddr.toInt())
                returnValue(callspec.returns!!, memstring.toDouble(), vm)
            }
            Syscall.COMPARE_STRINGS -> {
                val (firstV, secondV) = getArgValues(callspec.arguments, vm)
                val firstAddr = firstV as UShort
                val secondAddr = secondV as UShort
                val first = vm.memory.getString(firstAddr.toInt())
                val second = vm.memory.getString(secondAddr.toInt())
                val comparison = first.compareTo(second)
                if(comparison==0)
                    returnValue(callspec.returns!!, 0, vm)
                else if(comparison<0)
                    returnValue(callspec.returns!!, -1, vm)
                else
                    returnValue(callspec.returns!!, 1, vm)
            }
            Syscall.RNDFSEED -> {
                val seed = getArgValues(callspec.arguments, vm).single() as Double
                if(seed>0)  // always use negative seed, this mimics the behavior on CBM machines
                    vm.randomSeedFloat(-seed)
                else
                    vm.randomSeedFloat(seed)
            }
            Syscall.RNDSEED -> {
                val (seed1, seed2) = getArgValues(callspec.arguments, vm)
                vm.randomSeed(seed1 as UShort, seed2 as UShort)
            }
            Syscall.RND -> {
                returnValue(callspec.returns!!, vm.randomGenerator.nextInt().toUByte(), vm)
            }
            Syscall.RNDW -> {
                returnValue(callspec.returns!!, vm.randomGenerator.nextInt().toUShort(), vm)
            }
            Syscall.RNDF -> {
                returnValue(callspec.returns!!, vm.randomGeneratorFloats.nextFloat(), vm)
            }
            Syscall.STRING_CONTAINS -> {
                val (charV, addr) = getArgValues(callspec.arguments, vm)
                val stringAddr = addr as UShort
                val char = (charV as UByte).toInt().toChar()
                val string = vm.memory.getString(stringAddr.toInt())
                returnValue(callspec.returns!!, if(char in string) 1u else 0u, vm)
            }
            Syscall.BYTEARRAY_CONTAINS -> {
                val (value, arrayV, lengthV) = getArgValues(callspec.arguments, vm)
                var length = lengthV as UByte
                var array = (arrayV as UShort).toInt()
                while(length>0u) {
                    if(vm.memory.getUB(array)==value)
                        return returnValue(callspec.returns!!, 1u, vm)
                    array++
                    length--
                }
                returnValue(callspec.returns!!, 0u, vm)
            }
            Syscall.WORDARRAY_CONTAINS -> {
                val (value, arrayV, lengthV) = getArgValues(callspec.arguments, vm)
                var length = lengthV as UByte
                var array = (arrayV as UShort).toInt()
                while(length>0u) {
                    if(vm.memory.getUW(array)==value)
                        return returnValue(callspec.returns!!, 1u, vm)
                    array += 2
                    length--
                }
                returnValue(callspec.returns!!, 0u, vm)
            }
            Syscall.CLAMP_BYTE -> {
                val (valueU, minimumU, maximumU) = getArgValues(callspec.arguments, vm)
                val value = (valueU as UByte).toByte().toInt()
                val minimum = (minimumU as UByte).toByte().toInt()
                val maximum = (maximumU as UByte).toByte().toInt()
                val result = min(max(value, minimum), maximum)
                returnValue(callspec.returns!!, result, vm)
            }
            Syscall.CLAMP_UBYTE -> {
                val (valueU, minimumU, maximumU) = getArgValues(callspec.arguments, vm)
                val value = (valueU as UByte).toInt()
                val minimum = (minimumU as UByte).toInt()
                val maximum = (maximumU as UByte).toInt()
                val result = min(max(value, minimum), maximum)
                returnValue(callspec.returns!!, result, vm)
            }
            Syscall.CLAMP_WORD -> {
                val (valueU, minimumU, maximumU) = getArgValues(callspec.arguments, vm)
                val value = (valueU as UShort).toShort().toInt()
                val minimum = (minimumU as UShort).toShort().toInt()
                val maximum = (maximumU as UShort).toShort().toInt()
                val result = min(max(value, minimum), maximum)
                returnValue(callspec.returns!!, result, vm)
            }
            Syscall.CLAMP_UWORD -> {
                val (valueU, minimumU, maximumU) = getArgValues(callspec.arguments, vm)
                val value = (valueU as UShort).toInt()
                val minimum = (minimumU as UShort).toInt()
                val maximum = (maximumU as UShort).toInt()
                val result = min(max(value, minimum), maximum)
                returnValue(callspec.returns!!, result, vm)
            }
            Syscall.CLAMP_FLOAT -> {
                val (valueU, minimumU, maximumU) = getArgValues(callspec.arguments, vm)
                val value = valueU as Double
                val minimum = minimumU as Double
                val maximum = maximumU as Double
                val result = min(max(value, minimum), maximum)
                returnValue(callspec.returns!!, result, vm)
            }
            Syscall.ATAN -> {
                val (x1, y1, x2, y2) = getArgValues(callspec.arguments, vm)
                val x1f = (x1 as UByte).toDouble()
                val y1f = (y1 as UByte).toDouble()
                val x2f = (x2 as UByte).toDouble()
                val y2f = (y2 as UByte).toDouble()
                var radians = atan2(y2f-y1f, x2f-x1f)
                if(radians<0)
                    radians+=2*PI
                val result = floor(radians/2.0/PI*256.0)
                returnValue(callspec.returns!!, result, vm)
            }
            Syscall.MUL16_LAST_UPPER -> {
                returnValue(callspec.returns!!, vm.mul16_last_upper, vm)
            }
        }
    }
}
