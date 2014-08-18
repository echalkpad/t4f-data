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
package io.datalayer.data.search;

import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;

class A {
    
}

public class ListExampleSearch {
    
    static int index;
    
    public static void main(String... args) {
        // Create a list with an ordered list of strings
        ArrayList list = new ArrayList();
        list.addAll(Arrays.asList(new String[] { "ant", "cat", "cat", "bat", "dog" }));
        list.add(new A());
        System.out.println(list);
        
        // Search for the word "cat"
        index = Collections.binarySearch(list, "cat"); // 2
        System.out.println(index);
        
        // Search for a non-existent element
        index = Collections.binarySearch(list, "cow"); // -5
        System.out.println(index);
    }
}
