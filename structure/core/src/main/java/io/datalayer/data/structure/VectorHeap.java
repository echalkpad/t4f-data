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
// An implementation of a priority queue in a vector.
// (c) 1998, 2001, 2002 duane a. bailey
package io.datalayer.data.structure;

/**
 * This class implements a priority queue based on a traditional
 * array-based heap.
 *
 * @version $Id: VectorHeap.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 */
public class VectorHeap implements PriorityQueue
{
    /**
     * The data, kept in heap order.
     */
    protected Vector data;  // the data, kept in heap order

    /**
     * Construct a new priority queue
     *
     * @post constructs a new priority queue
     */
    public VectorHeap()
    {
        data = new Vector();
    }

    /**
     * Construct a new priority queue from an unordered vector
     *
     * @post constructs a new priority queue from an unordered vector
     */
    public VectorHeap(Vector v)
    {
        int i;
        data = new Vector(v.size()); // we know ultimate size
        for (i = 0; i < v.size(); i++)
        {   // add elements to heap
            add((Comparable)v.get(i));
        }
    }

    /**
     * Returns parent index
     * @param i a node index
     * @return parent of node at i
     * @pre 0 <= i < size
     * @post returns parent of node at location i
     */
    protected static int parent(int i)
    {
        return (i-1)/2;
    }

    /**
     * Returns left child index.
     * @param i a node index
     * @return left child of node at i
     * @pre 0 <= i < size
     * @post returns index of left child of node at location i
     */
    protected static int left(int i)
    {
        return 2*i+1;
    }

    /**
     * Returns right child index.
     * @param i a node index
     * @return right child of node at i
     * @pre 0 <= i < size
     * @post returns index of right child of node at location i
     */
    protected static int right(int i)
    {
        return 2*(i+1);
    }

    /**
     * Fetch lowest valued (highest priority) item from queue.
     *
     * @pre !isEmpty()
     * @post returns the minimum value in priority queue
     * 
     * @return The smallest value from queue.
     */
    public Comparable getFirst()
    {
        return (Comparable)data.get(0);
    }

    /**
     * Returns the minimum value from the queue.
     *
     * @pre !isEmpty()
     * @post returns and removes minimum value from queue
     * 
     * @return The minimum value in the queue.
     */
    public Comparable remove()
    {
        Comparable minVal = getFirst();
        data.set(0,data.get(data.size()-1));
        data.setSize(data.size()-1);
        if (data.size() > 1) pushDownRoot(0);
        return minVal;
    }

    /**
     * Add a value to the priority queue.
     *
     * @pre value is non-null comparable
     * @post value is added to priority queue
     * 
     * @param value The value to be added.
     */
    public void add(Comparable value)
    {
        data.add(value);
        percolateUp(data.size()-1);
    }

    /**
     * Determine if the queue is empty.
     *
     * @post returns true iff no elements are in queue
     * 
     * @return True if the queue is empty.
     */
    public boolean isEmpty()
    {
        return data.size() == 0;
    }

    /**
     * Moves node upward to appropriate position within heap.
     * @param leaf Index of the node in the heap.
     * @pre 0 <= leaf < size
     * @post moves node at index leaf up to appropriate position
     */
    protected void percolateUp(int leaf)
    {
        int parent = parent(leaf);
        Comparable value = (Comparable)(data.get(leaf));
        while (leaf > 0 &&
          (value.compareTo((Comparable)(data.get(parent))) < 0))
        {
            data.set(leaf,data.get(parent));
            leaf = parent;
            parent = parent(leaf);
        }
        data.set(leaf,value);
    }

    /**
     * Moves node downward, into appropriate position within subheap.
     * @param root Index of the root of the subheap.
     * @pre 0 <= root < size
     * @post moves node at index root down 
     *   to appropriate position in subtree
     */
    protected void pushDownRoot(int root)
    {
        int heapSize = data.size();
        Comparable value = (Comparable)data.get(root);
        while (root < heapSize) {
            int childpos = left(root);
            if (childpos < heapSize)
            {
                if ((right(root) < heapSize) &&
                  (((Comparable)(data.get(childpos+1))).compareTo
                   ((Comparable)(data.get(childpos))) < 0))
                {
                    childpos++;
                }
                // Assert: childpos indexes smaller of two children
                if (((Comparable)(data.get(childpos))).compareTo
                    (value) < 0)
                {
                    data.set(root,data.get(childpos));
                    root = childpos; // keep moving down
                } else { // found right location
                    data.set(root,value);
                    return;
                }
            } else { // at a leaf! insert and halt
                data.set(root,value);
                return;
            }       
        }
    }

    /**
     * Determine the size of the queue.
     *
     * @post returns number of elements within queue
     * 
     * @return The number of elements within the queue.
     */
    public int size()
    {
        return data.size();
    }

    /**
     * Remove all the elements from the queue.
     *
     * @post removes all elements from queue
     */
    public void clear()
    {
        data.clear();
    } 
    
    /**
     * Construct a string representation of the heap.
     *
     * @post returns string representation of heap
     * 
     * @return The string representing the heap.
     */
    public String toString()
    {
        return "<VectorHeap: "+data+">";
    }
}
