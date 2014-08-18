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
// An implementation of an OrderedDictionary.
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * An implementation of an ordered dictionary.  Key-value pairs are 
 * kept in the structure in order.  To accomplish this, the keys of the
 * table must be comparable.
 * 
 * @version $Id: Table.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 * @see Comparable
 */
public class Table extends AbstractMap implements OrderedMap
{
    /**
     * An ordered structure that maintains the ComparableAssociations
     * that store the key-value pairings.
     */
    protected OrderedStructure data;

    /**
     * Construct a new, empty table.
     *
     * @post constructs a new table
     */
    public Table()
    {
        data = new SplayTree();
    }

    public Table(Table other)
    {
        data = new SplayTree();
        Iterator i = other.entrySet().iterator();
        while (i.hasNext())
        {
            java.util.Map.Entry o = (java.util.Map.Entry)i.next();
            put(o.getKey(),o.getValue());
        }
    }

    /**
     * Retrieve the value associated with the key provided.
     * Be aware, the value may be null.
     *
     * @pre key is a non-null object
     * @post returns value associated with key, or null
     * 
     * @param key The key of the key-value pair sought.
     * @return The value associated with the key.
     */
    public Object get(Object key)
    {
        ComparableAssociation ca =
            new ComparableAssociation((Comparable)key,null);
        ComparableAssociation result =
            ((ComparableAssociation)data.remove(ca));
        if (result == null) return null;
        data.add(result);
        return result.getValue();
    }

    /**
     * Enter a key-value pair into the table.  if the key is already
     * in the table, the old value is returned, and the old key-value
     * pair is replaced.  Otherwise null is returned.  The user is cautioned
     * that a null value returned may indicate there was no prior key-value
     * pair, or --- if null values are inserted --- that the key was 
     * previously associated with a null value.
     *
     * @pre key is non-null object
     * @post key-value pair is added to table
     * 
     * @param key The unique key in the table.
     * @param value The (possibly null) value associated with key.
     * @return The prior value, or null if no prior value found.
     */
    public Object put(Object key, Object value)
    {
        ComparableAssociation ca = 
            new ComparableAssociation((Comparable)key,value);
        // fetch old key-value pair
        ComparableAssociation old =
            (ComparableAssociation)data.remove(ca);
        // insert new key-value pair
        data.add(ca);
        // return old value
        if (old == null) return null;
        else return old.getValue();
    }
    
    /**
     * Determine if the table is empty.
     *
     * @post returns true iff table is empty
     * 
     * @return True iff the table has no elements.
     */
    public boolean isEmpty()
    {
        return data.isEmpty();
    }

    /**
     * Remove all the elements of the table.
     *
     * @post removes all elements from the table
     */
    public void clear()
    {
        data.clear();
    }

    /**
     * Construct an iterator over the keys of the table.
     * The order of the keys returned is in ascending order.  It will
     * be consistent with that of the iterator from elements, provided
     * the table is not modified.
     *
     * @post returns an iterator for traversing keys of table
     * 
     * @return An iterator over the keys of the table.
     */
    public Iterator keys()
    {
        return new KeyIterator(data.iterator());
    }

    /**
     * Construct an iterator over the values of the table.
     * The order of the values returned is determined by order of keys. It will
     * be consistent with that of the iterator returned from keys, provided
     * the table is not modified.
     *
     * @post returns an iterator for traversing values in table
     * 
     * @return An iterator over the values of the table.
     */
    public Iterator iterator()
    {
        return new ValueIterator(data.iterator());
    }

    /**
     * Determine if the key is in the table.  The key should
     * not be null.
     *
     * @pre key is non-null object
     * @post returns true iff key indexes a value in table
     * 
     * @param key A non-null key sought in the table.
     * @return True iff the key is used in association with some value.
     */
    public boolean containsKey(Object key)
    {
        ComparableAssociation a =
            new ComparableAssociation((Comparable)key,null);
        return data.contains(a);
    }

    /**
     * Returns true if the value is associated with some key in the
     * table.  This is often difficult to implement efficiently.
     *
     * @pre value is non-null object
     * @post returns true iff value in table
     * 
     * @param value The value sought (possibly null).
     * @return True, if the value is associated with some key in table.
     */
    public boolean containsValue(Object value)
    {
        Iterator i = iterator();
        while (i.hasNext())
        {
            Object nextValue = i.next();
            if (nextValue != null &&
                nextValue.equals(value)) return true;
        }
        return false;
    }

    
    /**
     * Remove a key-value pair, based on key.  The value is returned.
     *
     * @pre key is non-null object
     * @post removes value indexed in table
     * 
     * @param key The key of the key-value pair to be removed.
     * @return The value associated with key, no longer in table.
     */
    public Object remove(Object key)
    {
        ComparableAssociation target = 
            new ComparableAssociation((Comparable)key,null);
        target = (ComparableAssociation)data.remove(target);
        if (target == null) return null;
        else return target.getValue();
    }

    /**
     * Determine the number of key-value pairs within the table.
     *
     * @post returns number of key-value pairs in table
     * 
     * @return The number of key-value pairs in the table.
     */
    public int size()
    {
        return data.size();
    }

    public Set keySet()
    {
        Set result = new SetList();
        Iterator i = new KeyIterator(data.iterator());
        while (i.hasNext())
        {
            result.add(i.next());
        }
        return result;
    }

    public Structure values()
    {
        List result = new SinglyLinkedList();
        Iterator i = new ValueIterator(data.iterator());
        while (i.hasNext())
        {
            result.add(i.next());
        }
        return result;
    }

    public Set entrySet()
    {
        Set result = new SetList();
        Iterator i = data.iterator();
        while (i.hasNext())
        {
            result.add(i.next());
        }
        return result;
    }
    


    /**
     * Construct a string representing value of table.
     *
     * @post returns string representation
     * 
     * @return String representing table.
     */
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        s.append("<Table: size="+size());
        Iterator ti = data.iterator();
        while (ti.hasNext()) {
            ComparableAssociation ca = (ComparableAssociation)ti.next();
            s.append(" key="+ca.getKey()+", value="+ca.getValue());
        }
        s.append(">");
        return s.toString();
    }
}
