/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	return filename;
    }

    int commentLength = 0;
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
		case COMMENT:
			yybegin(YYINITIAL);
          		return new Symbol(TokenConstants.ERROR, "EOF in comment");
		case STRING:
			yybegin(YYINITIAL);
			return new Symbol(TokenConstants.ERROR, "EOF in string constant");

    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup

%state COMMENT
%state STRING
%state STRING_ERROR_NULL

CLASS = [Cc][Ll][Aa][Ss][Ss]
ELSE = [Ee][Ll][Ss][Ee]

IF = [Ii][Ff]
FI = [Ff][Ii]
IN = [Ii][Nn]

INHERITS = [Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss]
ISVOID = [Ii][Ss][Vv][Oo][Ii][Dd]

LET = [Ll][Ee][Tt]
LOOP = [Ll][Oo][Oo][Pp]
POOL = [Pp][Oo][Oo][Ll]
THEN = [Tt][Hh][Ee][Nn]
WHILE = [Ww][Hh][Ii][Ll][Ee]
CASE = [Cc][Aa][Ss][Ee]
ESAC = [Ee][Ss][Aa][Cc]

NEW = [Nn][Ee][Ww]
OF = [Oo][Ff]
NOT = [Nn][Oo][Tt]
TRUE = t[Rr][Uu][Ee]
FALSE = f[Aa][Ll][Ss][Ee]

DIGIT = [0-9]
OBJ_ID = [a-z][A-Za-z0-9_]*
CHAR = .|\r
STRING_START = \"
STRING_CHAR  = [^\"\0\n\\]+
STRING_END = \"
COMMENT_LINE = --[^\n]*

BLANK_SPACE = [ \t\r\f\32]
NEWLINE = [\n]
SOMA = "+"
SUBTRACT = "-"
MULTIPLY = "*"
DIVIDE = "/"
EQUAL = "="
LESS = "<"
LESS_EQUAL = "<="
MORE = ">"
MORE_EQUAL = ">="
NEGATIVE = "~"
SEMICOLON = ";"
COLON = ":"
DOT = "."
COMMA = ","
OPEN_PARENTHESES = "("
CLOSE_PARENTHESES = ")"
OPEN_CURLY_PARENTHESES = "{"
CLOSE_CURL_PARENTHESES = "}"
ATTRIB = "<-"
AT = "@"
ARROW = "=>"


%%


<YYINITIAL> "(*" {
    yybegin(COMMENT);
    commentLength++;
}

<COMMENT> "(*" {
    commentLength++;
}

<COMMENT> "*)" {
    if(--commentLength == 0) {
        yybegin(YYINITIAL);
    }
}

<COMMENT> {NEWLINE} {}
<COMMENT> . {}

<YYINITIAL> "*)" {
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}

<YYINITIAL>{COMMENT_LINE}         { ; }
<COMMENT>{CHAR}                   { ; }
<YYINITIAL>{BLANK_SPACE}            { ; }

<YYINITIAL>{OBJ_ID} {
    if (Character.isLowerCase(yytext().charAt(0))) return new Symbol(TokenConstants.OBJECTID, new IdSymbol(yytext(), yytext().length(), yytext().hashCode()));
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), yytext().hashCode()));
}

