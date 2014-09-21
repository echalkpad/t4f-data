package io.datalayer.text.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

import org.junit.Test;

public class OpenNlpTest {

    @Test
    public void testSentenceDetect() throws InvalidFormatException, IOException {

        String paragraph = "Hi. How are you? This is Mike.";

        // Always start with a model, a model is learned from training data.
        InputStream is = new FileInputStream("en-sent.bin");
        SentenceModel model = new SentenceModel(is);
        SentenceDetectorME sdetector = new SentenceDetectorME(model);

        String sentences[] = sdetector.sentDetect(paragraph);

        System.out.println(sentences[0]);
        System.out.println(sentences[1]);
        is.close();
    }

}
