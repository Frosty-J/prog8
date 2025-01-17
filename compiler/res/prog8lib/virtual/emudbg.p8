; Emulator debug interface. (reflecting the Commander X16 emudbg library module)
; Docs: https://github.com/X16Community/x16-emulator#emulator-io-registers

%import textio

emudbg {

    sub is_emulator() -> bool {
        ; Test for emulator presence.
        return true  ; VM is always 'emulator'
    }

    sub console_write(str message) {
        txt.print("[EMUDBG: ")
        txt.print(message)
        txt.chrout(']')
    }

    sub console_chrout(ubyte char) {
        txt.chrout(char)
    }

    sub console_value1(ubyte value) {
        txt.print("[EMUDBG debug 1: ")
        txt.print_uwhex(value, true)
        txt.print("]\n")
    }

    sub console_value2(ubyte value) {
        txt.print("[EMUDBG debug 2: ")
        txt.print_uwhex(value, true)
        txt.print("]\n")
    }
}
