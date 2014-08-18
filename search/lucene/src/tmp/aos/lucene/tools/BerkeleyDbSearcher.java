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
import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class BerkeleyDbSearcher {

    public static void main(String[] args) throws IOException, DatabaseException {
        if (args.length != 1) {
            System.err.println("Usage: BerkeleyDbSearcher <index dir>");
            System.exit(-1);
        }
        File indexFile = new File(args[0]);

        EnvironmentConfig envConfig = new EnvironmentConfig();
        DatabaseConfig dbConfig = new DatabaseConfig();

        envConfig.setTransactional(true);
        envConfig.setInitializeCache(true);
        envConfig.setInitializeLocking(true);
        envConfig.setInitializeLogging(true);
        envConfig.setAllowCreate(true);
        envConfig.setThreaded(true);
        dbConfig.setAllowCreate(true);
        dbConfig.setType(DatabaseType.BTREE);

        Environment env = new Environment(indexFile, envConfig);

        Database index = env.openDatabase(null, "__index__", null, dbConfig);
        Database blocks = env.openDatabase(null, "__blocks__", null, dbConfig);

        DbDirectory directory = new DbDirectory(null, index, blocks, 0);

        IndexSearcher searcher = new IndexSearcher(directory, true);
        TopDocs hits = searcher.search(new TermQuery(new Term("contents", "fox")), 10);
        LOGGER.info(hits.totalHits + " documents found");
        searcher.close();

        index.close();
        blocks.close();
        env.close();
    }
}
