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
// This is an implementation of binary search trees.
// (c) 1998, 2001 duane a. bailey
package io.datalayer.data.structure;
import java.util.Iterator;
import java.util.Comparator;

/**
 * A binary search tree structure.  This structure maintains data
 * in an ordered tree.  It does not keep the tree balanced, so performance
 * may degrade if the tree height is not optimal.
 * 
 * @version $Id: BinarySearchTree.java 19 2006-08-10 04:52:00Z bailey $
 * @author, 2001 duane a. bailey
 * @see SplayTree
 * @see BinaryTree
 */
public class BinarySearchTree
    extends AbstractStructure implements OrderedStructure
{
    /**
     * A reference to the root of the tree
     */
    protected BinaryTree root;
    /**
     * The number of nodes in the tree
     */ 
    protected int count;
    /**
     * The ordering used on this search tree.
     */
    protected Comparator ordering;


    /**
     * Constructs a binary search tree with no data
     *
     * @post Constructs an empty binary search tree
     */
    public BinarySearchTree()
    {
        this(new NaturalComparator());
    }

    /**
     * Constructs a binary search tree with no data
     *
     * @post Constructs an empty binary search tree
     */
    public BinarySearchTree(Comparator alternateOrder)
    {
        root = BinaryTree.EMPTY;
        count = 0;
        ordering = alternateOrder;
    }

    /**
     * Checks for an empty binary search tree
     *
     * @post Returns true iff the binary search tree is empty
     * 
     * @return True iff the tree contains no data
     */
    public boolean isEmpty()
    {
        return root.isEmpty();
    }

    /**
     * Removes all data from the binary search tree
     *
     * @post Removes all elements from binary search tree
     */
    public void clear()
    {
        root = BinaryTree.EMPTY;
        count = 0;
    }

    /**
     * Determines the number of data values within the tree
     *
     * @post Returns the number of elements in binary search tree
     * 
     * @return The number of nodes in the binary search tree
     */
    public int size()
    {
        return count;
    }
    
    /**
     * @pre root and value are non-null
     * @post returned: 1 - existing tree node with the desired value, or
     *                 2 - the node to which value should be added
     */
    protected BinaryTree locate(BinaryTree root, Object value)
    {
        Object rootValue = root.value();
        BinaryTree child;

        // found at root: done
        if (rootValue.equals(value)) return root;
        // look left if less-than, right if greater-than
        if (ordering.compare(rootValue,value) < 0)
        {
            child = root.right();
        } else {
            child = root.left();
        }
        // no child there: not in tree, return this node,
        // else keep searching
        if (child.isEmpty()) {
            return root;
        } else {
            return locate(child, value);
        }
    }

    protected BinaryTree predecessor(BinaryTree root)
    {
        Assert.pre(!root.isEmpty(), "No predecessor to middle value.");
        Assert.pre(!root.left().isEmpty(), "Root has left child.");
        BinaryTree result = root.left();
        while (!result.right().isEmpty()) {
            result = result.right();
        }
        return result;
    }
    
    protected BinaryTree successor(BinaryTree root)
    {
        Assert.pre(!root.isEmpty(), "Tree is non-null.");
        Assert.pre(!root.right().isEmpty(), "Root has right child.");
        BinaryTree result = root.right();
        while (!result.left().isEmpty()) {
            result = result.left();
        }
        return result;
    }

    /**
     * Add a (possibly duplicate) value to binary search tree
     *
     * @post Adds a value to binary search tree
     * 
     * @param val A reference to non-null object
     */
    public void add(Object value)
    {
        BinaryTree newNode = new BinaryTree(value);

        // add value to binary search tree 
        // if there's no root, create value at root
        if (root.isEmpty())
        {
            root = newNode;
        } else {
            BinaryTree insertLocation = locate(root,value);
            Object nodeValue = insertLocation.value();
            // The location returned is the successor or predecessor
            // of the to-be-inserted value
            if (ordering.compare(nodeValue,value) < 0) {
                insertLocation.setRight(newNode);
            } else {
                if (!insertLocation.left().isEmpty()) {
                    // if value is in tree, we insert just before
                    predecessor(insertLocation).setRight(newNode);
                } else {
                    insertLocation.setLeft(newNode);
                }
            }
        }
        count++;
    }

    /**
     * Determines if the binary search tree contains a value
     *
     * @post Returns true iff val is a value found within the tree
     * 
     * @param val The value sought.  Should be non-null
     * @return True iff the tree contains a value "equals to" sought value
     */
    public boolean contains(Object value)
    {
        if (root.isEmpty()) return false;

        BinaryTree possibleLocation = locate(root,value);
        return value.equals(possibleLocation.value());
    }

    /**
     * Returns reference to value found within three.  This method can
     * be potentially dangerous if returned value is modified: if 
     * modification would change the relation of value to others within
     * the tree, the consistency of the structure is lost
     * <b>Don't modify returned value</b>
     *
     * @post Returns object found in tree, or null
     * 
     * @param val Value sought from within tree
     * @return A value "equals to" value sought; otherwise null
     */
    public Object get(Object value)
    {
        if (root.isEmpty()) return null;

        BinaryTree possibleLocation = locate(root,value);
        if (value.equals(possibleLocation.value()))
          return possibleLocation.value();
        else
          return null;
    }

    /**
     * Remove an value "equals to" the indicated value.  Only one value
     * is removed, and no guarantee is made concerning which of duplicate
     * values are removed.  Value returned is no longer part of the
     * structure
     *
     * @post Removes one instance of val, if found
     * 
     * @param val Value sought to be removed from tree
     * @return Actual value removed from tree
     */
    public Object remove(Object value) 
    {
        if (isEmpty()) return null;
      
        if (value.equals(root.value())) // delete root value
        {
            BinaryTree newroot = removeTop(root);
            count--;
            Object result = root.value();
            root = newroot;
            return result;
        }
        else
        {
            BinaryTree location = locate(root,value);

            if (value.equals(location.value())) {
                count--;
                BinaryTree parent = location.parent();
                if (parent.right() == location) {
                    parent.setRight(removeTop(location));
                } else {
                    parent.setLeft(removeTop(location));
                }
                return location.value();
            }
        }
        return null;
    }

    protected BinaryTree removeTop(BinaryTree topNode)
    {
        // remove topmost BinaryTree from a binary search tree
        BinaryTree left  = topNode.left();
        BinaryTree right = topNode.right();
        // disconnect top node
        topNode.setLeft(BinaryTree.EMPTY);
        topNode.setRight(BinaryTree.EMPTY);
        // Case a, no left BinaryTree
        //   easy: right subtree is new tree
        if (left.isEmpty()) { return right; }
        // Case b, no right BinaryTree
        //   easy: left subtree is new tree
        if (right.isEmpty()) { return left; }
        // Case c, left node has no right subtree
        //   easy: make right subtree of left
        BinaryTree predecessor = left.right();
        if (predecessor.isEmpty())
        {
            left.setRight(right);
            return left;
        }
        // General case, slide down left tree
        //   harder: successor of root becomes new root
        //           parent always points to parent of predecessor
        BinaryTree parent = left;
        while (!predecessor.right().isEmpty())
        {
            parent = predecessor;
            predecessor = predecessor.right();
        }
        // Assert: predecessor is predecessor of root
        parent.setRight(predecessor.left());
        predecessor.setLeft(left);
        predecessor.setRight(right);
        return predecessor;
    }

    /**
     * Returns an iterator over the binary search tree.  Iterator should
     * not be used if tree is modified, as behavior may be unpredicatable
     * Traverses elements using in-order traversal order
     *
     * @post Returns iterator to traverse BST
     * 
     * @return An iterator over binary search tree
     */
    public Iterator iterator()
    {
        return root.inorderIterator();
    }

    /**
     * Returns a string representing tree
     *
     * @post Generates a string representation of the BST
     * 
     * @return String representation of tree
     */
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        s.append("<BinarySearchTree:");
        if (!root.isEmpty()) {
            s.append(root);
        }
        s.append(">");
        return s.toString();
    }
}
