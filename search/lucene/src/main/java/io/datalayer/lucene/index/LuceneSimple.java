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

import java.io.File;
import java.io.IOException;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public class LuceneSimple {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneSimple.class);

    public static void main(String... args) throws Exception {
        index("/Users/androidyou/Documents/lucence/data/test.txt", "/Users/androidyou/Documents/lucence/index");
        search("/Users/androidyou/Documents/lucence/index", "nonexistedkeyworld");
        search("/Users/androidyou/Documents/lucence/index", "apache");

    }

    private static void index(String datafolder, String indexfolder) throws CorruptIndexException,
            LockObtainFailedException, IOException {
        Analyzer a = new StandardAnalyzer(Version.LUCENE_46);
        Directory d = FSDirectory.open(new File(indexfolder));
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, new StandardAnalyzer(Version.LUCENE_46));
        IndexWriter indexWriter = new IndexWriter(d, config);

        Document doc = new Document();
        // Fieldable contentfield=new Field("content", new
        // FileReader(datafolder));
        // doc.add(contentfield);
        // Fieldable namefield=new Field("filename",datafolder, Store.YES,
        // Index.NOT_ANALYZED);
        // doc.add(namefield);

        indexWriter.addDocument(doc);
        indexWriter.commit();

    }

    private static void search(String indexpath, String keyword) throws Exception, IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexpath)));
        IndexSearcher searcher = new IndexSearcher(reader);
        LOGGER.info("Search  keyword " + keyword);
        Query query = new QueryParser(Version.LUCENE_46, "content", new StandardAnalyzer(Version.LUCENE_46))
                .parse(keyword);

        TopDocs docs = searcher.search(query, 10);
        LOGGER.info("hits " + docs.totalHits);
        for (ScoreDoc doc : docs.scoreDocs) {
            LOGGER.info("doc id" + doc.doc + "doc filename" + searcher.doc(doc.doc).get("filename"));
        }

    }

}
