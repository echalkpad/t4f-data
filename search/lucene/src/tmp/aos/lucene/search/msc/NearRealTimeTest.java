/****************************************************************
 * Licensed to the AOS Community (AOS) under one or more        *
 * contributor license agreements.  See the NOTICE file         *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The AOS licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http:www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package aos.lucene.search.msc;

import junit.framework.TestCase;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * #1 Create near-real-time reader
 * 
 * #A Wrap reader in IndexSearcher
 * 
 * #B Search returns 10 hits
 * 
 * #2 Delete 1 document
 * 
 * #3 Add 1 document
 * 
 * #4 Reopen reader
 * 
 * #5 Confirm reader is new
 * 
 * #6 Close old reader
 * 
 * #7 Verify 9 hits now
 * 
 * #8 Confirm new document matched
 * 
 * #1 IndexWriter returns a reader that's able to search all previously
 * committed changes to the index, plus any uncommitted changes. The returned
 * reader is always readOnly.
 * 
 * #2,#3 We make changes to the index, but do not commit them.
 * 
 * #4,#5,#6 Ask the reader to reopen. Note that this simply re-calls
 * writer.getReader again under the hood. Because we made changes, the newReader
 * will be different from the old one so we must close the old one.
 * 
 * #7, #8 The changes made with the writer are reflected in new searches.
 */
public class NearRealTimeTest extends TestCase {
    public void testNearRealTime() throws Exception {
        Directory dir = new RAMDirectory();
        IndexWriter writer = new IndexWriter(dir, new StandardAnalyzer(Version.LUCENE_46),
                IndexWriter.MaxFieldLength.UNLIMITED);
        for (int i = 0; i < 10; i++) {
            Document doc = new Document();
            doc.add(new Field("id", "" + i, Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
            doc.add(new Field("text", "aaa", Field.Store.NO, Field.Index.ANALYZED));
            writer.addDocument(doc);
        }
        IndexReader reader = writer.getReader();
        IndexSearcher searcher = new IndexSearcher(reader);

        Query query = new TermQuery(new Term("text", "aaa"));
        TopDocs docs = searcher.search(query, 1);
        assertEquals(10, docs.totalHits);

        writer.deleteDocuments(new Term("id", "7"));

        Document doc = new Document();
        doc.add(new Field("id", "11", Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
        doc.add(new Field("text", "bbb", Field.Store.NO, Field.Index.ANALYZED));
        writer.addDocument(doc);

        IndexReader newReader = reader.reopen();
        assertFalse(reader == newReader);
        reader.close();
        searcher = new IndexSearcher(newReader);

        TopDocs hits = searcher.search(query, 10);
        assertEquals(9, hits.totalHits);

        query = new TermQuery(new Term("text", "bbb"));
        hits = searcher.search(query, 1);
        assertEquals(1, hits.totalHits);

        newReader.close();
        writer.close();
    }
}
