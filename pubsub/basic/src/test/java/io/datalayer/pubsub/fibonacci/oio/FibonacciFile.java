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
package io.datalayer.pubsub.fibonacci.oio;
import java.io.*;

public class FibonacciFile {

  public static void main(String... args) throws IOException {

    int howMany = 20;
     
    // To avoid resizing the buffer, calculate the size of the
    // byte array in advance.
    ByteArrayOutputStream bout = new ByteArrayOutputStream(howMany*4);
    DataOutputStream dout = new DataOutputStream(bout);

    // First two Fibonacci numbers must be given
    // to start the process.
    int f1 = 1;
    int f2 = 1;
    dout.writeInt(f1);
    dout.writeInt(f2);

    // Now calculate the rest.
    for (int i = 3; i <= 20; i++) {
      int temp = f2;
      f2 = f2 + f1;
      f1 = temp;
      dout.writeInt(f2);
    }

    FileOutputStream fout = new FileOutputStream("fibonacci.dat");
    try {
      bout.writeTo(fout);
      fout.flush();
    }
    finally {
      fout.close();
    }
  }  
}
