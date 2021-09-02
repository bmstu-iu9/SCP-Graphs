package refalpractice.scpgraphs.lexer;

public class StringToken extends Token {
    private String s;

    public StringToken(int tag, String s, int line1, int column1, int line2, int column2) {
        super(tag, line1, column1, line2, column2);
        this.s = s;
    }

    public String toString() {
        return super.toString() + ": " + s;
    }

    public String getValue() {
        return s;
    }

    public String toInternal() {
        return s;
    }
}
