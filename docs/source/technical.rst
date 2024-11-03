=================
Technical details
=================

All variables are static in memory
----------------------------------

All variables are allocated statically, there is no concept of dynamic heap or stack frames.
Essentially all variables are global (but scoped) and can be accessed and modified anywhere,
but care should be taken of course to avoid unexpected side effects.

Especially when you're dealing with interrupts or re-entrant routines: don't modify variables
that you not own or else you will break stuff.

Variables that are not put into zeropage, will be put into a special 'BSS' section for the assembler.
This section is usually placed at the end of the resulting program but because it only contains empty space
it won't actually increase the size of the resulting program binary.
Prog8 takes care of properly filling this memory area with zeros at program startup and then reinitializes
the subset of variables that have a nonzero initialization value.

Arrays with initialization values are not put into BSS but just occupy a sequence of bytes in
the program memory: their values are not reinitialized at program start.

It is possible to relocate the BSS section using a compiler option
so that more system ram is available for the program code itself.


.. _banking:

ROM/RAM bank selection
----------------------

On certain systems prog8 provides support for managing the ROM or RAM banks that are active.
For example, on the Commander X16, you can use ``cx16.getrombank()`` to get the active ROM bank,
and ``cx16.rombank(10)`` to make rom bank 10 active. Likewise, ``cx16.getrambank()`` to get the active RAM bank,
and ``cx16.rambank(10)`` to make ram bank 10 active. This is explicit manual banking control.

However, Prog8 also provides something more sophisticated than this, when dealing with banked subroutines:

External subroutines defined with ``romsub`` can have a non-standard ROM or RAM bank specified as well.
The compiler will then transparently change a call to this routine so that the correct bank is activated
automatically before the normal jump to the subroutine (and switched back on return). The programmer doesn't
have to bother anymore with setting/resetting the banks manually, or having the program crash because
the routine is called in the wrong bank!  You define such a routine by adding ``@bank <bank>``
to the romsub subroutine definition. This specifies the bank number where the subroutine is located in::

    romsub @bank 10  $C09F = audio_init()

When you then call this routine in your program as usual, the compiler will no longer generate a simple JSR instruction to the
routine. Instead it will generate a piece of code that automatically switches the ROM or RAM bank to the
correct value, does the call, and switches the bank back. The exact code will be different for different
compilation targets, and not all targets even have banking or support this. As an example,
on the Commander X16, prog8 will use the JSRFAR kernal routine for this. On the Commodore 128, a similar call exists.
Other compilation targets don't have banking or prog8 doesn't yet support automatic bank selection on them.

There's a "banking" (not financial) example for the Commander X16 that shows a possible application
of the romsub with bank support, check out the `bank example code <https://github.com/irmen/prog8/tree/master/examples/cx16/banking>`_ .


Notice that the symbol for this routine in the assembly source code will still be defined as usual.
The bank number is not translated into assembly (only as a comment)::

	p8s_audio_init = $c09f ; @bank 10

.. caution::
    Calls with automatic bank switching like this are not safe to use from IRQ handlers. Don't use them there.
    Instead change banks in a controlled manual way (or not at all).


.. _symbol-prefixing:

Symbol prefixing in generated Assembly code
-------------------------------------------

*All* symbols in the prog8 program will be prefixed in the generated assembly code:

============ ========
Element type prefix
============ ========
Block        ``p8b_``
Subroutine   ``p8s_``
Variable     ``p8v_``
Constant     ``p8c_``
Label        ``p8l_``
other        ``p8_``
============ ========

This is to avoid naming conflicts with CPU registers, assembly instructions, etc.
So if you're referencing symbols from the prog8 program in inlined assembly code, you have to take
this into account. Stick the proper prefix in front of every symbol name component that you want to reference that is coming
from a prog8 source file.
All elements in scoped names such as ``main.routine.var1`` are prefixed so this becomes ``p8b_main.p8s_routine.p8v_var1``.

.. attention::
    Symbols from library modules are *not* prefixed and can be used
    in assembly code as-is. So you can write::

        %asm {{
            lda  #'a'
            jsr  cbm.CHROUT
        }}


Subroutine Calling Convention
-----------------------------

Calling a subroutine requires three steps:

#. preparing the arguments (if any) and passing them to the routine.
   Numeric types are passed by value (bytes, words, booleans, floats),
   but array types and strings are passed by reference which means as ``uword`` being a pointer to their address in memory.
#. calling the subroutine
#. preparing the return value (if any) and returning that from the call.


``asmsub`` routines
^^^^^^^^^^^^^^^^^^^

These are usually declarations of Kernal (ROM) routines or low-level assembly only routines,
that have their arguments solely passed into specific registers.
Sometimes even via a processor status flag such as the Carry flag.
Return values also via designated registers.
The processor status flag is preserved on returning so you can immediately act on that for instance
via a special branch instruction such as ``if_z`` or ``if_cs`` etc.


regular subroutines
^^^^^^^^^^^^^^^^^^^

- subroutine parameters are just variables scoped to the subroutine.
- the arguments passed in a call are evaluated and then copied into those variables.
  Using variables for this sometimes can seem inefficient but it's required to allow subroutines to work locally
  with their parameters and allow them to modify them as required, without changing the
  variables used in the call's arguments.  If you want to get rid of this overhead you'll
  have to make an ``asmsub`` routine in assembly instead.
