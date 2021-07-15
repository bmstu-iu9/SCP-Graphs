package refalpractice.scpgraphs.parser;

import java.util.ArrayList;

public class Node {
    public ParenGroup status;
    public ArrayList<String> name = new ArrayList<>();
    public ParenGroup edge;
    public ParenGroup nodeData;
    public SubNode subNode;

    public Node() {}
}
