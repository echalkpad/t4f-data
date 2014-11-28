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
// == ArrayWritableTest
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.junit.Test;

public class ArrayWritableTest extends WritableTestBase {
  
  @Test
  public void test() throws IOException {
    // vv ArrayWritableTest
    ArrayWritable writable = new ArrayWritable(Text.class);
    // ^^ ArrayWritableTest
    writable.set(new Text[] { new Text("cat"), new Text("dog") });
    
    TextArrayWritable dest = new TextArrayWritable();
    WritableUtils.cloneInto(dest, writable);
    assertThat(dest.get().length, is(2));
    // TODO: fix cast, also use single assert
    assertThat((Text) dest.get()[0], is(new Text("cat")));
    assertThat((Text) dest.get()[1], is(new Text("dog")));
    
    Text[] copy = (Text[]) dest.toArray();
    assertThat(copy[0], is(new Text("cat")));
    assertThat(copy[1], is(new Text("dog")));
  }
}