- the order of evaluation of subroutine call arguments *is unspecified* and should not be relied upon.
- the return value is passed back to the caller via cpu register(s):
  Byte values will be put in ``A`` .
  Word values will be put in ``A`` + ``Y`` register pair.
  Float values will be put in the ``FAC1`` float 'register' (BASIC allocated this somewhere in ram).


Calls to builtin functions are treated in a special way:
Generally if they have a single argument it's passed in a register or register pair.
Multiple arguments are passed like a normal subroutine, into variables.
Some builtin functions have a fully custom implementation.


The compiler will warn about routines that are called and that return a value, if you're not
doing something with that returnvalue. This can be on purpose if you're simply not interested in it.
Use the ``void`` keyword in front of the subroutine call to get rid of the warning in that case.


Compiler Internals
------------------

Here is a diagram of how the compiler translates your program source code into a binary program:

.. image:: prog8compiler.svg

Some notes and references into the compiler's source code modules:

#. The ``compileProgram()`` function (in the ``compiler`` module) does all the coordination and basically drives all of the flow shown in the diagram.
#. ANTLR is a Java parser generator and is used for initial parsing of the source code. (``parser`` module)
#. Most of the compiler and the optimizer operate on the *Compiler AST*. These are complicated
   syntax nodes closely representing the Prog8 program structure. (``compilerAst`` module)
#. For code generation, a much simpler AST has been defined that replaces the *Compiler AST*.
   Most notably, node type information is now baked in. (``codeCore`` module, Pt- classes)
#. An *Intermediate Representation* has been defined that is generated from the intermediate AST. This IR
   is more or less a machine code language for a virtual machine - and indeed this is what the built-in
   prog8 VM will execute if you use the 'virtual' compilation target and use ``-emu`` to launch the VM.
   (``intermediate`` and ``codeGenIntermediate`` modules, and ``virtualmachine`` module for the VM related stuff)
#. The code generator backends all implement a common interface ``ICodeGeneratorBackend`` defined in the ``codeCore`` module.
   Currently they get handed the program Ast, Symboltable and several other things.
   If the code generator wants it can use the ``IRCodeGen`` class from the ``codeGenIntermediate`` module
   to convert the Ast into IR first. The VM target uses this, but the 6502 codegen doesn't right now.


Run-time memory profiling with the X16 emulator
-----------------------------------------------
The X16 emulator has a ``-memorystats`` option that enables it to keep track of memory access count statistics,
and write the accumulated counts to a file on exit.
Prog8 includes a Python script ``profiler.py`` (find it in the "scripts" subdirectory of the source code distribution)
that can cross-reference that file with an assembly listing produced by the compiler with the ``-asmlist`` option.
It then prints the top N lines in your (assembly) program source that perform the most reads and writes,
which you can use to identify possible hot spots/bottlenecks/variables that should be better placed in zeropage etc.
Note that the profiler just works with the number of accesses to memory locations, this is *not* the same
as the most run-time (cpu instructions cycle times aren't taken into account at all).
Here is an example of the output it generates::

    $ scripts/profiler.py -n 10 cobramk3-gfx.list memstats.txt                                                                             ✔

    number of actual lines in the assembly listing: 2134
    number of distinct addresses read from  : 22006
    number of distinct addresses written to : 8179
    total number of reads  : 375106285 (375M)
    total number of writes : 63601962 (63M)

    top 10 most reads:
    $007f (7198687) : $007e 'P8ZP_SCRATCH_W2' (line 13), $007e 'remainder' (line 1855)
    $007e (6990527) : $007e 'P8ZP_SCRATCH_W2' (line 13), $007e 'remainder' (line 1855)
    $0265 (5029230) : unknown
    $007c (4455140) : $007c 'P8ZP_SCRATCH_W1' (line 12), $007c 'dividend' (line 1854), $007c 'result' (line 1856)
    $007d (4275195) : $007c 'P8ZP_SCRATCH_W1' (line 12), $007c 'dividend' (line 1854), $007c 'result' (line 1856)
    $0076 (3374800) : $0076 'label_asm_35_counter' (line 2082)
    $15d7 (3374800) : $15d7 '9c 23 9f               stz  cx16.VERA_DATA0' (line 2022), $15d7 'label_asm_34_repeat' (line 2021)
    $15d8 (3374800) : $15d7 '9c 23 9f               stz  cx16.VERA_DATA0' (line 2022), $15d7 'label_asm_34_repeat' (line 2021)
    $15d9 (3374800) : $15da '9c 23 9f               stz  cx16.VERA_DATA0' (line 2023)
    $15da (3374800) : $15da '9c 23 9f               stz  cx16.VERA_DATA0' (line 2023)

    top 10 most writes:
    $9f23 (14748104) : $9f23 'VERA_DATA0' (line 1451)
    $0265 (5657743) : unknown
    $007e (4464393) : $007e 'P8ZP_SCRATCH_W2' (line 13), $007e 'remainder' (line 1855)
    $007f (4464393) : $007e 'P8ZP_SCRATCH_W2' (line 13), $007e 'remainder' (line 1855)
    $007c (4416537) : $007c 'P8ZP_SCRATCH_W1' (line 12), $007c 'dividend' (line 1854), $007c 'result' (line 1856)
    $007d (3820272) : $007c 'P8ZP_SCRATCH_W1' (line 12), $007c 'dividend' (line 1854), $007c 'result' (line 1856)
    $0076 (3375568) : $0076 'label_asm_35_counter' (line 2082)
    $01e8 (1310425) : cpu stack
    $01e7 (1280140) : cpu stack
    $0264 (1258159) : unknown

Apparently the most cpu activity while running this program is spent in a division routine.
