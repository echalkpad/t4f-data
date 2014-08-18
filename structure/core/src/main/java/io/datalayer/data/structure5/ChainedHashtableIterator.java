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
package io.datalayer.data.structure5;
import java.util.Iterator;

/**
 * A traversal of all the elements as they appear in a chained hashtable.
 * No order is guaranteed.  This iterator is not publically accessable
 * and is used to implement ChainedHashtable's key and value iterators.
 * This iteration returns objects that are instances of {@link Association}.
 * <P>
 * Typical use:
 * <P>
 * <pre>
 *      ChainedHashtable h = new ChainedHashtable();
 *      // ...hashtable gets built up...
 *      Iterator hi = new {@link #ChainedHashtableIterator(List[]) ChainedHashtableIterator(h.data)};
 *      while (hi.{@link #hasNext() hasNext()})
 *      {
 *          System.out.println(ai.{@link #next() next()});
 *      }
 * </pre> 
 *
 * @version $Id: ChainedHashtableIterator.java 22 2006-08-21 19:27:26Z bailey $
 * @author, 2001 duane a. bailey
 */
class ChainedHashtableIterator<K,V> extends AbstractIterator<Association<K,V>>
{
    /**
     * The list of values within the table.
     */
    protected List<Association<K,V>> data;
    /**
     * The iterator over the elements of the list.
     */
    protected AbstractIterator<Association<K,V>> elements;

    /**
     * Construct an iterator over a chained hashtable.
     *
     * @post constructs a new hash table iterator
     * @param table The array of lists to be traversed.
     */
    public ChainedHashtableIterator(Vector<List<Association<K,V>>> table)
    {
        int i;
        int capacity = table.size();
        data = new SinglyLinkedList<Association<K,V>>();
        for (i = 0; i < capacity; i++) {
            if (table.get(i) != null) {
                List<Association<K,V>> entry = table.get(i);
                for (Association<K,V> a : entry)
                {
                    data.addFirst(a);
                }
            }
        }
        elements = (AbstractIterator<Association<K,V>>)data.iterator();
    }

    /**
     * Resets the iterator to point to the beginning of the chained table.
     *
     * @post resets iterator to beginning of hash table
     */
    public void reset()
    {
        elements.reset();
    }

    /**
     * Returns true iff there are unconsidered elements within the table.
     *
     * @post returns true if there are unvisited elements
     * 
     * @return True iff there are elements yet to be considered within table.
     */
    public boolean hasNext()
    {
        return elements.hasNext();
    }

    /**
     * Returns current value and increments iterator.
     *
     * @pre hasNext()
     * @post returns current element, increments iterator
     * 
     * @return The current value, before incrementing.
     */
    public Association<K,V> next()
    {
        return elements.next();
    }

    /**
     * Get current value of iterator.
     *
     * @post returns current element
     * 
     * @return The current value.
     */
    public Association<K,V> get()
    {
        return elements.get();
    }
}
