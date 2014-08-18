package io.datalayer.lucene.frequency;

import java.io.IOException;

import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.junit.Test;

public class AosTermFrequencyTest {
    
    @Test
    public void test() throws IOException {
        
        Term term = new Term("", "");
        IndexReader reader = null;

        DocsEnum docEnum = MultiFields.getTermDocsEnum(reader, MultiFields.getLiveDocs(reader), "contents", term.bytes());
        int termFreq = 0;

        int doc = DocsEnum.NO_MORE_DOCS;
        while ((doc = docEnum.nextDoc()) != DocsEnum.NO_MORE_DOCS) {
            termFreq += docEnum.freq();
        }
        
    }

}
