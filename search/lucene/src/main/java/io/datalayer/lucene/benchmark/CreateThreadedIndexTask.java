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

import org.apache.lucene.benchmark.byTask.PerfRunData;
import org.apache.lucene.benchmark.byTask.tasks.CreateIndexTask;
import org.apache.lucene.benchmark.byTask.utils.Config;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;

/**
 * A task that you can use from a contrib/benchmark algorithm to create a
 * ThreadedIndexWriter.
 */
public class CreateThreadedIndexTask extends CreateIndexTask {

    public CreateThreadedIndexTask(PerfRunData runData) {
        super(runData);
    }

    @Override
    public int doLogic() throws IOException {
        PerfRunData runData = getRunData();
        Config config = runData.getConfig();
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_46, runData.getAnalyzer());
        IndexWriter writer = new ThreadedIndexWriter(runData.getDirectory(), conf, true, config.get(
                "writer.num.threads", 4), config.get("writer.max.thread.queue.size", 20));
        // CreateIndexTask.configureWriter(writer, config);
        runData.setIndexWriter(writer);
        return 1;
    }

}
