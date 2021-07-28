package refalpractice.scpgraphs.dot;

import refalpractice.scpgraphs.encoder.EncodedChildrenNode;
import refalpractice.scpgraphs.encoder.EncodedLoopedNode;
import refalpractice.scpgraphs.encoder.EncodedNode;

import java.util.ArrayList;
import java.util.Objects;

public class DotNode {
    private EncodedNode node;
    private StringBuilder dotRepresentation = new StringBuilder();

    public DotNode(EncodedNode node) {
        this.node = node;
    }

    public String ToDot() {
        dotRepresentation.append("digraph SCPGraph {\n");
        dotRepresentation.append("node[shape=box, style=rounded]\n");
        dotRepresentation.append(ToDotNode(node));
        dotRepresentation.append("}");
        return dotRepresentation.toString();
    }

    private String ToDotNode(EncodedNode node) {
        StringBuilder dotNode = new StringBuilder();

        String nodeName = GetNodeName(node.name);

        dotNode.append(nodeName);
        dotNode.append("[label = <<table border=\"0\"><tr><td align=\"text\">Name: ");
        dotNode.append(nodeName);
        dotNode.append("<br align=\"left\" /></td></tr>");

        dotNode.append("<tr><td align=\"text\">NodeData: ");
        dotNode.append(node.nodeData.replaceAll("->", "-&gt;"));
        dotNode.append("<br align=\"left\" /></td></tr></table>>];\n");

        if (node.subNode instanceof EncodedChildrenNode) {
            EncodedChildrenNode children = (EncodedChildrenNode)node.subNode;
            for (EncodedNode childrenNode : children.nodes) {
                dotNode.append(nodeName);
                dotNode.append("->");
                dotNode.append(GetNodeName(childrenNode.name));
                dotNode.append("[label = <<table border=\"0\"><tr><td align=\"text\">");
                if (!Objects.equals(node.edge, "()"))
                    dotNode.append(node.edge.replaceAll("->", "-&gt;"));
                dotNode.append("<br align=\"left\" /></td></tr></table>>];\n");
                dotNode.append(ToDotNode(childrenNode));
            }
        } else if (node.subNode instanceof EncodedLoopedNode) {
            EncodedLoopedNode loopedNode = (EncodedLoopedNode)node.subNode;
            dotNode.append(nodeName);
            dotNode.append("->");
            dotNode.append(GetNodeName(loopedNode.name));
            dotNode.append("[style=dashed, label = <<table border=\"0\"><tr><td align=\"text\">");
            dotNode.append(loopedNode.assignment.replaceAll("->", "-&gt;"));
            dotNode.append("<br align=\"left\" /></td></tr></table>>];\n");
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
