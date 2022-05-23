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
		57,
		58,
		59
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
		/* 49 */ YY_NOT_ACCEPT,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NOT_ACCEPT,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NOT_ACCEPT,
		/* 54 */ YY_NOT_ACCEPT,
		/* 55 */ YY_NOT_ACCEPT,
		/* 56 */ YY_NOT_ACCEPT,
		/* 57 */ YY_NOT_ACCEPT,
		/* 58 */ YY_NOT_ACCEPT,
		/* 59 */ YY_NOT_ACCEPT
	};
	private int yy_cmap[] = unpackFromString(1,130,
"32,6:8,7,35,6,7,5,6:12,7,6:5,7,6,31,6:5,1,3,2,18,27,4,26,6,30:10,25,24,22,2" +
"1,28,6,29,8:3,17,11,8:3,12,8:12,16,8:4,6,33,6:2,9,6,10,34,10:3,13,10:7,14,1" +
"0:5,15,10:6,19,6,20,23,6,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,60,
"0,1,2,3,1,4,1:2,5,6,1,7,1,8,9,1:6,10,1:4,11,1:4,2,1,12,1:2,13,1:5,14,1:6,15" +
",16,17,1,18,19,20,21,22,23,24")[0];

	private int yy_nxt[][] = unpackFromString(25,36,
"1,2,3,4,5,6,7,6,8,7,9,8:2,9:3,8:2,10,11,12,13,14,15,16,17,18,19,7,20,21,22," +
"7:2,9,23,-1:38,24,-1:36,25,-1:36,26,-1:39,8:10,-1:12,8,-1:3,8,-1:9,9:10,-1:" +
"12,9,-1:3,9,-1:18,49,-1:46,27,-1:11,28,-1:16,29,-1:44,21,-1:6,26:34,-1:2,33" +
":30,-1:3,33,-1:14,38,39,40,-1:15,41,-1,42,43,44,-1:14,45,-1:33,51,-1:26,32," +
"-1:48,53,-1:31,54,-1:40,55,-1:29,56,-1:44,30,-1:15,1,31,50,52:32,23,1,33:30" +
",34,35,36,33,37,1,46:4,-1,46:25,47,46:3,48");

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
          		return new Symbol(TokenConstants.ERROR, "Found an EOF in the comment");
		case STRING:
			yybegin(YYINITIAL);
			return new Symbol(TokenConstants.ERROR, "Found an EOF in the string");
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
						{ return new Symbol(TokenConstants.MINUS); }
					case -6:
						break;
					case 6:
						{ ; }
					case -7:
						break;
					case 7:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  return new Symbol(TokenConstants.ERROR, "Unknown character: " + yytext()); }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.OBJECTID, new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
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
						{ curr_lineno++; }
					case -24:
						break;
					case 24:
						{
			yybegin(COMMENT);
			commentLength++;
}
					case -25:
						break;
					case 25:
						{ return new Symbol(TokenConstants.ERROR, "Closing character not found"); }
					case -26:
						break;
					case 26:
						{ ; }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.DARROW); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.LE); }
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.DIV); }
					case -31:
						break;
					case 31:
						{ ; }
					case -32:
						break;
					case 32:
						{
			commentLength--;
			if(commentLength == 0) {
				 yybegin(YYINITIAL);
			}
}
					case -33:
						break;
					case 33:
						{ string_buf.append(yytext()); }
					case -34:
						break;
					case 34:
						{
		yybegin(YYINITIAL);
                String s = string_buf.toString();
		if(s.length() >= MAX_STR_CONST) {
                	return new Symbol(TokenConstants.ERROR, "String too long");
                } else {
                	return new Symbol(TokenConstants.STR_CONST, new StringSymbol(s, s.length(), s.hashCode()));
                }
}
					case -35:
						break;
					case 35:
						{
		yybegin(STRING_ERROR_NULL);
		return new Symbol(TokenConstants.ERROR, "A null character was found in the string");
}
					case -36:
						break;
					case 36:
						{ ; }
					case -37:
						break;
					case 37:
						{
		string_buf.setLength(0);
                yybegin(YYINITIAL);
                return new Symbol(TokenConstants.ERROR, "The string was not closed");
}
					case -38:
						break;
					case 38:
						{ string_buf.append("\f"); }
					case -39:
						break;
					case 39:
						{ string_buf.append("\n"); }
					case -40:
						break;
					case 40:
						{ string_buf.append("\t"); }
					case -41:
						break;
					case 41:
						{ string_buf.append("\""); }
					case -42:
						break;
					case 42:
						{ string_buf.append("\\"); }
					case -43:
						break;
					case 43:
						{ string_buf.append("\b"); }
					case -44:
						break;
					case 44:
						{ string_buf.append("\n"); }
					case -45:
						break;
					case 45:
						{ string_buf.append("\\n"); }
					case -46:
						break;
					case 46:
						{ ; }
					case -47:
						break;
					case 47:
						{ yybegin(YYINITIAL); }
					case -48:
						break;
					case 48:
						{ yybegin(YYINITIAL); }
					case -49:
						break;
					case 50:
						{ ; }
					case -50:
						break;
					case 52:
						{ ; }
					case -51:
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
