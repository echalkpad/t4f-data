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
package io.datalayer.lucene.search;

import static org.junit.Assert.assertTrue;
import io.datalayer.lucene.helper.AosDirectory;
import io.datalayer.lucene.helper.AosIndexUtil;
import io.datalayer.lucene.helper.AosLuceneUtil;
import io.datalayer.lucene.index.IndexerMain;
import io.datalayer.lucene.search.FileSearcherMain;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.Charset;

import org.junit.Ignore;
import org.junit.Test;

public class LuceneSearch2Test {

    @Test
    @Ignore
    public void testIndexSearch() throws Exception {

        File fileDir = new File(AosIndexUtil.TEST_DOC_PATH);
        File indexDir = AosLuceneUtil.createIndexFile(AosDirectory.TEST_INDEX_DIR);

        IndexerMain.main(new String[] { "-create", "-docs", fileDir.getPath(), "-index", indexDir.getPath() });

        testOneSearch(indexDir, "apache", 3);
        testOneSearch(indexDir, "patent", 8);
        testOneSearch(indexDir, "lucene", 0);
        testOneSearch(indexDir, "gnu", 6);
        testOneSearch(indexDir, "derivative", 8);
        testOneSearch(indexDir, "license", 13);

    }

    public void testOneSearch(File indexPath, String query, int expectedHitCount) throws Exception {
        PrintStream outSave = System.out;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            PrintStream fakeSystemOut = new PrintStream(bytes, false, Charset.defaultCharset().name());
            System.setOut(fakeSystemOut);
            FileSearcherMain.main(new String[] { "-query", query, "-index", indexPath.getPath() });
            fakeSystemOut.flush();
            // intentionally use default encoding
            String output = bytes.toString(Charset.defaultCharset().name());
            assertTrue("output=" + output, output.contains(expectedHitCount + " total matching documents"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            System.setOut(outSave);
        }
    }

}
