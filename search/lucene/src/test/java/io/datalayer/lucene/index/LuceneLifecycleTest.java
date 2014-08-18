/****************************************************************
 * Licensed to the AOS Community (AOS) under one or more        *
 * contributor license agreements.  See the NOTICE file         *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The AOS licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package io.datalayer.lucene.index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import io.datalayer.lucene.helper.AosAnalyser;
import io.datalayer.lucene.helper.AosDirectory;
import io.datalayer.lucene.helper.AosFieldType;
import io.datalayer.lucene.helper.AosLuceneUtil;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

/**
 * #1 One initial document has bridges
 * 
 * #2 Create writer with maxFieldLength 1
 * 
 * #3 Index document with bridges
 * 
 * #4 Document can't be found
 */
public class LuceneLifecycleTest {

    private static String[] ids = { "1", "2" };
    private static String[] unindexed = { "Netherlands", "Italy" };
    private static String[] unstored = { "Amsterdam has lots of bridges", "Venice has lots of canals" };
    private static String[] text = { "Amsterdam", "Venice" };

    private Directory directory;

    @Before
    public void setUp() throws Exception {

        directory = AosDirectory.newDirectory();

        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_46,
                AosAnalyser.NO_LIMIT_TOKEN_COUNT_WHITE_SPACE_ANALYSER);

        IndexWriter writer = new IndexWriter(directory, conf);

        for (int i = 0; i < ids.length; i++) {
            Document doc = new Document();
            doc.add(new Field("id", ids[i], AosFieldType.INDEXED_STORED_TERMVECTORS));
            doc.add(new Field("country", unindexed[i], AosFieldType.INDEXEDNOT_STORED_TERMVECTORSNOT));
            doc.add(new Field("contents", unstored[i], AosFieldType.INDEXED_STOREDNOT_TERMVECTORS));
            doc.add(new Field("city", text[i], AosFieldType.INDEXED_STORED_TERMVECTORS));
            writer.addDocument(doc);
        }

        writer.close();

    }

    @Test
    public void testWriter() throws IOException {
        IndexWriter writer = getWriter();
        assertEquals(ids.length, writer.numDocs());
        writer.close();
    }

    @Test
    public void testReader() throws IOException {
        IndexReader reader = DirectoryReader.open(directory);
        assertEquals(ids.length, reader.maxDoc());
        assertEquals(ids.length, reader.numDocs());
        reader.close();
    }

    /**
     * #1 Run before every test
     * 
     * #2 Create IndexWriter
     * 
     * #3 Add documents
     * 
     * #4 Create new searcher
     * 
     * #5 Build simple single-term query
     * 
     * #6 Get number of hits
     * 
     * #7 Verify writer document count
     * 
     * #8 Verify reader document count
     */
    @Test
    public void testDeleteBeforeOptimize() throws IOException {
        IndexWriter writer = getWriter();
        assertEquals(2, writer.numDocs());
        writer.deleteDocuments(new Term("id", "1"));
        writer.commit();
        assertTrue(writer.hasDeletions());
        assertEquals(2, writer.maxDoc());
        assertEquals(1, writer.numDocs());
        writer.close();
    }

    /**
     * #1 Index contains deletions
     * 
     * #2 1 indexed document, 1 deleted document
     * 
     * #3 Optimize compacts deletes
     */
    @Test
    public void testDeleteAfterOptimize() throws IOException {
        IndexWriter writer = getWriter();
        assertEquals(2, writer.numDocs());
        writer.deleteDocuments(new Term("id", "1"));
        writer.forceMerge(1);
        writer.commit();
        assertFalse(writer.hasDeletions());
        assertEquals(1, writer.maxDoc());
        assertEquals(1, writer.numDocs());
        writer.close();
    }

    /**
     * #A 2 docs in the index
     * 
     * #B Delete first document
     * 
     * #C 1 indexed document, 0 deleted documents
     */
    @Test
    public void testUpdate() throws IOException {

        assertEquals(1, getHitCount("city", "Amsterdam"));

        IndexWriter writer = getWriter();

        Document doc = new Document();
        doc.add(new StoredField("id", "1"));
        doc.add(new StoredField("country", "Netherlands"));
        doc.add(new Field("contents", "Den Haag has a lot of museums", AosFieldType.INDEXED_STOREDNOT_TERMVECTORS));
        doc.add(new Field("city", "Den Haag", AosFieldType.INDEXED_STOREDNOT_TERMVECTORS));

        writer.updateDocument(new Term("id", "1"), doc);
        writer.close();

        assertEquals(0, getHitCount("city", "Amsterdam"));
        assertEquals(1, getHitCount("city", "Haag"));
    }

    /**
     * #A Create new document with "Haag" in city field
     * 
     * #B Replace original document with new version
     * 
     * #C Verify old document is gone #D Verify new document is indexed
     */
    @Test
    public void testMaxFieldLength() throws IOException {

        assertEquals(1, getHitCount("contents", "bridges"));

        IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_46,
                AosAnalyser.ONE_TOKEN_COUNT_WHITE_SPACE_ANALYSER));

        Document doc = new Document();
        doc.add(new Field("contents", "these bridges can't be found", AosFieldType.INDEXED_STOREDNOT_TERMVECTORS));
        writer.addDocument(doc);
        writer.close();

        assertEquals(1, getHitCount("contents", "bridges"));

    }

    private int getHitCount(String fieldName, String searchString) throws IOException {
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        Term t = new Term(fieldName, searchString);
        Query query = new TermQuery(t);
        int hitCount = AosLuceneUtil.hitCount(searcher, query);
        reader.close();
        return hitCount;
    }

    private IndexWriter getWriter() throws IOException {
        return new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_46,
                AosAnalyser.NO_LIMIT_TOKEN_COUNT_WHITE_SPACE_ANALYSER));
    }

}
