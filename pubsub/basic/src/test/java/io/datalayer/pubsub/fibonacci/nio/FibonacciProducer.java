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
import java.nio.*;
import java.nio.channels.*;

public class FibonacciProducer extends Thread {

  private WritableByteChannel out;
  private int howMany;
  
  public FibonacciProducer(WritableByteChannel out, int howMany) {
    this.out = out;
    this.howMany = howMany;
  }

  public void run() {
   
    BigInteger low = BigInteger.ONE;
    BigInteger high = BigInteger.ONE;
    try {
       ByteBuffer buffer = ByteBuffer.allocate(4);
       buffer.putInt(this.howMany);
       buffer.flip();
       while (buffer.hasRemaining()) out.write(buffer);
       
       for (int i = 0; i < howMany; i++) {
        byte[] data = low.toByteArray();
        // These numbers can become arbitrarily large, and they grow
        // exponentially so no fixed size buffer will suffice.
        buffer = ByteBuffer.allocate(4 + data.length);
        
        buffer.putInt(data.length);
        buffer.put(data);
        buffer.flip();
        
        while (buffer.hasRemaining()) out.write(buffer);
        
        // find the next number in the series
        BigInteger temp = high;
        high = high.add(low);
        low = temp;
      }
      out.close();
      System.err.println("Closed");
    }
    catch (IOException ex) {
      System.err.println(ex);
    }
  }
}
