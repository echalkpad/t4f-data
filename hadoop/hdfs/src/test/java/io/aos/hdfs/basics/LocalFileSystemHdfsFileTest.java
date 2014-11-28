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
package io.aos.hdfs.basics;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalFileSystemHdfsFileTest extends AbstractHdfsFileTest {
    private static final Logger LOG = LoggerFactory.getLogger(LocalFileSystemHdfsFileTest.class);
    private static FileSystem hdfs;

    @BeforeClass
    public static void beforeClass() throws IOException {
        AbstractHdfsFileTest.beforeClass();
        hdfs = FileSystem.get(getConfiguration());
        hdfs.makeQualified(new Path(FILE_NAME));
        hdfs.makeQualified(HDFS_FILE_1);
        hdfs.makeQualified(HDFS_FILE_2);
        hdfs.makeQualified(HDFS_FILE_3);
    }
    
    @AfterClass
    public static void tearDown() throws IOException {
        hdfs.close();
    }

    protected FileSystem getFileSystem() {
        return hdfs;
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

}
