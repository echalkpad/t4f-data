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
package io.datalayer.hdfs.basics;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiniClusterHdfsFileTest extends AbstractHdfsFileTest {
    private static final Logger LOG = LoggerFactory.getLogger(MiniClusterHdfsFileTest.class);
    private static MiniDFSCluster miniDfsCluster;
    private static DistributedFileSystem dhdfs;

    @BeforeClass
    public static void setUp() throws IOException {

        AbstractHdfsFileTest.beforeClass();
        
        getConfiguration().setClass("fs.hdfs.impl", DistributedFileSystem.class, FileSystem.class);
        
        miniDfsCluster = new MiniDFSCluster.Builder(getConfiguration()).numDataNodes(8)
                .hosts(new String[] { "host1", "host2", "host3", "host4", "host5", "host6", "host7", "host8" })
                .racks(new String[] { "/rack1", "/rack1", "/rack2", "/rack2", "/rack2", "/rack3", "/rack4", "/rack4" })
                .build();

        dhdfs = (DistributedFileSystem) miniDfsCluster.getFileSystem();

        dhdfs.makeQualified(HDFS_FILE_1);
        dhdfs.makeQualified(HDFS_FILE_2);
        dhdfs.makeQualified(HDFS_FILE_3);

    }

    @AfterClass
    public static void tearDown() throws IOException {
        dhdfs.close();
        miniDfsCluster.shutdown();
    }

    protected FileSystem getFileSystem() {
        return dhdfs;
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

}
