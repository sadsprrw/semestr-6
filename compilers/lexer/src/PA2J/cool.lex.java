/*
 *  The scanner definition for COOL.
 */
package PA2J;
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 2;
	private final int STRING_ERROR_NULL = 3;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		110,
		111,
		112
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NOT_ACCEPT,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NOT_ACCEPT,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NOT_ACCEPT,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NOT_ACCEPT,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NOT_ACCEPT,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NOT_ACCEPT,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NOT_ACCEPT,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NOT_ACCEPT,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NOT_ACCEPT,
		/* 87 */ YY_NOT_ACCEPT,
		/* 88 */ YY_NOT_ACCEPT,
		/* 89 */ YY_NOT_ACCEPT,
		/* 90 */ YY_NOT_ACCEPT,
		/* 91 */ YY_NOT_ACCEPT,
		/* 92 */ YY_NOT_ACCEPT,
		/* 93 */ YY_NOT_ACCEPT,
		/* 94 */ YY_NOT_ACCEPT,
		/* 95 */ YY_NOT_ACCEPT,
		/* 96 */ YY_NOT_ACCEPT,
		/* 97 */ YY_NOT_ACCEPT,
		/* 98 */ YY_NOT_ACCEPT,
		/* 99 */ YY_NOT_ACCEPT,
		/* 100 */ YY_NOT_ACCEPT,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NOT_ACCEPT,
		/* 103 */ YY_NOT_ACCEPT,
		/* 104 */ YY_NOT_ACCEPT,
		/* 105 */ YY_NOT_ACCEPT,
		/* 106 */ YY_NOT_ACCEPT,
		/* 107 */ YY_NOT_ACCEPT,
		/* 108 */ YY_NOT_ACCEPT,
		/* 109 */ YY_NOT_ACCEPT,
		/* 110 */ YY_NOT_ACCEPT,
		/* 111 */ YY_NOT_ACCEPT,
		/* 112 */ YY_NOT_ACCEPT,
		/* 113 */ YY_NOT_ACCEPT,
		/* 114 */ YY_NOT_ACCEPT,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NOT_ACCEPT,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NOT_ACCEPT
	};
	private int yy_cmap[] = unpackFromString(1,130,
"57,5:8,8,4,5,8,7,5:12,8,5:5,8,5,56,5:5,1,3,2,43,52,6,51,5,55:10,50,49,47,46" +
",53,5,54,41,10,39,33,11,19,10,23,17,10:2,13,10,21,31,35,10,25,15,27,10,29,3" +
"7,10:3,5,58,5:2,10,5,42,59,40,34,12,20,9,24,18,9:2,14,9,22,32,36,9,26,16,28" +
",9,30,38,9:3,44,5,45,48,5,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,119,
"0,1,2,3,1:3,4,1,5,1,6,1,7,8,1:6,9,1:3,10,1,11,1:19,12,1:4,13,1:3,14,1:5,15," +
"1:5,16,17,18,19,20,1,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38," +
"39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63," +
"64,65")[0];

	private int yy_nxt[][] = unpackFromString(66,60,
"1,2,3,4,5,6,7,8:2,9,6,69,9,72,9,6,9,75,9,77,9,115,9,6,9,6,9,79,9,6,9,81,9,6" +
",9,83,9,117,9,85,9,6,9,10,11,12,13,14,15,16,17,18,19,6,20,21,22,6:2,9,-1:62" +
",23,-1:60,24,-1:62,25,-1:62,9:34,-1:12,9,-1:3,9,-1:33,87,-1:79,30,-1:12,31," +
"-1:39,32,-1:68,21,-1:5,25:3,-1,25:55,-1:23,118:2,-1:37,50,-1:58,52:3,-1,52:" +
"51,-1:3,52,-1:4,57,-1:15,58,-1,59,-1:5,60,-1:27,61,-1,62,63,-1:22,64,-1:52," +
"88:2,-1:56,68:2,71:2,-1:46,51,-1:97,89:2,-1:28,74:2,-1:18,76:2,-1:54,33:2,-" +
"1:46,78:2,-1:2,26:2,27:2,-1:68,90:2,-1:44,28:2,-1:70,91:2,-1:52,84:2,-1:72," +
"34:2,-1:40,29:2,-1:66,35:2,-1:62,116:2,-1:38,92:2,-1:60,114:2,-1:26,113:2,-" +
"1:34,94:2,-1:58,97,-1:53,36:2,-1:86,37:2,-1:54,38:2,-1:54,98:2,-1:48,39:2,-" +
"1:50,40:2,-1:58,100:2,-1:60,101:2,-1:54,41:2,-1:76,102,-1:47,103:2,-1:66,10" +
"4:2,-1:44,42:2,-1:62,43:2,-1:60,105,-1:75,44:2,-1:42,106:2,-1:74,107,-1:53," +
"108:2,-1:42,109,-1:63,45:2,-1:88,46,-1:14,1,47,70,73,48,73:2,49,73:52,1,52:" +
"3,53,52:51,54,55,56,52,1,65:3,66,65:2,-1,65:48,67,65:3,-1:15,96:2,-1:84,95:" +
"2,-1:28,80:2,-1:18,82:2,-1:58,93:2,-1:50,86:2,-1:46,99:2,-1:47");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

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
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.MULT); }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -5:
						break;
					case 5:
						{ curr_lineno++; }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.MINUS); }
					case -8:
						break;
					case 8:
						{ ; }
					case -9:
						break;
					case 9:
						{
    if (Character.isLowerCase(yytext().charAt(0))) return new Symbol(TokenConstants.OBJECTID, new IdSymbol(yytext(), yytext().length(), yytext().hashCode()));
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), yytext().hashCode()));
}
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.PLUS); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.EQ); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.LT); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.NEG); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.SEMI); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.COLON); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.DOT); }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.COMMA); }
					case -20:
						break;
					case 20:
						{ return new Symbol(TokenConstants.AT); }
					case -21:
						break;
					case 21:
						{ return new Symbol(TokenConstants.INT_CONST, new IntSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -22:
						break;
					case 22:
						{ string_buf.setLength(0); yybegin(STRING); }
					case -23:
						break;
					case 23:
						{
    yybegin(COMMENT);
    commentLength++;
}
					case -24:
						break;
					case 24:
						{
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
					case -25:
						break;
					case 25:
						{ ; }
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.IF); }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.IN); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.FI); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.OF); }
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.DARROW); }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.LE); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.LET); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.NEW); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.NOT); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.ELSE); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.ESAC); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.LOOP); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.THEN); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.POOL); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.CASE); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.WHILE); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.CLASS); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.DIV); }
					case -47:
						break;
					case 47:
						{}
					case -48:
						break;
					case 48:
						{}
					case -49:
						break;
					case 49:
						{ ; }
					case -50:
						break;
					case 50:
						{
    commentLength++;
}
					case -51:
						break;
					case 51:
						{
    if(--commentLength == 0) {
        yybegin(YYINITIAL);
    }
}
					case -52:
						break;
					case 52:
						{ string_buf.append(yytext()); }
					case -53:
						break;
					case 53:
						{
		string_buf.setLength(0);
                yybegin(YYINITIAL);
                return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -54:
						break;
					case 54:
						{
		yybegin(YYINITIAL);
                String s = string_buf.toString();
		if(s.length() >= MAX_STR_CONST) {
                	return new Symbol(TokenConstants.ERROR, "String constant too long");
                } else {
                	return new Symbol(TokenConstants.STR_CONST, new StringSymbol(s, s.length(), s.hashCode()));
                }
}
					case -55:
						break;
					case 55:
						{
		yybegin(STRING_ERROR_NULL);
		return new Symbol(TokenConstants.ERROR, "String contains null character");
}
					case -56:
						break;
					case 56:
						{ ; }
					case -57:
						break;
					case 57:
						{ string_buf.append("\n"); }
					case -58:
						break;
					case 58:
						{ string_buf.append("\f"); }
					case -59:
						break;
					case 59:
						{ string_buf.append("\n"); }
					case -60:
						break;
					case 60:
						{ string_buf.append("\t"); }
					case -61:
						break;
					case 61:
						{ string_buf.append("\""); }
					case -62:
						break;
					case 62:
						{ string_buf.append("\\"); }
					case -63:
						break;
					case 63:
						{ string_buf.append("\b"); }
					case -64:
						break;
					case 64:
						{ string_buf.append("\\n"); }
					case -65:
						break;
					case 65:
						{ ; }
					case -66:
						break;
					case 66:
						{ yybegin(YYINITIAL); }
					case -67:
						break;
					case 67:
						{ yybegin(YYINITIAL); }
					case -68:
						break;
					case 69:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -69:
						break;
					case 70:
						{}
					case -70:
						break;
					case 72:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -71:
						break;
					case 73:
						{}
					case -72:
						break;
					case 75:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -73:
						break;
					case 77:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -74:
						break;
					case 79:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -75:
						break;
					case 81:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -76:
						break;
					case 83:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -77:
						break;
					case 85:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -78:
						break;
					case 115:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -79:
						break;
					case 117:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -80:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
