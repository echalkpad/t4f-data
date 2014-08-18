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
package aos.lucene.admin;

import java.io.IOException;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;

/**
 * Utility class to get/refresh searchers when you are using multiple threads.
 * 
 * #A Current IndexSearcher #B Create searcher from Directory #C Create searcher
 * from near-real-time reader #D Implement in subclass #E Reopen searcher #F
 * Returns current searcher #G Release searcher
 */
public class SearcherManager {

    private IndexSearcher currentSearcher;
    private IndexWriter writer;

    public SearcherManager(Directory dir) throws IOException {
        currentSearcher = new IndexSearcher(DirectoryReader.open(dir));
        warm(currentSearcher);
    }

    public SearcherManager(IndexWriter writer) throws IOException {

        this.writer = writer;
        IndexReader reader = DirectoryReader.open(writer.getDirectory());
        currentSearcher = new IndexSearcher(reader);
        warm(currentSearcher);
        
        writer.getConfig().setMergedSegmentWarmer(new IndexWriter.IndexReaderWarmer() {
            public void warm(AtomicReader reader) throws IOException {
                SearcherManager.this.warm(new IndexSearcher(reader));
            }
        });
    }

    public void warm(IndexSearcher searcher) throws IOException {
    }

    private boolean reopening;

    private synchronized void startReopen() throws InterruptedException {
        while (reopening) {
            wait();
        }
        reopening = true;
    }

    private synchronized void doneReopen() {
        reopening = false;
        notifyAll();
    }

    public void maybeReopen() throws InterruptedException, IOException {

        startReopen();

        try {
            final IndexSearcher searcher = get();
            try {
                IndexReader newReader = currentSearcher.getIndexReader().reopen();
                if (newReader != currentSearcher.getIndexReader()) {
                    IndexSearcher newSearcher = new IndexSearcher(newReader);
                    if (writer == null) {
                        warm(newSearcher);
                    }
                    swapSearcher(newSearcher);
                }
            }
            finally {
                release(searcher);
            }
        }
        finally {
            doneReopen();
        }
    }

    public synchronized IndexSearcher get() {
        currentSearcher.getIndexReader().incRef();
        return currentSearcher;
    }

    public synchronized void release(IndexSearcher searcher) throws IOException {
        searcher.getIndexReader().decRef();
    }

    private synchronized void swapSearcher(IndexSearcher newSearcher) throws IOException {
        release(currentSearcher);
        currentSearcher = newSearcher;
    }

    public void close() throws IOException {
        swapSearcher(null);
    }

}
