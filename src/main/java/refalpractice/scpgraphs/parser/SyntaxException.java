package refalpractice.scpgraphs.parser;

import refalpractice.scpgraphs.lexer.Token;

public class SyntaxException extends Exception {
    public SyntaxException(String msg, Token token) {
        super("Syntax error: " + msg + "\n\tat token " + token.toString());
    }
}
