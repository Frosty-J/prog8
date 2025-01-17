; Prog8 definitions for the Commodore-64
; Including memory registers, I/O registers, Basic and Kernal subroutines.


cbm {

    ; Commodore (CBM) common variables, vectors and kernal routines

    %option no_symbol_prefixing

        &ubyte  TIME_HI         = $a0       ; software jiffy clock, hi byte
        &ubyte  TIME_MID        = $a1       ;  .. mid byte
        &ubyte  TIME_LO         = $a2       ;    .. lo byte. Updated by IRQ every 1/60 sec
        &ubyte  STATUS          = $90       ; kernal status variable for I/O
        &ubyte  STKEY           = $91       ; various keyboard statuses (updated by IRQ)
        &ubyte  SFDX            = $cb       ; current key pressed (matrix value) (updated by IRQ)
        &ubyte  SHFLAG          = $028d     ; various modifier key status (updated by IRQ)

        &ubyte  COLOR           = $0286     ; cursor color
        &ubyte  HIBASE          = $0288     ; screen base address / 256 (hi-byte of screen memory address)

        &uword  IERROR          = $0300
        &uword  IMAIN           = $0302
        &uword  ICRNCH          = $0304
        &uword  IQPLOP          = $0306
        &uword  IGONE           = $0308
        &uword  IEVAL           = $030a
        &ubyte  SAREG           = $030c     ; register storage for A for SYS calls
        &ubyte  SXREG           = $030d     ; register storage for X for SYS calls
        &ubyte  SYREG           = $030e     ; register storage for Y for SYS calls
        &ubyte  SPREG           = $030f     ; register storage for P (status register) for SYS calls
        &uword  USRADD          = $0311     ; vector for the USR() basic command
        ; $0313 is unused.
        &uword  CINV            = $0314     ; IRQ vector (in ram)
        &uword  CBINV           = $0316     ; BRK vector (in ram)
        &uword  NMINV           = $0318     ; NMI vector (in ram)
        &uword  IOPEN           = $031a
        &uword  ICLOSE          = $031c
        &uword  ICHKIN          = $031e
        &uword  ICKOUT          = $0320
        &uword  ICLRCH          = $0322
        &uword  IBASIN          = $0324
        &uword  IBSOUT          = $0326
        &uword  ISTOP           = $0328
        &uword  IGETIN          = $032a
        &uword  ICLALL          = $032c
        &uword  USERCMD         = $032e
        &uword  ILOAD           = $0330
        &uword  ISAVE           = $0332

        &uword  NMI_VEC         = $FFFA     ; 6502 nmi vector, determined by the kernal if banked in
        &uword  RESET_VEC       = $FFFC     ; 6502 reset vector, determined by the kernal if banked in
        &uword  IRQ_VEC         = $FFFE     ; 6502 interrupt vector, determined by the kernal if banked in

        ; the default addresses for the character screen chars and colors
        const  uword  Screen    = $0400     ; to have this as an array[40*25] the compiler would have to support array size > 255
        const  uword  Colors    = $d800     ; to have this as an array[40*25] the compiler would have to support array size > 255


; ---- CBM ROM kernal routines (C64 addresses) ----

romsub $AB1E = STROUT(uword strptr @ AY) clobbers(A, X, Y)      ; print null-terminated string (use txt.print instead)
romsub $E544 = CLEARSCR() clobbers(A,X,Y)                       ; clear the screen
romsub $E566 = HOMECRSR() clobbers(A,X,Y)                       ; cursor to top left of screen
romsub $EA31 = IRQDFRT() clobbers(A,X,Y)                        ; default IRQ routine
romsub $EA81 = IRQDFEND() clobbers(A,X,Y)                       ; default IRQ end/cleanup
romsub $FF81 = CINT() clobbers(A,X,Y)                           ; (alias: SCINIT) initialize screen editor and video chip
romsub $FF84 = IOINIT() clobbers(A, X)                          ; initialize I/O devices (CIA, SID, IRQ)
romsub $FF87 = RAMTAS() clobbers(A,X,Y)                         ; initialize RAM, tape buffer, screen
romsub $FF8A = RESTOR() clobbers(A,X,Y)                         ; restore default I/O vectors
romsub $FF8D = VECTOR(uword userptr @ XY, bool dir @ Pc) clobbers(A,Y)     ; read/set I/O vector table
romsub $FF90 = SETMSG(ubyte value @ A)                          ; set Kernal message control flag
romsub $FF93 = SECOND(ubyte address @ A) clobbers(A)            ; (alias: LSTNSA) send secondary address after LISTEN
romsub $FF96 = TKSA(ubyte address @ A) clobbers(A)              ; (alias: TALKSA) send secondary address after TALK
romsub $FF99 = MEMTOP(uword address @ XY, bool dir @ Pc) -> uword @ XY     ; read/set top of memory  pointer
romsub $FF9C = MEMBOT(uword address @ XY, bool dir @ Pc) -> uword @ XY     ; read/set bottom of memory  pointer
romsub $FF9F = SCNKEY() clobbers(A,X,Y)                         ; scan the keyboard
romsub $FFA2 = SETTMO(ubyte timeout @ A)                        ; set time-out flag for IEEE bus
romsub $FFA5 = ACPTR() -> ubyte @ A                             ; (alias: IECIN) input byte from serial bus
romsub $FFA8 = CIOUT(ubyte databyte @ A)                        ; (alias: IECOUT) output byte to serial bus
romsub $FFAB = UNTLK() clobbers(A)                              ; command serial bus device to UNTALK
romsub $FFAE = UNLSN() clobbers(A)                              ; command serial bus device to UNLISTEN
romsub $FFB1 = LISTEN(ubyte device @ A) clobbers(A)             ; command serial bus device to LISTEN
romsub $FFB4 = TALK(ubyte device @ A) clobbers(A)               ; command serial bus device to TALK
romsub $FFB7 = READST() -> ubyte @ A                            ; read I/O status word
romsub $FFBA = SETLFS(ubyte logical @ A, ubyte device @ X, ubyte secondary @ Y)   ; set logical file parameters
romsub $FFBD = SETNAM(ubyte namelen @ A, str filename @ XY)     ; set filename parameters
romsub $FFC0 = OPEN() clobbers(X,Y) -> bool @Pc, ubyte @A      ; (via 794 ($31A)) open a logical file
romsub $FFC3 = CLOSE(ubyte logical @ A) clobbers(A,X,Y)         ; (via 796 ($31C)) close a logical file
romsub $FFC6 = CHKIN(ubyte logical @ X) clobbers(A,X) -> bool @Pc    ; (via 798 ($31E)) define an input channel
romsub $FFC9 = CHKOUT(ubyte logical @ X) clobbers(A,X)          ; (via 800 ($320)) define an output channel
romsub $FFCC = CLRCHN() clobbers(A,X)                           ; (via 802 ($322)) restore default devices
romsub $FFCF = CHRIN() clobbers(X, Y) -> ubyte @ A   ; (via 804 ($324)) input a character (for keyboard, read a whole line from the screen) A=byte read.
romsub $FFD2 = CHROUT(ubyte character @ A)                      ; (via 806 ($326)) output a character
romsub $FFD5 = LOAD(ubyte verify @ A, uword address @ XY) -> bool @Pc, ubyte @ A, uword @ XY     ; (via 816 ($330)) load from device
romsub $FFD8 = SAVE(ubyte zp_startaddr @ A, uword endaddr @ XY) -> bool @ Pc, ubyte @ A          ; (via 818 ($332)) save to a device
romsub $FFDB = SETTIM(ubyte low @ A, ubyte middle @ X, ubyte high @ Y)      ; set the software clock
romsub $FFDE = RDTIM() -> ubyte @ A, ubyte @ X, ubyte @ Y       ; read the software clock (A=lo,X=mid,Y=high)
romsub $FFE1 = STOP() clobbers(X) -> bool @ Pz, ubyte @ A      ; (via 808 ($328)) check the STOP key (and some others in A)
romsub $FFE4 = GETIN() clobbers(X,Y) -> bool @Pc, ubyte @ A    ; (via 810 ($32A)) get a character
romsub $FFE7 = CLALL() clobbers(A,X)                            ; (via 812 ($32C)) close all files
romsub $FFEA = UDTIM() clobbers(A,X)                            ; update the software clock
romsub $FFED = SCREEN() -> ubyte @ X, ubyte @ Y                 ; read number of screen rows and columns
romsub $FFF0 = PLOT(ubyte col @ Y, ubyte row @ X, bool dir @ Pc) -> ubyte @ X, ubyte @ Y       ; read/set position of cursor on screen.  Use txt.plot for a 'safe' wrapper that preserves X.
romsub $FFF3 = IOBASE() -> uword @ XY                           ; read base address of I/O devices

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

asmsub RDTIM16() clobbers(X) -> uword @AY {
    ; --  like RDTIM() but only returning the lower 16 bits in AY for convenience
    %asm {{
        jsr  cbm.RDTIM
        pha
        txa
        tay
        pla
        rts
    }}
}

}

