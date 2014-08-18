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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import junit.framework.TestCase;

// From chapter 5
public class IndexSearcherTest extends TestCase {
  private IndexSearcher[] searchers;

  public void setUp() throws Exception {
    String[] animals = { "aardvark", "beaver", "coati",
                       "dog", "elephant", "frog", "gila monster",
                       "horse", "iguana", "javelina", "kangaroo",
                       "lemur", "moose", "nematode", "orca",
                       "python", "quokka", "rat", "scorpion",
                       "tarantula", "uromastyx", "vicuna",
                       "walrus", "xiphias", "yak", "zebra"};

    Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_46);

    Directory aTOmDirectory = new RAMDirectory();     //
    Directory nTOzDirectory = new RAMDirectory();     //

    IndexWriter aTOmWriter = new IndexWriter(aTOmDirectory,
                                             analyzer,
                                             IndexWriter.MaxFieldLength.UNLIMITED);
    IndexWriter nTOzWriter = new IndexWriter(nTOzDirectory,
                                             analyzer,
                                             IndexWriter.MaxFieldLength.UNLIMITED);
    

    for (int i=animals.length - 1; i >= 0; i--) {
      Document doc = new Document();
      String animal = animals[i];
      doc.add(new Field("animal", animal, Field.Store.YES, Field.Index.NOT_ANALYZED));
      if (animal.charAt(0) < 'n') {
        aTOmWriter.addDocument(doc);                 //
      } else {                                       
        nTOzWriter.addDocument(doc);                 //
      }
    }

    aTOmWriter.close();
    nTOzWriter.close();

    searchers = new IndexSearcher[2];
    searchers[0] = new IndexSearcher(aTOmDirectory);
    searchers[1] = new IndexSearcher(nTOzDirectory);
  }

  public void testMulti() throws Exception {

    IndexSearcher searcher = new IndexSearcher(searchers);

    TermRangeQuery query = new TermRangeQuery("animal",   //
                                              "h",        //
                                              "t",        //
                                              true, true);// #3

    TopDocs hits = searcher.search(query, 10);
    assertEquals("tarantula not included", 12, hits.totalHits);
  }

  /*
    #1 Create two directories
    #2 Index halves of the alphabet
    #3 Search both indexes
  */
}
