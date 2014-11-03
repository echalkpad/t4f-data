/**
 * scala
 * :load dsl2.scala
 * import ExprParser._
 * test("1+2")
 * test("1+2*3")
 * test("(1+2)*3")
 */
import scala.util.parsing.combinator.syntactical.StandardTokenParsers

sealed abstract class Expr2 {
    def eval():Int
}

case class EConst2(value:Int) extends Expr2 {
    def eval():Int = value
}

case class EAdd2(left:Expr2, right:Expr2) extends Expr2 {
    def eval():Int = left.eval + right.eval
}

case class ESub2(left:Expr2, right:Expr2) extends Expr2 {
    def eval():Int = left.eval - right.eval
}

case class EMul2(left:Expr2, right:Expr2) extends Expr2 {
    def eval():Int = left.eval * right.eval
}

case class EDiv2(left:Expr2, right:Expr2) extends Expr2 {
    def eval():Int = left.eval / right.eval
}

case class EUMinus2(e:Expr2) extends Expr2 {
    def eval():Int = -e.eval
}

object Expr2Parser extends StandardTokenParsers {
    lexical.delimiters ++= List("+","-","*","/","(",")")

    def value = numericLit ^^ { s => EConst2(s.toInt) }

    def parens:Parser[Expr2] = "(" ~> expr <~ ")"

    def unaryMinus:Parser[EUMinus2] = "-" ~> term ^^ { EUMinus2(_) }

    def term = ( value |  parens | unaryMinus )

    def binaryOp(level:Int):Parser[((Expr2,Expr2)=>Expr2)] = {
        level match {
            case 1 =>
                "+" ^^^ { (a:Expr2, b:Expr2) => EAdd2(a,b) } |
                "-" ^^^ { (a:Expr2, b:Expr2) => ESub2(a,b) }
            case 2 =>
                "*" ^^^ { (a:Expr2, b:Expr2) => EMul2(a,b) } |
                "/" ^^^ { (a:Expr2, b:Expr2) => EDiv2(a,b) }
            case _ => throw new RuntimeException("bad precedence level "+level)
        }
    }
    val minPrec = 1
    val maxPrec = 2

    def binary(level:Int):Parser[Expr2] =
        if (level>maxPrec) term
        else binary(level+1) * binaryOp(level)

    def expr = ( binary(minPrec) | term )

    def parse(s:String) = {
        val tokens = new lexical.Scanner(s)
        phrase(expr)(tokens)
    }

    def apply(s:String):Expr2 = {
        parse(s) match {
            case Success(tree, _) => tree
            case e: NoSuccess =>
                   throw new IllegalArgumentException("Bad syntax: "+s)
        }
    }

    def test(exprstr: String) = {
        parse(exprstr) match {
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
