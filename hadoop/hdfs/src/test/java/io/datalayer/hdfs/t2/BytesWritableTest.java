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
package io.datalayer.hdfs.t2;
// == BytesWritableTest
// == BytesWritableTest-Capacity
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.util.StringUtils;
import org.junit.Test;

public class BytesWritableTest extends WritableTestBase {
  
  @Test
  public void test() throws IOException {
    // vv BytesWritableTest
    BytesWritable b = new BytesWritable(new byte[] { 3, 5 });
    byte[] bytes = serialize(b);
    assertThat(StringUtils.byteToHexString(bytes), is("000000020305"));
    // ^^ BytesWritableTest
    
    // vv BytesWritableTest-Capacity
    b.setCapacity(11);
    assertThat(b.getLength(), is(2));
    assertThat(b.getBytes().length, is(11));
    // ^^ BytesWritableTest-Capacity
  }
}
