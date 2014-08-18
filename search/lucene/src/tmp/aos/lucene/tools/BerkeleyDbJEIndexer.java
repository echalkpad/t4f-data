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

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.util.Version;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.Transaction;

// From chapter 9

/**
 * Same as BerkeleyDbIndexer, but uses the Java edition of Berkeley DB
 */

public class BerkeleyDbJEIndexer {
    public static void main(String[] args) throws IOException, DatabaseException {
        if (args.length != 1) {
            System.err.println("Usage: BerkeleyDbIndexer <index dir>");
            System.exit(-1);
        }

        File indexFile = new File(args[0]);

        if (indexFile.exists()) { 
            File[] files = indexFile.listFiles(); 
            for (int i = 0; i < files.length; i++)
                
                if (files[i].getName().startsWith("__")) 
                    files[i].delete(); 
            indexFile.delete(); 
        }

        indexFile.mkdir();

        EnvironmentConfig envConfig = new EnvironmentConfig(); 
        DatabaseConfig dbConfig = new DatabaseConfig(); 

        envConfig.setTransactional(true); 
        envConfig.setAllowCreate(true); 
        dbConfig.setTransactional(true); 
        dbConfig.setAllowCreate(true); 

        Environment env = new Environment(indexFile, envConfig);

        Transaction txn = env.beginTransaction(null, null);
        Database index = env.openDatabase(txn, "__index__", dbConfig);
        Database blocks = env.openDatabase(txn, "__blocks__", dbConfig);
        txn.commit();
        txn = env.beginTransaction(null, null);

        JEDirectory directory = new JEDirectory(txn, index, blocks);

        IndexWriter writer = new IndexWriter(directory, new StandardAnalyzer(Version.LUCENE_46), true,
                IndexWriter.MaxFieldLength.UNLIMITED);

        Document doc = new Document();
        doc.add(new Field("contents", "The quick brown fox...", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);

        writer.merge(writer.getNextMerge());
        writer.close();

        directory.close();
        txn.commit();

        index.close();
        blocks.close();
        env.close();

        LOGGER.info("Indexing Complete");
    }
}

/*
 * #A Remove existing index, if present #B Configure BDB's environment and
 * database #C Open database and transaction #D Create JEDirectory
 */
