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
package aos.lucene.analysis.synonym;

import java.io.StringReader;

import junit.framework.TestCase;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import io.aos.lucene.util.TestUtil;

// From chapter 4
public class SynonymAnalyzerTest extends TestCase {
  private IndexSearcher searcher;
  private static SynonymAnalyzer synonymAnalyzer =
                      new SynonymAnalyzer(new TestSynonymEngine());

  public void setUp() throws Exception {
    RAMDirectory directory = new RAMDirectory();

    IndexWriter writer = new IndexWriter(directory,
                                         synonymAnalyzer,  //#1  
                                         IndexWriter.MaxFieldLength.UNLIMITED);
    Document doc = new Document();
    doc.add(new Field("content",
                      "The quick brown fox jumps over the lazy dog",
                      Field.Store.YES,
                      Field.Index.ANALYZED));  //#2
    writer.addDocument(doc);
                                  
    writer.close();

    searcher = new IndexSearcher(directory, true);
  }

  public void tearDown() throws Exception {
    searcher.close();
  }

  public void testJumps() throws Exception {
    TokenStream stream =
      synonymAnalyzer.tokenStream("contents",                   // #A
                                  new StringReader("jumps"));   // #A
    TermAttribute term = stream.addAttribute(TermAttribute.class);
    PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);

    int i = 0;
    String[] expected = new String[]{"jumps",              // #B
                                     "hops",               // #B
                                     "leaps"};             // #B
    while(stream.incrementToken()) {
      assertEquals(expected[i], term.term());

      int expectedPos;      // #C
      if (i == 0) {         // #C
        expectedPos = 1;    // #C
      } else {              // #C
        expectedPos = 0;    // #C
      }                     // #C
      assertEquals(expectedPos,                      // #C
                   posIncr.getPositionIncrement());  // #C
      i++;
    }
    assertEquals(3, i);
  }

  /*
    #A Analyze with SynonymAnalyzer
    #B Check for correct synonyms
    #C Verify synonyms positions
  */

  public void testSearchByAPI() throws Exception {

    TermQuery tq = new TermQuery(new Term("content", "hops"));  //#1
    assertEquals(1, TestUtil.hitCount(searcher, tq));

    PhraseQuery pq = new PhraseQuery();    //#2
    pq.add(new Term("content", "fox"));    //#2
    pq.add(new Term("content", "hops"));   //#2
    assertEquals(1, TestUtil.hitCount(searcher, pq));
  }

  /*
    #1 Search for "hops"
    #2 Search for "fox hops"
  */

  public void testWithQueryParser() throws Exception {
    Query query = new QueryParser(Version.LUCENE_46,                   
                                  "content",                                
                                  synonymAnalyzer).parse("\"fox jumps\"");  
    assertEquals(1, TestUtil.hitCount(searcher, query));                   
    LOGGER.info("With SynonymAnalyzer, \"fox jumps\" parses to " +
                                         query.toString("content"));

    query = new QueryParser(Version.LUCENE_46,                         
                            "content",                                      
                            new StandardAnalyzer(Version.LUCENE_46)).parse("\"fox jumps\""); 
    assertEquals(1, TestUtil.hitCount(searcher, query));                   
    LOGGER.info("With StandardAnalyzer, \"fox jumps\" parses to " +
                                         query.toString("content"));
  }

  /*
    #1 SynonymAnalyzer finds the document
    #2 StandardAnalyzer also finds document
  */
}
