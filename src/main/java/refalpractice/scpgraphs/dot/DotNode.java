package refalpractice.scpgraphs.dot;

import refalpractice.scpgraphs.encoder.EncodedChildrenNode;
import refalpractice.scpgraphs.encoder.EncodedLoopedNode;
import refalpractice.scpgraphs.encoder.EncodedNode;

import java.util.ArrayList;
import java.util.Objects;

public class DotNode {
    private final String EPSILON = "&epsilon;", LET = "Let", IN = "in", DOT_ARROW = "->",
                         EMPTY_LABEL = "(() ())";

    private EncodedNode node;
    private StringBuilder dotRepresentation = new StringBuilder();

    public DotNode(EncodedNode node) {
        this.node = node;
    }

    public String ToDot() {
        dotRepresentation.append("digraph SCPGraph {\n");
        dotRepresentation.append("node[shape=octagon, style=rounded]\n");
        dotRepresentation.append(ToDotNode(node));
        dotRepresentation.append("}");
        return dotRepresentation.toString();
    }

    private boolean isEmptyLabel(String label) {
        return  label.equals(EMPTY_LABEL);
    }

    private String ToDotNodeData(EncodedNode node, boolean isLetNode) {
        StringBuilder sb = new StringBuilder();

        sb.append("<tr><td align=\"text\"><font face=\"Courier\">");
        if (isLetNode) {
            sb.append(LET);
            sb.append(node.nodeData, 1, node.nodeData.length() - 1);
        } else {
            if (!isEmptyLabel(node.nodeData)) {
                sb.append(node.nodeData, 5, node.nodeData.length() - 2);
            } else {
                sb.append(EPSILON);
            }
        }
        sb.append("</font><br align=\"left\" /></td></tr></table>>];\n");

        return sb.toString();
    }

    private String ToDotNode(EncodedNode node) {
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
