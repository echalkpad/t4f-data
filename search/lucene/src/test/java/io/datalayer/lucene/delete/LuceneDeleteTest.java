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
package io.datalayer.lucene.delete;

import static io.datalayer.lucene.helper.AosField.ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import io.datalayer.lucene.helper.AosIndexUtil;

import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuceneDeleteTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneDeleteTest.class);

    @Test
    public void testDelete() throws IOException {

        IndexWriter writer = AosIndexUtil.newIndexWithDocuments();
        
        Term term = new Term(ID, "1");
        Query query = new TermQuery(term);
        
        IndexReader reader = DirectoryReader.open(writer, true);
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(writer, true));

        TopDocs topDocs = indexSearcher.search(query, 1);
        LOGGER.info("" + topDocs.scoreDocs[0].doc);
        assertNotNull(reader.document(topDocs.scoreDocs[0].doc));

        LOGGER.info("Deleting documents containing " + term);
        writer.deleteDocuments(term);
//        writer.deleteDocuments(query);
        writer.commit();

        indexSearcher = new IndexSearcher(DirectoryReader.open(writer, true));
        topDocs = indexSearcher.search(query, 1);
        assertEquals(0, topDocs.scoreDocs.length);

        reader.close();
        writer.close();

    }

}
