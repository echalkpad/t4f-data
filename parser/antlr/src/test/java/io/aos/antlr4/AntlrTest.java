package io.aos.antlr4;

import io.aos.antlr4.drink.DrinkLexer;
import io.aos.antlr4.drink.DrinkParser;
import io.aos.antlr4.simple1.Simple1BaseListener;
import io.aos.antlr4.simple1.Simple1Lexer;
import io.aos.antlr4.simple1.Simple1Listener;
import io.aos.antlr4.simple1.Simple1Parser;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

public class AntlrTest {

    public static final String SEPARATOR = "===============================";

    @Test
    public void testSimple1() throws NoSuchMethodException, SecurityException, InterruptedException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // InputStream is = new FileInputStream(...);
        ANTLRInputStream input = new ANTLRInputStream("100 + 2 * 34" + "\n");
        Simple1Lexer lexer = new Simple1Lexer((CharStream) input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();

        System.out.println(SEPARATOR + " TOKENS " + SEPARATOR);
        for (Token token : tokens.getTokens()) {
            System.out.println(token.getText() + " " + token);
        }

        Simple1Parser parser = new Simple1Parser(tokens);

        DiagnosticErrorListener del = new DiagnosticErrorListener();
        parser.addErrorListener(del);
        parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);

        // parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        // parser.setTrace(true);

        parser.setTokenStream(tokens);
        parser.setBuildParseTree(true);

        ParserRuleContext parserRuleContext = parser.prog();

        System.out.println(SEPARATOR + " TREE " + SEPARATOR);
        System.out.println(parserRuleContext.toStringTree(parser));

        System.out.println(SEPARATOR + "GUI " + SEPARATOR);
        parserRuleContext.inspect(parser);
        TimeUnit.SECONDS.sleep(10);

        // tree.save(parser, this.psFile);

        ParserRuleContext prog = parser.prog();
        ParseTreeWalker walker = new ParseTreeWalker();
        Simple1Listener listener = new Simple1BaseListener();
        walker.walk(listener, prog);

    }
    
    @Test
    public void testDrink() {

        // Get our lexer
        DrinkLexer lexer = new DrinkLexer(new ANTLRInputStream("the cup of tea"));
     
        // Get a list of matched tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);
     
        // Pass the tokens to the parser
        DrinkParser parser = new DrinkParser(tokens);
     
        // Specify our entry point
        ParserRuleContext parserRuleContext = parser.drinkSentence();
     
        System.out.println(SEPARATOR + " TREE " + SEPARATOR);
        System.out.println(parserRuleContext.toStringTree(parser));
        
    }

}
