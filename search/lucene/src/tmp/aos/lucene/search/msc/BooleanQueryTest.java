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

import junit.framework.TestCase;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.store.Directory;

import io.aos.lucene.util.TestUtil;

// From chapter 3
public class BooleanQueryTest extends TestCase {
  public void testAnd() throws Exception {
    TermQuery searchingBooks =
      new TermQuery(new Term("subject","search"));  //#1

    Query books2010 =                       //#2
      NumericRangeQuery.newIntRange("pubmonth", 201001,       //#2
                                    201012,                   //#2
                                    true, true);                //#2

    BooleanQuery searchingBooks2010 = new BooleanQuery();              //#3
    searchingBooks2010.add(searchingBooks, BooleanClause.Occur.MUST);  //#3
    searchingBooks2010.add(books2010, BooleanClause.Occur.MUST);       //#3

    Directory dir = TestUtil.getBookIndexDirectory();
    IndexSearcher searcher = new IndexSearcher(dir);
    TopDocs matches = searcher.search(searchingBooks2010, 10);

    assertTrue(TestUtil.hitsIncludeTitle(searcher, matches,
                                 "Lucene in Action, Second Edition"));
    searcher.close();
    dir.close();
  }

/*
#1 Match books with subject “search”
#2 Match books in 2004
#3 Combines two queries
*/

  public void testOr() throws Exception {
    TermQuery methodologyBooks = new TermQuery(                       //
               new Term("category",                                   //
                 "/technology/computers/programming/methodology"));   //

    TermQuery easternPhilosophyBooks = new TermQuery(                 //
        new Term("category",                                          //
            "/philosophy/eastern"));                                  //

    BooleanQuery enlightenmentBooks = new BooleanQuery();             //
    enlightenmentBooks.add(methodologyBooks,                          //
                           BooleanClause.Occur.SHOULD);               //
    enlightenmentBooks.add(easternPhilosophyBooks,                    //
                           BooleanClause.Occur.SHOULD);               //

    Directory dir = TestUtil.getBookIndexDirectory();
    IndexSearcher searcher = new IndexSearcher(dir);
    TopDocs matches = searcher.search(enlightenmentBooks, 10);
    LOGGER.info("or = " + enlightenmentBooks);

    assertTrue(TestUtil.hitsIncludeTitle(searcher, matches,
                                         "Extreme Programming Explained"));
    assertTrue(TestUtil.hitsIncludeTitle(searcher, matches,
                                         "Tao Te Ching \u9053\u5FB7\u7D93"));
    searcher.close();
    dir.close();
  }

  /*
#1 Match 1st category
#2 Match 2nd category
#3 Combine
   */
}
