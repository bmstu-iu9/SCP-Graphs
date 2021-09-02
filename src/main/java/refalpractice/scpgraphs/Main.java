package refalpractice.scpgraphs;

import com.beust.jcommander.JCommander;
import refalpractice.scpgraphs.dot.DotNode;
import refalpractice.scpgraphs.encoder.EncodedNode;
import refalpractice.scpgraphs.encoder.Encoder;
import refalpractice.scpgraphs.lexer.Lexer;
import refalpractice.scpgraphs.parser.Node;
import refalpractice.scpgraphs.parser.Parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.beust.jcommander.Parameter;


public class Main 
{
    @Parameter(names={"--path", "-p"}, description = "Path to scpgraph file.", required = true)
    private static String path = "";

    @Parameter(names="-trs", description = "Flag to generate labels in TRS-format. Can not work with -ref.")
    private static boolean trs = false;

    @Parameter(names="-ref", description = "Flag to generate labels in REFAL-format. Can not work with -trs.")
    private static boolean ref = false;

    @Parameter(names = "--help", help = true)
    private static boolean help = false;

    private static String getOutputFileName() {
        if (trs) {
            return Paths.get(path).getFileName().toString().split("\\.", 2)[0] + "_trs.dot";
        }
        return Paths.get(path).getFileName().toString().split("\\.", 2)[0] + "_ref.dot";
    }

    public static void main(String ... argv)
    {
        try {
            Main main = new Main();
            JCommander jct = JCommander.newBuilder()
                    .addObject(main)
                    .build();
            jct.parse(argv);
            if (help || (ref & trs)) {
                jct.usage();
                return;
            }

            String inputGraph = new String(Files.readAllBytes(Paths.get(path)));
            String outputFilename = getOutputFileName();

            StringReader src = new StringReader(inputGraph);
            Lexer lexer = new Lexer(src);
            Node node = Parser.parse(lexer);
            Encoder encoder;
            if (trs)
                encoder = new Encoder(node, "-trs");
            else
                encoder = new Encoder(node, "");
            EncodedNode encNode = encoder.encodeNode(node);
            DotNode dotNode = new DotNode(encNode);

            PrintStream console = System.out;
            File file = new File(outputFilename);
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
