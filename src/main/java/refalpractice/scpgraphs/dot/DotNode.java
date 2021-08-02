package refalpractice.scpgraphs.dot;

import refalpractice.scpgraphs.encoder.EncodedChildrenNode;
import refalpractice.scpgraphs.encoder.EncodedLoopedNode;
import refalpractice.scpgraphs.encoder.EncodedNode;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class DotNode {
    private final String EPSILON = "&epsilon;", LET = "Let", IN = "in", DOT_ARROW = "->",
                         EMPTY_LABEL = "(() ())", SPACE = " ";

    private EncodedNode node;
    private StringBuilder dotRepresentation = new StringBuilder();

    public DotNode(EncodedNode node) {
        this.node = node;
    }

    public String ToDot() throws Exception {
        dotRepresentation.append("digraph SCPGraph {\n");
        dotRepresentation.append("node[shape=octagon, style=rounded]\n");
        dotRepresentation.append(ToDotNode(node));
        dotRepresentation.append("}");
        return dotRepresentation.toString();
    }

    private boolean isEmptyLabel(String label) {
        return  label.equals(EMPTY_LABEL);
    }

    private String beautifyLetLabel(String label) {
        StringBuilder sb = new StringBuilder();

        Position pos = new Position(1, 1);
        Lexer lexer = new Lexer();
        MatchesIterator iterator = lexer.testMatch(label, pos);

        while (iterator.hasNext()) {
            Match match = iterator.next();
            if (match.type == TYPES.IDENT || match.type == TYPES.GT) {
                sb.append(SPACE);
                sb.append(match.value);
                sb.append("</font><br align=\"left\" /></td></tr>");
                sb.append("<tr><td align=\"text\"><font face=\"Courier\">");
            } else if (match.type == TYPES.ELITER || match.type == TYPES.ENUMBER || match.type == TYPES.EPSILON) {
                sb.append(match.value);
            } else if (match.type == TYPES.SEMICOLON) {
                sb.append(match.value);
                sb.append("</font><br align=\"left\" /></td></tr>");
                sb.append("<tr><td align=\"text\"><font face=\"Courier\">");
            } else if (match.type == TYPES.LT) {
                sb.append(match.value);
            } else if (match.type == TYPES.LET) {
                sb.append(match.value);
                sb.append(SPACE);
            } else if (match.type == TYPES.ASSIGN) {
                sb.append(SPACE);
                sb.append(match.value);
                sb.append(SPACE);
            } else if (match.type == TYPES.IN) {
                sb.append(SPACE);
                sb.append(match.value);
                sb.append("</font><br align=\"left\" /></td></tr>");
                sb.append("<tr><td align=\"text\"><font face=\"Courier\">");
            } else if (match.type == TYPES.LPAREN) {
                //System.out.println(letLabel.substring(match.pos, letLabel.length() - 1));
                sb.append(beatufiyParenLabel(label.substring(match.pos, label.length() - 1)));
                break;
            } else {
                sb.append(SPACE);
            }
        }
        //sb.append(SPACE);
        return sb.toString();
    }

    private String beatufiyParenLabel(String label) {
        StringBuilder sb = new StringBuilder();
        Stack<Integer> stackLenOfLabel = new Stack<>();
        Position pos = new Position(1, 1);
        Lexer lexer = new Lexer();
        MatchesIterator iterator = lexer.testMatch(label, pos);
        Integer curLen = 0;

        stackLenOfLabel.push(0);
        while (iterator.hasNext()) {
            Match match = iterator.next();
            if (match.type ==  TYPES.LT) {
                String repeatedSpace = new String(new char[curLen]).replace("\0", SPACE);
                sb.append(repeatedSpace);
                sb.append(match.value);
                match = iterator.next();
                sb.append(match.value);
                stackLenOfLabel.push(1 + match.value.length());
                curLen += stackLenOfLabel.peek();
                sb.append("</font><br align=\"left\" /></td></tr>");
                sb.append("<tr><td align=\"text\"><font face=\"Courier\">");
            } else if (match.type == TYPES.LPAREN) {
                String repeatedSpace = new String(new char[curLen]).replace("\0", SPACE);
                sb.append(repeatedSpace);
                while(match.type != TYPES.RPAREN) {
                    match = iterator.next();
                    sb.append(match.value);
                    sb.append(SPACE);
                }
                sb.deleteCharAt(sb.length() - 2);
                sb.append("</font><br align=\"left\" /></td></tr>");
                sb.append("<tr><td align=\"text\"><font face=\"Courier\">");
            } else if (match.type == TYPES.GT) {
                curLen -= stackLenOfLabel.pop();
                String repeatedSpace = new String(new char[curLen]).replace("\0", SPACE);
                sb.append(repeatedSpace);
                sb.append(match.value);
                sb.append("</font><br align=\"left\" /></td></tr>");
                sb.append("<tr><td align=\"text\"><font face=\"Courier\">");
            } else if (match.type == TYPES.ENUMBER || match.type == TYPES.ELITER || match.type == TYPES.IDENT) {
                String repeatedSpace = new String(new char[curLen]).replace("\0", SPACE);
                sb.append(repeatedSpace);
                sb.append(match.value);
                sb.append("</font><br align=\"left\" /></td></tr>");
                sb.append("<tr><td align=\"text\"><font face=\"Courier\">");
            }
        }
        sb.append(SPACE);
        //sb.append("</font><br align=\"left\" /></td></tr>");

        return sb.toString();
    }

    private String ToDotNodeData(EncodedNode node, boolean isLetNode) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("<tr><td align=\"text\"><font face=\"Courier\">");
        //node.nodeData = node.nodeData.replaceAll("\\(\\)", "&epsilon;");
        String label;
        if (isLetNode) {
            //sb.append(LET);
            //sb.append(node.nodeData, 1, node.nodeData.length() - 1);
            //System.out.println(LET + node.nodeData.substring(1, node.nodeData.length() - 1));
            label = (LET + node.nodeData.substring(1, node.nodeData.length() - 1))
                    .replaceAll("\\(\\)", "(&epsilon;)");
            sb.append(beautifyLetLabel(label));
        } else {
            if (!isEmptyLabel(node.nodeData)) {
                //sb.append(node.nodeData, 5, node.nodeData.length() - 2);
                //System.out.println(node.nodeData);
                label = node.nodeData.substring( 5, node.nodeData.length() - 2)
                        .replaceAll("\\(\\)", "(&epsilon;)");
                if (node.nodeData.charAt(5) == '&') {
                    sb.append(beatufiyParenLabel(label));
                } else {
                    sb.append(label);
                }
            } else {
                sb.append(EPSILON);
            }
        }
        sb.append("</font><br align=\"left\" /></td></tr></table>>];\n");

        return sb.toString();
    }

    private String ToDotNode(EncodedNode node) throws Exception {
        StringBuilder dotNode = new StringBuilder();

        String nodeName = GetNodeName(node.name);

        boolean isLetNode = false;

        if (node.nodeData.contains(IN)) {
            dotNode.append(nodeName);
            dotNode.append("[shape=doubleoctagon]\n");
            isLetNode = true;
        }

        dotNode.append(nodeName);
        dotNode.append("[label = <<table border=\"0\">");

        dotNode.append(ToDotNodeData(node, isLetNode));

        if (node.subNode instanceof EncodedChildrenNode) {
            EncodedChildrenNode children = (EncodedChildrenNode)node.subNode;
            for (EncodedNode childrenNode : children.nodes) {
                dotNode.append(nodeName);
                dotNode.append(DOT_ARROW);
                dotNode.append(GetNodeName(childrenNode.name));
                dotNode.append("[label = <<table border=\"0\"><tr><td align=\"text\"><font face=\"Courier\">");
                if (Objects.equals(childrenNode.edge, "")) {
                    dotNode.append(" ");

                } else {
                    dotNode.append(childrenNode.edge, 1, childrenNode.edge.length() - 1);
                }
                dotNode.append("</font><br align=\"left\" /></td></tr></table>>];\n");
                dotNode.append(ToDotNode(childrenNode));
            }
        } else if (node.subNode instanceof EncodedLoopedNode) {
            EncodedLoopedNode loopedNode = (EncodedLoopedNode)node.subNode;
            dotNode.append(nodeName);
            dotNode.append(DOT_ARROW);
            dotNode.append(GetNodeName(loopedNode.name));
            dotNode.append("[style=dashed, label = <<table border=\"0\"><tr><td align=\"text\"><font face=\"Courier\">");
            if (Objects.equals(loopedNode.assignment, EPSILON)) {
                dotNode.append(EPSILON);
            } else {
                dotNode.append(loopedNode.assignment, 1, loopedNode.assignment.length() - 1);
            }
            dotNode.append("</font><br align=\"left\" /></td></tr></table>>];\n");
        }
        return dotNode.toString();
    }

    private String GetNodeName(ArrayList<String> name) {
        StringBuilder nodeName = new StringBuilder();
        if (name.size() > 0) {
            nodeName.append(name.get(0));
            for (int i = 1; i < name.size(); i++) {
                nodeName.append(name.get(i));
            }
        }
        return nodeName.toString();
    }
}
