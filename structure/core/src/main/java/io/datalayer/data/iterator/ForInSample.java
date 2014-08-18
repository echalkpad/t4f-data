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
package io.datalayer.data.iterator;

import java.util.HashSet;
import java.util.Set;

public class ForInSample {
     public static void main(String... args) {
         // This is a collection we'll iterate over below.
         Set<String> wordset = new HashSet<String>();

         // We start with a basic loop over the elements of an array.
         // The body of the loop is executed once for each element of args[].
         // Each time through one element is assigned to the variable word.
         for(String word : args) {
             System.out.print(word + " ");
             wordset.add(word);
         }
         System.out.println();

         // Now iterate through the elements of the Set.
         for(String word : wordset) System.out.print(word + " ");
     }
}
