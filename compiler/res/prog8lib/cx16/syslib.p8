; Prog8 definitions for the CommanderX16
; Including memory registers, I/O registers, Basic and Kernal subroutines.

cbm {

    ; Commodore (CBM) common variables, vectors and kernal routines

    %option no_symbol_prefixing

; STROUT --> use txt.print
; CLEARSCR -> use txt.clear_screen
; HOMECRSR -> use txt.home or txt.plot

romsub $FF81 = CINT() clobbers(A,X,Y)                           ; (alias: SCINIT) initialize screen editor and video chip, including resetting to the default color palette
romsub $FF84 = IOINIT() clobbers(A, X)                          ; initialize I/O devices (CIA, IRQ, ...)
romsub $FF87 = RAMTAS() clobbers(A,X,Y)                         ; initialize RAM, screen
romsub $FF8A = RESTOR() clobbers(A,X,Y)                         ; restore default I/O vectors
romsub $FF8D = VECTOR(uword userptr @ XY, bool dir @ Pc) clobbers(A,Y)     ; read/set I/O vector table
romsub $FF90 = SETMSG(ubyte value @ A)                          ; set Kernal message control flag
romsub $FF93 = SECOND(ubyte address @ A) clobbers(A)            ; (alias: LSTNSA) send secondary address after LISTEN
romsub $FF96 = TKSA(ubyte address @ A) clobbers(A)              ; (alias: TALKSA) send secondary address after TALK
romsub $FF99 = MEMTOP(uword address @ XY, bool dir @ Pc) -> uword @ XY     ; read/set top of memory  pointer.   NOTE: as a Cx16 extension, also returns the number of RAM memory banks in register A !  See cx16.numbanks()
romsub $FF9C = MEMBOT(uword address @ XY, bool dir @ Pc) -> uword @ XY     ; read/set bottom of memory  pointer
romsub $FF9F = SCNKEY() clobbers(A,X,Y)                         ; scan the keyboard, also called  kbd_scan
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
romsub $FFD2 = CHROUT(ubyte character @ A)                           ; (via 806 ($326)) output a character
romsub $FFD5 = LOAD(ubyte verify @ A, uword address @ XY) -> bool @Pc, ubyte @ A, uword @ XY     ; (via 816 ($330)) load from device
romsub $FFD8 = SAVE(ubyte zp_startaddr @ A, uword endaddr @ XY) clobbers (X, Y) -> bool @ Pc, ubyte @ A       ; (via 818 ($332)) save to a device.  See also BSAVE
romsub $FFDB = SETTIM(ubyte low @ A, ubyte middle @ X, ubyte high @ Y)      ; set the software clock
romsub $FFDE = RDTIM() -> ubyte @ A, ubyte @ X, ubyte @ Y       ; read the software clock (A=lo,X=mid,Y=high)
romsub $FFE1 = STOP() clobbers(X) -> bool @ Pz, ubyte @ A      ; (via 808 ($328)) check the STOP key (and some others in A)
romsub $FFE4 = GETIN() clobbers(X,Y) -> bool @Pc, ubyte @ A    ; (via 810 ($32A)) get a character
romsub $FFE7 = CLALL() clobbers(A,X)                            ; (via 812 ($32C)) close all files
romsub $FFEA = UDTIM() clobbers(A,X)                            ; update the software clock
romsub $FFED = SCREEN() -> ubyte @ X, ubyte @ Y                 ; read number of screen rows and columns
romsub $FFF0 = PLOT(ubyte col @ Y, ubyte row @ X, bool dir @ Pc) -> ubyte @ X, ubyte @ Y       ; read/set position of cursor on screen.  Use txt.plot for a 'safe' wrapper that preserves X.
romsub $FFF3 = IOBASE() -> uword @ XY                           ; read base address of I/O devices

; ---- utility

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
    ; --  like RDTIM() but only returning the lower 16 bits in AY for convenience. Also avoids ram bank issue for irqs.
    %asm {{
        php
        sei
        jsr  cbm.RDTIM
        plp
        cli
        pha
        txa
        tay
        pla
        rts
    }}
}

}

