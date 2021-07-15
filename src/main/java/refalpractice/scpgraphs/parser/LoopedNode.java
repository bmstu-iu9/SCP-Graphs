package refalpractice.scpgraphs.parser;

import java.util.ArrayList;

public class LoopedNode implements SubNode {
    public ArrayList<String> name = new ArrayList<>();
    public ParenGroup assignment;

    public LoopedNode() {}
}
