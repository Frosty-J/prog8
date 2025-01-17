package prog8.code.core

import kotlin.math.abs

fun Number.toHex(): String {
    //  0..15 -> "0".."15"
    //  16..255 -> "$10".."$ff"
    //  256..65536 -> "$0100".."$ffff"
    // negative values are prefixed with '-'.
    val integer = this.toInt()
    if(integer<0)
        return '-' + abs(integer).toHex()
    return when (integer) {
        in 0 until 16 -> integer.toString()
        in 0 until 0x100 -> "$"+integer.toString(16).padStart(2,'0')
        in 0 until 0x10000 -> "$"+integer.toString(16).padStart(4,'0')
        else -> throw IllegalArgumentException("number too large for 16 bits $this")
    }
}

fun UInt.toHex(): String {
    //  0..15 -> "0".."15"
    //  16..255 -> "$10".."$ff"
    //  256..65536 -> "$0100".."$ffff"
    return when (this) {
        in 0u until 16u -> this.toString()
        in 0u until 0x100u -> "$"+this.toString(16).padStart(2,'0')
        in 0u until 0x10000u -> "$"+this.toString(16).padStart(4,'0')
        else -> throw IllegalArgumentException("number too large for 16 bits $this")
    }
}

fun Char.escape(): Char = this.toString().escape()[0]

fun String.escape(): String {
    val es = this.map {
        when(it) {
            '\t' -> "\\t"
            '\n' -> "\\n"
            '\r' -> "\\r"
            '"' -> "\\\""
            in '\u8000'..'\u80ff' -> "\\x" + (it.code - 0x8000).toString(16).padStart(2, '0')       // 'ugly' passthrough hack
            in '\u0000'..'\u00ff' -> it.toString()
            else -> "\\u" + it.code.toString(16).padStart(4, '0')
        }
    }
    return es.joinToString("")
}

fun String.unescape(): String {
    val result = mutableListOf<Char>()
    val iter = this.iterator()
    while(iter.hasNext()) {
        val c = iter.nextChar()
        if(c=='\\') {
            val ec = iter.nextChar()
            result.add(when(ec) {
                '\\' -> '\\'
                'n' -> '\n'
                'r' -> '\r'
                '"' -> '"'
                '\'' -> '\''
                'u' -> {
                    try {
                        "${iter.nextChar()}${iter.nextChar()}${iter.nextChar()}${iter.nextChar()}".toInt(16).toChar()
                    } catch (sb: StringIndexOutOfBoundsException) {
                        throw IllegalArgumentException("invalid \\u escape sequence")
                    } catch (nf: NumberFormatException) {
                        throw IllegalArgumentException("invalid \\u escape sequence")
                    }
                }
                'x' -> {
                    try {
                        val hex = ("" + iter.nextChar() + iter.nextChar()).toInt(16)
                        (0x8000 + hex).toChar()         // 'ugly' pass-through hack
                    } catch (sb: StringIndexOutOfBoundsException) {
                        throw IllegalArgumentException("invalid \\x escape sequence")
                    } catch (nf: NumberFormatException) {
                        throw IllegalArgumentException("invalid \\x escape sequence")
                    }
                }
                else -> throw IllegalArgumentException("invalid escape char in string: \\$ec")
            })
        } else {
            result.add(c)
        }
    }
    return result.joinToString("")
}
