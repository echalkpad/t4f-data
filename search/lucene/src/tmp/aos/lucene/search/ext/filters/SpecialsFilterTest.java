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
package aos.lucene.search.ext.filters;

import junit.framework.TestCase;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;

import io.aos.lucene.util.TestUtil;

/**
 * #1 Rudolf Steiner's book #2 All education books on special #3 All books with
 * "logo" in subject #4 Combine queries
 */
public class SpecialsFilterTest extends TestCase {

    private Query allBooks;
    private IndexSearcher searcher;

    @Override
    protected void setUp() throws Exception {
        allBooks = new MatchAllDocsQuery();
        searcher = new IndexSearcher(TestUtil.getBookIndexDirectory(), true);
    }

    public void testCustomFilter() throws Exception {
        String[] isbns = new String[] { "9780061142666", "9780394756820" };

        SpecialsAccessor accessor = new TestSpecialsAccessor(isbns);
        Filter filter = new SpecialsFilter(accessor);
        TopDocs hits = searcher.search(allBooks, filter, 10);
        assertEquals("the specials", isbns.length, hits.totalHits);
    }

    public void testFilteredQuery() throws Exception {
        String[] isbns = new String[] { "9780880105118" };

        SpecialsAccessor accessor = new TestSpecialsAccessor(isbns);
        Filter filter = new SpecialsFilter(accessor);

        WildcardQuery educationBooks = new WildcardQuery(new Term("category", "*education*"));
        FilteredQuery edBooksOnSpecial = new FilteredQuery(educationBooks, filter);

        TermQuery logoBooks = new TermQuery(new Term("subject", "logo"));

        BooleanQuery logoOrEdBooks = new BooleanQuery();
        logoOrEdBooks.add(logoBooks, BooleanClause.Occur.SHOULD);
        logoOrEdBooks.add(edBooksOnSpecial, BooleanClause.Occur.SHOULD);

        TopDocs hits = searcher.search(logoOrEdBooks, 10);
        LOGGER.info(logoOrEdBooks.toString());
        assertEquals("Papert and Steiner", 2, hits.totalHits);
    }

}
