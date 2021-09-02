package refalpractice.scpgraphs.encoder;

import org.junit.Test;
import refalpractice.scpgraphs.lexer.Lexer;
import refalpractice.scpgraphs.parser.Node;
import refalpractice.scpgraphs.parser.Parser;

import java.io.StringReader;

public class EncoderTest {
    private void runEncoder(String s, String header, String key) throws Exception {
        System.out.println(header);
        StringReader src = new StringReader(s);
        Lexer lexer = new Lexer(src);
        Node node = Parser.parse(lexer);
        Encoder encoder = new Encoder(node, key);
        EncodedNode encNode = encoder.encodeNode(node);
        Printer.printEncNode(encNode, node);
    }

    @Test
    public void test1() throws Exception {
        runEncoder("(Node (Finished)(0 )()(()((call Equal(arg (*A(call Gram(arg (par e N )))(par e x ))(*(call " +
                        "Gram(arg (par e N )))(par e y ))))))(Children (Node (Finished)(0 0 )((assign (par e N )(I(par e 1 " +
                        "))))(()(F)))(Node (Finished)(0 1 )((assign (par e N )(S(par e 2 ))))(()(F)))(Node (Finished)(0 2 )" +
                        "((assign (par e N )()))(()((call Equal(arg (*A(par e x ))(*(par e y ))))))(Children (Node (Finished)" +
                        "(0 2 0 )((assign (par e y )(A(par e x ))))(()(T)))(Node (Finished)(0 2 1 )()(()(F)))))))",
                "ENCODER TEST 1", "");
    }

    @Test
    public void test2() throws Exception {
        runEncoder("(Node (Finished)(0 )()(()((call Equal(arg (*A(call Gram(arg (par e N )))(par e x ))(*(call " +
                        "Gram(arg (par e N )))(par e y ))))))(Children (Node (Finished)(0 0 )((assign (par e N )(I(par e 1 " +
                        "))))(()(F)))(Node (Finished)(0 1 )((assign (par e N )(S(par e 2 ))))(()(F)))(Node (Finished)(0 2 )" +
                        "((assign (par e N )()))(()((call Equal(arg (*A(par e x ))(*(par e y ))))))(Children (Node (Finished)" +
                        "(0 2 0 )((assign (par e y )(A(par e x ))))(()(T)))(Node (Finished)(0 2 1 )()(()(F)))))))",
                "ENCODER TEST 2", "-trs");
    }

    @Test
    public void test3() throws Exception {
        runEncoder("(Node (Finished)(0 )()(let (assign (par e 6 )())(assign (par e 7 )((par e x )))in ((call " +
                        "Check(arg (par e 6 )(call F(arg (par e 7 )))))))(Children (Node (Finished)(0 0 )()(()((call " +
                        "Check(arg (par e 6 )(call F(arg (par e 7 )))))))(Children (Node (Finished)(0 0 0 )((assign (par e 7 )" +
                        "()))(()(T)))(Node (Finished)(0 0 1 )((assign (par e 7 )((par t 3 )(par e 9 ))))(let (assign (par e 17 " +
                        ")((par e 6 )A))(assign (par e 18 )((par e 9 )))in ((call Check(arg (par e 17 )(call F(arg (par e 18 ))" +
                        ")))))(Children (Node (Looped )(0 0 1 0 )()(aboba)(Looped to (0 0 )((assign (par e 6 )((par e 17 )))(assign " +
                        "(par e 7 )((par e 18 ))))))(Node (Finished)(0 0 1 1 )()(()((par e 6 )A)))(Node (Finished)(0 0 1 2 )()" +
                        "(()((par e 9 ))))))))(Node (Finished)(0 1 )()(()()))(Node (Finished)(0 2 )()(()((par e x ))))))",
                "ENCODER TEST 3", "-ref");
    }

    @Test
    public void test4() throws Exception {
        runEncoder("(Node (Finished)(0 )()(() (" +
                        "(call F (arg ('*' A)('*' B)('*' C)))" +
                        "(call F (arg ('*' (par e 1))('*' (par e 1))('*' (par e 1))))" +
                        ")))",
                "ENCODER TEST 4", "-trs");
    }

    @Test
    public void test5() throws Exception {
        runEncoder("(Node (Finished)(0 )()(() (" +
                        "(call F (arg ('*' A) (par e 22)(call G (arg A))))" +
                        "(call F (arg ('*' (par e 1001) ('*' A)) ))" +
                        "(call F (arg ('*')('*')))" +
                        ")))",
                "ENCODER TEST 5", "-trs");
    }
}
