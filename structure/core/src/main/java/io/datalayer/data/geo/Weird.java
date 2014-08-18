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
package io.datalayer.data.geo;

public class Weird {
  // A static member interface used below
  public static interface IntHolder { public int getValue(); }

  public static void main(String... args) {     
    IntHolder[] holders = new IntHolder[10];   // An array to hold 10 objects
    for(int i = 0; i < 10; i++) {              // Loop to fill the array up
      final int fi = i;                        // A final local variable
      class MyIntHolder implements IntHolder { // A local class
        public int getValue() { return fi; }  // It uses the final variable
      }
      holders[i] = new MyIntHolder();          // Instantiate the local class
    }

    // The local class is now out of scope, so we can't use it. But
    // we have 10 valid instances of that class in our array. The local
    // variable fi is not in our scope here, but it is still in scope for
    // the getValue() method of each of those 10 objects. So call getValue()
    // for each object and print it out. This prints the digits 0 to 9. 
    for(int i = 0; i < 10; i++) System.out.println(holders[i].getValue());
  }
}
