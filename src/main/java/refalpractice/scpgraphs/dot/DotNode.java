package refalpractice.scpgraphs.dot;

import refalpractice.scpgraphs.parser.ChildrenNode;
import refalpractice.scpgraphs.parser.LoopedNode;
import refalpractice.scpgraphs.parser.Node;

import java.util.ArrayList;

public class DotNode {
    private Node node;
    private StringBuilder dotRepresentation = new StringBuilder();

    public DotNode(Node node) {
        this.node = node;
    }

    public String ToDot() {
        dotRepresentation.append("digraph SCPGraph {\n");
        dotRepresentation.append(ToDotNode(node));
        dotRepresentation.append("}");
        return dotRepresentation.toString();
    }

    private String ToDotNode(Node node) {
        StringBuilder dotNode = new StringBuilder();

        String nodeName = GetNodeName(node.name);

        dotNode.append(nodeName);
        dotNode.append("[label = \"Name: ");
        dotNode.append(nodeName);
        dotNode.append("\\n");

        dotNode.append("Status: ");
        dotNode.append(node.status.toInternal());
        dotNode.append("\\n");

        dotNode.append("NodeData: ");
        dotNode.append(node.nodeData.toInternal());
        dotNode.append("\"];\n");

        if (node.subNode instanceof ChildrenNode) {
            ChildrenNode children = (ChildrenNode)node.subNode;
            for (Node childrenNode : children.nodes) {
                dotNode.append(nodeName);
                dotNode.append("->");
                dotNode.append(GetNodeName(childrenNode.name));
                dotNode.append("[label = \"");
                dotNode.append(node.edge.toInternal());
                dotNode.append("\"];\n");
                dotNode.append(ToDotNode(childrenNode));
            }
        } else if (node.subNode instanceof LoopedNode) {
            LoopedNode loopedNode = (LoopedNode)node.subNode;
            dotNode.append(nodeName);
            dotNode.append("->");
            dotNode.append(loopedNode.name);
            dotNode.append("[style=dashed, label = \"");
            dotNode.append(loopedNode.assignment.toInternal());
            dotNode.append("\"];\n");
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
