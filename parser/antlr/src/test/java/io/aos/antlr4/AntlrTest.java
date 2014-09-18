package io.aos.antlr4;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import simple1.Simple1BaseListener;
import simple1.Simple1Lexer;
import simple1.Simple1Listener;
import simple1.Simple1Parser;

public class AntlrTest {

    public static final String SEPARATOR = "===============================";

    @Test
    public void testSimple2() throws NoSuchMethodException, SecurityException, InterruptedException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // InputStream is = new FileInputStream(...);
        ANTLRInputStream input = new ANTLRInputStream("100+2*34" + "\n");
        Simple1Lexer lexer = new Simple1Lexer((CharStream) input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();

        System.out.println(SEPARATOR + " TOKENS " + SEPARATOR);
        for (Object tok : tokens.getTokens()) {
            System.out.println(tok);
        }

        Simple1Parser parser = new Simple1Parser(tokens);

        DiagnosticErrorListener del = new DiagnosticErrorListener();
        parser.addErrorListener(del);
        parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);

        // parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        // parser.setTrace(true);

        parser.setTokenStream(tokens);
        parser.setBuildParseTree(true);

        ParserRuleContext tree = parser.prog();
        // Method startRule = parser.getClass().getMethod("prog");
        // ParserRuleContext tree = (ParserRuleContext) startRule.invoke(parser,
        // (Object[])null);

        System.out.println(SEPARATOR + " TREE " + SEPARATOR);
        System.out.println(tree.toStringTree(parser));

        // tree.save(parser, this.psFile);

        ParserRuleContext entryPoint = parser.prog();
        ParseTreeWalker walker = new ParseTreeWalker();
        Simple1Listener listener = new Simple1BaseListener();
        walker.walk(listener, entryPoint);

        System.out.println(SEPARATOR + "GUI " + SEPARATOR);
        tree.inspect(parser);
        TimeUnit.SECONDS.sleep(10);

    }

}
