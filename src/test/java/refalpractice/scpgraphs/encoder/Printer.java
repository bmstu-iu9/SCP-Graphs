package refalpractice.scpgraphs.encoder;

import refalpractice.scpgraphs.parser.*;

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

    public static void printChildrenNode(EncodedChildrenNode encodedChildren, ChildrenNode children) {
        System.out.println("children:");
        for (int i = 0; i < encodedChildren.nodes.size(); i++) {
            printEncNode(encodedChildren.nodes.get(i), children.nodes.get(i));
        }
        System.out.println("endchildren");
    }

    public static void printParenGroup(ParenGroup parenGroup) {
        System.out.println(parenGroup.toInternal());
    }

    public static void printString(String string) {
        System.out.println(string);
    }

    public static void printLoopedNode(EncodedLoopedNode encodedLooped, LoopedNode looped) {
        System.out.print("looped: ");
        printName(encodedLooped.name);
        System.out.printf("looped assignment: %s\n", looped.assignment.toInternal());
        System.out.printf("encoded looped assignment: %s\n", encodedLooped.assignment);
    }

    public static void printSubNode(EncodedSubNode encSubNode, SubNode subNode) {
        if (encSubNode instanceof EncodedChildrenNode)
            printChildrenNode((EncodedChildrenNode)encSubNode, (ChildrenNode)subNode);
        else if (encSubNode instanceof EncodedLoopedNode)
            printLoopedNode((EncodedLoopedNode)encSubNode, (LoopedNode) subNode);
    }

    public static void printEncNode(EncodedNode encNode, Node node) {
        System.out.println("ENCODED NODE");
        System.out.print("status: ");
        printParenGroup(encNode.status);
        System.out.print("name: ");
        printName(encNode.name);
        System.out.print("edge: ");
        printParenGroup(node.edge);
        System.out.print("encoded edge: ");
        printString(encNode.edge);
        System.out.print("data: ");
        printParenGroup(node.nodeData);
        System.out.print("encoded data: ");
        printString(encNode.nodeData);
        printSubNode(encNode.subNode, node.subNode);
        System.out.println();
    }
}
