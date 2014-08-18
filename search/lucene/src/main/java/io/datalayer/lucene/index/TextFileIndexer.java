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
package io.datalayer.lucene.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This terminal application creates an Apache Lucene index in a folder and adds
 * files into this index based on the input of the user.
 */
public class TextFileIndexer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextFileIndexer.class);

    private final IndexWriter writer;
    private final ArrayList<File> queue = new ArrayList<File>();

    public static void main(String... args) throws IOException {

        LOGGER.info("Enter the path where the index will be created: ");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();

        TextFileIndexer indexer = null;
        try {
            indexer = new TextFileIndexer(s);
        }
        catch (Exception ex) {
            LOGGER.info("Cannot create index..." + ex.getMessage());
            System.exit(-1);
        }

        while (!s.equalsIgnoreCase("q")) {
            try {
                LOGGER.info("Enter the file or folder name to add into the index (q=quit):");
                LOGGER.info("[Acceptable file types: .xml, .html, .html, .txt]");
                s = br.readLine();
                if (s.equalsIgnoreCase("q")) {
                    break;
                }

                // try to add file into the index
                indexer.indexFileOrDirectory(s);
            }
            catch (Exception e) {
                LOGGER.info("Error indexing " + s + " : " + e.getMessage());
            }
        }

        indexer.closeIndex();

    }

    /**
     * Constructor
     * 
     * @param indexDir
     *            the name of the folder in which the index should be created
     * @throws java.io.IOException
     */
    TextFileIndexer(String indexDir) throws IOException {
        // the boolean true parameter means to create a new index everytime,
        // potentially overwriting any existing files there.
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, new StandardAnalyzer(Version.LUCENE_46));
        writer = new IndexWriter(FSDirectory.open(new File(indexDir)), config);
    }

    /**
     * Indexes a file or directory
     * 
     * @param fileName
     *            the name of a text file or a folder we wish to add to the
     *            index
     * @throws java.io.IOException
     */
    public void indexFileOrDirectory(String fileName) throws IOException {

        // ===================================================
        // gets the list of files in a folder (if user has submitted
        // the name of a folder) or gets a single file name (is user
        // has submitted only the file name)
        // ===================================================

        listFiles(new File(fileName));

        int originalNumDocs = writer.numDocs();
        for (File f : queue) {
            FileReader fr = null;
            try {
                Document doc = new Document();

                // ===================================================
                // add contents of file
                // ===================================================
                fr = new FileReader(f);
                // doc.add(new Field("contents", fr));

                // ===================================================
                // adding second field which contains the path of the file
                // ===================================================
                // doc.add(new Field("path", fileName, Field.Store.YES,
                // Field.Index.NOT_ANALYZED));

                writer.addDocument(doc);
                LOGGER.info("Added: " + f);
            }
            catch (Exception e) {
                LOGGER.info("Could not add: " + f);
            }
            finally {
                fr.close();
            }
        }

        int newNumDocs = writer.numDocs();
        LOGGER.info("");
        LOGGER.info("************************");
        LOGGER.info((newNumDocs - originalNumDocs) + " documents added.");
        LOGGER.info("************************");

        queue.clear();
    }

    private void listFiles(File file) {
        if (!file.exists()) {
            LOGGER.info(file + " does not exist.");
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                listFiles(f);
            }
        }
        else {
            String filename = file.getName().toLowerCase();
            // ===================================================
            // Only index text files
            // ===================================================
            if (filename.endsWith(".htm") || filename.endsWith(".html") || filename.endsWith(".xml")
                    || filename.endsWith(".txt")) {
                queue.add(file);
            }
            else {
                LOGGER.info("Skipped " + filename);
            }
        }
    }

    /**
     * Close the index.
     * 
     * @throws java.io.IOException
     */
    public void closeIndex() throws IOException {
        writer.merge(writer.getNextMerge());
        writer.close();
    }

}
