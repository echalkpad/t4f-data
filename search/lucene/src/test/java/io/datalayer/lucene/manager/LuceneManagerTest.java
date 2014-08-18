package io.datalayer.lucene.manager;

import io.datalayer.lucene.helper.AosIndexUtil;

import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.ReaderManager;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LuceneManagerTest {
    private IndexWriter indexWriter;
    
    @Before
    public void setUp() throws IOException {
        indexWriter = AosIndexUtil.newIndexWithDocuments();
    }
    
    @After
    public void tearDown() throws IOException {
        indexWriter.close();
    }

    @Test
    public void testReaderManager() throws IOException {
        ReaderManager readerManager = new ReaderManager(AosIndexUtil.directory());
        IndexReader indexReader = null;
        try {
            indexReader = readerManager.acquire();
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        }
        finally {
            if (indexReader == null) {
                readerManager.release(DirectoryReader.open(AosIndexUtil.directory()));
            }
        }
    }

    @Test
    public void testSearcherManager() throws IOException {
        SearcherManager searcherManager = new SearcherManager(indexWriter.getDirectory(), new SearcherFactory());
        IndexSearcher indexSearcher = null;
        try {
            indexSearcher = searcherManager.acquire();
        }
        finally {
            if (indexSearcher == null) {
                searcherManager.release(indexSearcher);
            }
        }
    }
    
    /**
     * Lucene 4.3 does not have NRTManager
     * 
     * @see https://issues.apache.org/jira/browse/LUCENE-4967
     * @see http://svn.apache.org/viewvc?view=revision&revision=1478438
     * @see http://svn.apache.org/viewvc/lucene/dev/trunk/lucene/core/src/test/org/apache/lucene/search/TestControlledRealTimeReopenThread.java?view=markup&pathrev=1478438
     * 
     * @throws IOException
     */
    @Test
    public void testNrtManager() throws IOException {
//        TrackingIndexWriter trackingIndexWriter = new TrackingIndexWriter(indexWriter);
//        NRTManager nrtManager = new NRTManager(trackingIndexWriter, new SearcherFactory());
//        IndexSearcher indexSearcher = null;
//        try {
//            indexSearcher = nrtManager.acquire();
//        }
//        finally {
//            if (indexSearcher == null) {
//                nrtManager.release(indexSearcher);
//            }
//        }
    }

}
