"""
Programming Language for 6502/6510 microprocessors, codename 'Sick'
This is the optimizer that applies various optimizations to the parse tree.

Written by Irmen de Jong (irmen@razorvine.net) - license: GNU GPL 3.0
"""

from .plyparse import Module, Subroutine, Block, Directive, Assignment, AugAssignment, Goto, Expression, IncrDecr,\
    datatype_of, coerce_constant_value, AssignmentTargets, LiteralValue
from .plylex import print_warning, print_bold


class Optimizer:
    def __init__(self, mod: Module) -> None:
        self.num_warnings = 0
        self.module = mod

    def optimize(self) -> None:
        self.num_warnings = 0
        self.optimize_assignments()
        return  # XXX fix all methods below
        #self.combine_assignments_into_multi()
        #self.optimize_multiassigns()
        #self.remove_unused_subroutines()
        #self.optimize_compare_with_zero()
        # @todo join multiple incr/decr of same var into one (if value stays < 256)
        # @todo analyse for unreachable code and remove that (f.i. code after goto or return that has no label so can never be jumped to)
        #self.remove_empty_blocks()

    def optimize_assignments(self) -> None:
        # remove assignment statements that do nothing (A=A)
        # and augmented assignments that have no effect (A+=0)
        # convert augmented assignments to simple incr/decr if possible (A+=10 =>  A++ by 10)
        # @todo remove or simplify logical aug assigns like A |= 0, A |= true, A |= false  (or perhaps turn them into byte values first?)
        for assignment in self.module.all_nodes():
            if isinstance(assignment, Assignment):
                if any(lv != assignment.right for lv in assignment.left.nodes):
                    assignment.left.nodes = [lv for lv in assignment.left.nodes if lv != assignment.right]
                if not assignment.left:
                    assignment.my_scope().remove_node(assignment)
                    self.num_warnings += 1
                    print_warning("{}: removed statement that has no effect".format(assignment.sourceref))
            if isinstance(assignment, AugAssignment):
                if isinstance(assignment.right, LiteralValue) and isinstance(assignment.right.value, (int, float)):
                    if assignment.right.value == 0 and assignment.operator in ("+=", "-=", "|=", "<<=", ">>=", "^="):
                        self.num_warnings += 1
                        print_warning("{}: removed statement that has no effect".format(assignment.sourceref))
                        assignment.my_scope().remove_node(assignment)
                    if assignment.right.value >= 8 and assignment.operator in ("<<=", ">>="):
                        print("{}: shifting result is always zero".format(assignment.sourceref))
                        new_stmt = Assignment(sourceref=assignment.sourceref)
                        new_stmt.nodes.append(AssignmentTargets(nodes=[assignment.left], sourceref=assignment.sourceref))
                        new_stmt.nodes.append(0)
                        assignment.my_scope().replace_node(assignment, new_stmt)
                    if assignment.operator in ("+=", "-=") and 0 < assignment.right.value < 256:
                        howmuch = assignment.right
                        if howmuch.value not in (0, 1):
                            _, howmuch = coerce_constant_value(datatype_of(assignment.left, assignment.my_scope()),
                                                               howmuch, assignment.sourceref)
                        new_stmt = IncrDecr(operator="++" if assignment.operator == "+=" else "--",
                                            howmuch=howmuch.value, sourceref=assignment.sourceref)
                        new_stmt.target = assignment.left
                        assignment.my_scope().replace_node(assignment, new_stmt)

    def combine_assignments_into_multi(self) -> None:
        # fold multiple consecutive assignments with the same rvalue into one multi-assignment
        for block, parent in self.module.all_scopes():
            rvalue = None
            assignments = []
            for stmt in list(block.nodes):
                if isinstance(stmt, Assignment):
                    if assignments:
                        if stmt.right == rvalue:
                            assignments.append(stmt)
                            continue
                        elif len(assignments) > 1:
                            # replace the first assignment by a multi-assign with all the others
                            for assignment in assignments[1:]:
                                print("{}: joined with previous assignment".format(assignment.sourceref))
                                assignments[0].left.extend(assignment.left)
                                block.scope.remove_node(assignment)
                            rvalue = None
                            assignments.clear()
                    else:
                        rvalue = stmt.right
                        assignments.append(stmt)
                else:
                    rvalue = None
                    assignments.clear()

    def optimize_multiassigns(self) -> None:
        # optimize multi-assign statements (remove duplicate targets, optimize order)
        for block, parent in self.module.all_scopes():
            for assignment in block.nodes:
                if isinstance(assignment, Assignment) and len(assignment.left) > 1:
                    # remove duplicates
                    lvalues = set(assignment.left)
                    if len(lvalues) != len(assignment.left):
                        print("{}: removed duplicate assignment targets".format(assignment.sourceref))
                    # @todo change order: first registers, then zp addresses, then non-zp addresses, then the rest (if any)
                    assignment.left = list(lvalues)

    def remove_unused_subroutines(self) -> None:
        # some symbols are used by the emitted assembly code from the code generator,
        # and should never be removed or the assembler will fail
        never_remove = {"c64.FREADUY", "c64.FTOMEMXY", "c64.FADD", "c64.FSUB",
                        "c64flt.GIVUAYF", "c64flt.copy_mflt", "c64flt.float_add_one", "c64flt.float_sub_one",
                        "c64flt.float_add_SW1_to_XY", "c64flt.float_sub_SW1_from_XY"}
        num_discarded = 0
        for sub, parent in self.module.all_scopes():
            if isinstance(sub, Subroutine):
                usages = self.module.subroutine_usage[(parent.name, sub.name)]
                if not usages and parent.name + '.' + sub.name not in never_remove:
                    parent.scope.remove_node(sub)
                    num_discarded += 1
        if num_discarded:
            print("discarded {:d} unused subroutines".format(num_discarded))

    def optimize_compare_with_zero(self) -> None:
        # a conditional goto that compares a value with zero will be simplified
        # the comparison operator and rvalue (0) will be removed and the if-status changed accordingly
        for block, parent in self.module.all_scopes():
            if block.scope:
                for goto in block.all_nodes(Goto):
                    if isinstance(goto.condition, Expression):
                        print("NOT IMPLEMENTED YET: optimize goto conditionals", goto.condition)   # @todo
                        # if cond and isinstance(cond.rvalue, (int, float)) and cond.rvalue.value == 0:
                        #     simplified = False
                        #     if cond.ifstatus in ("true", "ne"):
                        #         if cond.comparison_op == "==":
                        #             # if_true something == 0   ->  if_not something
                        #             cond.ifstatus = "not"
                        #             cond.comparison_op, cond.rvalue = "", None
                        #             simplified = True
                        #         elif cond.comparison_op == "!=":
                        #             # if_true something != 0  -> if_true something
                        #             cond.comparison_op, cond.rvalue = "", None
                        #             simplified = True
                        #     elif cond.ifstatus in ("not", "eq"):
                        #         if cond.comparison_op == "==":
                        #             # if_not something == 0   ->  if_true something
                        #             cond.ifstatus = "true"
                        #             cond.comparison_op, cond.rvalue = "", None
                        #             simplified = True
                        #         elif cond.comparison_op == "!=":
                        #             # if_not something != 0  -> if_not something
                        #             cond.comparison_op, cond.rvalue = "", None
                        #             simplified = True
                        #     if simplified:
                        #         print("{}: simplified comparison with zero".format(stmt.sourceref))

    def remove_empty_blocks(self) -> None:
        # remove blocks without name and without address, or that are empty
        for node in self.module.all_nodes():
            if isinstance(node, (Subroutine, Block)):
                if not node.scope:
                    continue
                if all(isinstance(n, Directive) for n in node.scope.nodes):
                    empty = True
                    for n in node.scope.nodes:
                        empty = empty and n.name not in {"asmbinary", "asminclude"}
                    if empty:
                        self.num_warnings += 1
                        print_warning("ignoring empty block or subroutine", node.sourceref)
                        assert isinstance(node.parent, (Block, Module))
                        node.my_scope().nodes.remove(node)
            if isinstance(node, Block):
                if not node.name and node.address is None:
                    self.num_warnings += 1
                    print_warning("ignoring block without name and address", node.sourceref)
                    assert isinstance(node.parent, Module)
                    node.my_scope().nodes.remove(node)


def optimize(mod: Module) -> None:
    opt = Optimizer(mod)
    opt.optimize()
    if opt.num_warnings:
        print_bold("There are {:d} optimization warnings.".format(opt.num_warnings))
