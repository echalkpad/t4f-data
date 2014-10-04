package io.aos.tika;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TikaTest {;

    @Test
    public void test1() throws IOException, SAXException, TikaException {

        InputStream is = new BufferedInputStream(new FileInputStream(new File("src/test/resources/io/aos/pdf/ScalaByExample.pdf")));
        
        Parser ps = new AutoDetectParser();
        BodyContentHandler bch = new BodyContentHandler(1000000);
        ps.parse(is, bch, new Metadata(), new ParseContext());

        String cnt = bch.toString();
        String[] sentences = detectSentences(cnt);
        String[] tokens = tokenize(cnt);
        String names = findNames(sentences);

    }

    @Test
    public void test2() throws IOException {
        String st = "src/test/resources/io/aos/pdf/ScalaByExample.pdf";
        FileWriter fw = new FileWriter("src/test/resources/io/aos/txt/subtitles_124.txt", false);
        BufferedWriter bufferWritter = new BufferedWriter(fw);
        bufferWritter.write(st);
        bufferWritter.close();
    }
    
    private String[] detectSentences(String content) throws InvalidFormatException, IOException {
        InputStream om = new FileInputStream("src/test/resources/io/aos/opennlp/en-sent.bin");
        SentenceModel sm = new SentenceModel(om);
        SentenceDetectorME sdm = new SentenceDetectorME(sm);
        String[] sentences = sdm.sentDetect(content);
        for (String sentence: sentences) {
            System.out.println(sentence);
        }
        return sentences;
    }

    private String[] tokenize(String token) throws InvalidFormatException, IOException {
        InputStream is = new FileInputStream("src/test/resources/io/aos/opennlp/en-token.bin");
        TokenizerModel tm = new TokenizerModel(is);
        Tokenizer tz = new TokenizerME(tm);
        return tz.tokenize(token);
    }

    private String findNames(String[] sentences) throws InvalidFormatException, IOException {
        
        InputStream is = new FileInputStream("src/test/resources/io/aos/opennlp/en-ner-person.bin");
        TokenNameFinderModel tnf = new TokenNameFinderModel(is);
        NameFinderME nf = new NameFinderME(tnf);

        Span[] sp = nf.find(sentences);

        String[] a = Span.spansToStrings(sp, sentences);
        StringBuilder fd = new StringBuilder();
        int l = a.length;

        for (int j = 0; j < l; j++) {
            fd = fd.append(a[j] + "\n");
        }
        
        return fd.toString();

    }

}
