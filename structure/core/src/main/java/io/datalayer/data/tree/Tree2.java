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
package io.datalayer.data.tree;
import java.io.Serializable;
import java.util.*;

public class Tree2<V extends Serializable & Comparable<V>>
    implements Serializable, Comparable<Tree2<V>>
{
    V value;
    List<Tree2<V>> branches = new ArrayList<Tree2<V>>();

    public Tree2(V value) { this.value = value; }

    // Instance methods
    V getValue() { return value; }
    void setValue(V value) { this.value = value; }
    int getNumBranches() { return branches.size(); }
    Tree2<V> getBranch(int n) { return branches.get(n); }
    void addBranch(Tree2<V> branch) { branches.add(branch); }

    // This method is a nonrecursive implementation of Comparable<Tree<V>>
    // It only compares the value of this node and ignores branches.
    public int compareTo(Tree2<V> that) {
        if (this.value == null && that.value == null) return 0;
        if (this.value == null) return -1;
        if (that.value == null) return 1;
        return this.value.compareTo(that.value);
    }

    // javac -Xlint warns us if we omit this field in a Serializable class
    private static final long serialVersionUID = 833546143621133467L;
}
