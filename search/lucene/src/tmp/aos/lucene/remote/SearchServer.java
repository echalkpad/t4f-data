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

import java.io.File;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import io.aos.lucene.search.simple.SimpleSearcher;

public class SearchServer {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.err.println("Usage: SearchServer <basedir>");
            System.exit(-1);
        }

        String basedir = args[0];
        Directory[] dirs = new Directory[ALPHABET.length()];
        IndexSearcher[] searchables = new IndexSearcher[ALPHABET.length()];
        for (int i = 0; i < ALPHABET.length(); i++) {
            dirs[i] = FSDirectory.open(new File(basedir, "" + ALPHABET.charAt(i)));
            searchables[i] = new IndexSearcher(dirs[i]);
        }

        LocateRegistry.createRegistry(1099);

        SimpleSearcher multiSearcher = new IndexSearcher(searchables);
        RemoteIndexSearcher multiImpl = new RemoteIndexSearcher(multiSearcher);
        Naming.rebind("//localhost/LIA_Multi", multiImpl);

        SimpleSearcher parallelSearcher = new ParallelIndexSearcher(searchables);
        RemoteIndexSearcher parallelImpl = new RemoteIndexSearcher(parallelSearcher);
        Naming.rebind("//localhost/LIA_Parallel", parallelImpl);

        LOGGER.info("Server started");

        for (int i = 0; i < ALPHABET.length(); i++) {
            dirs[i].close();
        }
    }
}

/*
 * #1 Indexes under basedir #2 Open IndexSearcher for each index #3 Create RMI
 * registry #4 IndexSearcher over all indexes #5 ParallelIndexSearcher over all
 * indexes
 */
