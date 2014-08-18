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
package io.aos.t4f.hadoop.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class WordCountMapReduceTest2 {

    static final long MEGABYTES = 1024 * 1024;

    static void printUsage() {
        System.out.println("WordCount [-r <reduces>] <input> <output>");
        System.exit(-1);
    }

    private static List<String> parseArguments(String args[], Job j) {

        List<String> argList = new ArrayList<String>();
        for (int i = 0; i < args.length; ++i) {
            try {
                if ("-r".equals(args[i])) {
                    // set the number of reducers to the specified parameter
                    j.setNumReduceTasks(Integer.parseInt(args[++i]));
                } else {
                    argList.add(args[i]);
                }
            } catch (NumberFormatException except) {
                System.out.println("ERROR: Integer expected instead of "
                        + args[i]);
                printUsage();
            } catch (ArrayIndexOutOfBoundsException except) {
                System.out.println("ERROR: Required parameter missing from "
                        + args[i - 1]);
                printUsage();
            }
        }

        return argList;
    }

    public static int main(String... args) throws Exception {

        // Get the default configuration object
        Configuration conf = new Configuration();

        // Add resources
        conf.addResource("hdfs-default.xml");
        conf.addResource("hdfs-site.xml");
        conf.addResource("mapred-default.xml");
        conf.addResource("mapred-site.xml");

        Job job = new Job(conf);
        job.setJobName("WordCount");

        List<String> other_args = parseArguments(args, job);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // the keys are words (strings)
        job.setOutputKeyClass(Text.class);
        // the values are counts (ints)
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(MapClass.class);
        job.setCombinerClass(ReduceClass.class);
        job.setReducerClass(ReduceClass.class);

        // Set the input format class
        job.setInputFormatClass(TextInputFormat.class);
        // Set the output format class
        job.setOutputFormatClass(TextOutputFormat.class);
        // Set the input path
        TextInputFormat.setInputPaths(job, other_args.get(0));
        // Set the output path
        TextOutputFormat.setOutputPath(job, new Path(other_args.get(1)));

        /*
         * Set the minimum and maximum split sizes This parameter helps to
         * specify the number of map tasks. For each input split, there will be
         * a separate map task. In this example each split is of size 32 MB
         */
        TextInputFormat.setMinInputSplitSize(job, 32 * MEGABYTES);
        TextInputFormat.setMaxInputSplitSize(job, 32 * MEGABYTES);

        // Set the jar file to run
        job.setJarByClass(WordCountMapReduceTest2.class);

        // Submit the job
        Date startTime = new Date();
        System.out.println("Job started: " + startTime);
        int exitCode = job.waitForCompletion(true) ? 0 : 1;

        if (exitCode == 0) {
            Date end_time = new Date();
            System.out.println("Job ended: " + end_time);
            System.out.println("The job took "
                    + (end_time.getTime() - startTime.getTime()) / 1000
                    + " seconds.");
        } else {
            System.out.println("Job Failed!!!");
        }

        return exitCode;

    }

    public static class ReduceClass extends Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }

    public static class MapClass extends Mapper<LongWritable, Text, Text, IntWritable> {
        private static final IntWritable one = new IntWritable(1);
        private Text word = new Text();
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                context.write(word, one);
            }
        }
    }
    
}