cx16 {

    %option no_symbol_prefixing

; irq, system and hardware vectors:
    &uword  IERROR      = $0300
    &uword  IMAIN       = $0302
    &uword  ICRNCH      = $0304
    &uword  IQPLOP      = $0306
    &uword  IGONE       = $0308
    &uword  IEVAL       = $030a
    &ubyte  SAREG       = $030c     ; register storage for A for SYS calls
    &ubyte  SXREG       = $030d     ; register storage for X for SYS calls
    &ubyte  SYREG       = $030e     ; register storage for Y for SYS calls
    &ubyte  SPREG       = $030f     ; register storage for P (status register) for SYS calls
    &uword  USRADD      = $0311     ; vector for the USR() basic command
    ; $0313 is unused.
    &uword  CINV        = $0314     ; IRQ vector (in ram)
    &uword  CBINV       = $0316     ; BRK vector (in ram)
    &uword  NMINV       = $0318     ; NMI vector (in ram)
    &uword  IOPEN       = $031a
    &uword  ICLOSE      = $031c
    &uword  ICHKIN      = $031e
    &uword  ICKOUT      = $0320
    &uword  ICLRCH      = $0322
    &uword  IBASIN      = $0324
    &uword  IBSOUT      = $0326
    &uword  ISTOP       = $0328
    &uword  IGETIN      = $032a
    &uword  ICLALL      = $032c
    &uword  KEYHDL      = $032e     ; keyboard scan code handler see examples/cx16/keyboardhandler.p8
    &uword  ILOAD       = $0330
    &uword  ISAVE       = $0332
    &uword  NMI_VEC     = $FFFA     ; 65c02 nmi vector, determined by the kernal if banked in
    &uword  RESET_VEC   = $FFFC     ; 65c02 reset vector, determined by the kernal if banked in
    &uword  IRQ_VEC     = $FFFE     ; 65c02 interrupt vector, determined by the kernal if banked in

    &uword  edkeyvec    = $ac03     ; for intercepting BASIN/CHRIN key strokes. See set_basin_handler()
    &uword  edkeybk     = $ac05     ; ...the RAM bank of this routine if not in low ram


; the sixteen virtual 16-bit registers in both normal unsigned mode and signed mode (s)
    &uword r0  = $0002
    &uword r1  = $0004
    &uword r2  = $0006
    &uword r3  = $0008
    &uword r4  = $000a
    &uword r5  = $000c
    &uword r6  = $000e
    &uword r7  = $0010
    &uword r8  = $0012
    &uword r9  = $0014
    &uword r10 = $0016
    &uword r11 = $0018
    &uword r12 = $001a
    &uword r13 = $001c
    &uword r14 = $001e
    &uword r15 = $0020

    &word r0s  = $0002
    &word r1s  = $0004
    &word r2s  = $0006
    &word r3s  = $0008
    &word r4s  = $000a
    &word r5s  = $000c
    &word r6s  = $000e
    &word r7s  = $0010
    &word r8s  = $0012
    &word r9s  = $0014
    &word r10s = $0016
    &word r11s = $0018
    &word r12s = $001a
    &word r13s = $001c
    &word r14s = $001e
    &word r15s = $0020

    &ubyte r0L  = $0002
    &ubyte r1L  = $0004
    &ubyte r2L  = $0006
    &ubyte r3L  = $0008
    &ubyte r4L  = $000a
    &ubyte r5L  = $000c
    &ubyte r6L  = $000e
    &ubyte r7L  = $0010
    &ubyte r8L  = $0012
    &ubyte r9L  = $0014
    &ubyte r10L = $0016
    &ubyte r11L = $0018
    &ubyte r12L = $001a
    &ubyte r13L = $001c
    &ubyte r14L = $001e
    &ubyte r15L = $0020

    &ubyte r0H  = $0003
    &ubyte r1H  = $0005
    &ubyte r2H  = $0007
    &ubyte r3H  = $0009
    &ubyte r4H  = $000b
    &ubyte r5H  = $000d
    &ubyte r6H  = $000f
    &ubyte r7H  = $0011
    &ubyte r8H  = $0013
    &ubyte r9H  = $0015
    &ubyte r10H = $0017
    &ubyte r11H = $0019
    &ubyte r12H = $001b
    &ubyte r13H = $001d
    &ubyte r14H = $001f
    &ubyte r15H = $0021

    &byte r0sL  = $0002
    &byte r1sL  = $0004
    &byte r2sL  = $0006
    &byte r3sL  = $0008
    &byte r4sL  = $000a
    &byte r5sL  = $000c
    &byte r6sL  = $000e
    &byte r7sL  = $0010
    &byte r8sL  = $0012
    &byte r9sL  = $0014
    &byte r10sL = $0016
    &byte r11sL = $0018
    &byte r12sL = $001a
    &byte r13sL = $001c
    &byte r14sL = $001e
    &byte r15sL = $0020

    &byte r0sH  = $0003
    &byte r1sH  = $0005
    &byte r2sH  = $0007
    &byte r3sH  = $0009
    &byte r4sH  = $000b
    &byte r5sH  = $000d
    &byte r6sH  = $000f
    &byte r7sH  = $0011
    &byte r8sH  = $0013
    &byte r9sH  = $0015
    &byte r10sH = $0017
    &byte r11sH = $0019
    &byte r12sH = $001b
    &byte r13sH = $001d
    &byte r14sH = $001f
    &byte r15sH = $0021

; VERA registers

    const uword VERA_BASE       = $9F20
    &ubyte  VERA_ADDR_L         = VERA_BASE + $0000
    &ubyte  VERA_ADDR_M         = VERA_BASE + $0001
    &ubyte  VERA_ADDR_H         = VERA_BASE + $0002
    &ubyte  VERA_DATA0          = VERA_BASE + $0003
    &ubyte  VERA_DATA1          = VERA_BASE + $0004
    &ubyte  VERA_CTRL           = VERA_BASE + $0005
    &ubyte  VERA_IEN            = VERA_BASE + $0006
    &ubyte  VERA_ISR            = VERA_BASE + $0007
    &ubyte  VERA_IRQLINE_L      = VERA_BASE + $0008 ; write only
    &ubyte  VERA_SCANLINE_L     = VERA_BASE + $0008 ; read only
    &ubyte  VERA_DC_VIDEO       = VERA_BASE + $0009 ; DCSEL= 0
    &ubyte  VERA_DC_HSCALE      = VERA_BASE + $000A ; DCSEL= 0
    &ubyte  VERA_DC_VSCALE      = VERA_BASE + $000B ; DCSEL= 0
    &ubyte  VERA_DC_BORDER      = VERA_BASE + $000C ; DCSEL= 0
    &ubyte  VERA_DC_HSTART      = VERA_BASE + $0009 ; DCSEL= 1
    &ubyte  VERA_DC_HSTOP       = VERA_BASE + $000A ; DCSEL= 1
    &ubyte  VERA_DC_VSTART      = VERA_BASE + $000B ; DCSEL= 1
    &ubyte  VERA_DC_VSTOP       = VERA_BASE + $000C ; DCSEL= 1
    &ubyte  VERA_DC_VER0        = VERA_BASE + $0009 ; DCSEL=63
    &ubyte  VERA_DC_VER1        = VERA_BASE + $000A ; DCSEL=63
    &ubyte  VERA_DC_VER2        = VERA_BASE + $000B ; DCSEL=63
    &ubyte  VERA_DC_VER3        = VERA_BASE + $000C ; DCSEL=63
    &ubyte  VERA_L0_CONFIG      = VERA_BASE + $000D
    &ubyte  VERA_L0_MAPBASE     = VERA_BASE + $000E
    &ubyte  VERA_L0_TILEBASE    = VERA_BASE + $000F
    &ubyte  VERA_L0_HSCROLL_L   = VERA_BASE + $0010
    &ubyte  VERA_L0_HSCROLL_H   = VERA_BASE + $0011
    &ubyte  VERA_L0_VSCROLL_L   = VERA_BASE + $0012
    &ubyte  VERA_L0_VSCROLL_H   = VERA_BASE + $0013
    &ubyte  VERA_L1_CONFIG      = VERA_BASE + $0014
    &ubyte  VERA_L1_MAPBASE     = VERA_BASE + $0015
    &ubyte  VERA_L1_TILEBASE    = VERA_BASE + $0016
    &ubyte  VERA_L1_HSCROLL_L   = VERA_BASE + $0017
    &ubyte  VERA_L1_HSCROLL_H   = VERA_BASE + $0018
    &ubyte  VERA_L1_VSCROLL_L   = VERA_BASE + $0019
    &ubyte  VERA_L1_VSCROLL_H   = VERA_BASE + $001A
    &ubyte  VERA_AUDIO_CTRL     = VERA_BASE + $001B
    &ubyte  VERA_AUDIO_RATE     = VERA_BASE + $001C
    &ubyte  VERA_AUDIO_DATA     = VERA_BASE + $001D
    &ubyte  VERA_SPI_DATA       = VERA_BASE + $001E
    &ubyte  VERA_SPI_CTRL       = VERA_BASE + $001F

    ; experimental Vera FX registers: (depends on particular value set in VERA_CTRL!!!)
    &ubyte VERA_FX_CTRL         = VERA_BASE + $0009
    &ubyte VERA_FX_MULT         = VERA_BASE + $000C
    &ubyte VERA_FX_CACHE_L      = VERA_BASE + $0009
    &ubyte VERA_FX_CACHE_M      = VERA_BASE + $000A
    &ubyte VERA_FX_CACHE_H      = VERA_BASE + $000B
    &ubyte VERA_FX_CACHE_U      = VERA_BASE + $000C
    &ubyte VERA_FX_ACCUM_RESET  = VERA_BASE + $0009     ; (DCSEL=6)


; VERA_PSG_BASE     = $1F9C0
; VERA_PALETTE_BASE = $1FA00
; VERA_SPRITES_BASE = $1FC00

; I/O

    const uword  VIA1_BASE   = $9f00                  ;VIA 6522 #1
    &ubyte  via1prb    = VIA1_BASE + 0
    &ubyte  via1pra    = VIA1_BASE + 1
    &ubyte  via1ddrb   = VIA1_BASE + 2
    &ubyte  via1ddra   = VIA1_BASE + 3
    &ubyte  via1t1l    = VIA1_BASE + 4
    &ubyte  via1t1h    = VIA1_BASE + 5
    &ubyte  via1t1ll   = VIA1_BASE + 6
    &ubyte  via1t1lh   = VIA1_BASE + 7
    &ubyte  via1t2l    = VIA1_BASE + 8
    &ubyte  via1t2h    = VIA1_BASE + 9
    &ubyte  via1sr     = VIA1_BASE + 10
    &ubyte  via1acr    = VIA1_BASE + 11
    &ubyte  via1pcr    = VIA1_BASE + 12
    &ubyte  via1ifr    = VIA1_BASE + 13
    &ubyte  via1ier    = VIA1_BASE + 14
    &ubyte  via1ora    = VIA1_BASE + 15

    const uword  VIA2_BASE   = $9f10                  ;VIA 6522 #2
    &ubyte  via2prb    = VIA2_BASE + 0
    &ubyte  via2pra    = VIA2_BASE + 1
    &ubyte  via2ddrb   = VIA2_BASE + 2
    &ubyte  via2ddra   = VIA2_BASE + 3
    &ubyte  via2t1l    = VIA2_BASE + 4
    &ubyte  via2t1h    = VIA2_BASE + 5
    &ubyte  via2t1ll   = VIA2_BASE + 6
    &ubyte  via2t1lh   = VIA2_BASE + 7
    &ubyte  via2t2l    = VIA2_BASE + 8
    &ubyte  via2t2h    = VIA2_BASE + 9
    &ubyte  via2sr     = VIA2_BASE + 10
    &ubyte  via2acr    = VIA2_BASE + 11
    &ubyte  via2pcr    = VIA2_BASE + 12
    &ubyte  via2ifr    = VIA2_BASE + 13
    &ubyte  via2ier    = VIA2_BASE + 14
    &ubyte  via2ora    = VIA2_BASE + 15

; YM-2151 sound chip
    &ubyte  YM_ADDRESS	= $9f40
    &ubyte  YM_DATA	    = $9f41

    const uword  extdev	= $9f60


; ---- Commander X-16 additions on top of C64 kernal routines ----
; spelling of the names is taken from the Commander X-16 rom sources

; supported C128 additions
romsub $ff4a = CLOSE_ALL(ubyte device @A)  clobbers(A,X,Y)
romsub $ff59 = LKUPLA(ubyte la @A)  clobbers(A,X,Y)
romsub $ff5c = LKUPSA(ubyte sa @Y)  clobbers(A,X,Y)
romsub $ff5f = screen_mode(ubyte mode @A, bool getCurrent @Pc)  clobbers(X, Y) -> ubyte @A, bool @Pc        ; note: X,Y size result is not supported, use SCREEN or get_screen_mode routine for that
romsub $ff62 = screen_set_charset(ubyte charset @A, uword charsetptr @XY)  clobbers(A,X,Y)      ; incompatible with C128  dlchr()
; not yet supported: romsub $ff65 = pfkey()  clobbers(A,X,Y)
romsub $ff6e = JSRFAR()  ; following word = address to call, byte after that=rom/ram bank it is in
romsub $ff74 = fetch(ubyte bank @X, ubyte index @Y)  clobbers(X) -> ubyte @A
romsub $ff77 = stash(ubyte data @A, ubyte bank @X, ubyte index @Y)  clobbers(X)
romsub $ff7d = PRIMM()

; It's not documented what registers are clobbered, so we assume the worst for all following kernal routines...:

; high level graphics & fonts
romsub $ff20 = GRAPH_init(uword vectors @R0)  clobbers(A,X,Y)
romsub $ff23 = GRAPH_clear()  clobbers(A,X,Y)
romsub $ff26 = GRAPH_set_window(uword x @R0, uword y @R1, uword width @R2, uword height @R3)  clobbers(A,X,Y)
romsub $ff29 = GRAPH_set_colors(ubyte stroke @A, ubyte fill @X, ubyte background @Y)  clobbers (A,X,Y)
romsub $ff2c = GRAPH_draw_line(uword x1 @R0, uword y1 @R1, uword x2 @R2, uword y2 @R3)  clobbers(A,X,Y)
romsub $ff2f = GRAPH_draw_rect(uword x @R0, uword y @R1, uword width @R2, uword height @R3, uword cornerradius @R4, bool fill @Pc)  clobbers(A,X,Y)
romsub $ff32 = GRAPH_move_rect(uword sx @R0, uword sy @R1, uword tx @R2, uword ty @R3, uword width @R4, uword height @R5)  clobbers(A,X,Y)
romsub $ff35 = GRAPH_draw_oval(uword x @R0, uword y @R1, uword width @R2, uword height @R3, bool fill @Pc)  clobbers(A,X,Y)
romsub $ff38 = GRAPH_draw_image(uword x @R0, uword y @R1, uword ptr @R2, uword width @R3, uword height @R4)  clobbers(A,X,Y)
romsub $ff3b = GRAPH_set_font(uword fontptr @R0)  clobbers(A,X,Y)
romsub $ff3e = GRAPH_get_char_size(ubyte baseline @A, ubyte width @X, ubyte height_or_style @Y, bool is_control @Pc)  clobbers(A,X,Y)
romsub $ff41 = GRAPH_put_char(uword x @R0, uword y @R1, ubyte character @A)  clobbers(A,X,Y)
romsub $ff41 = GRAPH_put_next_char(ubyte character @A)  clobbers(A,X,Y)     ; alias for the routine above that doesn't reset the position of the initial character

; framebuffer
romsub $fef6 = FB_init()  clobbers(A,X,Y)
romsub $fef9 = FB_get_info()  clobbers(X,Y) -> byte @A, uword @R0, uword @R1    ; width=r0, height=r1
romsub $fefc = FB_set_palette(uword pointer @R0, ubyte index @A, ubyte colorcount @X)  clobbers(A,X,Y)
romsub $feff = FB_cursor_position(uword x @R0, uword y @R1)  clobbers(A,X,Y)
romsub $ff02 = FB_cursor_next_line(uword x @R0)  clobbers(A,X,Y)
romsub $ff05 = FB_get_pixel()  clobbers(X,Y) -> ubyte @A
romsub $ff08 = FB_get_pixels(uword pointer @R0, uword count @R1)  clobbers(A,X,Y)
romsub $ff0b = FB_set_pixel(ubyte color @A)  clobbers(A,X,Y)
romsub $ff0e = FB_set_pixels(uword pointer @R0, uword count @R1)  clobbers(A,X,Y)
romsub $ff11 = FB_set_8_pixels(ubyte pattern @A, ubyte color @X)  clobbers(A,X,Y)
romsub $ff14 = FB_set_8_pixels_opaque(ubyte pattern @R0, ubyte mask @A, ubyte color1 @X, ubyte color2 @Y)  clobbers(A,X,Y)
romsub $ff17 = FB_fill_pixels(uword count @R0, uword pstep @R1, ubyte color @A)  clobbers(A,X,Y)
romsub $ff1a = FB_filter_pixels(uword pointer @ R0, uword count @R1)  clobbers(A,X,Y)
romsub $ff1d = FB_move_pixels(uword sx @R0, uword sy @R1, uword tx @R2, uword ty @R3, uword count @R4)  clobbers(A,X,Y)

; misc
romsub $FEBA = BSAVE(ubyte zp_startaddr @ A, uword endaddr @ XY) clobbers (X, Y) -> bool @ Pc, ubyte @ A      ; like cbm.SAVE, but omits the 2-byte prg header
romsub $fec6 = i2c_read_byte(ubyte device @X, ubyte offset @Y) clobbers (X,Y) -> ubyte @A, bool @Pc
romsub $fec9 = i2c_write_byte(ubyte device @X, ubyte offset @Y, ubyte data @A) clobbers (A,X,Y) -> bool @Pc
romsub $fef0 = sprite_set_image(uword pixels @R0, uword mask @R1, ubyte bpp @R2, ubyte number @A, ubyte width @X, ubyte height @Y, bool apply_mask @Pc)  clobbers(A,X,Y) -> bool @Pc
romsub $fef3 = sprite_set_position(uword x @R0, uword y @R1, ubyte number @A)  clobbers(A,X,Y)
romsub $fee4 = memory_fill(uword address @R0, uword num_bytes @R1, ubyte value @A)  clobbers(A,X,Y)
romsub $fee7 = memory_copy(uword source @R0, uword target @R1, uword num_bytes @R2)  clobbers(A,X,Y)
romsub $feea = memory_crc(uword address @R0, uword num_bytes @R1)  clobbers(A,X,Y) -> uword @R2
romsub $feed = memory_decompress(uword input @R0, uword output @R1)  clobbers(A,X,Y) -> uword @R1       ; last address +1 is result in R1
romsub $fedb = console_init(uword x @R0, uword y @R1, uword width @R2, uword height @R3)  clobbers(A,X,Y)
romsub $fede = console_put_char(ubyte character @A, bool wrapping @Pc)  clobbers(A,X,Y)
romsub $fee1 = console_get_char()  clobbers(X,Y) -> ubyte @A
romsub $fed8 = console_put_image(uword pointer @R0, uword width @R1, uword height @R2)  clobbers(A,X,Y)
romsub $fed5 = console_set_paging_message(uword msgptr @R0)  clobbers(A,X,Y)
romsub $fecf = entropy_get() -> ubyte @A, ubyte @X, ubyte @Y
romsub $fecc = monitor()  clobbers(A,X,Y)

romsub $ff44 = MACPTR(ubyte length @A, uword buffer @XY, bool dontAdvance @Pc)  clobbers(A) -> bool @Pc, uword @XY
romsub $feb1 = MCIOUT(ubyte length @A, uword buffer @XY, bool dontAdvance @Pc)  clobbers(A) -> bool @Pc, uword @XY
romsub $ff47 = enter_basic(bool cold_or_warm @Pc)  clobbers(A,X,Y)
romsub $ff4d = clock_set_date_time(uword yearmonth @R0, uword dayhours @R1, uword minsecs @R2, ubyte jiffies @R3)  clobbers(A, X, Y)
romsub $ff50 = clock_get_date_time()  clobbers(A, X, Y)  -> uword @R0, uword @R1, uword @R2, ubyte @R3   ; result registers see clock_set_date_time()

; keyboard, mouse, joystick
; note: also see the kbdbuf_clear() helper routine below!
romsub $febd = kbdbuf_peek() -> ubyte @A, ubyte @X     ; key in A, queue length in X
romsub $febd = kbdbuf_peek2() -> uword @AX             ; alternative to above to not have the hassle to deal with multiple return values
romsub $fec0 = kbdbuf_get_modifiers() -> ubyte @A
romsub $fec3 = kbdbuf_put(ubyte key @A) clobbers(X)
romsub $fed2 = keymap(uword identifier @XY, bool read @Pc) -> bool @Pc
romsub $ff68 = mouse_config(byte shape @A, ubyte resX @X, ubyte resY @Y)  clobbers (A, X, Y)
romsub $ff6b = mouse_get(ubyte zpdataptr @X) -> ubyte @A
romsub $ff71 = mouse_scan()  clobbers(A, X, Y)
romsub $ff53 = joystick_scan()  clobbers(A, X, Y)
romsub $ff56 = joystick_get(ubyte joynr @A) -> ubyte @A, ubyte @X, ubyte @Y
romsub $ff56 = joystick_get2(ubyte joynr @A) clobbers(Y) -> uword @AX   ; alternative to above to not have the hassle to deal with multiple return values

; Audio (rom bank 10)
romsub $C04B = psg_init() clobbers(A,X,Y)                              ; (re)init Vera PSG
romsub $C063 = ym_init() clobbers(A,X,Y) -> bool @Pc                   ; (re)init YM chip
romsub $C066 = ym_loaddefpatches() clobbers(A,X,Y) -> bool @Pc         ; load default YM patches
romsub $C09F = audio_init() clobbers(A,X,Y) -> bool @Pc                ; (re)initialize both vera PSG and YM audio chips
; TODO: add more of the audio routines?


asmsub set_screen_mode(ubyte mode @A) clobbers(A,X,Y) -> bool @Pc {
    ; -- convenience wrapper for screen_mode() to just set a new mode (and return success)
    %asm {{
        clc
        jmp  screen_mode
    }}
}

asmsub get_screen_mode() -> byte @A, byte @X, byte @Y {
    ; -- convenience wrapper for screen_mode() to just get the current mode in A, and size in characters in X+Y
    ;    this does need a piece of inlined asm to call it ans store the result values if you call this from prog8 code
    ;    Note: you can also just do the SEC yourself and simply call screen_mode() directly,
    ;          or use the existing SCREEN kernal routine for just getting the size in characters.
    %asm {{
        sec
        jmp  screen_mode
    }}
}

asmsub kbdbuf_clear() {
    ; -- convenience helper routine to clear the keyboard buffer
    %asm {{
-       jsr  cbm.GETIN
        bne  -
        rts
    }}
}

asmsub mouse_config2(byte shape @A) clobbers (A, X, Y) {
    ; -- convenience wrapper function that handles the screen resolution for mouse_config() for you
    %asm {{
        pha                         ; save shape
        sec
        jsr  cx16.screen_mode       ; set current screen mode and res in A, X, Y
        pla                         ; get shape back
        jmp  cx16.mouse_config
    }}
}

asmsub mouse_pos() clobbers(X) -> ubyte @A {
    ; -- short wrapper around mouse_get() kernal routine:
    ; -- gets the position of the mouse cursor in cx16.r0 and cx16.r1 (x/y coordinate), returns mouse button status.
    %asm {{
        ldx  #cx16.r0
        jmp  cx16.mouse_get
    }}
}


; ---- end of kernal routines ----


; ---- utilities -----

inline asmsub rombank(ubyte bank @A) {
    ; -- set the rom banks
    %asm {{
        sta  $01
    }}
}

inline asmsub rambank(ubyte bank @A) {
    ; -- set the ram bank
    %asm {{
        sta  $00
    }}
}

inline asmsub getrombank() -> ubyte @A {
    ; -- get the current rom bank
    %asm {{
        lda  $01
    }}
}

inline asmsub getrambank() -> ubyte @A {
    ; -- get the current RAM bank
    %asm {{
        lda  $00
    }}
}

asmsub numbanks() clobbers(X) -> uword @AY {
    ; -- Returns the number of available RAM banks according to the kernal (each bank is 8 Kb).
    ;    Note that the number of such banks can be 256 so a word is returned.
    ;    But just looking at the A register (the LSB of the result word) could suffice if you know that A=0 means 256 banks:
    ;    The maximum number of RAM banks in the X16 is currently 256 (2 Megabytes of banked RAM).
    ;    Kernal's MEMTOP routine reports 0 in this case but that doesn't mean 'zero banks', instead it means 256 banks,
    ;    as there is no X16 without at least 1 page of banked RAM. So this routine returns 256 instead of 0.
    %asm {{
        sec
        jsr  cbm.MEMTOP
        ldy  #0
        cmp  #0
        bne  +
        iny
+       rts
    }}
}

asmsub vpeek(ubyte bank @A, uword address @XY) -> ubyte @A {
        ; -- get a byte from VERA's video memory
        ;    note: inefficient when reading multiple sequential bytes!
        %asm {{
                stz  cx16.VERA_CTRL
                and  #1
                sta  cx16.VERA_ADDR_H
                sty  cx16.VERA_ADDR_M
                stx  cx16.VERA_ADDR_L
                lda  cx16.VERA_DATA0
                rts
            }}
}

asmsub vaddr(ubyte bank @A, uword address @R0, ubyte addrsel @R1, byte autoIncrOrDecrByOne @Y) clobbers(A) {
        ; -- setup the VERA's data address register 0 or 1
        ;    with optional auto increment or decrement of 1.
        ;    Note that the vaddr_autoincr() and vaddr_autodecr() routines allow to set all possible strides, not just 1.
        %asm {{
            and  #1
            pha
            lda  cx16.r1
            and  #1
            sta  cx16.VERA_CTRL
            lda  cx16.r0
            sta  cx16.VERA_ADDR_L
            lda  cx16.r0+1
            sta  cx16.VERA_ADDR_M
            pla
            cpy  #0
            bmi  ++
            beq  +
            ora  #%00010000
+           sta  cx16.VERA_ADDR_H
            rts
+           ora  #%00011000
            sta  cx16.VERA_ADDR_H
            rts
        }}
}

asmsub vaddr_clone(ubyte port @A) clobbers (A,X,Y) {
    ; -- clones Vera addresses from the given source port to the other one.
    %asm {{
        sta  VERA_CTRL
        ldx  VERA_ADDR_L
        ldy  VERA_ADDR_H
        phy
        ldy  VERA_ADDR_M
        eor  #1
        sta  VERA_CTRL
        stx  VERA_ADDR_L
        sty  VERA_ADDR_M
        ply
        sty  VERA_ADDR_H
        eor  #1
        sta  VERA_CTRL
        rts
    }}
}

asmsub vaddr_autoincr(ubyte bank @A, uword address @R0, ubyte addrsel @R1, uword autoIncrAmount @R2) clobbers(A,Y) {
        ; -- setup the VERA's data address register 0 or 1
        ;    including setting up optional auto increment amount.
        ;    Specifiying an unsupported amount results in amount of zero. See the Vera docs about what amounts are possible.
        %asm {{
            jsr  _setup
            lda  cx16.r2H
            ora  cx16.r2L
            beq  +
            jsr  _determine_incr_bits
+           ora  P8ZP_SCRATCH_REG
            sta  cx16.VERA_ADDR_H
            rts

_setup      and  #1
            sta  P8ZP_SCRATCH_REG
            lda  cx16.r1
            and  #1
            sta  cx16.VERA_CTRL
            lda  cx16.r0
            sta  cx16.VERA_ADDR_L
            lda  cx16.r0+1
            sta  cx16.VERA_ADDR_M
            rts

_determine_incr_bits
            lda  cx16.r2H
            bne  _large
            lda  cx16.r2L
            ldy  #13
-           cmp  _strides_lsb,y
            beq  +
            dey
            bpl  -
+           tya
            asl  a
            asl  a
            asl  a
            asl  a
            rts
_large      ora  cx16.r2L
            cmp  #1         ; 256
            bne  +
            lda  #9<<4
            rts
+           cmp  #2         ; 512
            bne  +
            lda  #10<<4
            rts
+           cmp  #65        ; 320
            bne  +
            lda  #14<<4
            rts
+           cmp  #130       ; 640
            bne  +
            lda  #15<<4
            rts
+           lda  #0
            rts
_strides_lsb    .byte   0,1,2,4,8,16,32,64,128,255,255,40,80,160,255,255
        }}
}

asmsub vaddr_autodecr(ubyte bank @A, uword address @R0, ubyte addrsel @R1, uword autoDecrAmount @R2) clobbers(A,Y) {
        ; -- setup the VERA's data address register 0 or 1
        ;    including setting up optional auto decrement amount.
        ;    Specifiying an unsupported amount results in amount of zero. See the Vera docs about what amounts are possible.
        %asm {{
            jsr  vaddr_autoincr._setup
            lda  cx16.r2H
            ora  cx16.r2L
            beq  +
            jsr  vaddr_autoincr._determine_incr_bits
            ora  #%00001000         ; autodecrement
+           ora  P8ZP_SCRATCH_REG
            sta  cx16.VERA_ADDR_H
            rts
        }}
}

asmsub vpoke(ubyte bank @A, uword address @R0, ubyte value @Y) clobbers(A) {
    ; -- write a single byte to VERA's video memory
    ;    note: inefficient when writing multiple sequential bytes!
    %asm {{
        stz  cx16.VERA_CTRL
        and  #1
        sta  cx16.VERA_ADDR_H
        lda  cx16.r0
        sta  cx16.VERA_ADDR_L
        lda  cx16.r0+1
        sta  cx16.VERA_ADDR_M
        sty  cx16.VERA_DATA0
        rts
    }}
}

asmsub vpoke_or(ubyte bank @A, uword address @R0, ubyte value @Y) clobbers (A) {
    ; -- or a single byte to the value already in the VERA's video memory at that location
    ;    note: inefficient when writing multiple sequential bytes!
    %asm {{
        stz  cx16.VERA_CTRL
        and  #1
        sta  cx16.VERA_ADDR_H
        lda  cx16.r0
        sta  cx16.VERA_ADDR_L
        lda  cx16.r0+1
        sta  cx16.VERA_ADDR_M
        tya
        ora  cx16.VERA_DATA0
        sta  cx16.VERA_DATA0
        rts
    }}
}

asmsub vpoke_and(ubyte bank @A, uword address @R0, ubyte value @Y) clobbers(A) {
    ; -- and a single byte to the value already in the VERA's video memory at that location
    ;    note: inefficient when writing multiple sequential bytes!
    %asm {{
        stz  cx16.VERA_CTRL
        and  #1
        sta  cx16.VERA_ADDR_H
        lda  cx16.r0
        sta  cx16.VERA_ADDR_L
        lda  cx16.r0+1
        sta  cx16.VERA_ADDR_M
        tya
        and  cx16.VERA_DATA0
        sta  cx16.VERA_DATA0
        rts
    }}
}

asmsub vpoke_xor(ubyte bank @A, uword address @R0, ubyte value @Y) clobbers (A) {
    ; -- xor a single byte to the value already in the VERA's video memory at that location
    ;    note: inefficient when writing multiple sequential bytes!
    %asm {{
        stz  cx16.VERA_CTRL
        and  #1
        sta  cx16.VERA_ADDR_H
        lda  cx16.r0
        sta  cx16.VERA_ADDR_L
        lda  cx16.r0+1
        sta  cx16.VERA_ADDR_M
        tya
        eor  cx16.VERA_DATA0
        sta  cx16.VERA_DATA0
        rts
    }}
}

asmsub vpoke_mask(ubyte bank @A, uword address @R0, ubyte mask @X, ubyte value @Y) clobbers (A) {
    ; -- bitwise or a single byte to the value already in the VERA's video memory at that location
    ;    after applying the and-mask. Note: inefficient when writing multiple sequential bytes!
    %asm {{
        sty  P8ZP_SCRATCH_B1
        stz  cx16.VERA_CTRL
        and  #1
        sta  cx16.VERA_ADDR_H
        lda  cx16.r0
        sta  cx16.VERA_ADDR_L
        lda  cx16.r0+1
        sta  cx16.VERA_ADDR_M
        txa
        and  cx16.VERA_DATA0
        ora  P8ZP_SCRATCH_B1
        sta  cx16.VERA_DATA0
        rts
    }}
}

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


asmsub save_vera_context() clobbers(A) {
    ; -- use this at the start of your IRQ handler if it uses Vera registers, to save the state
    %asm {{
        ; note cannot store this on cpu hardware stack because this gets called as a subroutine
        lda  cx16.VERA_ADDR_L
        sta  _vera_storage
        lda  cx16.VERA_ADDR_M
        sta  _vera_storage+1
        lda  cx16.VERA_ADDR_H
        sta  _vera_storage+2
        lda  cx16.VERA_CTRL
        sta  _vera_storage+3
        eor  #1
        sta  _vera_storage+7
        sta  cx16.VERA_CTRL
        lda  cx16.VERA_ADDR_L
        sta  _vera_storage+4
        lda  cx16.VERA_ADDR_M
        sta  _vera_storage+5
        lda  cx16.VERA_ADDR_H
        sta  _vera_storage+6
        rts
_vera_storage:  .byte 0,0,0,0,0,0,0,0
    }}
}

asmsub restore_vera_context() clobbers(A) {
    ; -- use this at the end of your IRQ handler if it uses Vera registers, to restore the state
    %asm {{
        lda  cx16.save_vera_context._vera_storage+7
        sta  cx16.VERA_CTRL
        lda  cx16.save_vera_context._vera_storage+6
        sta  cx16.VERA_ADDR_H
        lda  cx16.save_vera_context._vera_storage+5
        sta  cx16.VERA_ADDR_M
        lda  cx16.save_vera_context._vera_storage+4
        sta  cx16.VERA_ADDR_L
        lda  cx16.save_vera_context._vera_storage+3
        sta  cx16.VERA_CTRL
        lda  cx16.save_vera_context._vera_storage+2
        sta  cx16.VERA_ADDR_H
        lda  cx16.save_vera_context._vera_storage+1
        sta  cx16.VERA_ADDR_M
        lda  cx16.save_vera_context._vera_storage+0
        sta  cx16.VERA_ADDR_L
        rts
    }}
}


    asmsub set_chrin_keyhandler(ubyte handlerbank @A, uword handler @XY) clobbers(A) {
        ; Install a custom CHRIN (BASIN) key handler. Call this before each line you want to read.
        ; See https://github.com/X16Community/x16-docs/blob/master/X16%20Reference%20-%2002%20-%20Editor.md#custom-basin-petscii-code-override-handler
        %asm {{
            sei
            sta  cx16.edkeybk
            lda  $00
            pha
            stz  $00
            stx  cx16.edkeyvec
            sty  cx16.edkeyvec+1
            pla
            sta  $00
            cli
            rts
        }}
    }


    ; Commander X16 IRQ dispatcher routines

inline asmsub  disable_irqs() clobbers(A) {
    ; Disable all Vera IRQ sources. Note that it does NOT set the CPU IRQ disabled status bit!
    %asm {{
        lda  #%00001111
        trb  cx16.VERA_IEN
    }}
}

asmsub  enable_irq_handlers(bool disable_all_irq_sources @Pc) clobbers(A,Y)  {
    ; Install the "master IRQ handler" that will dispatch IRQs
    ; to the registered handler for each type.  (Only Vera IRQs supported for now).
    ; The handlers don't need to clear its ISR bit, but have to return 0 or 1 in A,
    ; where 1 means: continue with the system IRQ handler, 0 means: don't call that.
	%asm {{
        php
        sei
        bcc  +
        lda  #%00001111
        trb  cx16.VERA_IEN      ; disable all IRQ sources
+       lda  #<_irq_dispatcher
        ldy  #>_irq_dispatcher
        sta  cx16.CINV
        sty  cx16.CINV+1
        plp
        rts

_irq_dispatcher
        ; order of handling: LINE, SPRCOL, AFLOW, VSYNC.
        jsr  sys.save_prog8_internals
        cld
        lda  cx16.VERA_ISR
        and  cx16.VERA_IEN          ; only consider the bits for sources that can actually raise the IRQ

        bit  #2
        beq  +
_mod_line_jump
        jsr  _default_line_handler      ; modified
        ldy  #2
        sty  cx16.VERA_ISR
        bra  _dispatch_end
+
        bit  #4
        beq  +
_mod_sprcol_jump
        jsr  _default_sprcol_handler      ; modified
        ldy  #4
        sty  cx16.VERA_ISR
        bra  _dispatch_end
+
        bit  #8
        beq  +
_mod_aflow_jump
        jsr  _default_aflow_handler      ; modified
        ; note: AFLOW can only be cleared by filling the audio FIFO for at least 1/4. Not via the ISR bit.
        bra  _dispatch_end
+
        bit  #1
        beq  +
_mod_vsync_jump
        jsr  _default_vsync_handler      ; modified
        cmp  #0
        bne  _dispatch_end
        ldy  #1
        sty  cx16.VERA_ISR
        bra  _return_irq
+
        lda  #0
_dispatch_end
        cmp  #0
        beq  _return_irq
        jsr  sys.restore_prog8_internals
		jmp  (sys.restore_irq._orig_irqvec)   ; continue with normal kernal irq routine
_return_irq
        jsr  sys.restore_prog8_internals
		ply
		plx
		pla
		rti

_default_vsync_handler
        lda  #1
        rts
_default_line_handler
        lda  #0
        rts
_default_sprcol_handler
        lda  #0
        rts
_default_aflow_handler
        lda  #0
        rts
    }}
}

asmsub set_vsync_irq_handler(uword address @AY) clobbers(A) {
    ; Sets the VSYNC irq handler to use with enable_irq_handlers().  Also enables VSYNC irqs.
    ; NOTE: unless a proper irq handler is already running, you should enclose this call in set_irqd() / clear_irqd() to avoid system crashes.
    %asm {{
        php
        sei
        sta  enable_irq_handlers._mod_vsync_jump+1
        sty  enable_irq_handlers._mod_vsync_jump+2
        lda  #1
        tsb  cx16.VERA_IEN
        plp
        rts
    }}
}

asmsub set_line_irq_handler(uword rasterline @R0, uword address @AY) clobbers(A,Y) {
    ; Sets the LINE irq handler to use with enable_irq_handlers(), for the given rasterline.  Also enables LINE irqs.
    ; You can use sys.set_rasterline() later to adjust the rasterline on which to trigger.
    ; NOTE: unless a proper irq handler is already running, you should enclose this call in set_irqd() / clear_irqd() to avoid system crashes.
    %asm {{
        php
        sei
        sta  enable_irq_handlers._mod_line_jump+1
        sty  enable_irq_handlers._mod_line_jump+2
        lda  cx16.r0
        ldy  cx16.r0+1
        jsr  sys.set_rasterline
        lda  #2
        tsb  cx16.VERA_IEN
        plp
        rts
    }}
}

asmsub set_sprcol_irq_handler(uword address @AY) clobbers(A) {
    ; Sets the SPRCOL irq handler to use with enable_irq_handlers().  Also enables SPRCOL irqs.
    ; NOTE: unless a proper irq handler is already running, you should enclose this call in set_irqd() / clear_irqd() to avoid system crashes.
    %asm {{
        php
        sei
        sta  enable_irq_handlers._mod_sprcol_jump+1
        sty  enable_irq_handlers._mod_sprcol_jump+2
        lda  #4
        tsb  cx16.VERA_IEN
        plp
        rts
    }}
}

asmsub set_aflow_irq_handler(uword address @AY) clobbers(A) {
    ; Sets the AFLOW irq handler to use with enable_irq_handlers().  Also enables AFLOW irqs.
    ; NOTE: unless a proper irq handler is already running, you should enclose this call in set_irqd() / clear_irqd() to avoid system crashes.
    %asm {{
        php
        sei
        sta  enable_irq_handlers._mod_aflow_jump+1
        sty  enable_irq_handlers._mod_aflow_jump+2
        lda  #8
        tsb  cx16.VERA_IEN
        plp
        rts
    }}
}


inline asmsub  disable_irq_handlers() {
    ; back to the system default IRQ handler.
    %asm {{
        jsr  sys.restore_irq
    }}
}

}


