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
package aos.lucene.search.msc;

import java.io.File;

import junit.framework.TestCase;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import io.aos.lucene.analysis.SimpleAnalyzer;
import io.aos.lucene.util.TestUtil;

/**
 * #A Create QueryParser #B Parse user's text
 */
public class BasicSearchingTest extends TestCase {

    public void testTerm() throws Exception {

        Directory dir = FSDirectory.open(new File("target/index"));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        Term t = new Term("subject", "ant");
        Query query = new TermQuery(t);
        TopDocs docs = searcher.search(query, 10);
        assertEquals("Ant in Action", 1, docs.totalHits);

        t = new Term("subject", "junit");
        docs = searcher.search(new TermQuery(t), 10);
        assertEquals("Ant in Action, " + "JUnit in Action, Second Edition", 2, docs.totalHits);

        reader.close();
        dir.close();
    }

    /**
     * #A Obtain directory from TestUtil #B Create IndexSearcher #C Confirm one
     * hit for "ant" #D Confirm two hits for "junit"
     */
    public void testKeyword() throws Exception {
        Directory dir = TestUtil.getBookIndexDirectory();
        IndexSearcher searcher = new IndexSearcher(dir);

        Term t = new Term("isbn", "9781935182023");
        Query query = new TermQuery(t);
        TopDocs docs = searcher.search(query, 10);
        assertEquals("JUnit in Action, Second Edition", 1, docs.totalHits);

        searcher.close();
        dir.close();
    }

    public void testQueryParser() throws Exception {
        Directory dir = TestUtil.getBookIndexDirectory();
        IndexSearcher searcher = new IndexSearcher(dir);

        QueryParser parser = new QueryParser(Version.LUCENE_46, "contents", new SimpleAnalyzer());

        Query query = parser.parse("+JUNIT +ANT -MOCK");
        TopDocs docs = searcher.search(query, 10);
        assertEquals(1, docs.totalHits);
        Document d = searcher.doc(docs.scoreDocs[0].doc);
        assertEquals("Ant in Action", d.get("title"));

        query = parser.parse("mock OR junit");
        docs = searcher.search(query, 10);
        assertEquals("Ant in Action, " + "JUnit in Action, Second Edition", 2, docs.totalHits);

        searcher.close();
        dir.close();
    }

}
