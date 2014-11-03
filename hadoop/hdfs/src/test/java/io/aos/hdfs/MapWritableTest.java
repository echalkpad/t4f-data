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
// == MapWritableTest
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.junit.Test;

public class MapWritableTest extends WritableTestBase {
  
  @Test
  public void mapWritable() throws IOException {
    // vv MapWritableTest
    MapWritable src = new MapWritable();
    src.put(new IntWritable(1), new Text("cat"));
    src.put(new VIntWritable(2), new LongWritable(163));
    
    MapWritable dest = new MapWritable();
    WritableUtils.cloneInto(dest, src);
    assertThat((Text) dest.get(new IntWritable(1)), is(new Text("cat")));
    assertThat((LongWritable) dest.get(new VIntWritable(2)), is(new LongWritable(163)));
    // ^^ MapWritableTest
  }

  @Test
  public void setWritableEmulation() throws IOException {
    MapWritable src = new MapWritable();
    src.put(new IntWritable(1), NullWritable.get());
    src.put(new IntWritable(2), NullWritable.get());
    
    MapWritable dest = new MapWritable();
    WritableUtils.cloneInto(dest, src);
    assertThat(dest.containsKey(new IntWritable(1)), is(true));
  }
}
