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
package aos.lucene.tools;

import java.io.File;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Document;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/*
 * #A Instantiate MoreLikeThis #B Lower default minimums #C Iterate through all
 * docs in the index #D Build query to find similar documents #E Don't show the
 * same document
 */
public class BooksMoreLikeThis {

    public static void main(String[] args) throws Throwable {

        String indexDir = System.getProperty("index.dir");
        FSDirectory directory = FSDirectory.open(new File(indexDir));
        IndexReader reader = DirectoryReader.open(directory);

        IndexSearcher searcher = new IndexSearcher(reader);

        int numDocs = reader.maxDoc();

        MoreLikeThis mlt = new MoreLikeThis(reader);
        mlt.setFieldNames(new String[] { "title", "author" });
        mlt.setMinTermFreq(1);
        mlt.setMinDocFreq(1);

        for (int docID = 0; docID < numDocs; docID++) {
            LOGGER.info();
            Document doc = reader.document(docID);
            LOGGER.info(doc.get("title"));

            Query query = mlt.like(docID);
            LOGGER.info("  query=" + query);

            TopDocs similarDocs = searcher.search(query, 10);
            if (similarDocs.totalHits == 0)
                LOGGER.info("  None like this");
            for (int i = 0; i < similarDocs.scoreDocs.length; i++) {
                if (similarDocs.scoreDocs[i].doc != docID) {
                    doc = reader.document(similarDocs.scoreDocs[i].doc);
                    LOGGER.info("  -> " + doc.getField("title").stringValue());
                }
            }
        }

        reader.close();
        directory.close();
    }
}
