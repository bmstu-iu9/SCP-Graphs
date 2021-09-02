package refalpractice.scpgraphs.encoder;

import refalpractice.scpgraphs.lexer.Token;

import java.util.ArrayList;

public class Position {
    private final int FIRST_INDEX = 0, OFFSET = 1, NUMBER_OF_ARGUMENT_TOKENS = 2, ERROR_CODE = -1;

    private ArrayList<Token> tokens;
    private int tokensSize, pointer;

    private boolean isWithin() {
        return (pointer >= FIRST_INDEX && pointer < tokensSize);
    }

    public Position(ArrayList<Token> tokens) {
        this.tokens = tokens;
        tokensSize = tokens.size();
        pointer = FIRST_INDEX;
    }

    public boolean isLastToken() {
        return (pointer == tokensSize - OFFSET);
    }

    public boolean belowLastToken() {
        return (pointer < tokensSize - OFFSET);
    }

    public boolean afterLastToken() {
        return (pointer > tokensSize - OFFSET);
    }

    public Token prev() {
        pointer--;
        if (isWithin())
            return tokens.get(pointer);
        else
            return null;
    }

    public Token current() {
        if (isWithin())
            return tokens.get(pointer);
        else
            return null;
    }

    public Token next() {
        pointer++;
        if (isWithin())
            return tokens.get(pointer);
        else
            return null;
    }

    public void moveToArgs() {
        pointer = pointer + NUMBER_OF_ARGUMENT_TOKENS;
    }

    public int getNextTag() {
        if (belowLastToken())
            return tokens.get(pointer + OFFSET).getTag();
        else
            return ERROR_CODE;
    }

}
