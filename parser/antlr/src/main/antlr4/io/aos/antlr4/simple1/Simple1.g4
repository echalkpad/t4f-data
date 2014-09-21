grammar Simple1;

/* 100+2*34 */

prog:   (expr NEWLINE)* ;

expr:   expr ('*'|'/') expr
    |   expr ('+'|'-') expr
    |   INT
    |   '(' expr ')'
    ;

NEWLINE : [\r\n]+ ;

INT     : [0-9]+ ;
