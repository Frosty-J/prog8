; Prog8 definitions for the Commodore PET
; Including memory registers, I/O registers, Basic and Kernal subroutines.
; see: https://www.pagetable.com/?p=926  ,  http://www.zimmers.net/cbmpics/cbm/PETx/petmem.txt

cbm {
    ; Commodore (CBM) common variables, vectors and kernal routines
    %option no_symbol_prefixing

        &ubyte  TIME_HI         = $8d       ; software jiffy clock, hi byte
        &ubyte  TIME_MID        = $8e       ;  .. mid byte
        &ubyte  TIME_LO         = $8f       ;    .. lo byte. Updated by IRQ every 1/60 sec
        &ubyte  STATUS          = $96       ; kernal status variable for I/O

        &uword  CINV            = $0090     ; IRQ vector (in ram)
        &uword  CBINV           = $0092     ; BRK vector (in ram)
        &uword  NMINV           = $0094     ; NMI vector (in ram)

        &uword  NMI_VEC         = $FFFA     ; 6502 nmi vector, determined by the kernal if banked in
        &uword  RESET_VEC       = $FFFC     ; 6502 reset vector, determined by the kernal if banked in
        &uword  IRQ_VEC         = $FFFE     ; 6502 interrupt vector, determined by the kernal if banked in

        ; the default addresses for the character screen chars and colors
        const  uword  Screen    = $8000     ; to have this as an array[40*25] the compiler would have to support array size > 255


romsub $FFC6 = CHKIN(ubyte logical @ X) clobbers(A,X) -> bool @Pc    ; define an input channel
romsub $FFC9 = CHKOUT(ubyte logical @ X) clobbers(A,X)          ; define an output channel
romsub $FFCC = CLRCHN() clobbers(A,X)                           ; restore default devices
romsub $FFCF = CHRIN() clobbers(X, Y) -> ubyte @ A              ; input a character (for keyboard, read a whole line from the screen) A=byte read.
romsub $FFD2 = CHROUT(ubyte character @ A)                           ; output a character
romsub $FFE1 = STOP() clobbers(X) -> bool @ Pz, ubyte @ A       ; check the STOP key (and some others in A)
romsub $FFE4 = GETIN() clobbers(X,Y) -> bool @Pc, ubyte @ A     ; get a character
romsub $FFE7 = CLALL() clobbers(A,X)                            ; close all files
romsub $FFEA = UDTIM() clobbers(A,X)                            ; update the software clock

asmsub STOP2() clobbers(X) -> ubyte @A  {
    ; -- check if STOP key was pressed, returns true if so.  More convenient to use than STOP() because that only sets the carry status flag.
    %asm {{
        jsr  cbm.STOP
        beq  +
        lda  #0
        rts
+       lda  #1
        rts
    }}
}

asmsub SETTIM(ubyte low @ A, ubyte middle @ X, ubyte high @ Y) {
    ; PET stub to set the software clock
    %asm {{
        sty  TIME_HI
        stx  TIME_MID
        sta  TIME_LO
        rts
    }}
}

asmsub RDTIM() -> ubyte @ A, ubyte @ X, ubyte @ Y {
    ; PET stub to read the software clock (A=lo,X=mid,Y=high)
    %asm {{
        ldy  TIME_HI
        ldx  TIME_MID
        lda  TIME_LO
        rts
    }}
}

asmsub RDTIM16() clobbers(X) -> uword @AY {
    ; --  like RDTIM() but only returning the lower 16 bits in AY for convenience
    %asm {{
        lda  TIME_LO
        ldy  TIME_MID
        rts
    }}
}


}

