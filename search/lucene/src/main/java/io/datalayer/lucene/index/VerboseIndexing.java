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

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class VerboseIndexing {

    private void index() throws IOException {

        Directory dir = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
                AosAnalyser.NO_LIMIT_TOKEN_COUNT_SIMPLE_ANALYSER);

        config.setInfoStream(System.out);

        IndexWriter writer = new IndexWriter(dir, config);

        for (int i = 0; i < 100; i++) {
            Document doc = new Document();
            doc.add(new StoredField("keyword", "goober"));
            writer.addDocument(doc);
        }

        writer.close();

    }

    public static void main(String[] args) throws IOException {
        VerboseIndexing vi = new VerboseIndexing();
        vi.index();
    }

}
