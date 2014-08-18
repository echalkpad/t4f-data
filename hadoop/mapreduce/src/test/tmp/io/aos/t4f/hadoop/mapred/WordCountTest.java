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
package io.aos.t4f.hadoop.mapred;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MiniMRCluster;
import org.apache.hadoop.mapred.OutputLogFilter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WordCountTest {
    
    private Configuration configuration;
    private MiniDFSCluster dfsCluster;
    private MiniMRCluster mrCluster;
    private final String TARGET_FOLDER = "./target";
    private final File LOCAL_FILE = new File(TARGET_FOLDER + "/localFile");
    private final String MINI_DFS_CLUSTER_FOLDER = TARGET_FOLDER + "/mini-hdfs-cluster";
    private final String DATA_FOLDER = MINI_DFS_CLUSTER_FOLDER + "/data";
    private final String LOG_FOLDER = MINI_DFS_CLUSTER_FOLDER + "/log";
    private final Path input = new Path(DATA_FOLDER + "/input");
    private final Path output = new Path(DATA_FOLDER + "/output");
    
    @Before
    public void init() throws IOException {
        new File(TARGET_FOLDER).mkdirs();
        LOCAL_FILE.createNewFile();
        FileUtils.deleteDirectory(new File(MINI_DFS_CLUSTER_FOLDER));
        new File(DATA_FOLDER).mkdirs();
        new File(LOG_FOLDER).mkdirs();
        configuration = new Configuration();
        dfsCluster = new MiniDFSCluster(configuration, 1, true, null);
        System.setProperty("hadoop.log.dir", LOG_FOLDER);
        mrCluster = new MiniMRCluster(1, getFileSystem().getUri().toString(), 1);
    }
    
    @Test
    public void doTestMapReduce() throws IOException {
        
    }

    protected FileSystem getFileSystem() throws IOException {
        return dfsCluster.getFileSystem();
      }

      private void createTextInputFile() throws IOException {
        OutputStream os = getFileSystem().create(new Path(input, "wordcount"));
        Writer wr = new OutputStreamWriter(os);
        wr.write("b a a\n");
        wr.close();
      }

      private JobConf createJobConf() {
        JobConf conf = mrCluster.createJobConf();
        conf.setJobName("wordcount test");

        conf.setMapperClass(WordCountMapper.class);
        conf.setReducerClass(WordCountReducer.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setMapOutputKeyClass(Text.class);
        conf.setMapOutputValueClass(IntWritable.class);
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);
        conf.setNumMapTasks(1);
        conf.setNumReduceTasks(1);
        FileInputFormat.setInputPaths(conf, input);
        FileOutputFormat.setOutputPath(conf, output);
        return conf;
      }

      @Test
      public void testCount() throws Exception {
        createTextInputFile();
        JobClient.runJob(createJobConf());
        Path[] outputFiles = FileUtil.stat2Paths(getFileSystem().listStatus(
          output, new OutputLogFilter()));
        Assert.assertEquals(1, outputFiles.length);
        InputStream is = getFileSystem().open(outputFiles[0]);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Assert.assertEquals("a\t2", reader.readLine());
        Assert.assertEquals("b\t1", reader.readLine());
        Assert.assertNull(reader.readLine());
        reader.close();
      }

      @After
      public void tearDown() throws Exception {
        if (dfsCluster != null) {
          dfsCluster.shutdown();
          dfsCluster = null;
        }
        if (mrCluster != null) {
          mrCluster.shutdown();
          mrCluster = null;
        }
      }

}
