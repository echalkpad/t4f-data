package io.datalayer.text.opennlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.WordTagSampleStream;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

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
    
    @Test
    public void train() throws IOException {
        FileInputStream dataIn = new FileInputStream("my-en-pos.train.txt");
        ObjectStream<String> os = new PlainTextByLineStream(dataIn, "UTF-8");
        ObjectStream<POSSample> sampleStream = new WordTagSampleStream(os);
        POSModel model = POSTaggerME.train("en", sampleStream,
        TrainingParameters.defaultParams(), null, null);
//        save(model);
    }
    
    @Test
    public void test() throws InvalidFormatException, IOException {        
        File posModelFile = new File("", "en-pos-maxent.bin");
        FileInputStream posModelStream = new FileInputStream(posModelFile);
        POSModel model = new POSModel(posModelStream);
        POSTaggerME tagger = new POSTaggerME(model);
        String[] words = SimpleTokenizer.INSTANCE.tokenize("my text to classify.");
        String[] result = tagger.tag(words);
    }

}
