package refalpractice.scpgraphs.lexer;

public class TokenTag {
	public static final int EOF = 0;
	public static final int LPAREN = 1;
	public static final int RPAREN = 2;
	public static final int NODE = 3;
	public static final int CHILDREN = 4;
	public static final int LOOPED = 5;
	public static final int TO = 6;
	public static final int ASSIGN = 7;
	public static final int STAR = 8;
	public static final int CALL = 9;
	public static final int ARG = 10;
	public static final int PAR = 11;
	public static final int E = 12;
	public static final int S = 13;
	public static final int T = 14;
	public static final int LET = 15;
	public static final int IN = 16;
	public static final int IDENT = 17;
	
	private static final String[] TAG_INT_TO_STRING = {
		"EOF", "LPAREN", "RPAREN", "NODE", "CHILDREN", "LOOPED", "TO", "ASSIGN", "STAR", "CALL", "ARG",
		"PAR", "E", "S", "T", "LET", "IN", "IDENT"
	};

	private static final String[] TAG_INT_TO_INTERNAL = {
			null, "(", ")", "Node", "Children", "Looped", "to", "assign", "*", "call", "arg",
			"par", "e", "s", "t", "let", "in", null
	};
	
	public static String tagToString(int tag) {
		return TAG_INT_TO_STRING[tag];
	}

	public static String tagToInternal(int tag) {
		return TAG_INT_TO_INTERNAL[tag];
	}
}
