package refalpractice.scpgraphs.lexer;

import java.io.Reader;

public class Position {
	private int line = 1, column = 0;
	private Reader src;
	private int c;
	
	public Position(Reader src) throws Exception {
		this.src = src;
		next();
	}
	
	public int getLine() {
		return line;
	}
	
	public int getColumn() {
		return column;
	}
	
	public int peek() {
		return c;
	}
	
	public void next() throws Exception{
		if (c < 0)
			return;
		int c2 = src.read();
		if (c == '\n' || c == '\r' && c2 != '\n') {
			line++;
			column = 1;
		} else
			column++;
		c = c2;
	}
}
