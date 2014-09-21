grammar Calculator;

/*
1+2*3
a=4-5
c=a*(6/2)
*/

PLUS    : '+' ;
MINUS   : '-' ;
MULT    : '*' ;
DIV     : '/' ;
RPAREN  : ')' ;
LPAREN  : '(' ;
ASSIGN  : '=' ;

/*----------------
* PARSER RULES
*----------------*/

prog :      stat+;
stat :      expr NEWLINE
    |   ID ASSIGN expr NEWLINE
    |   NEWLINE;            //Do nothing

expr    :   multExpr ((PLUS | MINUS )multExpr)*;

multExpr:   atom ((MULT | DIV) atom )*;

atom    :   INT
    |   ID
    |   LPAREN expr RPAREN;

/*----------------
* LEXER RULES
*----------------*/

ID  :   ('a'..'z'|'A'..'Z')+;
INT     :   '0'..'9'+;
NEWLINE :   '\r'?'\n';
WS  :   (' '|'\t'|'\n'|'\r')+;
