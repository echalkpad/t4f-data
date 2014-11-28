/**
 * scala
 * :load dsl.scala
 * import ExprParser._
 * test("1+2")
 * test("1+2*3")
 * test("(1+2)*3")
 */
import scala.util.parsing.combinator.syntactical.StandardTokenParsers

sealed abstract class Expr {
    def eval():Int
}

case class EConst(value:Int) extends Expr {
    def eval():Int = value
}

case class EAdd(left:Expr, right:Expr) extends Expr {
    def eval():Int = left.eval + right.eval
}

case class ESub(left:Expr, right:Expr) extends Expr {
    def eval():Int = left.eval - right.eval
}

case class EMul(left:Expr, right:Expr) extends Expr {
    def eval():Int = left.eval * right.eval
}

case class EDiv(left:Expr, right:Expr) extends Expr {
    def eval():Int = left.eval / right.eval
}

case class EUMinus(e:Expr) extends Expr {
    def eval():Int = -e.eval
}

trait DebugStandardTokenParsers extends StandardTokenParsers {
    class Wrap[+T](name:String,parser:Parser[T]) extends Parser[T] {
        def apply(in: Input): ParseResult[T] = {
            val first = in.first
            val pos = in.pos
            val offset = in.offset
            val t = parser.apply(in)
            println(name+".apply for token "+first+
                    " at position "+pos+" offset "+offset+" returns "+t)
            t
        }
    }

    implicit def toWrapped(name:String) = new {
        def !!![T](p:Parser[T]) = new Wrap(name,p) //for debugging
        //def !!![T](p:Parser[T]) = p              //for production
    }
}

object ExprParser extends DebugStandardTokenParsers {
    lexical.delimiters ++= List("+","-","*","/","(",")")

    def value = numericLit ^^ { s => EConst(s.toInt) }

    def parens:Parser[Expr] = "(" ~> expr <~ ")"

    def unaryMinus:Parser[EUMinus] = "-" ~> term ^^ { EUMinus(_) }

    def term = "term" !!! ( value |  "term-parens" !!! parens | unaryMinus )

    def binaryOp(level:Int):Parser[((Expr,Expr)=>Expr)] = {
        level match {
            case 1 =>
                "add" !!! "+" ^^^ { (a:Expr, b:Expr) => EAdd(a,b) } |
                "sub" !!! "-" ^^^ { (a:Expr, b:Expr) => ESub(a,b) }
            case 2 =>
                "mul" !!! "*" ^^^ { (a:Expr, b:Expr) => EMul(a,b) } |
                "div" !!! "/" ^^^ { (a:Expr, b:Expr) => EDiv(a,b) }
            case _ => throw new RuntimeException("bad precedence level "+level)
        }
    }
    val minPrec = 1
    val maxPrec = 2

    def binary(level:Int):Parser[Expr] =
        if (level>maxPrec) term
        else binary(level+1) * binaryOp(level)

    def expr = "expr" !!! ( binary(minPrec) | term )

    def parse(s:String) = {
        val tokens = new lexical.Scanner(s)
        phrase(expr)(tokens)
    }

    def parse(p:Parser[Expr], s:String) = {
        val tokens = new lexical.Scanner(s)
        phrase(p)(tokens)
    }

    def apply(s:String):Expr = {
        parse(s) match {
            case Success(tree, _) => tree
            case e: NoSuccess =>
                   throw new IllegalArgumentException("Bad syntax: "+s)
        }
    }

    def test(exprstr: String) =
        printParseResult(parse(exprstr))

    def test(p:Parser[Expr], exprstr: String) =
        printParseResult(parse(p,exprstr))

    def printParseResult(pr:ParseResult[Expr]) = {
        pr match {
            case Success(tree, _) =>
                println("Tree: "+tree)
                val v = tree.eval()
                println("Eval: "+v)
            case e: NoSuccess => Console.err.println(e)
        }
    }
    
    //A main method for testing
    def main(args: Array[String]) = test(args(0))

}
