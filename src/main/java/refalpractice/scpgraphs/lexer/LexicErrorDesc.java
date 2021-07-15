package refalpractice.scpgraphs.lexer;

public class LexicErrorDesc {
	private int line1, column1, line2, column2;
	private String str;
	
	public LexicErrorDesc(String str, int line1, int column1, int line2, int column2) {
		this.str = str;
		this.line1 = line1;
		this.column1 = column1;
		this.line2 = line2;
		this.column2 = column2;
	}
	
	public String toString() {
		return "lexic-error (" + line1 + ", " + column1 + ")-(" + line2 + ", " + column2 + "): " + str;
	}
	
	public String getString() {
		return str;
	}
}
