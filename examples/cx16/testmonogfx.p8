%import monogfx
%import textio
%import math

%option no_sysinit
%zeropage basicsafe


main {

    sub start() {
        monogfx.lores()
        demofill()
        sys.wait(2*60)
        monogfx.hires()
        demo1()
        sys.wait(2*60)
        demo2()

        monogfx.textmode()
        txt.print("done!\n")
    }

    sub demofill() {
        monogfx.circle(160, 120, 110, 1)
        monogfx.rect(180, 5, 25, 190, 1)
        monogfx.line(100, 150, 240, 10, 1)
        monogfx.line(101, 150, 241, 10, 1)
        monogfx.stipple(true)
        sys.wait(60)
        monogfx.fill(100,100,2)
    }

    sub demo1() {
        uword yy = 10
        uword xx
        uword cnt

        monogfx.stipple(true)
        monogfx.disc(320,240,200,1)
        for xx in 0 to 639 {
            monogfx.vertical_line(xx, 0, 480, 1)
        }
        for xx in 0 to 639 {
            monogfx.vertical_line(xx, 0, 480, 0)
        }

        xx=monogfx.width/2
        yy=10
        monogfx.stipple(false)
        linesy()
        linesx()
        monogfx.stipple(true)
        linesy()
        linesx()



        sub linesx() {
            repeat 8 {
                monogfx.horizontal_line(10,yy,300,3)
                yy++
            }
            yy+=4

            repeat 8 {
                monogfx.line(10,yy,309,yy,4)
                yy++
            }
            yy+=4

            repeat 8 {
                for cnt in 10 to 309 {
                    monogfx.plot(cnt, yy, 1)
                }
                yy+=1
            }
            yy += 4

            repeat 8 {
                monogfx.horizontal_line(10,yy,100,3)
                yy++
            }
            yy+=4

            repeat 8 {
                monogfx.line(10,yy,109,yy,4)
                yy++
            }
            yy+=4

            repeat 8 {
                for cnt in 10 to 109 {
                    monogfx.plot(cnt, yy, 1)
                }
                yy++
            }
            yy+=4
        }

        sub linesy() {
            repeat 8 {
                monogfx.vertical_line(xx,10,300,3)
                xx++
            }
            xx+=4

            repeat 8 {
                monogfx.line(xx,10, xx, 309, 4)
                xx++
            }
            xx+=4

            repeat 8 {
                for cnt in 10 to 309 {
                    monogfx.plot(xx, cnt, 1)
                }
                xx+=1
            }
            xx += 4

            repeat 8 {
                monogfx.vertical_line(xx,10,100,3)
                xx++
            }
            xx+=4

            repeat 8 {
                monogfx.line(xx,10,xx,109,4)
                xx++
            }
            xx+=4

            repeat 8 {
                for cnt in 10 to 109 {
                    monogfx.plot(xx, cnt, 1)
                }
                xx++
            }
            xx+=4
        }
    }

    sub demo2 () {
        monogfx.text_charset(3)
        monogfx.lores()
        draw()
        sys.wait(200)
        monogfx.hires()
        draw()
        sys.wait(200)
    }

    sub draw() {

        monogfx.rect(10,10, 1, 1, 4)
        monogfx.rect(20,10, 2, 1, 4)
        monogfx.rect(30,10, 3, 1, 4)
        monogfx.rect(40,10, 1, 2, 4)
        monogfx.rect(50,10, 1, 3, 4)
        monogfx.rect(60,10, 2, 2, 4)
        monogfx.rect(70,10, 3, 3, 4)
        monogfx.rect(80,10, 4, 4, 4)
        monogfx.rect(90,10, 5, 5, 4)
        monogfx.rect(100,10, 8, 8, 4)
        monogfx.rect(110,10, 20, 5, 4)
        monogfx.rect(80, 80, 200, 140, 4)

        monogfx.fillrect(10,40, 1, 1, 5)
        monogfx.fillrect(20,40, 2, 1, 5)
        monogfx.fillrect(30,40, 3, 1, 5)
        monogfx.fillrect(40,40, 1, 2, 5)
        monogfx.fillrect(50,40, 1, 3, 5)
        monogfx.fillrect(60,40, 2, 2, 5)
        monogfx.fillrect(70,40, 3, 3, 5)
        monogfx.fillrect(80,40, 4, 4, 5)
        monogfx.fillrect(90,40, 5, 5, 5)
        monogfx.fillrect(100,40, 8, 8, 5)
        monogfx.fillrect(110,40, 20, 5, 5)
        monogfx.fillrect(82, 82, 200-4, 140-4, 5)

        ubyte i
        for i in 0 to 254 step 4 {
            uword x1 = ((monogfx.width-256)/2 as uword) + math.sin8u(i)
            uword y1 = (monogfx.height-128)/2 + math.cos8u(i)/2
            uword x2 = ((monogfx.width-64)/2 as uword) + math.sin8u(i)/4
            uword y2 = (monogfx.height-64)/2 + math.cos8u(i)/4
            monogfx.line(x1, y1, x2, y2, i+1)
        }

        sys.wait(60)
        monogfx.clear_screen()

        ubyte radius

        for radius in 110 downto 8 step -4 {
            monogfx.circle(monogfx.width/2, (monogfx.height/2 as ubyte), radius, radius)
        }

        monogfx.disc(monogfx.width/2, monogfx.height/2, 80, 2)

        ubyte tp
        for tp in 0 to 15 {
            monogfx.text(19+tp,20+tp*11, 7, sc:"ScreenCODE text! 1234![]<>#$%&*()")
        }

    }
}