c64 {
        ; C64 I/O registers (VIC, SID, CIA)

    %option no_symbol_prefixing

        ; the default locations of the 8 sprite pointers (store address of sprite / 64)
        ; (depending on the VIC bank and screen ram address selection these can be shifted around though,
        ; see the two routines after this for a dynamic way of determining the correct memory location)
        &ubyte  SPRPTR0         = 2040
        &ubyte  SPRPTR1         = 2041
        &ubyte  SPRPTR2         = 2042
        &ubyte  SPRPTR3         = 2043
        &ubyte  SPRPTR4         = 2044
        &ubyte  SPRPTR5         = 2045
        &ubyte  SPRPTR6         = 2046
        &ubyte  SPRPTR7         = 2047
        &ubyte[8]  SPRPTR       = 2040      ; the 8 sprite pointers as an array.


; ---- VIC-II 6567/6569/856x registers ----

        &ubyte  SP0X            = $d000
        &ubyte  SP0Y            = $d001
        &ubyte  SP1X            = $d002
        &ubyte  SP1Y            = $d003
        &ubyte  SP2X            = $d004
        &ubyte  SP2Y            = $d005
        &ubyte  SP3X            = $d006
        &ubyte  SP3Y            = $d007
        &ubyte  SP4X            = $d008
        &ubyte  SP4Y            = $d009
        &ubyte  SP5X            = $d00a
        &ubyte  SP5Y            = $d00b
        &ubyte  SP6X            = $d00c
        &ubyte  SP6Y            = $d00d
        &ubyte  SP7X            = $d00e
        &ubyte  SP7Y            = $d00f
        &ubyte[16]  SPXY        = $d000        ; the 8 sprite X and Y registers as an array.
        &uword[8]  SPXYW        = $d000        ; the 8 sprite X and Y registers as a combined xy word array.

        &ubyte  MSIGX           = $d010
        &ubyte  SCROLY          = $d011
        &ubyte  RASTER          = $d012
        &ubyte  LPENX           = $d013
        &ubyte  LPENY           = $d014
        &ubyte  SPENA           = $d015
        &ubyte  SCROLX          = $d016
        &ubyte  YXPAND          = $d017
        &ubyte  VMCSB           = $d018
        &ubyte  VICIRQ          = $d019
        &ubyte  IREQMASK        = $d01a
        &ubyte  SPBGPR          = $d01b
        &ubyte  SPMC            = $d01c
        &ubyte  XXPAND          = $d01d
        &ubyte  SPSPCL          = $d01e
        &ubyte  SPBGCL          = $d01f

        &ubyte  EXTCOL          = $d020        ; border color
        &ubyte  BGCOL0          = $d021        ; screen color
        &ubyte  BGCOL1          = $d022
        &ubyte  BGCOL2          = $d023
        &ubyte  BGCOL4          = $d024
        &ubyte  SPMC0           = $d025
        &ubyte  SPMC1           = $d026
        &ubyte  SP0COL          = $d027
        &ubyte  SP1COL          = $d028
        &ubyte  SP2COL          = $d029
        &ubyte  SP3COL          = $d02a
        &ubyte  SP4COL          = $d02b
        &ubyte  SP5COL          = $d02c
        &ubyte  SP6COL          = $d02d
        &ubyte  SP7COL          = $d02e
        &ubyte[8]  SPCOL        = $d027


; ---- end of VIC-II registers ----

; ---- CIA 6526 1 & 2 registers ----

        &ubyte  CIA1PRA         = $DC00        ; CIA 1 DRA, keyboard column drive (and joystick control port #2)
        &ubyte  CIA1PRB         = $DC01        ; CIA 1 DRB, keyboard row port (and joystick control port #1)
        &ubyte  CIA1DDRA        = $DC02        ; CIA 1 DDRA, keyboard column
        &ubyte  CIA1DDRB        = $DC03        ; CIA 1 DDRB, keyboard row
        &ubyte  CIA1TAL         = $DC04        ; CIA 1 timer A low byte
        &ubyte  CIA1TAH         = $DC05        ; CIA 1 timer A high byte
        &ubyte  CIA1TBL         = $DC06        ; CIA 1 timer B low byte
        &ubyte  CIA1TBH         = $DC07        ; CIA 1 timer B high byte
        &ubyte  CIA1TOD10       = $DC08        ; time of day, 1/10 sec.
        &ubyte  CIA1TODSEC      = $DC09        ; time of day, seconds
        &ubyte  CIA1TODMMIN     = $DC0A        ; time of day, minutes
        &ubyte  CIA1TODHR       = $DC0B        ; time of day, hours
        &ubyte  CIA1SDR         = $DC0C        ; Serial Data Register
        &ubyte  CIA1ICR         = $DC0D
        &ubyte  CIA1CRA         = $DC0E
        &ubyte  CIA1CRB         = $DC0F

        &ubyte  CIA2PRA         = $DD00        ; CIA 2 DRA, serial port and video address
        &ubyte  CIA2PRB         = $DD01        ; CIA 2 DRB, RS232 port / USERPORT
        &ubyte  CIA2DDRA        = $DD02        ; CIA 2 DDRA, serial port and video address
        &ubyte  CIA2DDRB        = $DD03        ; CIA 2 DDRB, RS232 port / USERPORT
        &ubyte  CIA2TAL         = $DD04        ; CIA 2 timer A low byte
        &ubyte  CIA2TAH         = $DD05        ; CIA 2 timer A high byte
        &ubyte  CIA2TBL         = $DD06        ; CIA 2 timer B low byte
        &ubyte  CIA2TBH         = $DD07        ; CIA 2 timer B high byte
        &ubyte  CIA2TOD10       = $DD08        ; time of day, 1/10 sec.
        &ubyte  CIA2TODSEC      = $DD09        ; time of day, seconds
        &ubyte  CIA2TODMIN      = $DD0A        ; time of day, minutes
        &ubyte  CIA2TODHR       = $DD0B        ; time of day, hours
        &ubyte  CIA2SDR         = $DD0C        ; Serial Data Register
        &ubyte  CIA2ICR         = $DD0D
        &ubyte  CIA2CRA         = $DD0E
        &ubyte  CIA2CRB         = $DD0F

; ---- end of CIA registers ----

; ---- SID 6581/8580 registers ----

        &ubyte  FREQLO1         = $D400        ; channel 1 freq lo
        &ubyte  FREQHI1         = $D401        ; channel 1 freq hi
        &uword  FREQ1           = $D400        ; channel 1 freq (word)
        &ubyte  PWLO1           = $D402        ; channel 1 pulse width lo (7-0)
        &ubyte  PWHI1           = $D403        ; channel 1 pulse width hi (11-8)
        &uword  PW1             = $D402        ; channel 1 pulse width (word)
        &ubyte  CR1             = $D404        ; channel 1 voice control register
        &ubyte  AD1             = $D405        ; channel 1 attack & decay
        &ubyte  SR1             = $D406        ; channel 1 sustain & release
        &ubyte  FREQLO2         = $D407        ; channel 2 freq lo
        &ubyte  FREQHI2         = $D408        ; channel 2 freq hi
        &uword  FREQ2           = $D407        ; channel 2 freq (word)
        &ubyte  PWLO2           = $D409        ; channel 2 pulse width lo (7-0)
        &ubyte  PWHI2           = $D40A        ; channel 2 pulse width hi (11-8)
        &uword  PW2             = $D409        ; channel 2 pulse width (word)
        &ubyte  CR2             = $D40B        ; channel 2 voice control register
        &ubyte  AD2             = $D40C        ; channel 2 attack & decay
        &ubyte  SR2             = $D40D        ; channel 2 sustain & release
        &ubyte  FREQLO3         = $D40E        ; channel 3 freq lo
        &ubyte  FREQHI3         = $D40F        ; channel 3 freq hi
        &uword  FREQ3           = $D40E        ; channel 3 freq (word)
        &ubyte  PWLO3           = $D410        ; channel 3 pulse width lo (7-0)
        &ubyte  PWHI3           = $D411        ; channel 3 pulse width hi (11-8)
        &uword  PW3             = $D410        ; channel 3 pulse width (word)
        &ubyte  CR3             = $D412        ; channel 3 voice control register
        &ubyte  AD3             = $D413        ; channel 3 attack & decay
        &ubyte  SR3             = $D414        ; channel 3 sustain & release
        &ubyte  FCLO            = $D415        ; filter cutoff lo (2-0)
        &ubyte  FCHI            = $D416        ; filter cutoff hi (10-3)
        &uword  FC              = $D415        ; filter cutoff (word)
        &ubyte  RESFILT         = $D417        ; filter resonance and routing
        &ubyte  MVOL            = $D418        ; filter mode and main volume control
        &ubyte  POTX            = $D419        ; potentiometer X
        &ubyte  POTY            = $D41A        ; potentiometer Y
        &ubyte  OSC3            = $D41B        ; channel 3 oscillator value read
        &ubyte  ENV3            = $D41C        ; channel 3 envelope value read

; ---- end of SID registers ----


    sub get_vic_memory_base() -> uword {
        ; one of the 4 possible banks. $0000/$4000/$8000/$c000.
        c64.CIA2DDRA |= %11
        return ((c64.CIA2PRA & 3) ^ 3) as uword << 14
    }

    sub get_char_matrix_ptr() -> uword {
        ; Usually the character screen matrix is at 1024-2039 (see above)
        ; However the vic memory configuration can be altered and this moves these registers with it.
        ; So this routine determines it dynamically from the VIC memory setup.
        uword chars_matrix_offset = (c64.VMCSB & $f0) as uword << 6
        return get_vic_memory_base() + chars_matrix_offset
    }

    sub get_bitmap_ptr() -> uword {
        return get_vic_memory_base() + ((c64.VMCSB & %00001000) as uword << 10)
    }

    sub get_sprite_addr_ptrs() -> uword {
        ; Usually the sprite address pointers are at addresses 2040-2047 (see above)
        ; However the vic memory configuration can be altered and this moves these registers with it.
        ; So this routine determines it dynamically from the VIC memory setup.
        return get_char_matrix_ptr() + 1016
    }

    sub set_sprite_ptr(ubyte sprite_num, uword sprite_data_address) {
        ; Sets the sprite data pointer to the given address.
        ; Because it takes some time to calculate things based on the vic memory setup,
        ; its only suitable if you're not continuously changing the data address.
        ; Otherwise store the correct sprite data pointer location somewhere yourself and reuse it.
        @(get_sprite_addr_ptrs() + sprite_num) = lsb(sprite_data_address / 64)
    }
}

sys {
    ; ------- lowlevel system routines --------

    %option no_symbol_prefixing

    const ubyte target = 64         ;  compilation target specifier.  64 = C64, 128 = C128, 16 = CommanderX16.


asmsub  init_system()  {
    ; Initializes the machine to a sane starting state.
    ; Called automatically by the loader program logic.
    ; This means that the BASIC, KERNAL and CHARGEN ROMs are banked in,
    ; the VIC, SID and CIA chips are reset, screen is cleared, and the default IRQ is set.
    ; Also a different color scheme is chosen to identify ourselves a little.
    ; Uppercase charset is activated.
    %asm {{
        sei
        cld
        lda  #%00101111
        sta  $00
        lda  #%00100111
        sta  $01
        jsr  cbm.IOINIT
        jsr  cbm.RESTOR
        jsr  cbm.CINT
        lda  #6
        sta  c64.EXTCOL
        lda  #7
        sta  cbm.COLOR
        lda  #0
        sta  c64.BGCOL0
        jsr  disable_runstop_and_charsetswitch
        clc
        clv
        cli
        rts
    }}
}

asmsub  init_system_phase2()  {
    %asm {{
        rts     ; no phase 2 steps on the C64
    }}
}

asmsub  cleanup_at_exit() {
    ; executed when the main subroutine does rts
    %asm {{
        jmp  sys.enable_runstop_and_charsetswitch
    }}
}

asmsub  disable_runstop_and_charsetswitch() clobbers(A) {
    %asm {{
        lda  #$80
        sta  657    ; disable charset switching
        lda  #239
        sta  808    ; disable run/stop key
        rts
    }}
}

asmsub  enable_runstop_and_charsetswitch() clobbers(A) {
    %asm {{
        lda  #0
        sta  657    ; enable charset switching
        lda  #237
        sta  808    ; enable run/stop key
        rts
    }}
}

    asmsub save_prog8_internals() {
        %asm {{
            lda  P8ZP_SCRATCH_B1
            sta  save_SCRATCH_ZPB1
            lda  P8ZP_SCRATCH_REG
            sta  save_SCRATCH_ZPREG
            lda  P8ZP_SCRATCH_W1
            sta  save_SCRATCH_ZPWORD1
            lda  P8ZP_SCRATCH_W1+1
            sta  save_SCRATCH_ZPWORD1+1
            lda  P8ZP_SCRATCH_W2
            sta  save_SCRATCH_ZPWORD2
            lda  P8ZP_SCRATCH_W2+1
            sta  save_SCRATCH_ZPWORD2+1
            rts
save_SCRATCH_ZPB1	.byte  0
save_SCRATCH_ZPREG	.byte  0
save_SCRATCH_ZPWORD1	.word  0
save_SCRATCH_ZPWORD2	.word  0
        }}
    }

    asmsub restore_prog8_internals() {
        %asm {{
            lda  save_prog8_internals.save_SCRATCH_ZPB1
            sta  P8ZP_SCRATCH_B1
            lda  save_prog8_internals.save_SCRATCH_ZPREG
            sta  P8ZP_SCRATCH_REG
            lda  save_prog8_internals.save_SCRATCH_ZPWORD1
            sta  P8ZP_SCRATCH_W1
            lda  save_prog8_internals.save_SCRATCH_ZPWORD1+1
            sta  P8ZP_SCRATCH_W1+1
            lda  save_prog8_internals.save_SCRATCH_ZPWORD2
            sta  P8ZP_SCRATCH_W2
            lda  save_prog8_internals.save_SCRATCH_ZPWORD2+1
            sta  P8ZP_SCRATCH_W2+1
            rts
        }}
    }

asmsub  set_irq(uword handler @AY) clobbers(A)  {
	%asm {{
	    sei
        sta  _modified+1
        sty  _modified+2
		lda  #<_irq_handler
		sta  cbm.CINV
		lda  #>_irq_handler
		sta  cbm.CINV+1
		cli
		rts
_irq_handler
        jsr  sys.save_prog8_internals
        cld
_modified
        jsr  $ffff                      ; modified
        pha
		jsr  sys.restore_prog8_internals
		pla
		beq  +
		jmp  cbm.IRQDFRT		; continue with normal kernal irq routine
+		lda  #$ff
		sta  c64.VICIRQ			; acknowledge raster irq
		lda  c64.CIA1ICR		; acknowledge CIA1 interrupt
		pla
		tay
		pla
		tax
		pla
		rti
    }}
}

asmsub  restore_irq() clobbers(A) {
	%asm {{
		sei
		lda  #<cbm.IRQDFRT
		sta  cbm.CINV
		lda  #>cbm.IRQDFRT
		sta  cbm.CINV+1
		lda  #0
		sta  c64.IREQMASK	; disable raster irq
		lda  #%10000001
		sta  c64.CIA1ICR	; restore CIA1 irq
		cli
		rts
	}}
}

asmsub  set_rasterirq(uword handler @AY, uword rasterpos @R0) clobbers(A) {
	%asm {{
	    sei
        sta  _modified+1
        sty  _modified+2
        lda  cx16.r0
        ldy  cx16.r0+1
        jsr  _setup_raster_irq
        lda  #<_raster_irq_handler
        sta  cbm.CINV
        lda  #>_raster_irq_handler
        sta  cbm.CINV+1
        cli
        rts

_raster_irq_handler
		jsr  sys.save_prog8_internals
		cld
_modified
        jsr  $ffff              ; modified
        pha
        jsr  sys.restore_prog8_internals
        lda  #$ff
        sta  c64.VICIRQ			; acknowledge raster irq
        pla
        beq  +
		jmp  cbm.IRQDFRT                ; continue with kernal irq routine
+		pla
		tay
		pla
		tax
		pla
		rti

_setup_raster_irq
		pha
		lda  #%01111111
		sta  c64.CIA1ICR    ; "switch off" interrupts signals from cia-1
		sta  c64.CIA2ICR    ; "switch off" interrupts signals from cia-2
		and  c64.SCROLY
		sta  c64.SCROLY     ; clear most significant bit of raster position
		lda  c64.CIA1ICR    ; ack previous irq
		lda  c64.CIA2ICR    ; ack previous irq
		pla
		sta  c64.RASTER     ; set the raster line number where interrupt should occur
		cpy  #0
		beq  +
		lda  c64.SCROLY
		ora  #%10000000
		sta  c64.SCROLY     ; set most significant bit of raster position
+		lda  #%00000001
		sta  c64.IREQMASK   ;enable raster interrupt signals from vic
		rts
	}}
}


    asmsub reset_system()  {
        ; Soft-reset the system back to initial power-on Basic prompt.
        %asm {{
            sei
            lda  #14
            sta  $01        ; bank the kernal in
            jmp  (cbm.RESET_VEC)
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

    asmsub waitvsync() clobbers(A) {
        ; --- busy wait till the next vsync has occurred (approximately), without depending on custom irq handling.
        ;     note: a more accurate way to wait for vsync is to set up a vsync irq handler instead.
        %asm {{
-           bit  c64.SCROLY
            bpl  -
-           bit  c64.SCROLY
            bmi  -
            rts
        }}
    }

    inline asmsub waitrastborder() {
        ; --- busy wait till the raster position has reached the bottom screen border (approximately)
        ;     note: a more accurate way to do this is by using a raster irq handler instead.
        %asm {{
-           bit  c64.SCROLY
            bpl  -
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

    inline asmsub disable_caseswitch() {
        %asm {{
            lda  #$80
            sta  657
        }}
    }

    inline asmsub enable_caseswitch() {
        %asm {{
            lda  #0
            sta  657
        }}
    }

    inline asmsub exit(ubyte returnvalue @A) {
        ; -- immediately exit the program with a return code in the A register
        %asm {{
            lda  #31
            sta  $01        ; bank the kernal in
            jsr  cbm.CLRCHN		; reset i/o channels
            jsr  sys.enable_runstop_and_charsetswitch
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
    ; they are simulated on the C64 as well but their location in memory is different
    ; (because there's no room for them in the zeropage in the default configuration)
    ; Note that when using ZP options that free up more of the zeropage (such as %zeropage kernalsafe)
    ; there might be enough space to put them there after all, and the compiler will change these addresses!
    &uword r0  = $cfe0
    &uword r1  = $cfe2
    &uword r2  = $cfe4
    &uword r3  = $cfe6
    &uword r4  = $cfe8
    &uword r5  = $cfea
    &uword r6  = $cfec
    &uword r7  = $cfee
    &uword r8  = $cff0
    &uword r9  = $cff2
    &uword r10 = $cff4
    &uword r11 = $cff6
    &uword r12 = $cff8
    &uword r13 = $cffa
    &uword r14 = $cffc
    &uword r15 = $cffe

    &word r0s  = $cfe0
    &word r1s  = $cfe2
    &word r2s  = $cfe4
    &word r3s  = $cfe6
    &word r4s  = $cfe8
    &word r5s  = $cfea
    &word r6s  = $cfec
    &word r7s  = $cfee
    &word r8s  = $cff0
    &word r9s  = $cff2
    &word r10s = $cff4
    &word r11s = $cff6
    &word r12s = $cff8
    &word r13s = $cffa
    &word r14s = $cffc
    &word r15s = $cffe

    &ubyte r0L  = $cfe0
    &ubyte r1L  = $cfe2
    &ubyte r2L  = $cfe4
    &ubyte r3L  = $cfe6
    &ubyte r4L  = $cfe8
    &ubyte r5L  = $cfea
    &ubyte r6L  = $cfec
    &ubyte r7L  = $cfee
    &ubyte r8L  = $cff0
    &ubyte r9L  = $cff2
    &ubyte r10L = $cff4
    &ubyte r11L = $cff6
    &ubyte r12L = $cff8
    &ubyte r13L = $cffa
    &ubyte r14L = $cffc
    &ubyte r15L = $cffe

    &ubyte r0H  = $cfe1
    &ubyte r1H  = $cfe3
    &ubyte r2H  = $cfe5
    &ubyte r3H  = $cfe7
    &ubyte r4H  = $cfe9
    &ubyte r5H  = $cfeb
    &ubyte r6H  = $cfed
    &ubyte r7H  = $cfef
    &ubyte r8H  = $cff1
    &ubyte r9H  = $cff3
    &ubyte r10H = $cff5
    &ubyte r11H = $cff7
    &ubyte r12H = $cff9
    &ubyte r13H = $cffb
    &ubyte r14H = $cffd
    &ubyte r15H = $cfff

    &byte r0sL  = $cfe0
    &byte r1sL  = $cfe2
    &byte r2sL  = $cfe4
    &byte r3sL  = $cfe6
    &byte r4sL  = $cfe8
    &byte r5sL  = $cfea
    &byte r6sL  = $cfec
    &byte r7sL  = $cfee
    &byte r8sL  = $cff0
    &byte r9sL  = $cff2
    &byte r10sL = $cff4
    &byte r11sL = $cff6
    &byte r12sL = $cff8
    &byte r13sL = $cffa
    &byte r14sL = $cffc
    &byte r15sL = $cffe

    &byte r0sH  = $cfe1
    &byte r1sH  = $cfe3
    &byte r2sH  = $cfe5
    &byte r3sH  = $cfe7
    &byte r4sH  = $cfe9
    &byte r5sH  = $cfeb
    &byte r6sH  = $cfed
    &byte r7sH  = $cfef
    &byte r8sH  = $cff1
    &byte r9sH  = $cff3
    &byte r10sH = $cff5
    &byte r11sH = $cff7
    &byte r12sH = $cff9
    &byte r13sH = $cffb
    &byte r14sH = $cffd
    &byte r15sH = $cfff

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
