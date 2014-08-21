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
// == VIntWritableTest
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import org.apache.hadoop.io.VIntWritable;
import org.apache.hadoop.util.StringUtils;
import org.junit.Test;

public class VIntWritableTest extends WritableTestBase {
  
  @Test
  public void test() throws IOException {
    // vv VIntWritableTest
    byte[] data = serialize(new VIntWritable(163));
    assertThat(StringUtils.byteToHexString(data), is("8fa3"));
    // ^^ VIntWritableTest
  }
  
  @Test
  public void testSizes() throws IOException {
    assertThat(serializeToString(new VIntWritable(1)), is("01")); // 1 byte
    assertThat(serializeToString(new VIntWritable(-112)), is("90")); // 1 byte
    assertThat(serializeToString(new VIntWritable(127)), is("7f")); // 1 byte
    assertThat(serializeToString(new VIntWritable(128)), is("8f80")); // 2 byte
    assertThat(serializeToString(new VIntWritable(163)), is("8fa3")); // 2 byte
    assertThat(serializeToString(new VIntWritable(Integer.MAX_VALUE)),
        is("8c7fffffff")); // 5 byte
    assertThat(serializeToString(new VIntWritable(Integer.MIN_VALUE)),
        is("847fffffff")); // 5 byte
  }
}
