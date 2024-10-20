package prog8.code.core

import java.io.File
import java.io.IOException
import java.nio.file.Path
import java.text.Normalizer
import kotlin.io.path.Path
import kotlin.io.path.readText


const val internedStringsModuleName = "prog8_interned_strings"


/**
 * Encapsulates - and ties together - actual source code (=text) and its [origin].
 */
sealed class SourceCode {

    /**
     * Whether this [SourceCode] instance was created as a [Resource]
     */
    abstract val isFromResources: Boolean

    /**
     * Whether this [SourceCode] instance was created as a [File]
     */
    abstract val isFromFilesystem: Boolean

    /**
     * The logical name of the source code unit. Usually the module's name.
     */
    abstract val name: String

    /**
     * Where this [SourceCode] instance came from.
     * This can be one of the following:
     * * a normal string representation of a [java.nio.file.Path], if it originates from a file (see [File])
     * * `string:44c56085` if was created via [String]
     * * `library:/x/y/z.ext` if it is a library file that was loaded from resources (see [Resource])
     */
    abstract val origin: String

    /**
     * The source code as plain string.
     */
    abstract val text: String

    /**
     * Printable representation, deliberately does NOT return the actual text.
     */
    final override fun toString() = "${this.javaClass.name}[${this.origin}]"

    companion object {

        /**
         * filename prefix to designate library files that will be retreived from internal resources rather than disk
         */
        const val LIBRARYFILEPREFIX = "library:"
        const val STRINGSOURCEPREFIX = "string:"
        val curdir: Path = Path(".").toAbsolutePath()
        fun relative(path: Path): Path = curdir.relativize(path.toAbsolutePath())
        fun isRegularFilesystemPath(pathString: String) =
            !(pathString.startsWith(LIBRARYFILEPREFIX) || pathString.startsWith(STRINGSOURCEPREFIX))

        fun isLibraryResource(path: String) = path.startsWith(LIBRARYFILEPREFIX)
    }

    /**
     * Turn a plain String into a [SourceCode] object.
     * [origin] will be something like `string:44c56085`.
     */
    class Text(origText: String): SourceCode() {
        override val text = origText.replace("\\R".toRegex(), "\n")      // normalize line endings
        override val isFromResources = false
        override val isFromFilesystem = false
        override val origin = "$STRINGSOURCEPREFIX${System.identityHashCode(text).toString(16)}"
        override val name = "<unnamed-text>"
    }

    /**
     * Get [SourceCode] from the file represented by the specified Path.
     * This immediately reads the file fully into memory.
     *
     * [origin] will be the given path in absolute and normalized form.
     * @throws NoSuchFileException if the file does not exist
     * @throws FileSystemException if the file cannot be read
     */
    class File(path: Path): SourceCode() {
        override val text: String
        override val origin: String
        override val name: String
        override val isFromResources = false
        override val isFromFilesystem = true

        init {
            val normalized = path.normalize()
            origin = relative(normalized).toString()
            try {
                val contents = Normalizer.normalize(normalized.readText(), Normalizer.Form.NFC)
                text = contents.replace("\\R".toRegex(), "\n")      // normalize line endings
                name = normalized.toFile().nameWithoutExtension
            } catch (nfx: java.nio.file.NoSuchFileException) {
                throw NoSuchFileException(normalized.toFile()).also { it.initCause(nfx) }
            } catch (iox: IOException) {
                throw FileSystemException(normalized.toFile()).also { it.initCause(iox) }
            }
        }
    }

    /**
     * [origin]: `library:/x/y/z.p8` for a given `pathString` of "x/y/z.p8"
     */
    class Resource(pathString: String): SourceCode() {
        private val normalized = "/" + Path(pathString).normalize().toMutableList().joinToString("/")

        override val isFromResources = true
        override val isFromFilesystem = false
        override val origin = "$LIBRARYFILEPREFIX$normalized"
        override val text: String
        override val name: String

        init {
            val rscURL = object {}.javaClass.getResource(normalized)
            if (rscURL == null) {
                val rscRoot = object {}.javaClass.getResource("/")
                throw NoSuchFileException(
                    File(normalized),
                    reason = "looked in resources rooted at $rscRoot"
                )
            }
            val stream = object {}.javaClass.getResourceAsStream(normalized)
            val contents = stream!!.reader().use { Normalizer.normalize(it.readText(), Normalizer.Form.NFC) }
            text = contents.replace("\\R".toRegex(), "\n")      // normalize line endings
            name = Path(pathString).toFile().nameWithoutExtension
        }
    }

    /**
     * SourceCode for internally generated nodes (usually Modules)
     */
    class Generated(override val name: String) : SourceCode() {
        override val isFromResources: Boolean = false
        override val isFromFilesystem: Boolean = false
        override val origin: String = name
        override val text: String = "<generated code node, no text representation>"
    }
}


object SourceLineCache {
    private val cache = mutableMapOf<String, List<String>>()

    private fun getCachedFile(file: String): List<String> {
        val existing = cache[file]
        if(existing!=null)
            return existing
        if (SourceCode.isRegularFilesystemPath(file)) {
            val source = SourceCode.File(Path(file))
            cache[file] = source.text.split('\n', '\r').map { it.trim() }
            return cache.getValue(file)
        } else if(file.startsWith(SourceCode.LIBRARYFILEPREFIX)) {
            val source = SourceCode.Resource(file.drop(SourceCode.LIBRARYFILEPREFIX.length))
            cache[file] = source.text.split('\n', '\r').map { it.trim()}
            return cache.getValue(file)
        }
        return emptyList()
    }

    fun retrieveLine(position: Position): String? {
        if (position.line>0) {
            val lines = getCachedFile(position.file)
            if(lines.isNotEmpty())
                return lines[position.line-1]
        }
        return null
    }
}