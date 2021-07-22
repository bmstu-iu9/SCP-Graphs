package refalpractice.scpgraphs.parser;

import refalpractice.scpgraphs.lexer.Token;
import refalpractice.scpgraphs.lexer.TokenTag;

import java.util.ArrayList;

public class ParenGroup {
    private ArrayList<Token> tokens = new ArrayList<>();

    public String toInternal() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.size() - 1; i++) {
            sb.append(tokens.get(i).toInternal());
            if (tokens.get(i).getTag() != TokenTag.LPAREN && tokens.get(i + 1).getTag() != TokenTag.RPAREN)
                sb.append(' ');
        }
        sb.append(tokens.get(tokens.size() - 1).toInternal());
        return sb.toString();
    }

    public ParenGroup() {}

    public void add(Token t) {
        tokens.add(t);
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void trimTail() {
        tokens.remove(tokens.size() - 1);
    }
}
