package refalpractice.scpgraphs.parser;

import java.util.ArrayList;

public class Printer {
    public static void printName(ArrayList<String> name) {
        if (name.size() > 0) {
            System.out.print(name.get(0));
            for (int i = 1; i < name.size(); i++)
                System.out.printf(" %s", name.get(i));
        }
        System.out.println();
    }

    public static void printChildrenNode(ChildrenNode children) {
        System.out.println("children:");
        for (Node node : children.nodes)
            printNode(node);
        System.out.println("endchildren");
    }

    public static void printParenGroup(ParenGroup parenGroup) {
        System.out.println(parenGroup.toInternal());
    }

    public static void printLoopedNode(LoopedNode looped) {
        System.out.print("looped: ");
        printName(looped.name);
        System.out.printf("looped assignment: %s\n", looped.assignment.toInternal());
    }

    public static void printSubNode(SubNode subNode) {
        if (subNode instanceof ChildrenNode)
            printChildrenNode((ChildrenNode)subNode);
        else if (subNode instanceof LoopedNode)
            printLoopedNode((LoopedNode)subNode);
    }

    public static void printNode(Node node) {
        System.out.println("NODE");
        System.out.print("status: ");
        printParenGroup(node.status);
        System.out.print("name: ");
        printName(node.name);
        System.out.print("edge: ");
        printParenGroup(node.edge);
        System.out.print("data: ");
        printParenGroup(node.nodeData);
        printSubNode(node.subNode);
        System.out.println();
    }
}
