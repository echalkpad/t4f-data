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
package io.datalayer.lucene.snapshot;

import io.datalayer.lucene.helper.AosAnalyser;

import java.util.Collection;

import org.apache.lucene.index.IndexCommit;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy;
import org.apache.lucene.index.SnapshotDeletionPolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.junit.Ignore;
import org.junit.Test;

public class IndexSnapshotTest {

    @Test
    @Ignore
    public void testSnapshot() throws Exception {

        Directory dir = null;

        IndexDeletionPolicy policy = new KeepOnlyLastCommitDeletionPolicy();
        SnapshotDeletionPolicy snapshotter = new SnapshotDeletionPolicy(policy);
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_44, AosAnalyser.NO_LIMIT_TOKEN_COUNT_SIMPLE_ANALYSER);
        conf.setIndexDeletionPolicy(snapshotter);

        IndexWriter writer = new IndexWriter(dir, conf);

        IndexCommit indexCommit = null;
        try {
            indexCommit = snapshotter.snapshot();
            Collection<String> fileNames = indexCommit.getFileNames();
            /* <iterate over & copy files from fileNames> */
        }
        finally {
            if (indexCommit != null) {
                snapshotter.release(indexCommit);
            }
        }

        writer.close();
        
    }

}
