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
package io.aos.mapreduce.count;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WordCountTool extends Configured implements Tool {

    private WordCountTool() {
        /* Only Main */
    }

    public static void main(String... args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new WordCountTool(), args);
        System.exit(res);
    }

    public int run(String[] args) throws Exception {

        if (! ((args.length > 0) && (args.length < 3))) {
            System.out.println("WordCount <inDir> <outDir>");
            ToolRunner.printGenericCommandUsage(System.out);
            return 2;
        }
        
        Path inPath = new Path(args[0]);
        Path outPath = new Path(args[1]);

        Configuration conf = getConf();

        Job job = Job.getInstance(conf);

        job.setJobName("WordCount_" + inPath.getName());
        job.setJar("./target/datalayer-hadoop-mapreduce-1.0.0-SNAPSHOT.jar");
//        job.setJarByClass(WordCountTool.class);
        
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job, inPath);

        FileOutputFormat.setOutputPath(job, outPath);
        job.setOutputFormatClass(TextOutputFormat.class);

        boolean success = job.waitForCompletion(true);
        return success ? 0 : 1;

    }

}