sys {
    ; ------- lowlevel system routines --------
    %option no_symbol_prefixing

    const ubyte target = 32         ;  compilation target specifier.  64 = C64, 128 = C128,  16 = CommanderX16, 32=PET

asmsub  init_system()  {
    ; Initializes the machine to a sane starting state.
    ; Called automatically by the loader program logic.
    ; Uppercase charset is activated.
    %asm {{
        sei
        cld
        lda  #142
        jsr  cbm.CHROUT     ; uppercase
        lda  #147
        jsr  cbm.CHROUT     ; clear screen
        clc
        clv
        cli
        rts
    }}
}

asmsub  init_system_phase2()  {
    %asm {{
        rts     ; no phase 2 steps on the PET
    }}
}

asmsub  cleanup_at_exit() {
    ; executed when the main subroutine does rts
    %asm {{
        rts
    }}
}

    asmsub  reset_system()  {
        ; Soft-reset the system back to initial power-on Basic prompt.
        %asm {{
            sei
            jmp  (cbm.RESET_VEC)
        }}
    }

    asmsub waitvsync() clobbers(A) {
        ; --- busy wait till the next vsync has occurred (approximately), without depending on custom irq handling.
        ;     Note: on PET this simply waits until the next jiffy clock update, I don't know if a true vsync is possible there
        %asm {{
            lda  #1
            ldy  #0
            jmp  wait
        }}
    }

    asmsub wait(uword jiffies @AY) {
        ; --- wait approximately the given number of jiffies (1/60th seconds) (N or N+1)
        ;     note: the system irq handler has to be active for this to work as it depends on the system jiffy clock
        %asm {{
            stx  P8ZP_SCRATCH_B1
            sta  P8ZP_SCRATCH_W1
            sty  P8ZP_SCRATCH_W1+1

_loop       lda  P8ZP_SCRATCH_W1
            ora  P8ZP_SCRATCH_W1+1
            bne  +
            ldx  P8ZP_SCRATCH_B1
            rts

+           lda  cbm.TIME_LO
            sta  P8ZP_SCRATCH_B1
-           lda  cbm.TIME_LO
            cmp  P8ZP_SCRATCH_B1
            beq  -

            lda  P8ZP_SCRATCH_W1
            bne  +
            dec  P8ZP_SCRATCH_W1+1
+           dec  P8ZP_SCRATCH_W1
            jmp  _loop
        }}
    }

    asmsub internal_stringcopy(uword source @R0, uword target @AY) clobbers (A,Y) {
        ; Called when the compiler wants to assign a string value to another string.
        %asm {{
		sta  P8ZP_SCRATCH_W1
		sty  P8ZP_SCRATCH_W1+1
		lda  cx16.r0
		ldy  cx16.r0+1
		jmp  prog8_lib.strcpy
        }}
    }

    asmsub memcopy(uword source @R0, uword target @R1, uword count @AY) clobbers(A,X,Y) {
        ; note: only works for NON-OVERLAPPING memory regions!
        ; note: can't be inlined because is called from asm as well
        %asm {{
            ldx  cx16.r0
            stx  P8ZP_SCRATCH_W1        ; source in ZP
            ldx  cx16.r0+1
            stx  P8ZP_SCRATCH_W1+1
            ldx  cx16.r1
            stx  P8ZP_SCRATCH_W2        ; target in ZP
            ldx  cx16.r1+1
            stx  P8ZP_SCRATCH_W2+1
            cpy  #0
            bne  _longcopy

            ; copy <= 255 bytes
            tay
            bne  _copyshort
            rts     ; nothing to copy

_copyshort
            ; decrease source and target pointers so we can simply index by Y
            lda  P8ZP_SCRATCH_W1
            bne  +
            dec  P8ZP_SCRATCH_W1+1
+           dec  P8ZP_SCRATCH_W1
            lda  P8ZP_SCRATCH_W2
            bne  +
            dec  P8ZP_SCRATCH_W2+1
+           dec  P8ZP_SCRATCH_W2

-           lda  (P8ZP_SCRATCH_W1),y
            sta  (P8ZP_SCRATCH_W2),y
            dey
            bne  -
            rts

_longcopy
            sta  P8ZP_SCRATCH_B1        ; lsb(count) = remainder in last page
            tya
            tax                         ; x = num pages (1+)
            ldy  #0
-           lda  (P8ZP_SCRATCH_W1),y
            sta  (P8ZP_SCRATCH_W2),y
            iny
            bne  -
            inc  P8ZP_SCRATCH_W1+1
            inc  P8ZP_SCRATCH_W2+1
            dex
            bne  -
            ldy  P8ZP_SCRATCH_B1
            bne  _copyshort
            rts
        }}
    }

    asmsub memset(uword mem @R0, uword numbytes @R1, ubyte value @A) clobbers(A,X,Y) {
        %asm {{
            ldy  cx16.r0
            sty  P8ZP_SCRATCH_W1
            ldy  cx16.r0+1
            sty  P8ZP_SCRATCH_W1+1
            ldx  cx16.r1
            ldy  cx16.r1+1
            jmp  prog8_lib.memset
        }}
    }

    asmsub memsetw(uword mem @R0, uword numwords @R1, uword value @AY) clobbers(A,X,Y) {
        %asm {{
            ldx  cx16.r0
            stx  P8ZP_SCRATCH_W1
            ldx  cx16.r0+1
            stx  P8ZP_SCRATCH_W1+1
            ldx  cx16.r1
            stx  P8ZP_SCRATCH_W2
            ldx  cx16.r1+1
            stx  P8ZP_SCRATCH_W2+1
            jmp  prog8_lib.memsetw
        }}
    }

    inline asmsub read_flags() -> ubyte @A {
        %asm {{
        php
        pla
        }}
    }

    inline asmsub clear_carry() {
        %asm {{
        clc
        }}
    }

    inline asmsub set_carry() {
        %asm {{
        sec
        }}
    }

    inline asmsub clear_irqd() {
        %asm {{
        cli
        }}
    }

    inline asmsub set_irqd() {
        %asm {{
        sei
        }}
    }

    inline asmsub irqsafe_set_irqd() {
        %asm {{
        php
        sei
        }}
    }

    inline asmsub irqsafe_clear_irqd() {
        %asm {{
        plp
        }}
    }

    inline asmsub exit(ubyte returnvalue @A) {
        ; -- immediately exit the program with a return code in the A register
        %asm {{
            ldx  prog8_lib.orig_stackpointer
            txs
            rts		; return to original caller
        }}
    }

    inline asmsub progend() -> uword @AY {
        %asm {{
            lda  #<prog8_program_end
            ldy  #>prog8_program_end
        }}
    }

}

