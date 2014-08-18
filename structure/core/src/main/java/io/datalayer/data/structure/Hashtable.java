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
// An implementation of Dictionaries, using hash tables. 
// Keys need not be comparable, but they must have hashcode methods.
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure;
import java.util.Iterator;
import java.lang.Math;
/**
 * Implements a dictionary as a table of hashed key-value pairs.
 * Collisions are resolved through linear probing.  Values used
 * as keys in this structure must have a hashcode method that returns
 * the same value when two keys are "equals".  Initially, a table of suggested
 * size is allocated.  It will be expanded as the load factor (ratio of
 * pairs to entries) grows.
 *
 * @version $Id: Hashtable.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 * @see ChainedHashtable
 */
public class Hashtable implements Map
{
    /**
     * A single key-value pair to be used as a token
     * indicating a reserved location in the hashtable.
     * Reserved locations are available for insertion,
     * but cause collisions on lookup.
     */
    protected static Association reserved =
                new Association("reserved",null);
    /**
     * The data associated with the hashtable.
     */
    protected Association data[];
    /**
     * The number of key-value pairs in table.
     */
    protected int count;
    /**
     * The size of the hashtable
     */
    protected int capacity;
    /**
     * Load factor that causes rehashing of the table.
     */
    protected final double loadFactor = 0.6;

    /**
     * Construct a hash table that is capable of holding at least
     * initialCapacity values.  If that value is approached, it will
     * be expanded appropriately.  It is probably best if the capacity
     * is prime.  Table is initially empty.
     *
     * @pre initialCapacity > 0
     * @post constructs a new Hashtable
     *       holding initialCapacity elements
     * 
     * @param initialCapacity The initial capacity of the hash table.
     */
    public Hashtable(int initialCapacity)
    {
        data = new Association[initialCapacity];
        capacity = initialCapacity;
        count = 0;
    }

    /**
     * Construct a hash table that is initially empty.
     *
     * @post constructs a new Hashtable
     */
    public Hashtable()
    {
        this(997);
    }

    /**
     * Remove all key-value pairs from hashtable.
     *
     * @post removes all elements from Hashtable
     */
    public void clear()
    {
        int i;
        for (i = 0; i < capacity; i++) {
            data[i] = null;
        }
        count = 0;
    }

    /**
     * Return the number of key-value pairs within the table.
     *
     * @post returns number of elements in hash table
     * 
     * @return The number of key-value pairs currently in table.
     */
    public int size()
    {
        return count;
    }

    /**
     * Determine if table is empty.
     *
     * @post returns true iff hash table has 0 elements
     * 
     * @return True if table is empty.
     */
    public boolean isEmpty()
    {
        return size() == 0;
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
        Iterator i = iterator();
        while (i.hasNext())
        {
            Object nextValue = i.next();
            // the value we seek?
            if (nextValue != null &&
                nextValue.equals(value)) return true; // yes!
        }
        // no value found
        return false;
    }

    /**
     * Returns true iff a specific key appears within the table.
     *
     * @pre key is a non-null Object
     * @post returns true if key appears in hash table
     * 
     * @param key The key sought.
     * @return True iff the key sought appears within table.
     */
    public boolean containsKey(Object key)
    {
        int hash = locate(key);
        return data[hash] != null && data[hash] != reserved;
    }   

    /**
     * Returns a traversal that traverses over the values of the
     * hashtable.
     *
     * @post returns traversal to traverse hash table
     * 
     * @return A value traversal, over the values of the table.
     */
    public Iterator iterator()
    {
        return new ValueIterator(new HashtableIterator(data));
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
        int hash = locate(key);
        Association a = data[hash];
        if (a == null || a == reserved) return null;
        return data[hash].getValue();
    }

    /**
     * Get a traversal over the keys of the hashtable.
     *
     * @post returns traversal to traverse the keys of hash table;
     * 
     * @return a traversal over the key values appearing within table.
     */
    public Iterator keys()
    {
        return new KeyIterator(new HashtableIterator(data));
    }

    protected int locate(Object key)
    {
        // compute an initial hash code
        int hash = Math.abs(key.hashCode() % capacity);
        // keep track of first unused slot, in case we need it
        int firstReserved = -1;
        while (data[hash] != null)
        {
            if (data[hash] == reserved) {
                // remember reserved slot if we fail to locate value
                if (firstReserved == -1) firstReserved = hash;
            } else  {
                // value located? return the index in table
                if (key.equals(data[hash].getKey())) return hash;
            }
            // linear probing; other methods would change this line:
            hash = (1+hash)%capacity;
        }
        // return first empty slot we encountered
        if (firstReserved == -1) return hash;
        else return firstReserved;
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
        if (loadFactor*capacity <= (1+count)) {
            extend();
        }
        int hash = locate(key);
        Association a = data[hash];
        if (a == null || a == reserved)
        {   // logically empty slot; just add association
            data[hash] = new Association(key,value);
            count++;
            return null;
        } else {
            // full slot; add new and return old value
            Object oldValue = a.getValue();
            a.setValue(value);
            return oldValue;
        }
    }
    /**
     * Put all of the values found in another map into this map,
     * overriding previous key-value associations.
     * @param other is the source mapping
     * @pre other map is valid
     * @post this hashtable is augmented by the values found in other
     */
    public void putAll(Map other)
    {
        Iterator i = other.values().iterator();
        while (i.hasNext())
        {
            Association e = (Association)i.next();
            put(e.getKey(),e.getValue());
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
        int hash = locate(key);
        Association a = data[hash];
        if (a == null || a == reserved) {
            return null;
        }
        count--;
        Object oldValue = a.getValue();
        data[hash] = reserved; // in case anyone depends on us
        return oldValue;
    }

    /**
     * @post expands the hashtable to reduce loading
     */
    protected void extend()
    {
        // extends the hashtable for larger capacity.
        int i;
        AbstractIterator it = new HashtableIterator(data);
        // BE AWARE: at this point, we can change the hash table,
        // but changes to the hashtable traversal implementation might
        // be problematic.
        capacity = capacity*2+1;
        data = new Association[capacity];
        count = 0;
        while (it.hasNext())
        {
            Association a = (Association)it.next();
            put(a.getKey(),a.getValue());
        }
    }

    /**
     * @post returns a set of Associations associated with this Map
     */
    public Set entrySet()
    {
        Set result = new SetList();
        Iterator i = new HashtableIterator(data);
        while (i.hasNext())
        {
            result.add(i.next());
        }
        return result;
    }

    /**
     * @post returns a Set of keys used in this Map
     */
    public Set keySet()
    {
        Set result = new SetList();
        Iterator i = new KeyIterator(new HashtableIterator(data));
        while (i.hasNext())
        {
            result.add(i.next());
        }
        return result;
    }

    /**
     * @post returns a Structure that contains the (possibly repeating) 
     * values of the range of this map.
     */
    public Structure values()
    {
        List result = new SinglyLinkedList();
        Iterator i = new ValueIterator(new HashtableIterator(data));
        while (i.hasNext())
        {
            result.add(i.next());
        }
        return result;
    }


    /**
     * Generate a string representation of the hash table.
     *
     * @post returns a string representation of hash table
     * 
     * @return The string representing the table.
     */
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        int i;

        s.append("<Hashtable: size="+size()+" capacity="+capacity);
        Iterator hi = new HashtableIterator(data);
        while (hi.hasNext()) {
            Association a = (Association)hi.next();
            s.append(" key="+a.getKey()+", value="+a.getValue());
        }
        s.append(">");
        return s.toString();
    }
}

