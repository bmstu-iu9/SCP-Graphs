package refalpractice.scpgraphs.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.Reader;

public class Lexer {
	private Position pos;
	private ArrayList<LexicErrorDesc> errs = new ArrayList<>();
	private Token nextToken;
	
	private void tokenizeWord() throws Exception {
		int line1 = pos.getLine();
		int column1 = pos.getColumn();
		int line2 = line1;
		int column2 = column1;
		StringBuilder sb = new StringBuilder();
		
		for (int c = pos.peek(); c > 0 && (Character.isLetterOrDigit((char)c) || c == '_'); c = pos.peek()) {
			line2 = pos.getLine();
			column2 = pos.getColumn();
			sb.append((char)c);
			pos.next();
		}
		
		String s = sb.toString();
		if (s.equals("Node"))
			nextToken = new Token(TokenTag.NODE, line1, column1, line2, column2);
		else if (s.equals("Children"))
			nextToken = new Token(TokenTag.CHILDREN, line1, column1, line2, column2);
		else if (s.equals("Looped"))
			nextToken = new Token(TokenTag.LOOPED, line1, column1, line2, column2);
		else if (s.equals("to"))
			nextToken = new Token(TokenTag.TO, line1, column1, line2, column2);
		else if (s.equals("assign"))
			nextToken = new Token(TokenTag.ASSIGN, line1, column1, line2, column2);
		else if (s.equals("call"))
			nextToken = new Token(TokenTag.CALL, line1, column1, line2, column2);
		else if (s.equals("arg"))
			nextToken = new Token(TokenTag.ARG, line1, column1, line2, column2);
		else if (s.equals("par"))
			nextToken = new Token(TokenTag.PAR, line1, column1, line2, column2);
		else if (s.equals("e"))
			nextToken = new Token(TokenTag.E, line1, column1, line2, column2);
		else if (s.equals("s"))
			nextToken = new Token(TokenTag.S, line1, column1, line2, column2);
		else if (s.equals("t"))
			nextToken = new Token(TokenTag.T, line1, column1, line2, column2);
		else if (s.equals("let"))
			nextToken = new Token(TokenTag.LET, line1, column1, line2, column2);
		else if (s.equals("in"))
			nextToken = new Token(TokenTag.IN, line1, column1, line2, column2);
		else
			errs.add(new LexicErrorDesc(s, line1, column1, line2, column2));
	}
	
	private void skipWhiteSymbols() throws Exception {
		for (int c = pos.peek();; c = pos.peek()) {
			if (c > 0 && Character.isWhitespace((char)c))
				pos.next();
			else
				break;
		}
	}
	
	public void next() throws Exception {
		nextToken = null;
		
		while (nextToken == null) {
			skipWhiteSymbols();
			
			int c = pos.peek();
			
			switch (c) {
				case '(':
					nextToken = new Token(TokenTag.LPAREN, pos.getLine(), pos.getColumn(), pos.getLine(), pos.getColumn());
					pos.next();
					break;
				case ')':
					nextToken = new Token(TokenTag.RPAREN, pos.getLine(), pos.getColumn(), pos.getLine(), pos.getColumn());
					pos.next();
					break;
				case '*':
					nextToken = new Token(TokenTag.STAR, pos.getLine(), pos.getColumn(), pos.getLine(), pos.getColumn());
					pos.next();
					break;
				case -1:
					nextToken = new Token(TokenTag.EOF, pos.getLine(), pos.getColumn(), pos.getLine(), pos.getColumn());
					pos.next();
					break;
				default:
					if (Character.isLetterOrDigit((char)c) || c == '_')
						tokenizeWord();
					else {
						errs.add(new LexicErrorDesc(Character.toString((char)c), pos.getLine(), pos.getColumn(), pos.getLine(), pos.getColumn()));
						pos.next();
					}
			}
		}
	}
	
	public Token peek() {
		return nextToken;
	}
	
	public ArrayList<LexicErrorDesc> getErrors() {
		return errs;
	}
	
	public Lexer(Reader src) throws Exception {
		this.pos = new Position(src);
		
		next();
	}
}
