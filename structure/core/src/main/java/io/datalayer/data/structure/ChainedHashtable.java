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
// An implementation of hashtables, using external chaining
// Keys need not be comparable.
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure;
import java.util.Iterator;
import java.lang.Math;
/**
 * This class implements a hash table whose collisions are resolved
 * through external chaining.  Values used as keys in this structure
 * must have a hashcode method that returns the same value when two
 * keys are "equals".  Initially, a hash table of suggested size is
 * allocated.
 *
 * @version $Id: ChainedHashtable.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 * @see Hashtable
 */
public class ChainedHashtable extends AbstractMap implements Map
{
    /**
     * The array of chains used to store values.
     */
    protected List data[];
    /**
     * The number of key-value pairs stored within the table.
     */
    protected int count;
    /**
     * The length of the table.
     */
    protected int capacity;

    /**
     * Constructs a hashtable with capacity for at size elements
     * before chaining is absolutely required.
     *
     * @pre size > 0
     * @post constructs a new ChainedHashtable
     * 
     * @param size The number of entries initially allocated.
     */
    public ChainedHashtable(int size)
    {
        data = new List[size];
        capacity = size;
        count = 0;
    }

    /**
     * Constructs a reasonably large hashtable.
     *
     * @post constructs a new ChainedHashtable
     */
    public ChainedHashtable()
    {
        this(997);
    }

    /**
     * Removes the values from the hashtable.
     *
     * @post removes all the elements from the ChainedHashtable
     */
    public void clear()
    {
        int i;
        for (i = 0; i < capacity; i++) {
            data[i].clear();
        }
        count = 0;
    }

    /**
     * Computes the number of elements stored within the hashtable.
     *
     * @post returns number of elements in hash table
     * 
     * @return The number of elements within the hash table.
     */
    public int size()
    {
        return count;
    }

    /**
     * Returns true if no elements are stored within the table.
     *
     * @post returns true iff hash table has 0 elements
     * 
     * @return True iff size() == 0.
     */
    public boolean isEmpty()
    {
        return size() == 0;
    }

    protected List locate(Object key)
    {
        int hash = Math.abs(key.hashCode() % capacity);
        if (data[hash] == null) data[hash] = new SinglyLinkedList();
        return data[hash];
    }

    /**
     * Returns true if a specific value appears within the table.
     *
     * @pre value is non-null Object
     * @post returns true iff hash table contains value
     * 
     * @param value The value sought.
     * @return True iff the value appears within the table.
     */
    public boolean containsValue(Object value)
    {
        Iterator elements = iterator();

        while (elements.hasNext())
        {
            if (value.equals(elements.next())) return true;
        }
        return false;
    }

    /**
     * Returns true iff a specific key appears within the table.
     *
     * @pre value is non-null key
     * @post returns true if key appears in hash table
     * 
     * @param key The key sought.
     * @return True iff the key sought appears within table.
     */
    public boolean containsKey(Object key)
    {
        List l = locate(key);
        return l.contains(new Association(key,null));
    }

    /**
     * Returns an iterator that traverses over the values of the
     * hashtable.
     *
     * @post returns iterator to traverse hash table
     * 
     * @return A value iterator, over the values of the table.
     */
    public Iterator iterator()
    {
        return new ValueIterator(new ChainedHashtableIterator(data));
    }

    public Set keySet()
    {
        Set result = new SetList();
        Iterator i = new KeyIterator(new ChainedHashtableIterator(data));
        while (i.hasNext())
        {
            result.add(i.next());
        }
        return result;
    }

    public Set entrySet()
    {
        Set result = new SetList();
        Iterator i = new ChainedHashtableIterator(data);
        while (i.hasNext())
        {
            result.add(i.next());
        }
        return result;
    }

    public Structure values()
    {
        List result = new SinglyLinkedList();
        Iterator i = new ValueIterator(new ChainedHashtableIterator(data));
        while (i.hasNext())
        {
            result.add(i.next());
        }
        return result;
    }


    /**
     * Get the value associated with a key.
     *
     * @pre key is non-null Object
     * @post returns value associated with key, or null
     * 
     * @param key The key used to find the desired value.
     * @return The value associated with the desired key.
     */
    public Object get(Object key)
    {
        List l = locate(key);
        Association a = (Association)l.remove(new Association(key,null));
        if (a == null) return null;
        l.addFirst(a);
        return a.getValue();
    }

    /**
     * Get an iterator over the keys of the hashtable.
     *
     * @post returns iterator to traverse the keys of hash table
     * 
     * @return An iterator over the key values appearing within table.
     */
    public Iterator keys()
    {
        return new KeyIterator(new ChainedHashtableIterator(data));
    }

    /**
     * Place a key-value pair within the table.
     *
     * @pre key is non-null object
     * @post key-value pair is added to hash table
     * 
     * @param key The key to be added to table.
     * @param value The value associated with key.
     * @return The old value associated with key if previously present.
     */
    public Object put(Object key, Object value)
    {
        List l = locate(key);
        Association newa = new Association(key,value);
        Association olda = (Association)l.remove(newa);
        l.addFirst(newa);
        if (olda != null)
        {
            return olda.getValue();
        }
        else
        {
            count++;
            return null;
        }
    }

    /**
     * Remove a key-value pair from the table.
     *
     * @pre key is non-null object
     * @post removes key-value pair associated with key
     * 
     * @param key The key of the key-value pair to be removed.
     * @return The value associated with the removed key.
     */
    public Object remove(Object key)
    {
        List l = locate(key);
        Association pair = (Association)l.remove(new Association(key,null));
        if (pair == null) return null;
        count--;
        return pair.getValue();
    }

    /**
     * Generate a string representation of the chained hash table.
     *
     * @post returns a string representation of hash table
     * 
     * @return The string representing the table.
     */
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        int i;

        s.append("<ChainedHashtable:");
        Iterator hi = new ChainedHashtableIterator(data);
        while (hi.hasNext()) {
            Association a = (Association)hi.next();
            s.append(" "+a.getKey()+"="+a.getValue());
        }
        s.append(">");
        return s.toString();
    }
}
