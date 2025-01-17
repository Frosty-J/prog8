package prog8tests.codegeneration

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import prog8.code.target.C64Target
import prog8.code.target.VMTarget
import prog8tests.helpers.ErrorReporterForTests
import prog8tests.helpers.compileText
import kotlin.io.path.readText

class TestArrayThings: FunSpec({
    test("assign prefix var to array should compile fine and is not split into inplace array modification") {
        val text = """
            main {
                sub start() {
                    byte[5] array
                    byte bb
                    array[1] = -bb
                }
            }
        """
        compileText(C64Target(), false, text, writeAssembly = true) shouldNotBe null
        compileText(VMTarget(), false, text, writeAssembly = true) shouldNotBe null
    }

    test("array in-place negation (integer types)") {
        val text = """
main {
  byte[10] foo
  ubyte[10] foou
  word[10] foow
  uword[10] foowu

  sub start() {
    foo[1] = 42
    foo[1] = -foo[1]

    foow[1] = 4242
    foow[1] = -foow[1]
  }
}"""
        compileText(C64Target(), false, text, writeAssembly = true) shouldNotBe null
        compileText(VMTarget(), false, text, writeAssembly = true) shouldNotBe null
    }

    test("array in-place negation (float type) vm target") {
        val text = """
%import floats

main {
  float[10] flt

  sub start() {
    flt[1] = 42.42
    flt[1] = -flt[1]
  }
}"""
        compileText(VMTarget(), false, text, writeAssembly = true) shouldNotBe null
    }

    test("array in-place negation (float type) 6502 target") {
        val text = """
%import floats

main {
  float[10] flt

  sub start() {
    flt[1] = 42.42
    flt[1] = -flt[1]
  }
}"""
        compileText(C64Target(), false, text, writeAssembly = true) shouldNotBe null
    }

    test("array in-place invert") {
        val text = """
main {
  ubyte[10] foo
  uword[10] foow

  sub start() {
    foo[1] = 42
    foo[1] = ~foo[1]

    foow[1] = 4242
    foow[1] = ~foow[1]
  }
}"""
        compileText(C64Target(), false, text, writeAssembly = true) shouldNotBe null
        compileText(VMTarget(), false, text, writeAssembly = true) shouldNotBe null
    }

    test("split only for word arrays") {
        val text = """
main {
  ubyte[10] @split sb
  uword[10] @split sw
  word[10] @split sw2
  float[10] @split sf

  sub start() {
  }
}"""
        val errors = ErrorReporterForTests()
        compileText(C64Target(), false, text, writeAssembly = false, errors = errors) shouldBe null
        errors.errors.size shouldBe 2
        errors.errors.forEach {
            it shouldContain "split"
            it shouldContain "word arrays"
        }
    }

    test("split word arrays in asm as lsb/msb") {
        val text = """
main {
  uword[10] @split @shared uw
  word[10] @split @shared sw
  uword[10] @shared normal

  sub start() {
    %asm {{
        lda  p8_normal
        lda  p8_uw_lsb
        lda  p8_uw_msb
        lda  p8_sw_lsb
        lda  p8_sw_msb
    }}
  }
}"""
        compileText(C64Target(), false, text, writeAssembly = true) shouldNotBe null
        compileText(VMTarget(), false, text, writeAssembly = true) shouldNotBe null
    }

    test("split array assignments") {
        val text = """
main {
    sub start() {
        str name1 = "name1"
        str name2 = "name2"
        uword[] @split names = [name1, name2, "name3"]
        cx16.r0++
        names = [1111,2222,3333]
    } 
}"""
        compileText(C64Target(), false, text, writeAssembly = true) shouldNotBe null
        compileText(VMTarget(), false, text, writeAssembly = true) shouldNotBe null
    }

    test("array target with expression for index") {
        val text = """
main {
    sub start() {
        ubyte[] array = [1,2,3]
        array[cx16.r0L+1] += 42
        cx16.r0L = array[cx16.r0L+1]
    } 
}"""
        compileText(VMTarget(), false, text, writeAssembly = true) shouldNotBe null
        compileText(C64Target(), false, text, writeAssembly = true) shouldNotBe null
    }

    test("split array in zeropage is okay") {
        val text = """
main {
    sub start() {
        uword[3] @zp @split @shared thearray
    } 
}"""
        val result = compileText(C64Target(), false, text, writeAssembly = true)!!
        val assemblyFile = result.compilationOptions.outputDir.resolve(result.compilerAst.name + ".asm")
        val assembly = assemblyFile.readText()
        assembly shouldContain "thearray_lsb"
        assembly shouldContain "thearray_msb"
    }

    test("indexing str or pointervar with expression") {
        val text = """
main {
    sub start() {
        str name = "thing"
        modify(name)

        sub modify(str arg) {
            ubyte n=1
            uword pointervar
            arg[n+1] = arg[1]
            pointervar[n+1] = pointervar[1]
        }
    }
}"""
        compileText(C64Target(), false, text, writeAssembly = true) shouldNotBe null
    }
})

