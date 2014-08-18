package io.datalayer.mahout;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.mahout.classifier.sgd.L2;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.Dictionary;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

public class IrisTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IrisTest.class);

    @Test
    public void test() throws IOException {

        RandomUtils.useTestSeed();
        Splitter onComma = Splitter.on(",");

        // read the data
        List<String> raw = Resources.readLines(Resources.getResource("iris.csv"), Charsets.UTF_8);

        // holds features
        List<Vector> data = Lists.newArrayList();

        // holds target variable
        List<Integer> target = Lists.newArrayList();

        // for decoding target values
        Dictionary dict = new Dictionary();

        // for permuting data later
        List<Integer> order = Lists.newArrayList();

        for (String line : raw.subList(1, raw.size())) {

            // order gets a list of indexes
            order.add(order.size());

            // parse the predictor variables
            Vector v = new DenseVector(5);
            v.set(0, 1);
            int i = 1;
            Iterable<String> values = onComma.split(line);
            for (String value : Iterables.limit(values, 4)) {
                v.set(i++, Double.parseDouble(value));
            }
            data.add(v);

            // and the target
            target.add(dict.intern((String) Iterables.get(values, 4)));

        }

        // randomize the order ... original data has each species all together
        // note that this randomization is deterministic
        Random random = RandomUtils.getRandom();
        Collections.shuffle(order, random);

        // select training and test data
        List<Integer> train = order.subList(0, 100);
        List<Integer> test = order.subList(100, 150);

        LOGGER.warn("Training set = {}", train);
        LOGGER.warn("Test set = {}", test);

        // now train many times and collect information on accuracy each time
        int[] correct = new int[test.size() + 1];
        for (int run = 0; run < 200; run++) {
            OnlineLogisticRegression lr = new OnlineLogisticRegression(3, 5, new L2(1));
            // 30 training passes should converge to > 95% accuracy nearly
            // always but never to 100%
            for (int pass = 0; pass < 30; pass++) {
                Collections.shuffle(train, random);
                for (Object k : train) {
                    lr.train((int) target.get((int) k), (Vector) data.get((int) k));
                }
            }

            // check the accuracy on held out data
            int x = 0;
            int[] count = new int[3];
            for (Integer k : test) {
                int r = lr.classifyFull(data.get(k)).maxValueIndex();
                // count[r]++;
                x += r == target.get(k) ? 1 : 0;
            }
            correct[x]++;
        }

        // verify we never saw worse than 95% correct,
        for (int i = 0; i < Math.floor(0.95 * test.size()); i++) {
            assertEquals(
                    String.format("%d trials had unacceptable accuracy of only %.0f%%: ", correct[i],
                            100.0 * i / test.size()), 0, correct[i]);
        }
        // nor perfect
        assertEquals(String.format("%d trials had unrealistic accuracy of 100%%", correct[test.size() - 1]), 0,
                correct[test.size()]);
    }

}
