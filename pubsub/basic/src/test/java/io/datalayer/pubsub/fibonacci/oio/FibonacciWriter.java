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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FibonacciWriter extends Thread {

  private DataOutputStream theOutput;
  private int howMany;

  public FibonacciWriter(OutputStream out, int howMany) {
    theOutput = new DataOutputStream(out);
    this.howMany = howMany;
  }

  public void run() {

    try {
      int f1 = 1;
      int f2 = 1;
      theOutput.writeInt(f1);
      theOutput.writeInt(f2);

      // Now calculate the rest.
      for (int i = 2; i < howMany; i++) {
        int temp = f2;
        f2 = f2 + f1;
        f1 = temp;
        if (f2 < 0) { // overflow
         break;
        }
        theOutput.writeInt(f2);
      }
    }
    catch (IOException ex) { System.err.println(ex); }
  }
}
