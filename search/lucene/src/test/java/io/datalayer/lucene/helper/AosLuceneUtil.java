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
package io.datalayer.lucene.helper;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AosLuceneUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AosLuceneUtil.class);

    public static File createIndexFile(String indexPath) {
        File indexFile = new File(indexPath);
        try {
            FileUtils.deleteDirectory(indexFile);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return indexFile;
    }

    public static boolean hitsIncludeTitle(IndexSearcher searcher, TopDocs hits, String title) throws IOException {
        for (ScoreDoc match : hits.scoreDocs) {
            Document doc = searcher.doc(match.doc);
            if (title.equals(doc.get("title"))) {
                return true;
            }
        }
        LOGGER.info("title '" + title + "' not found");
        return false;
    }

    public static int hitCount(IndexSearcher searcher, Query query) throws IOException {
        return searcher.search(query, 1).totalHits;
    }

    public static int hitCount(IndexSearcher searcher, Query query, Filter filter) throws IOException {
        return searcher.search(query, filter, 1).totalHits;
    }

    public static void dumpHits(IndexSearcher searcher, TopDocs hits) throws IOException {
        if (hits.totalHits == 0) {
            LOGGER.info("No hits");
        }
        for (ScoreDoc match : hits.scoreDocs) {
            Document doc = searcher.doc(match.doc);
            LOGGER.info(match.score + ":" + doc.get("title"));
        }
    }

    public static Directory getBookIndexDirectory() throws IOException {
        return FSDirectory.open(new File(System.getProperty("index.dir")));
    }

    public static void rmDir(File dir) throws IOException {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (!files[i].delete()) {
                    throw new IOException("could not delete " + files[i]);
                }
            }
            dir.delete();
        }
    }

}
