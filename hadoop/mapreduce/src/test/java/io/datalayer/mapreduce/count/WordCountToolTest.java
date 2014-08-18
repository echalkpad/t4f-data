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
package io.datalayer.mapreduce.count;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.datalayer.mapreduce.count.WordCountMapper;
import io.datalayer.mapreduce.count.WordCountReducer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MiniMRClientCluster;
import org.apache.hadoop.mapred.MiniMRClientClusterFactory;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Basic testing for the MiniMRClientCluster. This test shows an example class
 * that can be used in MR1 or MR2, without any change to the test. The test will
 * use MiniMRYarnCluster in MR2, and MiniMRCluster in MR1.
 */
@Ignore
public class WordCountToolTest {
    private static Path inDir = null;
    private static Path outDir = null;
    private static Path testdir = null;
    private static Path[] inFiles = new Path[5];
    private static MiniMRClientCluster mrCluster;

    @BeforeClass
    public static void setup() throws IOException {

        Properties props = new Properties();
        InputStream is = ClassLoader.getSystemResourceAsStream("hdfs-conf.properties");
        props.load(is);
        for (Entry<Object, Object> entry : props.entrySet()) {
            System.setProperty((String) entry.getKey(), (String) entry.getValue());
        }

        Map<String, String> envMap = new HashMap<String, String>();
        envMap.put("JAVA_HOME", System.getProperty("java.home"));
        setEnv(envMap);

        final Configuration conf = new Configuration();
        final Path TEST_ROOT_DIR = new Path(System.getProperty("test.build.data", "/tmp"));
        testdir = new Path(TEST_ROOT_DIR, "TestMiniMRClientCluster");
        inDir = new Path(testdir, "in");
        outDir = new Path(testdir, "out");

        FileSystem fs = FileSystem.getLocal(conf);
        if (fs.exists(testdir) && !fs.delete(testdir, true)) {
            throw new IOException("Could not delete " + testdir);
        }
        if (!fs.mkdirs(inDir)) {
            throw new IOException("Mkdirs failed to create " + inDir);
        }

        for (int i = 0; i < inFiles.length; i++) {
            inFiles[i] = new Path(inDir, "part_" + i);
            createFile(inFiles[i], conf);
        }

        // create the mini cluster to be used for the tests
        mrCluster = MiniMRClientClusterFactory.create(WordCountToolTest.class, 1, new Configuration());

    }

    @Test
    public void testJob() throws Exception {
        final Job job = Job.getInstance(mrCluster.getConfig());
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        job.setNumReduceTasks(1);
        FileInputFormat.setInputPaths(job, inDir);
        FileOutputFormat.setOutputPath(job, new Path(outDir, "testJob"));
        assertTrue(job.waitForCompletion(true));
        validateCounters(job.getCounters(), 5, 25, 5, 5);
    }

    @AfterClass
    public static void cleanup() throws IOException {
        // Clean up the input and output files
        final Configuration conf = new Configuration();
        final FileSystem fs = testdir.getFileSystem(conf);
        if (fs.exists(testdir)) {
            fs.delete(testdir, true);
        }
        // stopping the mini cluster
        mrCluster.stop();
    }

    private void validateCounters(Counters counters, long mapInputRecords, long mapOutputRecords,
            long reduceInputGroups, long reduceOutputRecords) {
        assertEquals("MapInputRecords", mapInputRecords, counters.findCounter("MyCounterGroup", "MAP_INPUT_RECORDS")
                .getValue());
        assertEquals("MapOutputRecords", mapOutputRecords, counters.findCounter("MyCounterGroup", "MAP_OUTPUT_RECORDS")
                .getValue());
        assertEquals("ReduceInputGroups", reduceInputGroups,
                counters.findCounter("MyCounterGroup", "REDUCE_INPUT_GROUPS").getValue());
        assertEquals("ReduceOutputRecords", reduceOutputRecords,
                counters.findCounter("MyCounterGroup", "REDUCE_OUTPUT_RECORDS").getValue());
    }

    private static void createFile(Path inFile, Configuration conf) throws IOException {
        final FileSystem fs = inFile.getFileSystem(conf);
        if (fs.exists(inFile)) {
            return;
        }
        FSDataOutputStream out = fs.create(inFile);
        out.writeBytes("This is a test file");
        out.close();
    }

    protected static void setEnv(Map<String, String> newenv) {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass
                    .getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
        } catch (NoSuchFieldException e) {
            try {
                Class[] classes = Collections.class.getDeclaredClasses();
                Map<String, String> env = System.getenv();
                for (Class cl : classes) {
                    if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                        Field field = cl.getDeclaredField("m");
                        field.setAccessible(true);
                        Object obj = field.get(env);
                        Map<String, String> map = (Map<String, String>) obj;
                        map.clear();
                        map.putAll(newenv);
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