<YYINITIAL>{ELSE}                    { return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>{IF}                      { return new Symbol(TokenConstants.IF); }
<YYINITIAL>{FI}                      { return new Symbol(TokenConstants.FI); }
<YYINITIAL>{IN}                      { return new Symbol(TokenConstants.IN); }
<YYINITIAL>{INHERITS}                { return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL>{ISVOID}                  { return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>{LET}                     { return new Symbol(TokenConstants.LET); }
<YYINITIAL>{LOOP}                    { return new Symbol(TokenConstants.LOOP); }
<YYINITIAL>{POOL}                    { return new Symbol(TokenConstants.POOL); }
<YYINITIAL>{THEN}                    { return new Symbol(TokenConstants.THEN); }
<YYINITIAL>{WHILE}                   { return new Symbol(TokenConstants.WHILE); }
<YYINITIAL>{CASE}                    { return new Symbol(TokenConstants.CASE); }
<YYINITIAL>{ESAC}                    { return new Symbol(TokenConstants.ESAC); }
<YYINITIAL>{NEW}                     { return new Symbol(TokenConstants.NEW); }
<YYINITIAL>{OF}                      { return new Symbol(TokenConstants.OF); }
<YYINITIAL>{NOT}                     { return new Symbol(TokenConstants.NOT); }
<YYINITIAL>{CLASS}                   { return new Symbol(TokenConstants.CLASS); }

<YYINITIAL>{TRUE}                    { return new Symbol(TokenConstants.BOOL_CONST, "true"); }
<YYINITIAL>{FALSE}                   { return new Symbol(TokenConstants.BOOL_CONST, "false"); }

<YYINITIAL>{SOMA}                    { return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>{SUBTRACT}                 { return new Symbol(TokenConstants.MINUS); }
<YYINITIAL>{MULTIPLY}              { return new Symbol(TokenConstants.MULT); }
<YYINITIAL>\{DIVIDE}                 { return new Symbol(TokenConstants.DIV); }

<YYINITIAL>{EQUAL}                   { return new Symbol(TokenConstants.EQ); }
<YYINITIAL>{LESS}                   { return new Symbol(TokenConstants.LT); }
<YYINITIAL>{LESS_EQUAL}              { return new Symbol(TokenConstants.LE); }
<YYINITIAL>{NEGATIVE}                { return new Symbol(TokenConstants.NEG); }

<YYINITIAL>{SEMICOLON}            { return new Symbol(TokenConstants.SEMI); }
<YYINITIAL>{COLON}              { return new Symbol(TokenConstants.COLON); }
<YYINITIAL>{DOT}                   { return new Symbol(TokenConstants.DOT); }
<YYINITIAL>{COMMA}                 { return new Symbol(TokenConstants.COMMA); }

<YYINITIAL>{OPEN_PARENTHESES}          { return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL>{CLOSE_PARENTHESES}         { return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL>{OPEN_CURLY_PARENTHESES}               { return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL>{CLOSE_CURL_PARENTHESES}              { return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL>{ARROW}                   { return new Symbol(TokenConstants.DARROW); }
<YYINITIAL>{ATTRIB}                  { return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL>{AT}                      { return new Symbol(TokenConstants.AT); }

<YYINITIAL>{DIGIT}+                { return new Symbol(TokenConstants.INT_CONST, new IntSymbol(yytext(), yytext().length(), yytext().hashCode())); }

<YYINITIAL>{STRING_START}            { string_buf.setLength(0); yybegin(STRING); }

<STRING>\x00                       {
		yybegin(STRING_ERROR_NULL);
		return new Symbol(TokenConstants.ERROR, "String contains null character");
}
<STRING>\\b                        { string_buf.append("\b"); }
<STRING>\\f                        { string_buf.append("\f"); }
<STRING>\\t                        { string_buf.append("\t"); }
<STRING>\\\\n                      { string_buf.append("\\n"); }
<STRING>\\n                        { string_buf.append("\n"); }
<STRING>\\\n                       { string_buf.append("\n"); }
<STRING>\\\"                       { string_buf.append("\""); }
<STRING>\\\\                       { string_buf.append("\\"); }
<STRING>\\                         { ; }
<STRING>{STRING_CHAR}              { string_buf.append(yytext()); }

<STRING>\n                         {
		string_buf.setLength(0);
                yybegin(YYINITIAL);
                return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}

<STRING>{STRING_END}               {
		yybegin(YYINITIAL);
                String s = string_buf.toString();

		if(s.length() >= MAX_STR_CONST) {
                	return new Symbol(TokenConstants.ERROR, "String constant too long");
                } else {
                	return new Symbol(TokenConstants.STR_CONST, new StringSymbol(s, s.length(), s.hashCode()));
                }
}


<STRING_ERROR_NULL> {NEWLINE}           { yybegin(YYINITIAL); }
<STRING_ERROR_NULL>\"               { yybegin(YYINITIAL); }
<STRING_ERROR_NULL>.                { ; }
\n                                  { curr_lineno++; }
.                               { return new Symbol(TokenConstants.ERROR, yytext()); }