package refalpractice.scpgraphs.parser;

import refalpractice.scpgraphs.lexer.Lexer;
import refalpractice.scpgraphs.lexer.StringToken;
import refalpractice.scpgraphs.lexer.Token;
import refalpractice.scpgraphs.lexer.TokenTag;

import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;

    private void expect(int tag) throws Exception {
        Token t = lexer.peek();
        if (t.getTag() != tag)
            throw new SyntaxException("" + TokenTag.tagToString(tag) + " expected", t);
        else
            lexer.next();
    }

    private ParenGroup parseParenGroup() throws Exception {
        int cnt = 1;
        ParenGroup p = new ParenGroup();
        p.add(lexer.peek());
        expect(TokenTag.LPAREN);

        for (Token t = lexer.peek(); cnt > 0; t = lexer.peek()) {
            if (t.getTag() == TokenTag.LPAREN)
                cnt++;
            else if (t.getTag() == TokenTag.RPAREN)
                cnt--;
            p.add(t);
            lexer.next();
        }

        return p;
    }

    private ArrayList<String> parseName() throws Exception{
        expect(TokenTag.LPAREN);
        ArrayList<String> name = new ArrayList<>();
        for (Token t = lexer.peek(); t.getTag() != TokenTag.RPAREN; t = lexer.peek()) {
            if (t.getTag() != TokenTag.IDENT)
                throw new SyntaxException("No ident in name", t);
            name.add(((StringToken)t).getValue());
            lexer.next();
        }
        lexer.next();
        return name;
    }

    private ChildrenNode parseChildren() throws Exception {
        ChildrenNode children = new ChildrenNode();
        Token t = lexer.peek();
        while (t.getTag() != TokenTag.RPAREN) {
            if (t.getTag() == TokenTag.LPAREN)
                children.nodes.add(parseNode());
            else
                throw new SyntaxException("not node in children", t);
            t = lexer.peek();
        }
        expect(TokenTag.RPAREN);
        return children;
    }

    private LoopedNode parseLooped() throws Exception {
        expect(TokenTag.TO);
        LoopedNode looped = new LoopedNode();
        looped.name = parseName();
        looped.assignment = parseParenGroup();
        expect(TokenTag.RPAREN);
        return looped;
    }

    private SubNode parseSubNode() throws Exception {
        Token t = lexer.peek();
        if (t.getTag() != TokenTag.LPAREN)
            return null;
        lexer.next();
        t = lexer.peek();
        if (t.getTag() == TokenTag.CHILDREN) {
            lexer.next();
            return parseChildren();
        } else if (t.getTag() == TokenTag.LOOPED) {
            lexer.next();
            return parseLooped();
        } else {
            throw new SyntaxException("unexpected token in subnode", t);
        }
    }

    private Node parseNode() throws Exception {
        expect(TokenTag.LPAREN);
        expect(TokenTag.NODE);
        Node node = new Node();
        node.status = parseParenGroup();
        node.name = parseName();
        node.edge = parseParenGroup();
        node.nodeData = parseParenGroup();
        node.subNode = parseSubNode();
        expect(TokenTag.RPAREN);
        return node;
    }

    private Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public static Node parse(Lexer lexer) throws Exception {
        Parser parser = new Parser(lexer);
        Node node = parser.parseNode();
        parser.expect(TokenTag.EOF);
        return node;
    }
}
