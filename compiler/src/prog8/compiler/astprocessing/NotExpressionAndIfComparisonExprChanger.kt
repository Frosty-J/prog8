package prog8.compiler.astprocessing

import prog8.ast.Node
import prog8.ast.Program
import prog8.ast.base.FatalAstException
import prog8.ast.expressions.BinaryExpression
import prog8.ast.expressions.NumericLiteral
import prog8.ast.expressions.PrefixExpression
import prog8.ast.walk.AstWalker
import prog8.ast.walk.IAstModification
import prog8.code.core.DataType
import prog8.code.core.ICompilationTarget
import prog8.code.core.IErrorReporter
import prog8.code.core.IntegerDatatypes

internal class NotExpressionAndIfComparisonExprChanger(val program: Program, val errors: IErrorReporter, val compTarget: ICompilationTarget) : AstWalker() {

    override fun before(expr: BinaryExpression, parent: Node): Iterable<IAstModification> {
        if(expr.operator=="==" || expr.operator=="!=") {
            val left = expr.left as? BinaryExpression
            if (left != null) {
                val rightValue = expr.right.constValue(program)
                if (rightValue?.number == 0.0 && rightValue.type in IntegerDatatypes) {
                    if (left.operator == "==" && expr.operator == "==") {
                        // (x==something)==0  -->  x!=something
                        left.operator = "!="
                        return listOf(IAstModification.ReplaceNode(expr, left, parent))
                    } else if (left.operator == "!=" && expr.operator == "==") {
                        // (x!=something)==0  -->  x==something
                        left.operator = "=="
                        return listOf(IAstModification.ReplaceNode(expr, left, parent))
                    } else if (left.operator == "==" && expr.operator == "!=") {
                        // (x==something)!=0 --> x==something
                        left.operator = "=="
                        return listOf(IAstModification.ReplaceNode(expr, left, parent))
                    } else if (left.operator == "!=" && expr.operator == "!=") {
                        // (x!=something)!=0 --> x!=something
                        left.operator = "!="
                        return listOf(IAstModification.ReplaceNode(expr, left, parent))
                    }
                }
            }
        }

        if(expr.operator=="^" && expr.left.inferType(program) istype DataType.BOOL && expr.right.constValue(program)?.number == 1.0) {
            // boolean ^ 1 --> not boolean
            val notExpr = PrefixExpression("not", expr.left, expr.position)
            return listOf(IAstModification.ReplaceNode(expr, notExpr, parent))
        }

        return noModifications
    }

    override fun after(expr: PrefixExpression, parent: Node): Iterable<IAstModification> {
        if(expr.operator == "not") {

            // first check if we're already part of a "boolean" expresion (i.e. comparing against 0)
            // if so, simplify THAT whole expression rather than making it more complicated
            if(parent is BinaryExpression && parent.right.constValue(program)?.number==0.0) {
                if(parent.operator=="==") {
                    // (NOT X)==0 --> X!=0
                    val replacement = BinaryExpression(expr.expression, "!=", NumericLiteral.optimalInteger(0, expr.position), expr.position)
                    return listOf(IAstModification.ReplaceNode(parent, replacement, parent.parent))
                } else if(parent.operator=="!=") {
                    // (NOT X)!=0 --> X==0
                    val replacement = BinaryExpression(expr.expression, "==", NumericLiteral.optimalInteger(0, expr.position), expr.position)
                    return listOf(IAstModification.ReplaceNode(parent, replacement, parent.parent))
                }
            }

            // not(not(x)) -> x
            if((expr.expression as? PrefixExpression)?.operator=="not")
                return listOf(IAstModification.ReplaceNode(expr, expr.expression, parent))
            // not(~x) -> x!=0
            if((expr.expression as? PrefixExpression)?.operator=="~") {
                val x = (expr.expression as PrefixExpression).expression
                val dt = x.inferType(program).getOrElse { throw FatalAstException("invalid dt") }
                val notZero = BinaryExpression(x, "!=", NumericLiteral(dt, 0.0, expr.position), expr.position)
                return listOf(IAstModification.ReplaceNode(expr, notZero, parent))
            }
            val subBinExpr = expr.expression as? BinaryExpression
            if(subBinExpr?.operator=="==") {
                if(subBinExpr.right.constValue(program)?.number==0.0) {
                    // not(x==0) -> x!=0
                    subBinExpr.operator = "!="
                    return listOf(IAstModification.ReplaceNode(expr, subBinExpr, parent))
                }
            } else if(subBinExpr?.operator=="!=") {
                if(subBinExpr.right.constValue(program)?.number==0.0) {
                    // not(x!=0) -> x==0
                    subBinExpr.operator = "=="
                    return listOf(IAstModification.ReplaceNode(expr, subBinExpr, parent))
                }
            }

            // all other not(x)  -->  x==0
            // this means that "not" will never occur anywhere again in the ast after this
            val replacement = BinaryExpression(expr.expression, "==", NumericLiteral(DataType.UBYTE,0.0, expr.position), expr.position)
            return listOf(IAstModification.ReplaceNodeSafe(expr, replacement, parent))
        }
        return noModifications
    }
}
