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
import java.util.*;

public class SequencePrinter {

  public static void main(String... args) throws IOException {

    Vector theStreams = new Vector();

    for (int i = 0; i < args.length; i++) {
       FileInputStream fin = new FileInputStream(args[i]);
       theStreams.addElement(fin);
    }

    InputStream in = new SequenceInputStream(theStreams.elements());
    for (int i = in.read(); i != -1; i = in.read()) {
      System.out.write(i);
    }
  } 
}
