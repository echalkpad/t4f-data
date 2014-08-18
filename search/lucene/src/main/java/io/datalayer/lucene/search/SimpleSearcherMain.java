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

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This code was originally written for Erik's Lucene intro java.net article
 * 
 * #1 Parse provided index directory 
 * 
 * #2 Parse provided query string 
 * 
 * #3 Open index 
 * 
 * #4 Parse query 
 * 
 * #5 Search index 
 * 
 * #6 Write search stats 
 * 
 * #7 Retrieve matching document 
 * 
 * #8 Display filename #9 Close IndexSearcher
 */
public class SimpleSearcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSearcherMain.class);

    public static void main(String[] args) throws IllegalArgumentException, IOException, ParseException {

        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: java " + SimpleSearcherMain.class.getName() + " <index dir> <query>");
        }

        String indexDir = args[0];
        String q = args[1];

        search(indexDir, q);
    }

    public static void search(String indexDir, String q) throws IOException, ParseException {

        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher is = new IndexSearcher(reader);

        QueryParser parser = new QueryParser(Version.LUCENE_46, "contents", new StandardAnalyzer(Version.LUCENE_46));
        Query query = parser.parse(q);
        long start = System.currentTimeMillis();
        TopDocs hits = is.search(query, 10);
        long end = System.currentTimeMillis();

        System.err.println("Found " + hits.totalHits + " document(s) (in " + (end - start)
                + " milliseconds) that matched query '" + q + "':");

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);
            LOGGER.info(doc.get("fullpath"));
        }

        reader.close();

    }

}
