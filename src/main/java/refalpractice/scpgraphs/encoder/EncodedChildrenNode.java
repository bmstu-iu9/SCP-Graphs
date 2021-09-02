package refalpractice.scpgraphs.encoder;

import refalpractice.scpgraphs.encoder.EncodedNode;

import java.util.ArrayList;

public class EncodedChildrenNode implements EncodedSubNode {
    public ArrayList<EncodedNode> nodes = new ArrayList<>();

    public EncodedChildrenNode() {}
}
