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
// The interface for stacks.
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure5;

/**
 * An abstract structure implementing features common to all first-in, first-out
 * structures in this package.  
 * Queues are typically used to process values in the order that they appear 
 * and to store the state of buffered objects.
 * The structure package provides several implementations of the Queue interface, 
 * each of which has its particular strengths and weaknesses.
 * <P>
 * Example usage:
 * <P>
 * To compute the sum of the unicode value of every character in the standard input
 * we could use the following:
 * <P>
 * <pre>
 * public static void main(String... arguments)
 * {
 *     {@link AbstractQueue} q = new {@link QueueList#QueueList() QueueList()};
 *     int unicodeSum = 0;
 *
 *     if(arguments.length > 0){
 *         for(int i=0; i < arguments.length; i++){
 *             for(int j=0; j < arguments[i].length(); j++){
 *                 q.{@link #enqueue(Object) enqueue(new Character(arguments[i].charAt(j)))};
 *             }
 *         }
 *     }
 *
 *     while(!q.{@link #empty()}){
 *        char c = ((Character)q.{@link #dequeue()}).charValue();
 *        unicodeSum+=Character.getNumericValue(c);
 *     }
 *
 *     System.out.println("Total Value: " + unicodeSum);
 * }
 * </pre>
 * @see QueueArray
 * @see QueueVector
 * @see QueueList
 * @version $Id: AbstractQueue.java 22 2006-08-21 19:27:26Z bailey $
 * @author, 2001 duane a. bailey
 */
public abstract class AbstractQueue<E>
    extends AbstractLinear<E> implements Queue<E>
{
    /**
     * Add a value to the tail of the queue.
     *
     * @post the value is added to the tail of the structure
     * 
     * @param value The value added.
     */
    public void enqueue(E item)
    {
        add(item);
    }

    /**
     * Remove a value form the head of the queue.
     *
     * @pre the queue is not empty
     * @post the head of the queue is removed and returned
     * 
     * @return The value actually removed.
     */
    public E dequeue()
    {
        return remove();
    }

    /**
     * Fetch the value at the head of the queue.
     *
     * @pre the queue is not empty
     * @post the element at the head of the queue is returned
     * 
     * @return Reference to the first value of the queue.
     */
    public E getFirst()
    {
        return get();
    }

    /**
     * Fetch the value at the head of the queue.
     *
     * @pre the queue is not empty
     * @post the element at the head of the queue is returned
     * 
     * @return Reference to the first value of the queue.
     */
    public E peek()
    {
        return get();
    }
}
