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
package aos.lucene.remote;

import java.rmi.Naming;
import java.util.Date;
import java.util.HashMap;

import org.apache.lucene.index.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

/**
 * #1 Multiple identical searches #2 Cache searchers #3 Wrap IndexSearcher in
 * IndexSearcher #4 Time searching #5 Don't close searcher! #6 RMI lookup
 */
public class SearchClient {

    private static HashMap searcherCache = new HashMap();

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.err.println("Usage: SearchClient <query>");
            System.exit(-1);
        }

        String word = args[0];

        for (int i = 0; i < 5; i++) {
            search("LIA_Multi", word);
            search("LIA_Parallel", word);
        }

    }

    private static void search(String name, String word) throws Exception {
        TermQuery query = new TermQuery(new Term("word", word));

        IndexSearcher searcher = (IndexSearcher) searcherCache.get(name);

        if (searcher == null) {
            searcher = new IndexSearcher(new IndexSearcher[] { lookupRemote(name) });
            searcherCache.put(name, searcher);
        }

        long begin = new Date().getTime();
        TopDocs hits = searcher.search(query, 10);
        long end = new Date().getTime();

        System.out.print("Searched " + name + " for '" + word + "' (" + (end - begin) + " ms): ");

        if (hits.scoreDocs.length == 0) {
            System.out.print("<NONE FOUND>");
        }

        for (ScoreDoc sd : hits.scoreDocs) {
            Document doc = searcher.doc(sd.doc);
            String[] values = doc.getValues("syn");
            for (String syn : values) {
                System.out.print(syn + " ");
            }
        }
        LOGGER.info();
        LOGGER.info();

    }

    private static IndexSearcher lookupRemote(String name) throws Exception {
        return (IndexSearcher) Naming.lookup("//localhost/" + name);
    }

}
