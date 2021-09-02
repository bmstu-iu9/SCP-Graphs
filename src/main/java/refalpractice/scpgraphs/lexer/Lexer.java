package refalpractice.scpgraphs.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.Reader;

public class Lexer {
	private Position pos;
	private ArrayList<LexicErrorDesc> errs = new ArrayList<>();
	private Token nextToken;

	private static final HashMap<String, Integer> KEYWORD = new HashMap<>();
	static {
		KEYWORD.put("Node", TokenTag.NODE);
		KEYWORD.put("Children", TokenTag.CHILDREN);
		KEYWORD.put("Looped", TokenTag.LOOPED);
		KEYWORD.put("to", TokenTag.TO);
		KEYWORD.put("assign", TokenTag.ASSIGN);
		KEYWORD.put("call", TokenTag.CALL);
		KEYWORD.put("arg", TokenTag.ARG);
		KEYWORD.put("par", TokenTag.PAR);
		KEYWORD.put("e", TokenTag.E);
		KEYWORD.put("s", TokenTag.S);
		KEYWORD.put("t", TokenTag.T);
		KEYWORD.put("let", TokenTag.LET);
		KEYWORD.put("in", TokenTag.IN);
	}

	private static final HashMap<Integer, Integer> SPECSYMBOL = new HashMap<>();
	static {
		SPECSYMBOL.put(-1, TokenTag.EOF);
		SPECSYMBOL.put((int)'(', TokenTag.LPAREN);
		SPECSYMBOL.put((int)')', TokenTag.RPAREN);
		SPECSYMBOL.put((int)'*', TokenTag.STAR);
	}

	private void tokenizeWord() throws Exception {
		int line1 = pos.getLine();
		int column1 = pos.getColumn();
		int line2 = line1;
		int column2 = column1;
		StringBuilder sb = new StringBuilder();
		
		for (int c = pos.peek(); c > 0 && (Character.isLetterOrDigit((char)c)); c = pos.peek()) {
			line2 = pos.getLine();
			column2 = pos.getColumn();
			sb.append((char)c);
			pos.next();
		}
		
		String s = sb.toString();
		Integer keyword = KEYWORD.get(s);
		if (keyword != null)
			nextToken = new Token(keyword, line1, column1, line2, column2);
		else
			nextToken = new StringToken(TokenTag.IDENT, s, line1, column1, line2, column2);
	}

	private void tokenizeSymbol() throws Exception {
		int c = pos.peek();
		Integer specsymbol = SPECSYMBOL.get(c);
		if (specsymbol != null)
			nextToken = new Token(specsymbol, pos.getLine(), pos.getColumn(), pos.getLine(), pos.getColumn());
		else
			errs.add(new LexicErrorDesc(Character.toString((char)c), pos.getLine(), pos.getColumn(), pos.getLine(), pos.getColumn()));

		pos.next();
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

			if (Character.isLetterOrDigit((char)c))
				tokenizeWord();
			else
				tokenizeSymbol();
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
