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
package io.datalayer.pubsub.fibonacci.nio;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.*;
import java.nio.*;

public class FibonacciConsumer extends Thread{

  private ReadableByteChannel in;

  public FibonacciConsumer(ReadableByteChannel in) {
    this.in = in;
  }
  
  public void run() {

    ByteBuffer sizeb = ByteBuffer.allocate(4);
    try {
      while (sizeb.hasRemaining()) in.read(sizeb);
      sizeb.flip();
      int howMany = sizeb.getInt();
      sizeb.clear();
      
      for (int i = 0; i < howMany; i++) {
        while (sizeb.hasRemaining()) in.read(sizeb);
        sizeb.flip();
        int length = sizeb.getInt();
        sizeb.clear();
        
        ByteBuffer data = ByteBuffer.allocate(length);
        while (data.hasRemaining()) in.read(data);
        
        BigInteger result = new BigInteger(data.array());
        System.out.println(result);
      }
    }
    catch (IOException ex) {
      System.err.println(ex);
    }
    finally {
      try {
        in.close();
      } 
      catch (Exception ex) {
        // We tried
      } 
    }
  }
}
