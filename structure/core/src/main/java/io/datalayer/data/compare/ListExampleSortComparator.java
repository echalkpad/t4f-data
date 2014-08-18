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
package io.datalayer.data.compare;

import java.util.*;

class Name1 {
    
    private String firstName, lastName;
    
    public Name1(String firstName, String lastName) {
        if (firstName == null || lastName == null)
            throw new NullPointerException();
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public String firstName() {
        return firstName;
    }
    
    public String lastName() {
        return lastName;
    }
    
    public String toString() {
        return firstName + " " + lastName;
    }
    
}

class ListExampleSortComparator {
    
    static final Comparator ORDER = new Comparator() {
        public int compare(Object o1, Object o2) {
            Name1 n1 = (Name1) o1;
            Name1 n2 = (Name1) o2;
            int nameCmp = n1.lastName().compareTo(n2.lastName());
            if (nameCmp != 0)
                return nameCmp;
            //            return (n1.lastName().n2.lastName() ? -1 :
            //                    (n1.employeeNumber() == n2.lastName() ? 0 : 1));
            return nameCmp;
        }
    };
    
    public static void main(String... args) {
        Name1 n[] = { new Name1("John", "Lennon"), new Name1("Karl", "Marx"),
                new Name1("Groucho", "Marx"), new Name1("Oscar", "Grouch") };
        List l = Arrays.asList(n);
        Collections.sort(l, ORDER);
        System.out.println(l);
    }
}
