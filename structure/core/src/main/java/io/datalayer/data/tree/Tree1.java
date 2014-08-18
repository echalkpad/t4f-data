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
import java.util.*;

/**
 * A tree is a data structure that holds values of type V.
 * Each tree has a single value of type V and can have any number of
 * branches, each of which is itself a Tree.
 */
public class Tree1<V> {
    // The value of the tree is of type V.
    V value;

    // A Tree<V> can have branches, each of which is also a Tree<V>
    List<Tree1<V>> branches = new ArrayList<Tree1<V>>();

    // Here's the constructor.  Note the use of the type variable V.
    public Tree1(V value) { this.value = value; }

    // These are instance methods for manipulating the node value and branches.
    // Note the use of the type variable V in the arguments or return types.
    V getValue() { return value; }
    void setValue(V value) { this.value = value; }
    int getNumBranches() { return branches.size(); }
    Tree1<V> getBranch(int n) { return branches.get(n); }
    void addBranch(Tree1<V> branch) { branches.add(branch); }
}