sys {
    ; ------- lowlevel system routines --------

    %option no_symbol_prefixing

    const ubyte target = 16         ;  compilation target specifier.  64 = C64,  128 = C128,  16 = CommanderX16.

asmsub  init_system()  {
    ; Initializes the machine to a sane starting state.
    ; Called automatically by the loader program logic.
    %asm {{
        sei
        lda  #0
        tax
        tay
        jsr  cx16.mouse_config  ; disable mouse
        cld
        lda  cx16.VERA_DC_VIDEO
        and  #%00000111 ; retain chroma + output mode
        sta  P8ZP_SCRATCH_REG
        lda  #$0a
        sta  $01        ; rom bank 10 (audio)
        jsr  cx16.audio_init ; silence
        stz  $01        ; rom bank 0 (kernal)
        jsr  cbm.IOINIT
        jsr  cbm.RESTOR
        jsr  cbm.CINT
        lda  cx16.VERA_DC_VIDEO
        and  #%11111000
        ora  P8ZP_SCRATCH_REG
        sta  cx16.VERA_DC_VIDEO  ; restore old output mode
        lda  #$90       ; black
        jsr  cbm.CHROUT
        lda  #1
        jsr  cbm.CHROUT ; swap fg/bg
        lda  #$9e       ; yellow
        jsr  cbm.CHROUT
        lda  #147       ; clear screen
        jsr  cbm.CHROUT
        lda  #8         ; disable charset case switch
        jsr  cbm.CHROUT
        lda  #PROG8_VARSHIGH_RAMBANK
        sta  $00    ; select ram bank
        lda  #0
        tax
        tay
        clc
        clv
        cli
        rts
    }}
}

asmsub  init_system_phase2()  {
    %asm {{
        sei
        lda  cx16.CINV
        sta  restore_irq._orig_irqvec
        lda  cx16.CINV+1
        sta  restore_irq._orig_irqvec+1
        lda  #PROG8_VARSHIGH_RAMBANK
        sta  $00    ; select ram bank
        cli
        rts
    }}
}

asmsub  cleanup_at_exit() {
    ; executed when the main subroutine does rts
    %asm {{
        lda  #1
        sta  $00        ; ram bank 1
        lda  #4
        sta  $01        ; rom bank 4 (basic)
        stz  $2d        ; hack to reset machine code monitor bank to 0
        rts
    }}
}

asmsub  set_irq(uword handler @AY) clobbers(A)  {
    ; Sets the handler for the VSYNC interrupt, and enable that interrupt.
	%asm {{
        sei
        sta  _modified+1
        sty  _modified+2
        lda  #<_irq_handler
        sta  cx16.CINV
        lda  #>_irq_handler
        sta  cx16.CINV+1
        lda  #1
        tsb  cx16.VERA_IEN      ; enable the vsync irq
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
		jmp  (restore_irq._orig_irqvec)   ; continue with normal kernal irq routine
+		lda  #1
		sta  cx16.VERA_ISR      ; clear Vera Vsync irq status
		ply
		plx
		pla
		rti
    }}
}

asmsub  restore_irq() clobbers(A) {
	%asm {{
	    sei
	    lda  _orig_irqvec
	    sta  cx16.CINV
	    lda  _orig_irqvec+1
	    sta  cx16.CINV+1
	    lda  cx16.VERA_IEN
	    and  #%11110000     ; disable all Vera IRQs but the vsync
	    ora  #%00000001
	    sta  cx16.VERA_IEN
	    cli
	    rts
_orig_irqvec    .word  0
        }}
}

asmsub  set_rasterirq(uword handler @AY, uword rasterpos @R0) clobbers(A) {
    ; Sets the handler for the LINE interrupt, and enable (only) that interrupt.
	%asm {{
            sei
            sta  _modified+1
            sty  _modified+2
            lda  cx16.r0
            ldy  cx16.r0+1
            lda  cx16.VERA_IEN
            and  #%11110000     ; disable all irqs but the line(raster) one
            ora  #%00000010
            sta  cx16.VERA_IEN
            lda  cx16.r0
            ldy  cx16.r0+1
            jsr  set_rasterline
            lda  #<_raster_irq_handler
            sta  cx16.CINV
            lda  #>_raster_irq_handler
            sta  cx16.CINV+1
            cli
            rts

_raster_irq_handler
            jsr  sys.save_prog8_internals
            cld
_modified   jsr  $ffff    ; modified
            jsr  sys.restore_prog8_internals
            ; end irq processing - don't use kernal's irq handling
            lda  #2
            tsb  cx16.VERA_ISR      ; clear Vera line irq status
            ply
            plx
            pla
            rti
        }}
}

asmsub  set_rasterline(uword line @AY) {
    %asm {{
        php
        sei
        sta  cx16.VERA_IRQLINE_L
        tya
        lsr  a
        bcs  +
        lda  #%10000000
        trb  cx16.VERA_IEN
        plp
        rts
+       lda  #%10000000
        tsb  cx16.VERA_IEN
        plp
        rts
    }}
}

    asmsub reset_system() {
        ; Soft-reset the system back to initial power-on Basic prompt.
        ; We do this via the SMC so that a true reset is performed that also resets the Vera fully.
        %asm {{
            sei
            ldx #$42
            ldy #2
            lda #0
            jsr  cx16.i2c_write_byte
            bra  *
        }}
    }

    sub poweroff_system() {
        ; use the SMC to shutdown the computer
        void cx16.i2c_write_byte($42, $01, $00)
    }

    sub set_leds_brightness(ubyte activity, ubyte power) {
        void cx16.i2c_write_byte($42, $04, power)
        void cx16.i2c_write_byte($42, $05, activity)
    }

    asmsub wait(uword jiffies @AY) clobbers(X) {
        ; --- wait approximately the given number of jiffies (1/60th seconds) (N or N+1)
        ;     note: the system irq handler has to be active for this to work as it depends on the system jiffy clock
        ;     note: this routine cannot be used from inside a irq handler
        %asm {{
            sta  P8ZP_SCRATCH_W1
            sty  P8ZP_SCRATCH_W1+1

_loop       lda  P8ZP_SCRATCH_W1
            ora  P8ZP_SCRATCH_W1+1
            bne  +
            rts

+           sei
            jsr  cbm.RDTIM
            cli
            sta  P8ZP_SCRATCH_B1
-           sei
            jsr  cbm.RDTIM
            cli
            cmp  P8ZP_SCRATCH_B1
            beq  -

            lda  P8ZP_SCRATCH_W1
            bne  +
            dec  P8ZP_SCRATCH_W1+1
+           dec  P8ZP_SCRATCH_W1
            bra  _loop
        }}
    }

    inline asmsub waitvsync()  {
        ; --- suspend execution until the next vsync has occurred, without depending on custom irq handling.
        ;     note: system vsync irq handler has to be active for this routine to work (and no other irqs-- which is the default).
        ;     note: a more accurate way to wait for vsync is to set up a vsync irq handler instead.
        %asm {{
            wai
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
        ;       If you have to copy overlapping memory regions, consider using
        ;       the cx16 specific kernal routine `memory_copy` (make sure kernal rom is banked in).
        ; note: can't be inlined because is called from asm as well.
        ;       also: doesn't use cx16 ROM routine so this always works even when ROM is not banked in.
        %asm {{
            cpy  #0
            bne  _longcopy

            ; copy <= 255 bytes
            tay
            bne  _copyshort
            rts     ; nothing to copy

_copyshort
            ; decrease source and target pointers so we can simply index by Y
            lda  cx16.r0
            bne  +
            dec  cx16.r0+1
+           dec  cx16.r0
            lda  cx16.r1
            bne  +
            dec  cx16.r1+1
+           dec  cx16.r1

-           lda  (cx16.r0),y
            sta  (cx16.r1),y
            dey
            bne  -
            rts

_longcopy
            pha                         ; lsb(count) = remainder in last page
            tya
            tax                         ; x = num pages (1+)
            ldy  #0
-           lda  (cx16.r0),y
            sta  (cx16.r1),y
            iny
            bne  -
            inc  cx16.r0+1
            inc  cx16.r1+1
            dex
            bne  -
            ply
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

    asmsub memsetw(uword mem @R0, uword numwords @R1, uword value @AY) clobbers (A,X,Y) {
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
            lda  #8
            jsr  cbm.CHROUT
        }}
    }

    inline asmsub enable_caseswitch() {
        %asm {{
            lda  #9
            jsr  cbm.CHROUT
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

    asmsub exit(ubyte returnvalue @A) {
        ; -- immediately exit the program with a return code in the A register
        %asm {{
            jsr  cbm.CLRCHN		; reset i/o channels
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
