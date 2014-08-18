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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.HdfsConfiguration;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.junit.Test;
import org.slf4j.Logger;

public abstract class AbstractHdfsFileTest {
    private static final String TARGET_FOLDER = "./target";
    private static final String TEST_LOCAL_FILE_FOLDER = TARGET_FOLDER + "/local-test-folder";
    protected static final File LOCAL_FILE = new File(TEST_LOCAL_FILE_FOLDER + "/a-simple-local-file");
    protected static final String FILE_NAME = "/hello.txt";
    protected static final String MESSAGE = "Hello, world!\n";
    protected static final Path HDFS_FILE_0 = new Path("input_0.hdfs");
    protected static final Path HDFS_FILE_1 = new Path("input_1.hdfs");
    protected static final Path HDFS_FILE_2 = new Path("input_renamed.hdfs");
    protected static final Path HDFS_FILE_3 = new Path("output.hdfs");
    
    private static Configuration configuration;

    protected static void beforeClass() throws IOException {

        Properties props = new Properties();
        InputStream is =  ClassLoader.getSystemResourceAsStream("hdfs-conf.properties");
        props.load(is);
        for (Entry<Object, Object> entry: props.entrySet()) {
            System.setProperty((String) entry.getKey(), (String) entry.getValue());
        }
        
        new File(TEST_LOCAL_FILE_FOLDER).mkdirs();
        LOCAL_FILE.createNewFile();
        
        configuration = new HdfsConfiguration();
    
    }
    
    @Test
    public void doTestHdfsFile() throws IOException {
        testHdfsDeleteFile(HDFS_FILE_1);
        testHdfsFileNotExists(HDFS_FILE_1);
        testCreateHdfsFileUTF8(HDFS_FILE_0);
        testCreateHdfsFile(HDFS_FILE_1);
        testHdfsFileExists(HDFS_FILE_1);
        testCopyHdfsFile(LOCAL_FILE, HDFS_FILE_2);
        testHdfsFileExists(HDFS_FILE_2);
        testGetLastModificationDateHdfsFile();
        testHdfsFileLocation();
        testListNodes();
        testRenameHdfsFile(HDFS_FILE_1, HDFS_FILE_3);
        testDeleteHdfsFile(HDFS_FILE_0);
        testDeleteHdfsFile(HDFS_FILE_2);
        testRecursiveDeleteHdfsFile(HDFS_FILE_3);
    }

    private boolean testHdfsDeleteFile(Path path) throws IOException {
        return getFileSystem().delete(path, false);
      }
      
    private void testHdfsFileExists(Path path) throws IOException {
        getLogger().info("Testing if HDFS file exists.");
        Assert.assertEquals(true, getFileSystem().exists(path));
    }

    private void testHdfsFileNotExists(Path path) throws IOException {
        getLogger().info("Testing if HDFS file does not exists.");
        Assert.assertEquals(false, getFileSystem().exists(path));
    }

    private void testCreateHdfsFileUTF8(Path path) throws IOException {
        if (getFileSystem().exists(path)) {
            getFileSystem().delete(path, false);
        }
        FSDataOutputStream out = getFileSystem().create(path);
        out.writeUTF(MESSAGE);
        out.close();
        FSDataInputStream in = getFileSystem().open(path);
        String messageIn = in.readUTF();
        System.out.print(messageIn);
        in.close();
    }
    
    private void testCreateHdfsFile(Path path) throws IOException {
        getLogger().info("Testing HDFS file creation.");
        byte[] buff = "The HDFS File content".getBytes(Charset.forName("UTF-8"));
        FSDataOutputStream outputStream = getFileSystem().create(path);
        outputStream.write(buff, 0, buff.length);
        outputStream.close();
        FSDataInputStream in = getFileSystem().open(path);
        IOUtils.copyBytes(in, System.out, 4096, false);
        in.seek(0);
        IOUtils.copyBytes(in, System.out, 4096, false);
        IOUtils.closeStream(in);
    }

    private void testCopyHdfsFile(File from, Path to) throws IOException {
        getLogger().info("Testing HDFS file copy.");
        getFileSystem().copyFromLocalFile(new Path(from.getAbsolutePath()), to);
    }
    
    private void testCopyHdfsFile2(Path from, Path to) throws IOException {
        FSDataInputStream in = getFileSystem().open(from);
        OutputStream out = getFileSystem().create(to, new Progressable() {
            @Override
            public void progress() {
                System.out.println(".");
            }
        });
        IOUtils.copyBytes(in, out, 4096, true);
        FSDataOutputStream out2 = getFileSystem().create(to);
        out2.writeUTF(MESSAGE);
        out2.close();
        FSDataInputStream in2 = getFileSystem().open(from);
        String messageIn = in2.readUTF();
        System.out.print(messageIn);
        in2.close();
    }

