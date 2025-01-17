package prog8.code.target

import com.github.michaelbull.result.fold
import prog8.code.core.Encoding
import prog8.code.core.IStringEncoding
import prog8.code.core.InternalCompilerException
import prog8.code.target.cbm.AtasciiEncoding
import prog8.code.target.cbm.IsoEncoding
import prog8.code.target.cbm.PetsciiEncoding


object Encoder: IStringEncoding {
    override fun encodeString(str: String, encoding: Encoding): List<UByte> {
        val coded = when(encoding) {
            Encoding.PETSCII -> PetsciiEncoding.encodePetscii(str, true)
            Encoding.SCREENCODES -> PetsciiEncoding.encodeScreencode(str, true)
            Encoding.ISO -> IsoEncoding.encode(str)
            Encoding.ATASCII -> AtasciiEncoding.encode(str)
            else -> throw InternalCompilerException("unsupported encoding $encoding")
        }
        return coded.fold(
            failure = { throw it },
            success = { it }
        )
    }
    override fun decodeString(bytes: Iterable<UByte>, encoding: Encoding): String {
        val decoded = when(encoding) {
            Encoding.PETSCII -> PetsciiEncoding.decodePetscii(bytes, true)
            Encoding.SCREENCODES -> PetsciiEncoding.decodeScreencode(bytes, true)
            Encoding.ISO -> IsoEncoding.decode(bytes)
            Encoding.ATASCII -> AtasciiEncoding.decode(bytes)
            else -> throw InternalCompilerException("unsupported encoding $encoding")
        }
        return decoded.fold(
            failure = { throw it },
            success = { it }
        )
    }
}