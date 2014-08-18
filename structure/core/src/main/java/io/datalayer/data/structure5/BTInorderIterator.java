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
// In-order iterator for binary trees.
// (c) 1998, 2001 duane a. bailey
package io.datalayer.data.structure5;

/**
 * An iterator for traversing binary trees constructed from
 * BinaryTrees.  The iterator performs minimal work before
 * traversal.  Every node is considered after every left descendant,
 * but before any right descendant.  AbstractIterator finishes when
 * all descendants of the start node have been considered.
 * <P>
 * Example usage:
 * <P>
 * <pre>
 *      {@link io.datalayer.data.structure.BinaryTree BinaryTree} t = new {@link io.datalayer.data.structure.BinaryTree#BinaryTree() BinaryTree()};
 *      // ...tree is grown
 *      {@link java.util.Iterator Iterator} ti = t.{@link io.datalayer.data.structure.BinaryTree#inorderIterator() inorderIterator()};
 *      while (ti.{@link #hasNext() hasNext()})
 *      {
 *          System.out.println(ti.{@link #next() next()});
 *      }
 *      ti.{@link #reset() reset()};
 *      while (ti.{@link #hasNext() hasNext()})
 *      { .... }
 * </pre>
 *
 * @version $Id: BTInorderIterator.java 35 2007-08-09 20:38:38Z bailey $
 * @author, 2001 duane a. bailey
 */
class BTInorderIterator<E> extends AbstractIterator<E>
{
    /**
     * The root of the subtree being traversed.
     */
    protected BinaryTree<E> root;    // root of subtree to be traversed

    /** 
     * Stack of nodes that maintain the state of the iterator.
     */
    protected Stack<BinaryTree<E>> todo; // stack of unvisited ancestors

    /**
     * Construct a new inorder iterator of a tree.
     *
     * @post Constructs an iterator to traverse inorder
     * 
     * @param root The root of the subtree to be traversed.
     */
    public BTInorderIterator(BinaryTree<E> root)
    {
        todo = new StackList<BinaryTree<E>>();
        this.root = root;
        reset();
    }   

    /**
     * Reset the iterator to its initial state.
     *
     * @post Resets the iterator to retraverse
     */
    public void reset()
    {
        todo.clear();
        // stack is empty.  Push on nodes from root to
        // leftmost descendant
        BinaryTree<E> current = root;
        while (!current.isEmpty()) {
            todo.push(current);
            current = current.left();
        }
    }

    /**
     * Returns true iff the iterator has more nodes to be considered.
     *
     * @post Returns true iff iterator is not finished
     * 
     * @return True iff more nodes are to be considered.
     */
    public boolean hasNext()
    {
        return !todo.isEmpty();
    }

    /**
     * Return the node currently being considered.
     *
     * @pre hasNext()
     * @post Returns reference to current value
     * 
     * @return The node currently under consideration.
     */
    public E get()
    {   
        return todo.get().value();
    }

    /**
     * Return current node, and increment iterator.
     *
     * @pre hasNext()
     * @post Returns current value, increments iterator
     * @return The value of the current node, before iterator iterated.
     */
    public E next()
    {
        BinaryTree<E> old = todo.pop();
        E result = old.value();
        // we know this node has no unconsidered left children;
        // if this node has a right child, 
        //   we push the right child and its leftmost descendants:
        // else 
        //   top element of stack is next node to be visited
        if (!old.right().isEmpty()) {
            BinaryTree<E> current = old.right();
            do {
                todo.push(current);
                current = current.left();
            } while (!current.isEmpty());
        }
        return result;
    }
}
