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
package io.datalayer.data.map;

import java.util.Collections;
import java.util.LinkedHashMap;

class Stuff {
    
    int i;
    
    Stuff(int i) {
        this.i = i;
    }
    
    int getI() {
        return this.i;
    }
}

public class HashMapCacheExample {
    
    public static void main(String... args) {
        final int MAX_ENTRIES = 100;
        java.util.Map cache = new LinkedHashMap(MAX_ENTRIES + 1, .75F, true) {
            // This method is called just after a new entry has been added
            public boolean removeEldestEntry(Map.Entry eldest) {
                return size() > MAX_ENTRIES;
            }
        };
        
        // Add to cache
        Object key = "key";
        cache.put(key, new Stuff(1));
        cache.put(key, new Stuff(2));
        cache.put(key, new Stuff(3));
        cache.put(key, new Stuff(4));
        
        // Get object
        Object o = cache.get(key);
        if (o == null && !cache.containsKey(key)) {
            // Object not in cache. If null is not a possible value in the cache,
            // the call to cache.contains(key) is not needed
        }
        // If the cache is to be used by multiple threads,
        // the cache must be wrapped with code to synchronize the methods
        cache = (java.util.Map) Collections.synchronizedMap(cache);
    }
}
