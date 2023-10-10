// Circuit.flex
//
// CS2A Language Processing
//
// Description of lexer for circuit description language.
//
// Ian Stark
package esercitazione5;

import java_cup.runtime.Symbol; //This is how we pass tokens to the Parser
import Nodi.*;

%%

%class Lexer
%unicode
%cup
%line
%column

%{
    StringBuffer string = new StringBuffer();

%}
LineTerminator = \r|\n|\r\n
InputCharacter = [^|r|n]
Space = [" "]
WhiteSpace = {LineTerminator} | {Space} | \t


BlockComment = "#*"
InlineComment = "//" | "#"

Id = [:jletter:] [:jletterdigit:]*

ZeroNumber = [0-9]
NoZeroNumber = [1-9]

REAL_CONST = ({NoZeroNumber}{ZeroNumber}*|0)(\.({ZeroNumber}*{NoZeroNumber}+|0))
INTEGER_CONST = ({NoZeroNumber}{ZeroNumber}*|0)
STRING_CONST = \" ~ \" | \' ~ \'
TRUE = "true"
FALSE = "false"
VAR = "var"
//Relop
LT = "<"
GT = ">"
LE = "<="
GE = ">="
NE = "<>" | "!="
ASSIGN = ":="

//Separatori
LPAR = "("
RPAR = ")"
SEMI = ";"
COLON = ":"
COMMA = ","

OUTPAR = "@"
%state STRING
%state STRING2
%state COMMENT_BLOCK
%state COMMENT_IN_LINE
%%

<YYINITIAL> {
"main" { return new Symbol(sym.MAIN,yytext());}

"integer" { return new Symbol(sym.INTEGER,yytext());}
"string" {return new Symbol(sym.STRING,yytext());}
"real" { return new Symbol(sym.REAL,yytext());}
"bool" {return new Symbol(sym.BOOL,yytext());}

//Separatori
{LPAR} {return new Symbol(sym.LPAR,yytext());}
{RPAR} {return new Symbol(sym.RPAR,yytext());}
{COLON} {return new Symbol(sym.COLON,yytext());}

"fun" {return new Symbol(sym.FUN,yytext());}
"end" {return new Symbol(sym.END,yytext());}
"if" { return new Symbol(sym.IF,yytext());}
"then" { return new Symbol(sym.THEN,yytext());}
"else" { return new Symbol(sym.ELSE,yytext());}
"while" { return new Symbol(sym.WHILE,yytext());}
"loop" { return new Symbol(sym.LOOP,yytext());}
"%" {return new Symbol(sym.READ,yytext());}//READ
"?" {return new Symbol(sym.WRITE,yytext());}
"?." {return new Symbol(sym.WRITELN,yytext());}
"?," {return new Symbol(sym.WRITEB,yytext());}
"?:" {return new Symbol(sym.WRITET,yytext());}
"return" {return new Symbol(sym.RETURN,yytext());}

{ASSIGN} {return new Symbol(sym.ASSIGN,":=");}
//Operatori aritmetici
"+" {return new Symbol(sym.PLUS,yytext());}
"-" {return new Symbol(sym.MINUS,yytext());}
"*" {return new Symbol(sym.TIMES,yytext());}
"div" {return new Symbol(sym.DIVINT,yytext());}
"/" {return new Symbol(sym.DIV,yytext());}
"^" {return new Symbol(sym.POW,yytext());}
"&" {return new Symbol(sym.STR_CONCAT,yytext());}
"=" {return new Symbol(sym.EQ,yytext());}

//RELOP
{LT} {return new Symbol(sym.LT,"<");}
{GT} {return new Symbol(sym.GT,">");}
{LE} {return new Symbol(sym.LE,"<=");}
{GE} {return new Symbol(sym.GE,">=");}
{NE} {return new Symbol(sym.NE,"!=");}

//whitespace
{WhiteSpace} { /* ignore */ }


//Operatori logici
"and" {return new Symbol(sym.AND,yytext());}
"or" {return new Symbol(sym.OR,yytext());}
"not" {return new Symbol(sym.NOT,yytext());}

//Costanti
{REAL_CONST} {return new Symbol(sym.REAL_CONST,new RealConst(Float.parseFloat(yytext())));}
{INTEGER_CONST} {return new Symbol(sym.INTEGER_CONST,new IntConst(Integer.parseInt(yytext())));}
{STRING_CONST} {return new Symbol(sym.STRING_CONST,new StringConst(yytext()));}
{TRUE} {return new Symbol(sym.TRUE,new Bool(yytext()));}
{FALSE} {return new Symbol(sym.FALSE,new Bool(yytext()));}
{VAR}   {return new Symbol(sym.VAR, new Var());}
//Separatori
{SEMI} {return new Symbol(sym.SEMI,yytext());}
{COMMA} {return new Symbol(sym.COMMA,yytext());}

"return" {return new Symbol(sym.RETURN,yytext());}

{OUTPAR} {return new Symbol(sym.OUTPAR,yytext());}//OUTPAR

"out" {return new Symbol(sym.OUT,yytext());}

//Richiama stato string
\"  { string.setLength(0); yybegin(STRING); }
\'  { string.setLength(0); yybegin(STRING2); }

{Id} {return new Symbol(sym.ID,yytext());}//ID

//Commenti
{InlineComment}   { yybegin(COMMENT_IN_LINE); }
{BlockComment}    { yybegin(COMMENT_BLOCK); }

//Error fallback
[^] { return new Symbol(sym.error);}

}
//FINE yyinitial

//STATI
<STRING> {      //Stato STRING
    \" { yybegin(YYINITIAL); return new Symbol(sym.STRING_CONST,string.toString()); }
    [^\n\r\"\\]+ { string.append( yytext() ); }
    \\t { string.append('\t'); }
    \\n { string.append('\n'); }
    \\r { string.append('\r'); }
    \\\" { string.append('\"'); }
    \\ { string.append('\\'); }
    //[\n\r\;]*   { return new Symbol(sym.error,"Stringa non chiusa");}
    <<EOF>>     { return new Symbol(sym.error,"Stringa non chiusa. Fine File.");}
}

<STRING2> {
    \' { yybegin(YYINITIAL); return new Symbol(sym.STRING_CONST,string.toString()); }
    [^\n\r\'\\]+ { string.append( yytext() ); }
    \\t { string.append('\t'); }
    \\n { string.append('\n'); }
    \\r { string.append('\r'); }
    \\\' { string.append('\''); }
    \\ { string.append('\\'); }
    <<EOF>> { return new Symbol(sym.error,"Stringa non chiusa");}
}

<COMMENT_BLOCK> {
"#" { yybegin(YYINITIAL); }
<<EOF>> { throw new Error("Commento non chiuso ");}
[^] { /* Ignore */ }
}

<COMMENT_IN_LINE> {
{LineTerminator} { yybegin(YYINITIAL); }
[^] { /* Ignore */ }
}
//FIne stati



