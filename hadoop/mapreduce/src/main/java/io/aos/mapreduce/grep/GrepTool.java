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
package io.aos.mapreduce.grep;

import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.map.InverseMapper;
import org.apache.hadoop.mapreduce.lib.map.RegexMapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Extracts matching regexs from input files and counts them.
 */
public class GrepTool extends Configured implements Tool {

    private GrepTool() {
        /* Singleton */
    }

    public static void main(String... args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new GrepTool(), new String[] { "in", "out", "*" });
        System.exit(res);
    }

    public int run(String[] args) throws Exception {

        if (args.length < 3) {
            System.out.println("Grep <inDir> <outDir> <regex> [<group>]");
            ToolRunner.printGenericCommandUsage(System.out);
            org.apache.hadoop.util.Tool t;
            return 2;
        }

        Path tempDir = new Path("grep-temp-" + Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));

        Configuration conf = getConf();
        conf.set(RegexMapper.PATTERN, args[2]);

        if (args.length == 4) {
            conf.set(RegexMapper.GROUP, args[3]);
        }

        try {

            Job greJob = Job.getInstance(conf);
            greJob.setJobName("GrepSearch");

            FileInputFormat.setInputPaths(greJob, args[0]);

            greJob.setMapperClass(RegexMapper.class);
            greJob.setCombinerClass(LongSumReducer.class);
            greJob.setReducerClass(LongSumReducer.class);

            FileOutputFormat.setOutputPath(greJob, tempDir);
            greJob.setOutputFormatClass(SequenceFileOutputFormat.class);
            greJob.setOutputKeyClass(Text.class);
            greJob.setOutputValueClass(LongWritable.class);

            greJob.waitForCompletion(true);

            Job sortJob = Job.getInstance(conf);
            sortJob.setJobName("GrepSort");

            FileInputFormat.setInputPaths(sortJob, tempDir);
            sortJob.setInputFormatClass(SequenceFileInputFormat.class);

            sortJob.setMapperClass(InverseMapper.class);

            // Write a single file
            sortJob.setNumReduceTasks(1);

            FileOutputFormat.setOutputPath(sortJob, new Path(args[1]));
            sortJob.setSortComparatorClass( // sort by decreasing freq

            LongWritable.DecreasingComparator.class);

            sortJob.waitForCompletion(true);

        }
        
        catch (Exception e) {
            return 2;
        }

        finally {
            FileSystem.get(conf).delete(tempDir, true);
        }

        return 0;

    }

}
