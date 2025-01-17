; Prog8 definitions for floating point handling on the VirtualMachine

%option enable_floats

floats {

    const float  PI     = 3.141592653589793
    const float  TWOPI  = 6.283185307179586

sub print_f(float value) {
    ; ---- prints the floating point value (without a newline).
    %ir {{
        loadm.f fr65535,floats.print_f.value
        syscall 25 (fr65535.f)
        return
    }}
}

sub parse_f(str value) -> float {
    ; -- parse a string value of a number to float
    %ir {{
        loadm.w  r65535,floats.parse_f.value
        syscall 45 (r65535.w): fr0.f
        returnr.f fr0
    }}
}

sub pow(float value, float power) -> float {
    %ir {{
        loadm.f fr0,floats.pow.value
        loadm.f fr1,floats.pow.power
        fpow.f fr0,fr1
        returnr.f fr0
    }}
}

sub sin(float angle) -> float {
    %ir {{
        loadm.f fr0,floats.sin.angle
        fsin.f fr0,fr0
        returnr.f fr0
    }}
}

sub cos(float angle) -> float {
    %ir {{
        loadm.f fr0,floats.cos.angle
        fcos.f fr0,fr0
        returnr.f fr0
    }}
}

sub tan(float value) -> float {
    %ir {{
        loadm.f fr0,floats.tan.value
        ftan.f fr0,fr0
        returnr.f fr0
    }}
}

sub atan(float value) -> float {
    %ir {{
        loadm.f fr0,floats.atan.value
        fatan.f fr0,fr0
        returnr.f fr0
    }}
}

sub ln(float value) -> float {
    %ir {{
        loadm.f fr0,floats.ln.value
        fln.f fr0,fr0
        returnr.f fr0
    }}
}

sub log2(float value) -> float {
    %ir {{
        loadm.f fr0,floats.log2.value
        flog.f fr0,fr0
        returnr.f fr0
    }}
}

sub rad(float angle) -> float {
    ; -- convert degrees to radians (d * pi / 180)
    return angle * PI / 180.0
}

sub deg(float angle) -> float {
    ; -- convert radians to degrees (d * (1/ pi * 180))
    return angle * 180.0 / PI
}

sub round(float value) -> float {
    %ir {{
        loadm.f fr0,floats.round.value
        fround.f fr0,fr0
        returnr.f fr0
    }}
}

sub floor(float value) -> float {
    %ir {{
        loadm.f fr0,floats.floor.value
        ffloor.f fr0,fr0
        returnr.f fr0
    }}
}

sub ceil(float value) -> float {
    ; -- ceil: tr = int(f); if tr==f -> return  else return tr+1
    %ir {{
        loadm.f fr0,floats.ceil.value
        fceil.f fr0,fr0
        returnr.f fr0
    }}
}

sub rndf() -> float {
    %ir {{
        syscall 35 () : fr0.f
        returnr.f fr0
    }}
}

sub rndseedf(float seed) {
    %ir {{
        loadm.f  fr65535,floats.rndseedf.seed
        syscall 32 (fr65535.f)
        return
    }}
}


sub minf(float f1, float f2) -> float {
    if f1<f2
        return f1
    return f2
}


sub maxf(float f1, float f2) -> float {
    if f1>f2
        return f1
    return f2
}


sub clampf(float value, float minimum, float maximum) -> float {
    if value<minimum
        value=minimum
    if value<maximum
        return value
    return maximum
}

}
