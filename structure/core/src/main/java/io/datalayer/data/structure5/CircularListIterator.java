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
// A private iterator for circular lists.
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure5;
/**
 * An iterator for traversing the elements of a circular list.
 * The iterator traverses the list beginning at the head, and heads toward
 * tail.
 * <P>
 * Typical use:
 * <P>
 * <pre>
 *      List l = new CircularList();
 *      // ...list gets built up...
 *      Iterator li = l.iterator();
 *      while (li.hasNext())
 *      {
 *          System.out.println(li.get());
 *          li.next();
 *      }
 *      li.reset();
 *      while (li.hasNext())
 *      { .... }
 * </pre>
 * @version $Id: CircularListIterator.java 31 2007-08-06 17:19:56Z bailey $
 * @author, 2001 duane a. bailey
 */
class CircularListIterator<E> extends AbstractIterator<E>
{
    /**
     * The tail of the traversed list.
     */
    protected Node<E> tail;
    /**
     * The current value of the iterator.
     */
    protected Node<E> current;

    /**
     * Constructs an iterator over circular list whose tail is t
     *
     * @pre t is a reference to a circular list element
     * @post constructs an iterator for traversing circular list
     * 
     * @param t The tail of the list to be traversed.
     */
    public CircularListIterator(Node<E> t)
    {
        tail = t;
        reset();
    }

    /**
     * Resets iterator to consider the head of the list.
     *
     * @post rests iterator to point to head of list
     */
    public void reset()
    {
        if (tail == null) current = null;
        else current = tail.next();
    }

    /**
     * Determine if there are unconsidered elements.
     *
     * @post returns true if some elements not visited
     * 
     * @return True iff some element has not been considered.
     */
    public boolean hasNext()
    {
        return current != null;
    }

    /**
     * Return the current value and increment iterator.
     *
     * @pre hasNext()
     * @post returns current element, increments iterator
     * 
     * @return The current value before incrementing.
     */
    public E next()
    {
        E result = current.value();
        if (current == tail) current = null;
        else current = current.next();
        return result;
    }

    /**
     * Determine the current value of iterator.
     *
     * @pre hasNext()
     * @post returns current value
     * 
     * @return The current value of the iterator.
     */
    public E get()
    {
        return current.value();
    }
}
