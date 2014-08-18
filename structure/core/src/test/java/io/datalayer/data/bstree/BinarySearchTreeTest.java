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
package io.datalayer.data.bstree;

import io.datalayer.data.bstree.BinarySearchTree;
import io.datalayer.data.bstree.Node;
import io.datalayer.data.comparator.NaturalComparator;
import junit.framework.TestCase;

import org.junit.Ignore;

/**
 * Test cases for {@link BinarySearchTree}.
 */
@Ignore
public class BinarySearchTreeTest extends TestCase {
    private Node _a;
    private Node _d;
    private Node _f;
    private Node _h;
    private Node _i;
    private Node _k;
    private Node _l;
    private Node _m;
    private Node _p;
    private Node _root;
    private BinarySearchTree _tree;

    protected void setUp() throws Exception {
        super.setUp();

        _a = new Node("A");
        _h = new Node("H");
        _k = new Node("K");
        _p = new Node("P");
        _f = new Node("F", null, _h);
        _m = new Node("M", null, _p);
        _d = new Node("D", _a, _f);
        _l = new Node("L", _k, _m);
        _i = new Node("I", _d, _l);
        _root = _i;

        _tree = new BinarySearchTree(NaturalComparator.INSTANCE);
        _tree.insert(_i.getValue());
        _tree.insert(_d.getValue());
        _tree.insert(_l.getValue());
        _tree.insert(_a.getValue());
        _tree.insert(_f.getValue());
        _tree.insert(_k.getValue());
        _tree.insert(_m.getValue());
        _tree.insert(_h.getValue());
        _tree.insert(_p.getValue());
    }

    public void testInsert() {
        assertEquals(_root, _tree.getRoot());
    }

    public void testSearch() {
        assertEquals(_a, _tree.search(_a.getValue()));
        assertEquals(_d, _tree.search(_d.getValue()));
        assertEquals(_f, _tree.search(_f.getValue()));
        assertEquals(_h, _tree.search(_h.getValue()));
        assertEquals(_i, _tree.search(_i.getValue()));
        assertEquals(_k, _tree.search(_k.getValue()));
        assertEquals(_l, _tree.search(_l.getValue()));
        assertEquals(_m, _tree.search(_m.getValue()));
        assertEquals(_p, _tree.search(_p.getValue()));

        assertNull(_tree.search("UNKNOWN"));
    }

    public void testDeleteLeafNode() {
        Node deleted = _tree.delete(_h.getValue());
        assertNotNull(deleted);
        assertEquals(_h.getValue(), deleted.getValue());

        _f.setLarger(null);
        assertEquals(_root, _tree.getRoot());
    }

    public void testDeleteNodeWithOneChild() {
        Node deleted = _tree.delete(_m.getValue());
        assertNotNull(deleted);
        assertEquals(_m.getValue(), deleted.getValue());

        _l.setLarger(_p);
        assertEquals(_root, _tree.getRoot());
    }

    public void testDeleteNodeWithTwoChildren() {
        Node deleted = _tree.delete(_i.getValue());
        assertNotNull(deleted);
        assertEquals(_i.getValue(), deleted.getValue());

        _i.setValue(_k.getValue());
        _l.setSmaller(null);
        assertEquals(_root, _tree.getRoot());
    }

    public void testDeleteRootNodeUntilTreeIsEmpty() {
        while (_tree.getRoot() != null) {
            Object key = _tree.getRoot().getValue();
            Node deleted = _tree.delete(key);
            assertNotNull(deleted);
            assertEquals(key, deleted.getValue());
        }
    }
}
