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

import io.datalayer.lucene.helper.AosAnalyser;
import io.datalayer.lucene.helper.AosFieldType;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * #1 Create index in this directory
 * 
 * #2 Index *.txt files from this directory
 * 
 * #3 Create Lucene IndexWriter
 * 
 * #4 Close IndexWriter
 * 
 * #5 Return number of documents indexed
 * 
 * #6 Index .txt files only, using FileFilter
 * 
 * #7 Index file content
 * 
 * #8 Index file name
 * 
 * #9 Index file full path #10 Add document to Lucene index
 */
public class SimpleIndexer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleIndexer.class);

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: java " + SimpleIndexer.class.getName()
                    + " <index dir> <data dir>");
        }

        String indexDir = args[0];
        String dataDir = args[1];

        long start = System.currentTimeMillis();
        SimpleIndexer indexer = new SimpleIndexer(indexDir);
        int numIndexed;
        try {
            numIndexed = indexer.index(dataDir, new TextFilesFilter());
        }
        finally {
            indexer.close();
        }
        long end = System.currentTimeMillis();

        LOGGER.info("Indexing " + numIndexed + " files took " + (end - start) + " milliseconds");
    }

    private final IndexWriter writer;

    public SimpleIndexer(String indexDir) throws IOException {

        Directory dir = FSDirectory.open(new File(indexDir));

        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_46,
                AosAnalyser.NO_LIMIT_TOKEN_COUNT_SIMPLE_ANALYSER);

        writer = new IndexWriter(dir, conf);

    }

    public void close() throws IOException {
        writer.close();
    }

    public int index(String dataDir, FileFilter filter) throws Exception {

        File[] files = new File(dataDir).listFiles();

        for (File f : files) {
            if (!f.isDirectory() && !f.isHidden() && f.exists() && f.canRead() && (filter == null || filter.accept(f))) {
                indexFile(f);
            }
        }

        return writer.numDocs();

    }

    private static class TextFilesFilter implements FileFilter {
        @Override
        public boolean accept(File path) {
            return path.getName().toLowerCase().endsWith(".txt");
        }
    }

    protected Document getDocument(File f) throws Exception {
        Document doc = new Document();
        doc.add(new Field("contents", new FileReader(f), AosFieldType.INDEXED_STORED_TERMVECTORS));
        doc.add(new StoredField("filename", f.getName()));
        doc.add(new StoredField("fullpath", f.getCanonicalPath()));
        return doc;
    }

    private void indexFile(File f) throws Exception {
        LOGGER.info("Indexing " + f.getCanonicalPath());
        Document doc = getDocument(f);
        writer.addDocument(doc);
    }

}
