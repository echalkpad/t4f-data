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

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import io.aos.lucene.util.TestUtil;

public class BooksLikeThis {

    public static void main(String[] args) throws IOException {
        Directory dir = TestUtil.getBookIndexDirectory();

        IndexReader reader = DirectoryReader.open(dir);
        int numDocs = reader.maxDoc();

        BooksLikeThis blt = new BooksLikeThis(reader);
        for (int i = 0; i < numDocs; i++) { //
            LOGGER.info();
            Document doc = reader.document(i);
            LOGGER.info(doc.get("title"));

            Document[] docs = blt.docsLike(i, 10); //
            if (docs.length == 0) {
                LOGGER.info("  None like this");
            }
            for (Document likeThisDoc : docs) {
                LOGGER.info("  -> " + likeThisDoc.get("title"));
            }
        }
        reader.close();
        dir.close();
    }

    private final IndexReader reader;
    private final IndexSearcher searcher;

    public BooksLikeThis(IndexReader reader) {
        this.reader = reader;
        searcher = new IndexSearcher(reader);
    }

    public Document[] docsLike(int id, int max) throws IOException {
        Document doc = reader.document(id);

        String[] authors = doc.getValues("author");
        BooleanQuery authorQuery = new BooleanQuery(); //
        for (String author : authors) { //
            authorQuery.add(new TermQuery(new Term("author", author)), //
                    BooleanClause.Occur.SHOULD); //
        }
        authorQuery.setBoost(2.0f);

        TermFreqVector vector = //
        reader.getTermFreqVector(id, "subject"); //

        BooleanQuery subjectQuery = new BooleanQuery(); //
        for (String vecTerm : vector.getTerms()) { //
            TermQuery tq = new TermQuery( //
                    new Term("subject", vecTerm)); //
            subjectQuery.add(tq, BooleanClause.Occur.SHOULD); //
        }

        BooleanQuery likeThisQuery = new BooleanQuery(); //
        likeThisQuery.add(authorQuery, BooleanClause.Occur.SHOULD); //
        likeThisQuery.add(subjectQuery, BooleanClause.Occur.SHOULD); //

        likeThisQuery.add(new TermQuery( //
                new Term("isbn", doc.get("isbn"))), BooleanClause.Occur.MUST_NOT); //

        // LOGGER.info("  Query: " +
        // likeThisQuery.toString("contents"));
        TopDocs hits = searcher.search(likeThisQuery, 10);
        int size = max;
        if (max > hits.scoreDocs.length)
            size = hits.scoreDocs.length;

        Document[] docs = new Document[size];
        for (int i = 0; i < size; i++) {
            docs[i] = reader.document(hits.scoreDocs[i].doc);
        }

        return docs;
    }
}
/*
 * #1 Iterate over every book #2 Look up books like this #3 Boosts books by same
 * author #4 Use terms from "subject" term vectors #5 Create final query #6
 * Exclude current book
 */
