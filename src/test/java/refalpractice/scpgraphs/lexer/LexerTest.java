package refalpractice.scpgraphs.lexer;

import org.junit.Test;
import java.io.StringReader;
import java.util.ArrayList;

public class LexerTest {
	private void runLexer(String s, String header) throws Exception {
		System.out.println(header);
		StringReader src = new StringReader(s);
		Lexer lexer = new Lexer(src);
		while (lexer.peek().getTag() != TokenTag.EOF) {
			System.out.println(lexer.peek());
			lexer.next();
		}
		
		System.out.println("Errors:");
		ArrayList<LexicErrorDesc> errs = lexer.getErrors();
		for (LexicErrorDesc err : errs)
			System.out.println(err);
	}

	@Test
	public void test1() throws Exception {
		runLexer("(Node)", "LEXER TEST 1");
	}

	@Test
	public void test2() throws Exception {
		runLexer("(Node () + * in to too 2to\r\n - ___-__\n\r hhh to)", "LEXER TEST 2");
	}
}
