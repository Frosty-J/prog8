package prog8tests.ast

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import prog8.code.target.C64Target
import prog8.code.target.Cx16Target
import prog8.code.target.VMTarget
import prog8tests.helpers.ErrorReporterForTests
import prog8tests.helpers.compileText


class TestAstChecks: FunSpec({

    test("conditional expression w/float works") {
        val text = """
            %import floats
            main {
                sub start() {
                    uword xx
                    if xx+99.99 == xx+1.234 {
                        xx++
                    }
                }
            }
        """
        val errors = ErrorReporterForTests(keepMessagesAfterReporting = true)
        compileText(C64Target(), true, text, writeAssembly = true, errors=errors) shouldNotBe null
        errors.errors.size shouldBe 0
        errors.warnings.size shouldBe 2
        errors.warnings[0] shouldContain "converted to float"
        errors.warnings[1] shouldContain "converted to float"
    }

    test("can't assign label or subroutine without using address-of") {
        val text = """
            main {
                sub start() {
            
            label:
                    uword @shared addr
                    addr = label
                    addr = thing
                    addr = &label
                    addr = &thing
                }
            
                sub thing() {
                }
            }
            """
        val errors = ErrorReporterForTests()
        compileText(C64Target(), true, text, writeAssembly = true, errors=errors) shouldBe null
        errors.errors.size shouldBe 2
        errors.warnings.size shouldBe 0
        errors.errors[0] shouldContain ":7:28: invalid assignment value, maybe forgot '&'"
        errors.errors[1] shouldContain ":8:28: invalid assignment value, maybe forgot '&'"
    }

    test("can't do str or array expression without using address-of") {
        val text = """
            %import textio
            main {
                sub start() {
                    ubyte[] array = [1,2,3,4]
                    str s1 = "test"
                    ubyte ff = 1
                    txt.print(s1+ff)
                    txt.print(array+ff)
                    txt.print_uwhex(s1+ff, true)
                    txt.print_uwhex(array+ff, true)
                }
            }
            """
        val errors = ErrorReporterForTests()
        compileText(C64Target(), false, text, writeAssembly = false, errors=errors) shouldBe null
        errors.errors.filter { it.contains("missing &") }.size shouldBe 4
    }

    test("str or array expression with address-of") {
        val text = """
            %import textio
            main {
                sub start() {
                    ubyte[] array = [1,2,3,4]
                    str s1 = "test"
                    bool bb1, bb2
                    ubyte ff = 1
                    txt.print(&s1+ff)
                    txt.print(&array+ff)
                    txt.print_uwhex(&s1+ff, true)
                    txt.print_uwhex(&array+ff, true)
                    ; also good:
                    bb1 = (s1 == "derp")
                    bb2 = (s1 != "derp")
                }
            }
            """
        compileText(C64Target(), false, text, writeAssembly = false) shouldNotBe null
    }

    test("const is not allowed on arrays") {
        val text = """
            main {
                sub start() {
                    const ubyte[5] a = [1,2,3,4,5]
                    a[2]=42
                }
            }
        """
        val errors = ErrorReporterForTests(keepMessagesAfterReporting = true)
        compileText(C64Target(), true, text, writeAssembly = true, errors=errors)
        errors.errors.size shouldBe 1
        errors.warnings.size shouldBe 0
        errors.errors[0] shouldContain "const can only be used"
    }

    test("array indexing is not allowed on a memory mapped variable") {
        val text = """
            main {
                sub start() {
                    &ubyte a = 10000
                    uword z = 500
                    a[4] = (z % 3) as ubyte
                }
            }
        """
        val errors = ErrorReporterForTests(keepMessagesAfterReporting = true)
        compileText(C64Target(), true, text, writeAssembly = true, errors=errors)
        errors.errors.size shouldBe 1
        errors.warnings.size shouldBe 0
        errors.errors[0] shouldContain "indexing requires"
    }

    test("unicode in identifier names is working") {
        val text = """
%import floats

main {
    ubyte приблизительно = 99
    ubyte นี่คือตัวอักษรภาษาไท = 42
    
    sub start() {
        str knäckebröd = "crunchy"  ; with composed form
        prt(knäckebröd)             ; with decomposed form
        printf(2*floats.π)
    }

    sub prt(str message) {
        приблизительно++
    }

    sub printf(float fl) {
        นี่คือตัวอักษรภาษาไท++
    }
}"""
        compileText(C64Target(), false, text, writeAssembly = true)  shouldNotBe null
        compileText(Cx16Target(), false, text, writeAssembly = true)  shouldNotBe null
        compileText(VMTarget(), false, text, writeAssembly = true)  shouldNotBe null
    }

    test("return with a statement instead of a value is a syntax error") {
        val src="""
main {

    sub invalid() {
        return cx16.r0++
    }

    sub start() {
        invalid()
    }
}"""
        val errors=ErrorReporterForTests()
        compileText(C64Target(), false, src, writeAssembly = false, errors=errors)  shouldBe null
        errors.errors.size shouldBe 1
        errors.errors[0] shouldContain "statement"
    }

    test("redefined variable name in single declaration is reported") {
        val src="""
main {
    sub start() {
        const ubyte count=11
        cx16.r0++
        ubyte count = 88        ; redefinition
        cx16.r0 = count
    }
}"""
        val errors=ErrorReporterForTests()
        compileText(C64Target(), false, src, writeAssembly = false, errors=errors)  shouldBe null
        errors.errors.size shouldBe 1
        errors.errors[0] shouldContain "name conflict"

        errors.clear()
        compileText(C64Target(), true, src, writeAssembly = false, errors=errors)  shouldBe null
        errors.errors.size shouldBe 1
        errors.errors[0] shouldContain "name conflict"
    }

    test("redefined variable name in multi declaration is reported") {
        val src="""
main {
    sub start() {
        ubyte i
        i++
        ubyte i, j              ; redefinition
        i++
        j++
    }
}
"""
        val errors=ErrorReporterForTests()
        compileText(C64Target(), false, src, writeAssembly = false, errors=errors)  shouldBe null
        errors.errors.size shouldBe 1
        errors.errors[0] shouldContain "name conflict"

        errors.clear()
        compileText(C64Target(), true, src, writeAssembly = false, errors=errors)  shouldBe null
        errors.errors.size shouldBe 1
        errors.errors[0] shouldContain "name conflict"
    }

    test("various range datatype checks allow differences in type") {
        val src="""
main {
    sub func() -> ubyte {
        cx16.r0++
        return cx16.r0L
    }

    sub start() {
        bool[256] @shared cells
        word starw
        byte bb
        uword uw
        ubyte ub

        starw = (240-64 as word) + func()

        for starw in 50 downto 10  {
            cx16.r0++
        }
        for starw in cx16.r0L downto 10  {
            cx16.r0++
        }

        for ub in 0 to len(cells)-1 {
            cx16.r0++
        }
        for ub in cx16.r0L to len(cells)-1 {
            cx16.r0++
        }
        for bb in 50 downto 10  {
            cx16.r0++
        }
        for bb in cx16.r0sL downto 10  {
            cx16.r0++
        }

        for starw in 500 downto 10  {
            cx16.r0++
        }
        for uw in 50 downto 10 {
            cx16.r0++
        }
        for uw in 500 downto 10 {
            cx16.r0++
        }
    }
}"""
        compileText(C64Target(), false, src, writeAssembly = false) shouldNotBe null
        compileText(C64Target(), true, src, writeAssembly = false) shouldNotBe null
    }
})
