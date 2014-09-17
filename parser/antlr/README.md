@see commons-configuration
@see cassandra
@see lucene2

cd /usr/local/lib
curl -O http://www.antlr.org/download/antlr-4.4-complete.jar
export CLASSPATH=".:$PWD/*:$CLASSPATH"
alias antlr4='java -jar $PWD/antlr-4.4-complete.jar'
alias grun='java org.antlr.v4.runtime.misc.TestRig'

grammar Expr;       
prog:   (expr NEWLINE)* ;
expr:   expr ('*'|'/') expr
    |   expr ('+'|'-') expr
    |   INT
    |   '(' expr ')'
    ;
NEWLINE : [\r\n]+ ;
INT     : [0-9]+ ;

antlr4 Expr.g4
javac Expr*.java
grun Expr prog -gui
100+2*34
^D
