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
// Elements used in implementation of doubly linked lists.
// (c) 1998, 2001 duane a. bailey
package io.datalayer.data.structure;

/**
 * A private class implementing the element of a doubly linked list.
 * 
 * @version $Id: DoublyLinkedNode.java 31 2007-08-06 17:19:56Z bailey $
 * @author, 2001 duane a. bailey
 * @see io.datalayer.data.structure.DoublyLinkedList
 */
public class DoublyLinkedNode
{
    /**
     * The actual value stored within element; provided by user.
     */
    protected Object data;
    /**
     * The reference of element following.
     */
    protected DoublyLinkedNode nextElement;
    /**
     * The reference to element preceding.
     */
    protected DoublyLinkedNode previousElement;

    /**
     * Construct a doubly linked list element.
     *
     * @param v The value associated with the element.
     * @param next The reference to the next element.
     * @param previous The reference to the previous element.
     */
    public DoublyLinkedNode(Object v,
                            DoublyLinkedNode next,
                            DoublyLinkedNode previous)
    {
        data = v;
        nextElement = next;
        if (nextElement != null)
            nextElement.previousElement = this;
        previousElement = previous;
        if (previousElement != null)
            previousElement.nextElement = this;
    }

    /**
     * Construct a doubly linked list element containing a value.
     * Not part of any list (references are null).
     *
     * @post constructs a single element
     * 
     * @param v The value referenced by this element.
     */
    public DoublyLinkedNode(Object v)
    {
        this(v,null,null);
    }

    /**
     * Access the reference to the next value.
     *
     * @post returns the element that follows this
     *
     * @return Reference to the next element of the list.
     */
    public DoublyLinkedNode next()
    {
        return nextElement;
    }

    /**
     * Get a reference to the previous element of the list.
     *
     * @post returns element that precedes this
     *
     * @return Reference to the previous element.
     */
    public DoublyLinkedNode previous()
    {
        return previousElement;
    }

    /**
     * Get value stored within the element.
     *
     * @post returns value stored here
     * 
     * @return The reference to the value stored.
     */
    public Object value()
    {
        return data;
    }

    /**
     * Set reference to the next element.
     *
     * @post sets value associated with this element
     * 
     * @param next The reference to the new next element.
     */
    public void setNext(DoublyLinkedNode next)
    {
        nextElement = next;
    }

    /**
     * Set the reference to the previous element.
     *
     * @post establishes a new reference to a previous value
     * 
     * @param previous The new previous element.
     */
    public void setPrevious(DoublyLinkedNode previous)
    {
        previousElement = previous;
    }

    /**
     * Set the value of the element.
     *
     * @post sets a new value for this object
     * 
     * @param value The new value associated with the element.
     */
    public void setValue(Object value)
    {
        data = value;
    }

    /**
     * Determine if this element equal to another.
     *
     * @post returns true if this object and other are equal
     * 
     * @param other The other doubly linked list element.
     * @return True iff the values within elements are the same.
     */
    public boolean equals(Object other)
    {
        DoublyLinkedNode that = (DoublyLinkedNode)other;
        if (that == null) return false;
        if (that.value() == null || value() == null)
        {
            return value() == that.value();
        } else {
            return value().equals(that.value());
        }
    }

    /**
     * Generate hash code associated with the element.
     *
     * @post generates hash code for element
     * 
     * @return The hash code associated with the value in element.
     */
    public int hashCode()
    {
        if (value() == null) return super.hashCode();
        else return value().hashCode();
    }

    /**
     * Construct a string representation of the element.
     *
     * @post returns string representation of element
     * 
     * @return The string representing element.
     */
    public String toString()
    {
        return "<DoublyLinkedNode: "+value()+">";
    }
}       

