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
package aos.lucene.analysis.codec;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Document;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import io.aos.lucene.analysis.AnalyzerUtils;

public class MetaphoneAnalyzerTest extends TestCase {

    public void testKoolKat() throws Exception {

        RAMDirectory directory = new RAMDirectory();
        Analyzer analyzer = new MetaphoneReplacementAnalyzer();

        IndexWriter writer = new IndexWriter(directory, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);

        Document doc = new Document();
        doc.add(new Field("contents", "cool cat", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);
        writer.close();

        IndexSearcher searcher = new IndexSearcher(directory);

        Query query = new QueryParser(Version.LUCENE_46, "contents", analyzer).parse("kool kat");

        TopDocs hits = searcher.search(query, 1);
        assertEquals(1, hits.totalHits);
        int docID = hits.scoreDocs[0].doc;
        Document storedDoc = searcher.doc(docID);
        assertEquals("cool cat", storedDoc.get("contents"));

        searcher.close();
    }

    /*
     * #A Index document #B Parse query text #C Verify match #D Retrieve
     * original value
     */
    public static void main(String[] args) throws IOException {
        MetaphoneReplacementAnalyzer analyzer = new MetaphoneReplacementAnalyzer();
        AnalyzerUtils.displayTokens(analyzer, "The quick brown fox jumped over the lazy dog");

        LOGGER.info("");
        AnalyzerUtils.displayTokens(analyzer, "Tha quik brown phox jumpd ovvar tha lazi dag");
    }
}
