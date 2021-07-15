package refalpractice.scpgraphs.lexer;

public class Token {
	private int tag;
	private int line1, column1, line2, column2;
	
	public Token(int tag, int line1, int column1, int line2, int column2) {
		this.tag = tag;
		this.line1 = line1;
		this.column1 = column1;
		this.line2 = line2;
		this.column2 = column2;
	}
	
	public String toString() {
		return TokenTag.tagToString(tag) + " (" + line1 + ", " + column1 + ")-(" + line2 + ", " + column2 + ")";
	}
	
	public int getTag() {
		return tag;
	}
	
	public String getTagString() {
		return TokenTag.tagToString(tag);
	}
}
