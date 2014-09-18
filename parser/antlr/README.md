-------------------------------------------------------------------------------
```
 _____ ___ _____
|_   _| | |   __|
  | | |_  |   __|
  |_|   |_|__|

 #t4f-data-parser-antlr
```
-------------------------------------------------------------------------------

# Simple Test
```
curl -O http://www.antlr.org/download/antlr-4.4-complete.jar
export CLASSPATH=".:$PWD/*:$CLASSPATH"
alias antlr4='java -jar $PWD/antlr-4.4-complete.jar'
alias grun='java org.antlr.v4.runtime.misc.TestRig'
```
vi Expr.g4
```
grammar Expr;       
prog:   (expr NEWLINE)* ;
expr:   expr ('*'|'/') expr
    |   expr ('+'|'-') expr
    |   INT
    |   '(' expr ')'
    ;
NEWLINE : [\r\n]+ ;
INT     : [0-9]+ ;
```
antlr4 Expr.g4
javac Expr*.java
grun Expr prog -gui
or
grun Expr prog -tree
```
Type after the following (^D is CTRL-D - That ^D means EOF on unix; it's ^Z in Windows.)
```
100+2*34
^D
```
You should see as output the following:
```
(prog (expr (expr 100) + (expr (expr 2) * (expr 34))) \n)
```
-------------------------------------------------------------------------------
