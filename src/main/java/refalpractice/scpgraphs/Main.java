package refalpractice.scpgraphs;

import refalpractice.scpgraphs.dot.DotNode;
import refalpractice.scpgraphs.lexer.Lexer;
import refalpractice.scpgraphs.parser.Node;
import refalpractice.scpgraphs.parser.Parser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args )
    {
        if (args.length != 1) {
            System.out.println(Arrays.toString(args));
            System.out.println("You should give path : <path>");
            System.exit(1);
        }
        try {
            String inputGraph = new String(Files.readAllBytes(Paths.get(args[0])));
            System.out.println(inputGraph);

            StringReader src = new StringReader(inputGraph);
            Lexer lexer = new Lexer(src);
            Node node = Parser.parse(lexer);
            DotNode dotNode = new DotNode(node);

            PrintStream console = System.out;
            File file = new File("graph.dot");
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            System.setOut(ps);
            System.out.println(dotNode.ToDot());
            System.setOut(console);

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
