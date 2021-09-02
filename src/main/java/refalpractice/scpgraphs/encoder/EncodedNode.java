package refalpractice.scpgraphs.encoder;

import refalpractice.scpgraphs.parser.Node;
import refalpractice.scpgraphs.parser.ParenGroup;

import java.util.ArrayList;

public class EncodedNode {
    public ParenGroup status;
    public ArrayList<String> name = new ArrayList<>();
    public String edge;
    public String nodeData;
    public EncodedSubNode subNode;

    public EncodedNode(Node node) {
        this.status = node.status;
        this.name = node.name;
    }
}
