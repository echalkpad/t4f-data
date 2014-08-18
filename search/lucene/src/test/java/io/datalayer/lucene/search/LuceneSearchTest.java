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
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class LuceneSearchTest extends TestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneSearchTest.class);

    private Directory directory;

    @Override
    protected void setUp() throws Exception {
        BooleanQuery.setMaxClauseCount(10000);
        directory = FSDirectory.open(new File("/aos/aos.index/com.twitter.status.public.0"));
    }

    @Override
    protected void tearDown() throws Exception {
    }

    public void testSingleFieldQuery1() throws Exception {
        String fieldName = "title";
        Query query = new QueryParser(Version.LUCENE_46, fieldName, new StandardAnalyzer(Version.LUCENE_46)).parse("lucene");
        queryIndex(query, fieldName);
    }

    public void testSingleFieldQuery2() throws Exception {
        String fieldName = "com.twitter.status.text.token";
        Query query = new QueryParser(Version.LUCENE_46, fieldName, new StandardAnalyzer(Version.LUCENE_46)).parse("american");
        queryIndex(query, fieldName);
    }

    public void testSingleFieldQuery3() throws Exception {
        String fieldName = "com.twitter.status.text.url";
        Query query = new QueryParser(Version.LUCENE_46, fieldName, new StandardAnalyzer(Version.LUCENE_46)).parse("http*");
        queryIndex(query, "com.twitter.status.user.id");
    }

    public void testQuerySingleFieldRange1() throws Exception {
        String fieldName = "com.twitter.status.date.creation";
        Term begin = new Term(fieldName, "20090424150351000");
        Term end = new Term(fieldName, "20090424150354000");
        // Query query = new TermRangeQuery(begin, end, true);
        // queryIndex(query, fieldName);
    }

    public void testQuerySingleFieldRange2() throws Exception {
        String fieldName = "com.twitter.status.date.creation";
        Query query = new QueryParser(Version.LUCENE_46, fieldName, new StandardAnalyzer(Version.LUCENE_46)).parse("[20090424150351000 TO 20090424150354000]");
        queryIndex(query, fieldName);
    }

    public void testMultiFieldQuery1() throws Exception {
        String queryString = "com.twitter.status.text.url:h* AND com.twitter.status.id:(1604330852 OR 1604330857)";
        String[] queryFieldNames = new String[] { "com.twitter.status.text.url", "com.twitter.status.id" };
        BooleanClause.Occur[] flags = { BooleanClause.Occur.MUST, BooleanClause.Occur.MUST };
        Query query = MultiFieldQueryParser.parse(Version.LUCENE_46, queryString, queryFieldNames, flags,
                new KeywordAnalyzer());
        queryIndex(query, "com.twitter.status.text.url");
    }

    public void testMultiFieldQuery2() throws Exception {
        String queryString = "com.twitter.status.text.url:h* AND com.twitter.status.date.creation:[20090424150351000 TO 20090424150354000]";
        String[] queryFieldNames = new String[] { "com.twitter.status.text.url", "com.twitter.status.id" };
        BooleanClause.Occur[] flags = { BooleanClause.Occur.MUST, BooleanClause.Occur.MUST };
        Query query = MultiFieldQueryParser.parse(Version.LUCENE_46, queryString, queryFieldNames, flags, new KeywordAnalyzer());
        queryIndex(query, "com.twitter.status.text.url");
    }

    public void testMultiFieldQuery3() throws Exception {
        Date now = Calendar.getInstance().getTime();
        String nowString = DateTools.timeToString(now.getTime(), DateTools.Resolution.MILLISECOND);
        String queryString = "com.twitter.status.text.url:h* AND com.twitter.status.date.creation:[20090424150351000 TO "
                + nowString + "]";
        String[] queryFieldNames = new String[] { "com.twitter.status.text.url", "com.twitter.status.id" };
        BooleanClause.Occur[] flags = { BooleanClause.Occur.MUST, BooleanClause.Occur.MUST };
        Query query = MultiFieldQueryParser.parse(Version.LUCENE_46, queryString, queryFieldNames, flags, new KeywordAnalyzer());
        queryIndex(query, "com.twitter.status.text.url");
    }

    public void testMultiFieldQuery4() throws Exception {
        String[] fieldQueries = new String[] { "h*", "1604330852 OR 1604330857 OR 1604330866" };
        String[] queryFieldNames = new String[] { "com.twitter.status.text.url", "com.twitter.status.id" };
        BooleanClause.Occur[] flags = { BooleanClause.Occur.MUST, BooleanClause.Occur.MUST };
        Query query = MultiFieldQueryParser.parse(Version.LUCENE_46, fieldQueries, queryFieldNames, flags, new KeywordAnalyzer());
        queryIndex(query, "com.twitter.status.text.url");
    }

    public void testFilterQuery() throws Exception {
    }

    private void queryIndex(Query query, String fieldname) throws CorruptIndexException, IOException {

        LOGGER.info("-------------------------------------");

        long start = java.util.Calendar.getInstance().getTimeInMillis();

        int hitsPerPage = 100;

        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        TopDocsCollector collector = TopScoreDocCollector.create(hitsPerPage, false);

        indexSearcher.search(query, collector);

        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        long end = java.util.Calendar.getInstance().getTimeInMillis();

        // float duration = (end - start) / 1000;

        LOGGER.info("Found " + hits.length + " hits in " + (end - start) + " milliseconds");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document document = indexSearcher.doc(docId);
            LOGGER.info((i + 1) + ". " + document.get(fieldname));
        }

    }

}
