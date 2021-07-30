package refalpractice.scpgraphs.encoder;

import refalpractice.scpgraphs.lexer.Token;
import refalpractice.scpgraphs.lexer.TokenTag;
import refalpractice.scpgraphs.parser.ChildrenNode;
import refalpractice.scpgraphs.parser.LoopedNode;
import refalpractice.scpgraphs.parser.Node;
import refalpractice.scpgraphs.parser.ParenGroup;

import java.util.HashMap;

public class Encoder {
    private final String POINT = ".", COMMA = ",", SPACE = " ", LEFT_PAREN = "(", RIGHT_PAREN = ")", SEMICOLON = ";",
            LEFT_ANGLE_PAREN = "\u003c", RIGHT_ANGLE_PAREN = "\u003e", ASSIGNMENT = ":=", ARROW = "\u2192",
            EMPTY = "\u03b5", OPEN_SUB = "<SUB>", CLOSE_SUB = "</SUB>",
            DEFAULT_KEY = "", REF_KEY = "-ref", TRS_KEY = "-trs";
    private final int ZERO = 0, ONE = 1, NUMBER_OF_PARAMETER_TOKENS = 5;

    private String key;
    private HashMap<String, Integer> numberOfArguments;

    private boolean isRef() {
        return (key.equals(DEFAULT_KEY) || key.equals(REF_KEY));
    }

    private boolean isTRS() {
        return key.equals(TRS_KEY);
    }

    public int findNumberOfArguments(ParenGroup arguments) {
        Position argsPos = new Position(arguments.getTokens());
        int counter = ZERO;

        while (true) {
            if (argsPos.afterLastToken())
                break;
            else if (argsPos.getNextTag() == TokenTag.STAR) {
                argsPos.next();
                getSubExpr(argsPos);
                argsPos.next();

                counter++;
            }
            else {
                counter++;
                break;
            }
        }

        return counter;
    }

    public void splitCalls(ParenGroup expr) {
        Position callPos = new Position(expr.getTokens());

        while (!callPos.afterLastToken()) {
            if (callPos.current().getTag() == TokenTag.CALL) {
                String functionName = callPos.next().toInternal();
                numberOfArguments.putIfAbsent(functionName, ZERO);

                callPos.moveToArgs();
                ParenGroup arguments = getSubExpr(callPos);
                arguments.trimTail();

                int count = findNumberOfArguments(arguments);
                if (numberOfArguments.get(functionName).compareTo(count) < ZERO)
                    numberOfArguments.replace(functionName, count);

                splitCalls(arguments);
            }
            callPos.next();
        }
    }

    public void splitExpressions(Node node) {
        splitCalls(node.edge);
        splitCalls(node.nodeData);

        if (node.subNode instanceof ChildrenNode) {
            for (Node subNode : ((ChildrenNode) node.subNode).nodes)
                splitExpressions(subNode);
        }
        else if (node.subNode instanceof LoopedNode)
            splitCalls(((LoopedNode)node.subNode).assignment);
    }

    public Encoder(Node node, String key) {
        this.key = key;
        numberOfArguments = new HashMap<>();
        splitExpressions(node);
    }

    public EncodedNode encodeNode(Node node) {
        EncodedNode encNode = new EncodedNode(node);

        encNode.edge = encodeExpr(node.edge, false);
        encNode.nodeData = encodeExpr(node.nodeData, false);

        if (node.subNode instanceof ChildrenNode) {
            encNode.subNode = new EncodedChildrenNode();
            for (Node subNode : ((ChildrenNode) node.subNode).nodes) {
                ((EncodedChildrenNode)encNode.subNode).nodes.add(encodeNode(subNode));
            }
        }
        else if (node.subNode instanceof LoopedNode) {
            encNode.subNode = new EncodedLoopedNode();
            ((EncodedLoopedNode)encNode.subNode).name = ((LoopedNode) node.subNode).name;
            ((EncodedLoopedNode)encNode.subNode).assignment =
                    encodeExpr(((LoopedNode)node.subNode).assignment, true);
        }
        return encNode;
    }

