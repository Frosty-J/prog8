; File I/O routines for the VM target
;
; NOTE: not all is implemented.
; NOTE: some calls are slightly different from the "official" diskio library because for example,
;       here we cannot deal with multiple return values.

%import textio
%import syslib

diskio {
    %option no_symbol_prefixing, ignore_unused

    sub directory() -> bool {
        ; -- Prints the directory contents to the screen. Returns success.
        %ir {{
            loadm.w r65534,diskio.load.filenameptr
            loadm.w r65535,diskio.load.address_override
            syscall 45 (): r0.b
            returnr.b r0
        }}
    }

    sub list_filenames(uword pattern_ptr, uword filenames_buffer, uword filenames_buf_size) -> ubyte {
        ; -- fill the provided buffer with the names of the files on the disk (until buffer is full).
        ;    Files in the buffer are separated by a 0 byte. You can provide an optional pattern to match against.
        ;    After the last filename one additional 0 byte is placed to indicate the end of the list.
        ;    Returns number of files (it skips 'dir' entries i.e. subdirectories).
        ;    Also sets carry on exit: Carry clear = all files returned, Carry set = directory has more files that didn't fit in the buffer.
        txt.print("@TODO: list_filenames\n")
        sys.clear_carry()
        return 0
    }

    ; ----- iterative file lister functions (uses the read io channel) -----

    sub lf_start_list(uword pattern_ptr) -> bool {
        ; -- start an iterative file listing with optional pattern matching.
        ;    note: only a single iteration loop can be active at a time!
        txt.print("@TODO: lf_start_list\n")
        return false
    }

    sub lf_next_entry() -> bool {
        ; -- retrieve the next entry from an iterative file listing session.
        ;    results will be found in list_blocks, list_filename, and list_filetype.
        ;    if it returns false though, there are no more entries (or an error occurred).
        txt.print("@TODO: lf_next_entry\n")
        return false
    }

    sub lf_end_list() {
        txt.print("@TODO: lf_end_list\n")
    }


    ; ----- iterative file loader functions (uses the input io channel) -----

    sub f_open(uword filenameptr) -> bool {
        ; -- open a file for iterative reading with f_read
        ;    note: only a single iteration loop can be active at a time!
        ;    Returns true if the file is successfully opened and readable.
        ;    No need to check status(), unlike f_open_w() !
        ;    NOTE: the default input isn't yet set to this logical file, you must use reset_read_channel() to do this,
        ;          if you're going to read from it yourself instead of using f_read()!

        %ir {{
            loadm.w r65535,diskio.f_open.filenameptr
            syscall 52 (r65535.w): r0.b
            returnr.b r0
        }}
    }

    sub f_read(uword bufferpointer, uword num_bytes) -> uword {
        ; -- read from the currently open file, up to the given number of bytes.
        ;    returns the actual number of bytes read.  (checks for End-of-file and error conditions)
        uword actual
        repeat num_bytes {
            %ir {{
                syscall 54 (): r0.w
                storem.w r0,$ff02
            }}
            if cx16.r0H==0
                return actual
            @(bufferpointer) = cx16.r0L
            bufferpointer++
            actual++
        }
        return actual
    }

    sub f_read_all(uword bufferpointer) -> uword {
        ; -- read the full contents of the file, returns number of bytes read.
        ;    It is assumed the file size is less than 64 K.
        uword actual
        repeat {
            %ir {{
                syscall 54 (): r0.w
                storem.w r0,$ff02
            }}
            if cx16.r0H==0
                return actual
            @(bufferpointer) = cx16.r0L
            bufferpointer++
            actual++
        }
    }

    sub f_readline(uword bufptr) -> ubyte {
        ; Routine to read text lines from a text file. Lines must be less than 255 characters.
        ; Reads characters from the input file UNTIL a newline or return character (or EOF).
        ; The line read will be 0-terminated in the buffer (and not contain the end of line character).
        ; The length of the line is returned. Note that an empty line is okay and is length 0!
        ; The success status is returned in the Carry flag instead: C set = success, C clear = failure/endoffile
        ubyte size
        repeat {
            %ir {{
                syscall 54 (): r0.w
                storem.w r0,$ff02
            }}

            if cx16.r0H==0 {
                sys.clear_carry()
                return size
            } else {
                if cx16.r0L == '\n' or cx16.r0L=='\r' {
                    @(bufptr) = 0
                    sys.set_carry()
                    return size
                }
                @(bufptr) = cx16.r0L
                bufptr++
                size++
                if_z {
                    @(bufptr) = 0
                    return 255
                }
            }
        }
    }

    sub f_close() {
        ; -- end an iterative file loading session (close channels).
        %ir {{
            syscall 56 ()
        }}
    }


    ; ----- iterative file writing functions (uses write io channel) -----

    sub f_open_w(uword filenameptr) -> bool {
        ; -- open a file for iterative writing with f_write
        ;    WARNING: returns true if the open command was received by the device,
        ;    but this can still mean the file wasn't successfully opened for writing!
        ;    (for example, if it already exists). This is different than f_open()!
        ;    To be 100% sure if this call was successful, you have to use status()
        ;    and check the drive's status message!
        %ir {{
            loadm.w r65535,diskio.f_open_w.filenameptr
            syscall 53 (r65535.w): r0.b
            returnr.b r0
        }}
    }

    sub f_write(uword bufferpointer, uword num_bytes) -> bool {
        ; -- write the given number of bytes to the currently open file
        ;    you can call this multiple times to append more data
        repeat num_bytes {
            %ir {{
                loadm.w r0,diskio.f_write.bufferpointer
                loadi.b r1,r0
                syscall 55 (r1.b): r0.b
                storem.b r0,$ff02
            }}
            if cx16.r0L==0
                return false
            bufferpointer++
        }
        return true
    }

    sub f_close_w() {
        ; -- end an iterative file writing session (close channels).
        %ir {{
            syscall 57 ()
        }}
    }


    ; ---- other functions ----

    sub chdir(str path) {
        ; -- change current directory.
        %ir {{
            loadm.w r65535,diskio.chdir.path
            syscall 50 (r65535.w)
        }}
    }

    sub mkdir(str name) {
        ; -- make a new subdirectory.
        %ir {{
            loadm.w r65535,diskio.mkdir.name
            syscall 49 (r65535.w)
        }}
    }

    sub rmdir(str name) {
        ; -- remove a subdirectory.
        %ir {{
            loadm.w r65535,diskio.rmdir.name
            syscall 51 (r65535.w)
        }}
    }

    sub curdir() -> uword {
        ; return current directory name or 0 if error
        %ir {{
            syscall 48 (): r0.w
            returnr.w r0
        }}
    }

    sub status() -> str {
        ; -- retrieve the disk drive's current status message
        return "unknown"
    }

    sub status_code() -> ubyte {
        ; -- return status code instead of whole CBM-DOS status string. (in this case always 255, which means 'unable to return sensible value')
        return 255
    }


    sub save(uword filenameptr, uword start_address, uword savesize) -> bool {
        %ir {{
            load.b r65532,0
            loadm.w r65533,diskio.save.filenameptr
            loadm.w r65534,diskio.save.start_address
            loadm.w r65535,diskio.save.savesize
            syscall 42 (r65532.b, r65533.w, r65534.w, r65535.w): r0.b
            returnr.b r0
        }}
    }

    ; like save() but omits the 2 byte prg header.
    sub save_raw(uword filenameptr, uword startaddress, uword savesize) -> bool {
        %ir {{
            load.b r65532,1
            loadm.w r65533,diskio.save.filenameptr
            loadm.w r65534,diskio.save.start_address
            loadm.w r65535,diskio.save.savesize
            syscall 42 (r65532.b, r65533.w, r65534.w, r65535.w): r0.b
            returnr.b r0
        }}
    }

    ; Use kernal LOAD routine to load the given program file in memory.
    ; This is similar to Basic's  LOAD "filename",drive  /  LOAD "filename",drive,1
    ; If you don't give an address_override, the location in memory is taken from the 2-byte file header.
    ; If you specify a custom address_override, the first 2 bytes in the file are ignored
    ; and the rest is loaded at the given location in memory.
    ; Returns the end load address+1 if successful or 0 if a load error occurred.
    sub load(uword filenameptr, uword address_override) -> uword {
        %ir {{
            loadm.w r65534,diskio.load.filenameptr
            loadm.w r65535,diskio.load.address_override
            syscall 40 (r65534.w, r65535.w): r0.w
            returnr.w r0
        }}
    }

    ; Identical to load(), but DOES INCLUDE the first 2 bytes in the file.
    ; No program header is assumed in the file. Everything is loaded.
    ; See comments on load() for more details.
    sub load_raw(uword filenameptr, uword start_address) -> uword {
        %ir {{
            loadm.w r65534,diskio.load_raw.filenameptr
            loadm.w r65535,diskio.load_raw.start_address
            syscall 41 (r65534.w, r65535.w): r0.w
            returnr.w r0
        }}
    }

    sub delete(uword filenameptr) {
        ; -- delete a file on the drive
        %ir {{
            loadm.w r65535,diskio.delete.filenameptr
            syscall 43 (r65535.w)
        }}
    }

    sub rename(uword oldfileptr, uword newfileptr) {
        ; -- rename a file on the drive
        %ir {{
            loadm.w r65534,diskio.rename.oldfileptr
            loadm.w r65535,diskio.rename.newfileptr
            syscall 44 (r65534.w, r65535.w)
        }}
    }
}
