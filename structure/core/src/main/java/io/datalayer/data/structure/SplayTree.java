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
// This is a implementation of splay trees, in Java.
// (c) 1996, 1997, 1998, 2001 duane a. bailey
// (c) 1998, 2001 duane a. bailey
package io.datalayer.data.structure;
import java.util.Iterator;
import java.util.Comparator;

/**
 * An implementation of binary search trees, based on a splay operation
 * by Tarjan et al.  An extension of the binary search tree class.
 *
 * @version $Id: SplayTree.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 */
public class SplayTree extends BinarySearchTree
    implements OrderedStructure
{
    /**
     * Construct an empty search tree.
     *
     * @post construct a new splay tree
     * 
     */
    public SplayTree()
    {
        this(new NaturalComparator());
    }

    /**
     * Construct an empty search tree.
     *
     * @post construct a new splay tree
     * @param alternateOrder the ordering imposed on the values inserted
     * 
     */
    public SplayTree(Comparator alternateOrder)
    {
        super(alternateOrder);
    }

    /**
     * Add a value to the splay tree.
     *
     * @post adds a value to the binary search tree
     * 
     * @param val The value to be added.
     */
    public void add(Object val)
    {
        BinaryTree newNode = new BinaryTree(val);

        // add value to binary search tree 
        // if there's no root, create value at root.
        if (root.isEmpty())
        {
            root = newNode;
        }
        else
        {
            BinaryTree insertLocation = locate(root,val);
            Object nodeValue = insertLocation.value();

            // The location returned is the successor or predecessor
            // of the to-be-inserted value.

            if (ordering.compare(nodeValue,val) < 0) {
                insertLocation.setRight(newNode);
            } else {
                if (!insertLocation.left().isEmpty()) {
                    // if value is in tree, we insert just before
                    predecessor(insertLocation).setRight(newNode);
                } else {
                    insertLocation.setLeft(newNode);
                }
            }
            splay(root = newNode);
        }
        count++;
    }

    /**
     * Determine if a particular value is within the search tree.
     *
     * @post returns true iff val is a value found within the tree
     * 
     * @param val The comparable value to be found.
     * @return True iff the comparable value is within the tree.
     */
    public boolean contains(Object val)
    {
        if (root.isEmpty()) return false;

        BinaryTree possibleLocation = locate(root,val);
        if (val.equals(possibleLocation.value())) {
            splay(root = possibleLocation);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Fetch a reference to the comparable value in the tree.
     * Resulting value may be inspected, but should not be modified in
     * a way that might change its position within tree.
     *
     * @post returns object found in tree, or null
     * 
     * @param val The value to be sought in tree.
     * @return A reference to the value within the tree.
     */
    public Object get(Object val)
    {
        if (root.isEmpty()) return null;

        BinaryTree possibleLocation = locate(root,val);
        splay(root = possibleLocation);
        if (val.equals(possibleLocation.value()))
            return possibleLocation.value();
        else
            return null;
    }

    /**
     * Remove a comparable value from the tree.
     *
     * @post removes one instance of val, if found
     * 
     * @param val The value to be removed.
     * @return The actual value removed.
     */
    public Object remove(Object val) 
    {
        if (isEmpty()) return null;
      
        if (val.equals(root.value())) // delete root value
        {
            BinaryTree newroot = removeTop(root);
            count--;
            Object result = root.value();
            root = newroot;
            return result;
        }
        else
        {
            BinaryTree location = locate(root,val);

            if (val.equals(location.value())) {
                count--;
                BinaryTree parent = location.parent();
                if (parent.right() == location) {
                    parent.setRight(removeTop(location));
                } else {
                    parent.setLeft(removeTop(location));
                }
                splay(root = parent);
                return location.value();
            }
        }
        return null;
    }

    protected void splay(BinaryTree splayedNode)
    {
        BinaryTree parent,grandParent;

        while ((parent = splayedNode.parent()) != null)
        {
            if ((grandParent = parent.parent()) == null)
            {
                if (splayedNode.isLeftChild()) parent.rotateRight();
                else parent.rotateLeft();
            }
            else
            {
                if (parent.isLeftChild())
                {
                    if (splayedNode.isLeftChild())
                    {
                        // notice the order of this rotation.
                        // not doing this in order works, but not
                        // efficiently.
                        grandParent.rotateRight();
                        parent.rotateRight();
                    }
                    else
                    {
                        parent.rotateLeft();
                        grandParent.rotateRight();
                    }
                }
                else
                {
                    if (splayedNode.isRightChild()) {
                        grandParent.rotateLeft();
                        parent.rotateLeft();
                    }
                    else
                    {
                        parent.rotateRight();
                        grandParent.rotateLeft();
                    }
                }
            }
        }
    }

    /**
     * Construct an inorder traversal of the elements in the splay tree.
     *
     * @post returns iterator that traverses tree nodes in order
     * 
     * @return An iterator to traverse the tree.
     */
    public Iterator iterator()
    {
        return new SplayTreeIterator(root);
    }

    /**
     * Construct a string that represents the splay tree.
     *
     * @post returns string representation
     * 
     * @return A string representing the tree.
     */
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        s.append("<SplayTree: size="+count+" root="+root+">");
        return s.toString();
    }
}

