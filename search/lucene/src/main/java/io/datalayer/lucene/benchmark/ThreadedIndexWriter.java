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
package io.datalayer.lucene.benchmark;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;

/**
 * Drop-in replacement for IndexWriter that uses multiple threads, under the
 * hood, to index added documents.
 * 
 * #A Holds one document to be added 
 * 
 * #B Does real work to add or update document
 * 
 * #C Create thread pool
 * 
 * #D Have thread pool execute job 
 * 
 * #E Shuts down thread pool
 *
 */
public class ThreadedIndexWriter extends IndexWriter {

    private final ExecutorService threadPool;
    private final Analyzer defaultAnalyzer;

    private class Job implements Runnable {

        Document doc;
        Analyzer analyzer;
        Term delTerm;

        public Job(Document doc, Term delTerm, Analyzer analyzer) {
            this.doc = doc;
            this.analyzer = analyzer;
            this.delTerm = delTerm;
        }

        @Override
        public void run() {
            try {
                if (delTerm != null) {
                    ThreadedIndexWriter.super.updateDocument(delTerm, doc, analyzer);
                }
                else {
                    ThreadedIndexWriter.super.addDocument(doc, analyzer);
                }
            }
            catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
    }

    public ThreadedIndexWriter(Directory dir, IndexWriterConfig conf, boolean create, int numThreads, int maxQueueSize)
            throws CorruptIndexException, IOException {
        super(dir, conf);
        defaultAnalyzer = conf.getAnalyzer();
        threadPool = new ThreadPoolExecutor(numThreads, numThreads, 0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(maxQueueSize, false), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void addDocument(Document doc) {
        threadPool.execute(new Job(doc, null, defaultAnalyzer));
    }

    public void addDocument(Document doc, Analyzer a) {
        threadPool.execute(new Job(doc, null, a));
    }

    public void updateDocument(Term term, Document doc) {
        threadPool.execute(new Job(doc, term, defaultAnalyzer));
    }

    public void updateDocument(Term term, Document doc, Analyzer a) {
        threadPool.execute(new Job(doc, term, a));
    }

    @Override
    public void close() throws CorruptIndexException, IOException {
        finish();
        super.close();
    }

    @Override
    public void close(boolean doWait) throws CorruptIndexException, IOException {
        finish();
        super.close(doWait);
    }

    @Override
    public void rollback() throws CorruptIndexException, IOException {
        finish();
        super.rollback();
    }

    private void finish() {
        threadPool.shutdown();
        while (true) {
            try {
                if (threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
                    break;
                }
            }
            catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ie);
            }
        }
    }
}
