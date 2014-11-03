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
package io.aos.hdfs;
// cc PooledStreamCompressor A program to compress data read from standard input and write it to standard output using a pooled compressor
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.*;
import org.apache.hadoop.util.ReflectionUtils;

// vv PooledStreamCompressor
public class PooledStreamCompressor {

  public static void main(String... args) throws Exception {
    String codecClassname = args[0];
    Class<?> codecClass = Class.forName(codecClassname);
    Configuration conf = new Configuration();
    CompressionCodec codec = (CompressionCodec)
      ReflectionUtils.newInstance(codecClass, conf);
    /*[*/Compressor compressor = null;
    try {
      compressor = CodecPool.getCompressor(codec);/*]*/
      CompressionOutputStream out =
        codec.createOutputStream(System.out, /*[*/compressor/*]*/);
      IOUtils.copyBytes(System.in, out, 4096, false);
      out.finish();
    /*[*/} finally {
      CodecPool.returnCompressor(compressor);
    }/*]*/
  }
}
// ^^ PooledStreamCompressor
