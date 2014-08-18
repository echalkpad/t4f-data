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
package aos.lucene.search.advanced;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import io.aos.lucene.util.TestUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;

// From chapter 5
public class SortingExample {
  private Directory directory;

  public SortingExample(Directory directory) {
    this.directory = directory;
  }

  public void displayResults(Query query, Sort sort)            //
      throws IOException {
    IndexSearcher searcher = new IndexSearcher(directory);

    searcher.setDefaultFieldSortScoring(true, false);            //

    TopDocs results = searcher.search(query, null,         //
                                      20, sort);           //

    LOGGER.info("\nResults for: " +                      //
        query.toString() + " sorted by " + sort);

    LOGGER.info(StringUtils.rightPad("Title", 30) +
      StringUtils.rightPad("pubmonth", 10) +
      StringUtils.center("id", 4) +
      StringUtils.center("score", 15));

    PrintStream out = new PrintStream(System.out, true, "UTF-8");    //

    DecimalFormat scoreFormatter = new DecimalFormat("0.######");
    for (ScoreDoc sd : results.scoreDocs) {
      int docID = sd.doc;
      float score = sd.score;
      Document doc = searcher.doc(docID);
      out.println(
          StringUtils.rightPad(                                                  //
              StringUtils.abbreviate(doc.get("title"), 29), 30) +                //
          StringUtils.rightPad(doc.get("pubmonth"), 10) +                        //
          StringUtils.center("" + docID, 4) +                                    //
          StringUtils.leftPad(                                                   //
             scoreFormatter.format(score), 12));                                 //
      out.println("   " + doc.get("category"));
      //out.println(searcher.explain(query, docID));   //
    }

    searcher.close();
  }

/*
  The Sort object (#1) encapsulates an ordered collection of
  field sorting information. We ask IndexSearcher (#2) to
  compute scores per hit. Then we call the overloaded search
  method that accepts the custom Sort (#3). We use the
  useful toString method (#4) of the Sort class to describe
  itself, and then create PrintStream that accepts UTF-8
  encoded output (#5), and finally use StringUtils (#6) from
  Apache Commons Lang for nice columnar output
  formatting. Later youùll see a reason to look at the
  explanation of score . For now, itùs commented out (#7).
*/

  public static void main(String[] args) throws Exception {
    Query allBooks = new MatchAllDocsQuery();

    QueryParser parser = new QueryParser(Version.LUCENE_46,                 //
                                         "contents",                             //
                                         new StandardAnalyzer(                   //
                                           Version.LUCENE_46));             //
    BooleanQuery query = new BooleanQuery();                                     //
    query.add(allBooks, BooleanClause.Occur.SHOULD);                             //
    query.add(parser.parse("java OR action"), BooleanClause.Occur.SHOULD);       //

    Directory directory = TestUtil.getBookIndexDirectory();                     //
    SortingExample example = new SortingExample(directory);                     //

    example.displayResults(query, Sort.RELEVANCE);

    example.displayResults(query, Sort.INDEXORDER);

    example.displayResults(query, new Sort(new SortField("category", SortField.STRING)));

    example.displayResults(query, new Sort(new SortField("pubmonth", SortField.INT, true)));

    example.displayResults(query,
        new Sort(new SortField("category", SortField.STRING),
                 SortField.FIELD_SCORE,
                 new SortField("pubmonth", SortField.INT, true)
                 ));

    example.displayResults(query, new Sort(new SortField[] {SortField.FIELD_SCORE, new SortField("category", SortField.STRING)}));
    directory.close();
  }
}

/*
#1 Create test query
#2 Create example running
*/