    public String encodeExpr(ParenGroup expr, boolean isLooped) {
        Position pos = new Position(expr.getTokens());
        StringBuilder sb = new StringBuilder();
        boolean isLet = false;

        while (pos.belowLastToken()) {
            if (pos.current().getTag() == TokenTag.LPAREN) {
                switch (pos.next().getTag()) {

                    case TokenTag.PAR:
                        if (isRef()) {
                            sb.append(pos.next().toInternal());
                            sb.append(POINT);
                            sb.append(pos.next().toInternal());
                        }
                        else if (isTRS()) {
                            sb.append(pos.next().toInternal());
                            sb.append(OPEN_SUB);
                            sb.append(pos.next().toInternal());
                            sb.append(CLOSE_SUB);
                        }
                        pos.next();
                        break;

                    case TokenTag.CALL:
                        String functionName = pos.next().toInternal();

                        if (isRef())
                            sb.append(LEFT_ANGLE_PAREN);
                        sb.append(functionName);
                        if (isTRS())
                            sb.append(LEFT_PAREN);
                        pos.moveToArgs();

                        ParenGroup callExpr = getSubExpr(pos);
                        callExpr.trimTail();
                        if (isRef()) {
                            String argument = encodeExpr(callExpr, isLooped);
                            if (argument.length() > ZERO)
                                sb.append(SPACE);
                            sb.append(argument);
                            sb.append(RIGHT_ANGLE_PAREN);
                        }
                        else if (isTRS()) {
                            Position functionPos = new Position(callExpr.getTokens());
                            int numberOfArgs = numberOfArguments.get(functionName);
                            while (true) {
                                if (functionPos.afterLastToken()) {
                                    break;
                                }
                                else if (functionPos.getNextTag() == TokenTag.STAR) {
                                    functionPos.next();
                                    ParenGroup starArgument = getSubExpr(functionPos);
                                    starArgument.trimTail();
                                    functionPos.next();
                                    String encSubExpr = encodeExpr(starArgument, isLooped);
                                    if (encSubExpr.length() == ZERO)
                                        encSubExpr = EMPTY;
                                    sb.append(encSubExpr);

                                    numberOfArgs--;
                                    if (numberOfArgs > ZERO) {
                                        sb.append(COMMA);
                                        sb.append(SPACE);
                                    }
                                }
                                else {
                                    ParenGroup tailArgument = new ParenGroup();

                                    functionPos.prev();
                                    while (functionPos.belowLastToken()) {
                                        tailArgument.add(functionPos.next());
                                    }
                                    sb.append(encodeExpr(tailArgument, isLooped));

                                    numberOfArgs--;
                                    if (numberOfArgs > ZERO) {
                                        sb.append(COMMA);
                                        sb.append(SPACE);
                                    }
                                    break;
                                }
                            }
                            if (numberOfArgs != ZERO) {
                                while (numberOfArgs > ONE) {
                                    sb.append(EMPTY);
                                    sb.append(COMMA);
                                    sb.append(SPACE);
                                    numberOfArgs--;
                                }
                                sb.append(EMPTY);
                            }
                            sb.append(RIGHT_PAREN);
                        }
                        pos.next();
                        break;
                    case TokenTag.LET:
                        sb.append(pos.prev().toInternal());
                        pos.next();
                        isLet = true;
                        break;
                    case TokenTag.IN:
                        sb.append(pos.prev().toInternal());
                        pos.next();
                        isLet = false;
                        break;
                    case TokenTag.ASSIGN:
                        ParenGroup parameter = new ParenGroup();
                        for (int j = ZERO; j < NUMBER_OF_PARAMETER_TOKENS; j++) {
                            parameter.add(pos.next());
                        }
                        pos.next();

                        ParenGroup assignExpr = getSubExpr(pos);
                        assignExpr.trimTail();
                        sb.append(encodeExpr(parameter, isLooped));
                        if (isLet || isLooped) {
                            sb.append(SPACE);
                            sb.append(ASSIGNMENT);
                            sb.append(SPACE);
                        }
                        else {
                            sb.append(SPACE);
                            sb.append(ARROW);
                            sb.append(SPACE);
                        }

                        String assignParameter = encodeExpr(assignExpr, isLooped);
                        if (assignParameter.length() == 0) {
                            assignParameter = EMPTY;
                        }
                        sb.append(assignParameter);
                        pos.next();

                        if (pos.belowLastToken()) {
                            pos.next();
                            if (pos.belowLastToken() && pos.getNextTag() == TokenTag.ASSIGN) {
                                sb.append(SEMICOLON);
                            }
                            pos.prev();
                        }

                        break;
                    case TokenTag.STAR:
                        sb.append(pos.prev().toInternal());
                        pos.next();
                        ParenGroup starExpr = getSubExpr(pos);
                        sb.append(encodeExpr(starExpr, isLooped));
                        break;
                    default:
                        sb.append(pos.prev().toInternal());
                }
            }
            else {
                sb.append(pos.current().toInternal());
            }
            if (pos.belowLastToken() && pos.current().getTag() != TokenTag.LPAREN && pos.getNextTag() != TokenTag.RPAREN)
                sb.append(SPACE);
            pos.next();
        }
        if (pos.isLastToken()) {
            sb.append(pos.current().toInternal());
        }
        return sb.toString();
    }

    public ParenGroup getSubExpr(Position pos) {
        ParenGroup subExpr = new ParenGroup();
        int parensCounter = ONE;

        while (parensCounter != ZERO) {
            Token nextToken = pos.next();
            if (nextToken.getTag() == TokenTag.LPAREN) {
                parensCounter++;
            }
            else if (nextToken.getTag() == TokenTag.RPAREN) {
                parensCounter--;
            }
            subExpr.add(nextToken);
        }

        return subExpr;
    }
}
