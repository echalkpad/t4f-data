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

import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;

public class ListLooper {

    public static void main(String... args) {

        List names1 = new ArrayList();
        names1.add("a");
        names1.add("b");
        names1.add("c");

        for (AosIterator it = names1.iterator(); it.isDone();) {
            it.next();
            String name = (String) it.current();
            System.out.println(name.charAt(0));
        }

        AosIterator it = names1.iterator();
        while (it.isDone()) {
            it.next();
            Object o = it.current();
            Class cLass = o.getClass();
            System.out.println("\t" + cLass.getName());
        }

        String[] moreNames = { "d", "e", "f" };
        for (int i = 0; i < moreNames.length; i++)
            System.out.println(moreNames[i].charAt(0));

        List names2 = new ArrayList();
        names2.add("a");
        names2.add("b");
        names2.add("c");

        // for (String name : names2)
        // System.out.println(name.charAt(0));

    }
}