    private void testGetLastModificationDateHdfsFile() throws IOException {
        getLogger().info("Testing HDFS file modification date.");
        FileStatus fileStatus = getFileSystem().getFileStatus(HDFS_FILE_1);
        long modificationTime = fileStatus.getModificationTime();
        Assert.assertNotSame(new Long(0), new Long(modificationTime));
    }

    private void testHdfsFileLocation() throws IOException {
        getLogger().info("Testing HDFS file location.");
        FileStatus fileStatus = getFileSystem().getFileStatus(HDFS_FILE_1);
        BlockLocation[] blkLocations = getFileSystem().getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
        int blkCount = blkLocations.length;
        for (int i=0; i < blkCount; i++) {
          String[] hosts = blkLocations[i].getHosts();
          Assert.assertNotNull(hosts);
        }
    }

    private void testListNodes() throws IOException {
        getLogger().info("Testing HDFS node list.");
        if (getFileSystem() instanceof DistributedFileSystem) {
            DatanodeInfo[] dataNodeStats = ((DistributedFileSystem) getFileSystem()).getDataNodeStats();
            String[] names = new String[dataNodeStats.length];
            for (int i = 0; i < dataNodeStats.length; i++) {
                names[i] = dataNodeStats[i].getHostName();
            }
        }
    }

    private void testRenameHdfsFile(Path from, Path to) throws IOException {
        getLogger().info("Testing HDFS file rename.");
        Assert.assertEquals(true, getFileSystem().rename(from, to));
    }

    private void testDeleteHdfsFile(Path path) throws IOException {
        getLogger().info("Testing HDFS file delete.");
        Assert.assertEquals(true, getFileSystem().delete(path, false));
    }

    private void testRecursiveDeleteHdfsFile(Path path) throws IOException {
        getLogger().info("Testing HDFS file recursive delete.");
        Assert.assertEquals(true, getFileSystem().delete(path, true));
    }
    
    private void test1() throws IOException {
        Path srcPath = new Path("./src/test/resources/log4j.properties");
        Path dstPath = new Path("/log4j.properties");
        getFileSystem().copyFromLocalFile(srcPath, dstPath);
        FSDataInputStream in = getFileSystem().open(new Path("./log4j.properties"));
        IOUtils.copyBytes(in, System.out, 4096, false);
        in.seek(0);
        IOUtils.copyBytes(in, System.out, 4096, false);
        IOUtils.closeStream(in);
        OutputStream out = getFileSystem().create(new Path("./log4j.properties"),
                new Progressable() {
                    @Override
                    public void progress() {
                        System.out.println(".");
                    }
                });
        IOUtils.copyBytes(in, out, 4096, true);
    }

    private void test2() throws IOException {
        byte[] buff = null;
        Path path = new Path("fileName");
        FSDataOutputStream outputStream = getFileSystem().create(path);
        outputStream.write(buff, 0, buff.length);
    }
    
    private void test3() throws IOException {
      Path fromPath = new Path("fromFileName");
      Path toPath = new Path("toFileName");
      boolean isRenamed = getFileSystem().rename(fromPath, toPath);
    }
    
    private void test5() throws IOException {
      Path path = new Path("fileName");
      boolean isDeleted = getFileSystem().delete(path, true);
    }
    
    private void test6() throws IOException {
      Path path = new Path("fileName");
      FileStatus fileStatus = getFileSystem().getFileStatus(path);
      long modificationTime = fileStatus.getModificationTime();
    }
    
    private void test7() throws IOException {
      Path path = new Path("fileName");
      boolean isExists = getFileSystem().exists(path);
    }
    
    private void test8() throws IOException {
      Path path = new Path("fileName");
      FileStatus fileStatus = getFileSystem().getFileStatus(path);
      BlockLocation[] blkLocations = getFileSystem().getFileBlockLocations(path, 0, fileStatus.getLen());
      int blkCount = blkLocations.length;
      for (int i=0; i < blkCount; i++) {
        String[] hosts = blkLocations[i].getHosts();
        // Do something with the block hosts
      }
      
    }
    
    private void test9() throws IOException {
      DatanodeInfo[] dataNodeStats = ((DistributedFileSystem) getFileSystem()).getDataNodeStats();
      String[] names = new String[dataNodeStats.length];
      for (int i = 0; i < dataNodeStats.length; i++) {
          names[i] = dataNodeStats[i].getHostName();
      }

    }

    /**
     * Get the instanciated FileSystem of DistributedFileSystem
     * 
     * @return a FileSystem implementation.
     */
    protected abstract FileSystem getFileSystem();
    
    protected abstract Logger getLogger();
    
    protected static Configuration getConfiguration() {
        return configuration;
    }

}
