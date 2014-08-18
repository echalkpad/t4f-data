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
// Post-order iterator for binary trees.
// (c) 1998, 2001 duane a. bailey
package io.datalayer.data.structure;

/**
 * This class implements a post-order traversal of a binary tree.
 * This iterator considers every node after its non-trivial descendants.
 *
 * @see BinaryTree#PostorderElements
 * @version $Id: BTPostorderIterator.java 19 2006-08-10 04:52:00Z bailey $
 * @author, 2001 duane a. bailey
 */
class BTPostorderIterator extends AbstractIterator
{
    /**
     * The root of the tree currently being traversed.
     */
    protected BinaryTree root; // root of traversed subtree
    /**
     * The stack the maintains the state of the iterator.
     * Elements of the stack are nodes whose descendants are still being
     * considered.
     */
    protected Stack todo;  // stack of nodes whose descendants
                           // are currently being visited

    /**
     * Construct an iterator to traverse subtree rooted at root
     * in post-order.
     *
     * @post constructs an iterator to traverse in postorder
     * 
     * @param root The root of the subtree to be traversed.
     */
    public BTPostorderIterator(BinaryTree root)
    {
        todo = new StackList();
        this.root = root;
        reset();
    }   

    /**
     * Reset the iterator to the first node of the traversal.
     *
     * @post Resets the iterator to retraverse
     */
    public void reset()
    {
        todo.clear();
        // stack is empty; push on nodes from root to
        // leftmost descendant
        BinaryTree current = root;
        while (!current.isEmpty()) {
            todo.push(current);
            if (!current.left().isEmpty())
                current = current.left();
            else
                current = current.right();
        }
    }

    /**
     * Return true iff more nodes are to be considered in traversal.
     *
     * @post Returns true iff iterator is not finished
     * 
     * @return True iff more nodes are to be considered in traversal.
     */
    public boolean hasNext()
    {
        return !todo.isEmpty();
    }

    /**
     * Return the value of the current node.
     *
     * @pre hasNext()
     * @post returns reference to current value
     * 
     * @return The value referenced by current node.
     */
    public Object get()
    {   
        return ((BinaryTree)todo.get()).value();
    }

    /**
     * Return current value and increment iterator.
     *
     * @pre hasNext();
     * @post returns current value, increments iterator
     * 
     * @return The value currently considered by iterator, increment.
     */
    public Object next()
    {
        BinaryTree current = (BinaryTree)todo.pop();
        Object result = current.value();
        if (!todo.isEmpty())
        {
            BinaryTree parent = (BinaryTree)todo.get();
            if (current == parent.left()) {
                current = parent.right();
                while (!current.isEmpty())
                {
                    todo.push(current);
                    if (!current.left().isEmpty())
                         current = current.left();
                    else current = current.right();
                }
            }
        }
        return result;
    }
}
