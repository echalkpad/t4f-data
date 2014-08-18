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
package io.datalayer.lucene.search;

import io.datalayer.lucene.demo.parser.ParseException;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuceneQueryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneQueryTest.class);

    // String indexDir = "./aos.index.test/test1";
    // Directory dir = FSDirectory.open(new File(indexDir));
    // IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, new
    // StandardAnalyzer(Version.LUCENE_46));
    // IndexWriter indexWriter = new IndexWriter(dir, config);
    //
    // addDoc(indexWriter,
    // "Lucene in Action - Lucene in Action - Lucene in Action - Lucene in Action - Lucene in Action - Lucene in Action - ",
    // new String[] { "tag1", "tag2" });
    // addDoc(indexWriter,
    // "Lucene for Dummies - Lucene for Dummies - Lucene for Dummies - Lucene for Dummies - Lucene for Dummies",
    // new String[] { "tag1", "tag2" });
    // addDoc(indexWriter, "Managing Gigabytes", new String[] { "tag1", "tag3"
    // });
    // addDoc(indexWriter, "The Art of Computer Science", new String[] { "tag1",
    // "tag4" });
    // indexWriter.close();
    //
    // String querystr = "lucene"; // the "title" arg specifies the default
    // // field to use - when no field is
    // // explicitly specified in the query.
    // Query q = new QueryParser(Version.LUCENE_46, "title",
    // analyzer).parse(querystr);
    // query(indexDir, q);
    //
    // querystr = "tag4";
    // q = new QueryParser(Version.LUCENE_46, "tag", analyzer).parse(querystr);
    // query(indexDir, q);
    //
    // }

    private static void addDoc(IndexWriter indexWriter, String title, String[] tags) throws IOException {
        Document doc = new Document();
        // doc.add(new Field("title", title, Field.Store.YES,
        // Field.Index.ANALYZED));
        for (String tag : tags) {
            // doc.add(new Field("tag", tag, Field.Store.YES,
            // Field.Index.NOT_ANALYZED));
        }
        indexWriter.addDocument(doc);
    }

    private static void query(String indexDir, Query q) throws IOException, ParseException {

        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexDir)));
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        TopDocsCollector collector = TopScoreDocCollector.create(hitsPerPage, false);
        indexSearcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        LOGGER.info("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = indexSearcher.doc(docId);
            // LOGGER.info((i + 1) + ". " + d.get("title"));
        }

        // searcher can only be closed when there
        // is no need to access the documents any more.
        // indexSearcher.close();

    }

}
