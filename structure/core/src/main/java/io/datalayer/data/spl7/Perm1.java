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
package io.datalayer.data.spl7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Perm1 {

    public static void main(String... args) {
    
        int minGroupSize = Integer.parseInt(args[1]);
 
        // Read words from file and put into simulated multimap
        Map m = new HashMap();
        try {
            BufferedReader in =
                   new BufferedReader(new FileReader(args[0]));
            String word;
            while((word = in.readLine()) != null) {
                String alpha = alphabetize(word);
                List l = (List) m.get(alpha);
                if (l==null)
                    m.put(alpha, l=new ArrayList());
                l.add(word);
            }
        } catch(IOException e) {
            System.err.println(e);
            System.exit(1);
        }

        // Print all permutation groups above size threshold
        for (Iterator i = m.values().iterator(); i.hasNext(); ) {
            List l = (List) i.next();
            if (l.size() >= minGroupSize)
                System.out.println(l.size() + ": " + l);
        }
    }

    private static String alphabetize(String s) {
        int count[] = new int[256];
        int len = s.length();
        for (int i=0; i<len; i++)
            count[s.charAt(i)]++;
        StringBuffer result = new StringBuffer(len);
        for (char c='a'; c<='z'; c++)
            for (int i=0; i<count[c]; i++)
                result.append(c);
        return result.toString();
    }
}
