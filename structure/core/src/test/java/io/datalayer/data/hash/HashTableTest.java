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
package io.datalayer.data.hash;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

public class HashTableTest {

  static String[] array1 = {"C","B","A"};
  static String[] array2 = {"1","2","3"};

  public static void main(String... args) {

    java.util.Hashtable h = new java.util.Hashtable();
    h.put(array1[0], array2[0]);
    h.put(array1[1], array2[1]);
    h.put(array1[2], array2[2]);

    // unsorted keys output
    Iterator it = h.keySet().iterator();
    while (it.hasNext()) {
       String element =  (String)it.next();
       System.out.println(element + " " + (String)h.get(element));
    }

    Vector v = new Vector(h.keySet());
    Collections.sort(v);
    it = v.iterator();
    while (it.hasNext()) {
       String element =  (String)it.next();
       System.out.println( element + " " + (String)h.get(element));
    }

  }

}