cx16 {
    %option no_symbol_prefixing

    ; the sixteen virtual 16-bit registers that the CX16 has defined in the zeropage
    ; they are simulated on the PET as well but their location in memory is different
    ; (because there's no room for them in the zeropage)
    ; we select the top page of RAM (assume 32Kb)
    &uword r0  = $7fe0
    &uword r1  = $7fe2
    &uword r2  = $7fe4
    &uword r3  = $7fe6
    &uword r4  = $7fe8
    &uword r5  = $7fea
    &uword r6  = $7fec
    &uword r7  = $7fee
    &uword r8  = $7ff0
    &uword r9  = $7ff2
    &uword r10 = $7ff4
    &uword r11 = $7ff6
    &uword r12 = $7ff8
    &uword r13 = $7ffa
    &uword r14 = $7ffc
    &uword r15 = $7ffe

    &word r0s  = $7fe0
    &word r1s  = $7fe2
    &word r2s  = $7fe4
    &word r3s  = $7fe6
    &word r4s  = $7fe8
    &word r5s  = $7fea
    &word r6s  = $7fec
    &word r7s  = $7fee
    &word r8s  = $7ff0
    &word r9s  = $7ff2
    &word r10s = $7ff4
    &word r11s = $7ff6
    &word r12s = $7ff8
    &word r13s = $7ffa
    &word r14s = $7ffc
    &word r15s = $7ffe

    &ubyte r0L  = $7fe0
    &ubyte r1L  = $7fe2
    &ubyte r2L  = $7fe4
    &ubyte r3L  = $7fe6
    &ubyte r4L  = $7fe8
    &ubyte r5L  = $7fea
    &ubyte r6L  = $7fec
    &ubyte r7L  = $7fee
    &ubyte r8L  = $7ff0
    &ubyte r9L  = $7ff2
    &ubyte r10L = $7ff4
    &ubyte r11L = $7ff6
    &ubyte r12L = $7ff8
    &ubyte r13L = $7ffa
    &ubyte r14L = $7ffc
    &ubyte r15L = $7ffe

    &ubyte r0H  = $7fe1
    &ubyte r1H  = $7fe3
    &ubyte r2H  = $7fe5
    &ubyte r3H  = $7fe7
    &ubyte r4H  = $7fe9
    &ubyte r5H  = $7feb
    &ubyte r6H  = $7fed
    &ubyte r7H  = $7fef
    &ubyte r8H  = $7ff1
    &ubyte r9H  = $7ff3
    &ubyte r10H = $7ff5
    &ubyte r11H = $7ff7
    &ubyte r12H = $7ff9
    &ubyte r13H = $7ffb
    &ubyte r14H = $7ffd
    &ubyte r15H = $7fff

    &byte r0sL  = $7fe0
    &byte r1sL  = $7fe2
    &byte r2sL  = $7fe4
    &byte r3sL  = $7fe6
    &byte r4sL  = $7fe8
    &byte r5sL  = $7fea
    &byte r6sL  = $7fec
    &byte r7sL  = $7fee
    &byte r8sL  = $7ff0
    &byte r9sL  = $7ff2
    &byte r10sL = $7ff4
    &byte r11sL = $7ff6
    &byte r12sL = $7ff8
    &byte r13sL = $7ffa
    &byte r14sL = $7ffc
    &byte r15sL = $7ffe

    &byte r0sH  = $7fe1
    &byte r1sH  = $7fe3
    &byte r2sH  = $7fe5
    &byte r3sH  = $7fe7
    &byte r4sH  = $7fe9
    &byte r5sH  = $7feb
    &byte r6sH  = $7fed
    &byte r7sH  = $7fef
    &byte r8sH  = $7ff1
    &byte r9sH  = $7ff3
    &byte r10sH = $7ff5
    &byte r11sH = $7ff7
    &byte r12sH = $7ff9
    &byte r13sH = $7ffb
    &byte r14sH = $7ffd
    &byte r15sH = $7fff

    asmsub save_virtual_registers() clobbers(A,Y) {
        %asm {{
            ldy  #31
    -       lda  cx16.r0,y
            sta  _cx16_vreg_storage,y
            dey
            bpl  -
            rts

    _cx16_vreg_storage
            .word 0,0,0,0,0,0,0,0
            .word 0,0,0,0,0,0,0,0
        }}
    }

    asmsub restore_virtual_registers() clobbers(A,Y) {
        %asm {{
            ldy  #31
    -       lda  save_virtual_registers._cx16_vreg_storage,y
            sta  cx16.r0,y
            dey
            bpl  -
            rts
        }}
    }
}