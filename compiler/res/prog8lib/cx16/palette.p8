; Manipulate the Commander X16's display color palette.
; Should you want to restore the full default palette, you can call cbm.CINT()
; The first 16 colors can be restored to their default with set_default16()

palette {
    %option ignore_unused

    uword vera_palette_ptr

    sub set_color(ubyte index, uword color) {
        vera_palette_ptr = $fa00+(index as uword * 2)
        cx16.vpoke(1, vera_palette_ptr, lsb(color))
        vera_palette_ptr++
        cx16.vpoke(1, vera_palette_ptr, msb(color))
    }

    sub get_color(ubyte index) -> uword {
        vera_palette_ptr = $fa00+(index as uword * 2)
        return mkword(cx16.vpeek(1, vera_palette_ptr+1), cx16.vpeek(1, vera_palette_ptr))
    }

    sub set_rgb_be(uword palette_ptr, uword num_colors) {
        ; 1 word per color entry, $0rgb in big endian format
        vera_palette_ptr = $fa00
        repeat num_colors {
            cx16.vpoke(1, vera_palette_ptr+1, @(palette_ptr))
            palette_ptr++
            cx16.vpoke(1, vera_palette_ptr, @(palette_ptr))
            palette_ptr++
            vera_palette_ptr+=2
        }
    }

    sub set_rgb(uword palette_words_ptr, uword num_colors) {
        ; 1 word per color entry (in little endian format as layed out in video memory, so $gb;$0r)
        vera_palette_ptr = $fa00
        repeat num_colors*2 {
            cx16.vpoke(1, vera_palette_ptr, @(palette_words_ptr))
            palette_words_ptr++
            vera_palette_ptr++
        }
    }

    sub set_rgb8(uword palette_bytes_ptr, uword num_colors) {
        ; 3 bytes per color entry, adjust color depth from 8 to 4 bits per channel.
        vera_palette_ptr = $fa00
        ubyte red
        ubyte greenblue
        repeat num_colors {
            cx16.r1 = color8to4(palette_bytes_ptr)
            palette_bytes_ptr+=3
            cx16.vpoke(1, vera_palette_ptr, cx16.r1H)       ; $GB
            vera_palette_ptr++
            cx16.vpoke(1, vera_palette_ptr, cx16.r1L)       ; $0R
            vera_palette_ptr++
        }
    }

    sub color8to4(uword colorpointer) -> uword {
        ; accurately convert 24 bits (3 bytes) RGB color, in that order in memory, to 16 bits $GB;$0R colorvalue
        cx16.r1 = colorpointer
        cx16.r0 = channel8to4(@(cx16.r1))         ; (red)   -> $00:0R
        cx16.r0H = channel8to4(@(cx16.r1+1))<<4   ; (green) -> $G0:0R
        cx16.r0H |= channel8to4(@(cx16.r1+2))     ; (blue)  -> $GB:0R
        return cx16.r0
    }

    sub channel8to4(ubyte channelvalue) -> ubyte {
        ; accurately convert a single 8 bit color channel value to 4 bits,  see https://threadlocalmutex.com/?p=48
        return msb(channelvalue * $000f + 135)
    }


    sub set_monochrome(uword screencolorRGB, uword drawcolorRGB) {
        vera_palette_ptr = $fa00
        cx16.vpoke(1, vera_palette_ptr, lsb(screencolorRGB))   ; G,B
        vera_palette_ptr++
        cx16.vpoke(1, vera_palette_ptr, msb(screencolorRGB))   ; R
        vera_palette_ptr++
        repeat 255 {
            cx16.vpoke(1, vera_palette_ptr, lsb(drawcolorRGB)) ; G,B
            vera_palette_ptr++
            cx16.vpoke(1, vera_palette_ptr, msb(drawcolorRGB)) ; R
            vera_palette_ptr++
        }
    }

    sub set_all_black() {
        set_monochrome($000, $000)
    }

    sub set_all_white() {
        set_monochrome($fff, $fff)
    }

    sub set_grayscale() {
        ; set first 16 colors to a grayscale gradient from black to white
        vera_palette_ptr = $fa00
        cx16.r2L=0
        repeat 16 {
            cx16.vpoke(1, vera_palette_ptr, cx16.r2L)
            vera_palette_ptr++
            cx16.vpoke(1, vera_palette_ptr, cx16.r2L)
            vera_palette_ptr++
            cx16.r2L += $11
        }
    }

    sub fade_step_multi(ubyte startindex, ubyte endindex, uword target_rgb) -> bool {
        ; Perform one color fade step for multiple consecutive palette entries.
        ;   startindex = palette index of first color to fade
        ;   endindex = palette index of last color to fade
        ;   target_rgb = $RGB color value to fade towards
        ; Returns true if one or more colors were changed, false if no fade steps were done anymore.
        ; So you usually keep calling this until it returns false.
        bool changed = false
        while startindex <= endindex {
            if fade_step(startindex, target_rgb)
                changed=true
            startindex++
            if_z
                break
        }
        return changed
    }

    sub fade_step_colors(ubyte startindex, ubyte endindex, uword target_colors) -> bool {
        ; Perform one color fade step for multiple consecutive palette entries, to different target colors.
        ;   startindex = palette index of first color to fade
        ;   endindex = palette index of last color to fade
        ;   target_colors = address of uword $RGB array of colors to fade towards
        ; Returns true if one or more colors were changed, false if no fade steps were done anymore.
        ; So you usually keep calling this until it returns false.
        bool changed = false
        ubyte target_index = 0
        while startindex <= endindex {
            if fade_step(startindex, peekw(target_colors+target_index))
                changed=true
            target_index += 2
            startindex++
            if_z
                break
        }
        return changed
    }

    sub fade_step(ubyte index, uword target_rgb) -> bool {
        ; Perform one color fade step for a single palette entry.
        ;   index = palette index of the color to fade
        ;   target_rgb = $RGB color value to fade towards
        ; Returns true if the color was changed, false if no fade step was done anymore.
        ; So you usually keep calling this until it returns false.
        uword color = palette.get_color(index)
        cx16.r0L = msb(color)            ; r
        cx16.r1L = lsb(color) >> 4       ; g
        cx16.r2L = lsb(color) & 15       ; b
        cx16.r0H = msb(target_rgb) & 15  ; r2
        cx16.r1H = lsb(target_rgb) >> 4  ; g2
        cx16.r2H = lsb(target_rgb) & 15  ; b2

        ubyte changed

        ; use cmp() + status bits branches, to avoid multiple compares that could be done just once
        cmp(cx16.r0L, cx16.r0H)
        if_ne {
            if_cc
                cx16.r0L++
            else
                cx16.r0L--
            changed++
        }
        cmp(cx16.r1L, cx16.r1H)
        if_ne {
            if_cc
                cx16.r1L++
            else
                cx16.r1L--
            changed++
        }
        cmp(cx16.r2L, cx16.r2H)
        if_ne {
            if_cc
                cx16.r2L++
            else
                cx16.r2L--
            changed++
        }

        palette.set_color(index, mkword(cx16.r0L, cx16.r1L<<4 | cx16.r2L))
        return changed!=0
    }

    sub set_c64pepto() {
        ; set first 16 colors to the "Pepto" PAL commodore-64 palette  http://www.pepto.de/projects/colorvic/
        uword[] colors = [
            $000,  ; 0 = black
            $FFF,  ; 1 = white
            $833,  ; 2 = red
            $7cc,  ; 3 = cyan
            $839,  ; 4 = purple
            $5a4,  ; 5 = green
            $229,  ; 6 = blue
            $ef7,  ; 7 = yellow
            $852,  ; 8 = orange
            $530,  ; 9 = brown
            $c67,  ; 10 = light red
            $444,  ; 11 = dark grey
            $777,  ; 12 = medium grey
            $af9,  ; 13 = light green
            $76e,  ; 14 = light blue
            $bbb   ; 15 = light grey
        ]
        set_rgb(colors, len(colors))
    }

    sub set_c64ntsc() {
        ; set first 16 colors to a NTSC commodore-64 palette
        uword[] colors = [
            $000,   ; 0 = black
            $FFF,   ; 1 = white
            $934,   ; 2 = red
            $9ff,   ; 3 = cyan
            $73f,   ; 4 = purple
            $4b1,   ; 5 = green
            $20c,   ; 6 = blue
            $ee6,   ; 7 = yellow
            $b53,   ; 8 = orange
            $830,   ; 9 = brown
            $f8a,   ; 10 = light red
            $444,   ; 11 = dark grey
            $999,   ; 12 = medium grey
            $9f9,   ; 13 = light green
            $36f,   ; 14 = light blue
            $ccc    ; 15 = light grey
        ]
        set_rgb(colors, len(colors))
    }

    sub set_default16() {
        ; set first 16 colors to the defaults on the X16
        uword[] colors = [
            $000,   ; 0 = black
            $fff,   ; 1 = white
            $800,   ; 2 = red
            $afe,   ; 3 = cyan
            $c4c,   ; 4 = purple
            $0c5,   ; 5 = green
            $00a,   ; 6 = blue
            $ee7,   ; 7 = yellow
            $d85,   ; 8 = orange
            $640,   ; 9 = brown
            $f77,   ; 10 = light red
            $333,   ; 11 = dark grey
            $777,   ; 12 = medium grey
            $af6,   ; 13 = light green
            $08f,   ; 14 = light blue
            $bbb    ; 15 = light grey
        ]
        set_rgb(colors, len(colors))
    }
}
