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
package io.datalayer.hdfs;
// == MapFileSeekTest
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.ReflectionUtils;
import org.junit.*;

public class MapFileSeekTest {
  
  private static final String MAP_URI = "test.numbers.map";
  private FileSystem fs;
  private MapFile.Reader reader;
  private WritableComparable<?> key;
  private Writable value;

  @Before
  public void setUp() throws IOException {
    MapFileWriteDemo.main(new String[] { MAP_URI });

    Configuration conf = new Configuration();
    fs = FileSystem.get(URI.create(MAP_URI), conf);

    reader = new MapFile.Reader(fs, MAP_URI, conf);
    key = (WritableComparable<?>)
      ReflectionUtils.newInstance(reader.getKeyClass(), conf);
    value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
  }

  @After
  public void tearDown() throws IOException {
    fs.delete(new Path(MAP_URI), true);
  }
  
  @Test
  public void get() throws Exception {
    // vv MapFileSeekTest
    Text value = new Text();
    reader.get(new IntWritable(496), value);
    assertThat(value.toString(), is("One, two, buckle my shoe"));
    // ^^ MapFileSeekTest
  }
  
  @Test
  public void seek() throws Exception {
    assertThat(reader.seek(new IntWritable(496)), is(true));
    assertThat(reader.next(key, value), is(true));
    assertThat(((IntWritable) key).get(), is(497));
    assertThat(((Text) value).toString(), is("Three, four, shut the door"));
  }
  
  

}
