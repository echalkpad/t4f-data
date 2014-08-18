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
package io.datalayer.data.structure;
import java.util.Comparator;
import java.util.Iterator;

public class MapBST implements OrderedMap
{
    protected BinarySearchTree data;

    public MapBST(Comparator ordering)
    {
        data = new BinarySearchTree(ordering);
    }

    public MapBST()
    {
        data = new BinarySearchTree();
    }

    /**
     * @post returns the number of entries in the map
     */
    public int size()
    {
        return data.size();
    }

    /**
     * @post returns true iff this map does not contains any entries
     */
    public boolean isEmpty()
    {
        return data.isEmpty();
    }

    /**
     * @pre k is non-null
     * @post returns true iff k is a key that is mapped to a value;
     *  that is, k is in the domain of the map
     */
    public boolean containsKey(Object k)
    {
        ComparableAssociation a =
            new ComparableAssociation((Comparable)k,null);
        return data.contains(a);
    }

    /**
     * @pre v is non-null
     * @post returns true iff v is the target of at least one map entry;
     * that is, v is in the range of the map
     */
    public boolean containsValue(Object v)
    {
        Iterator i = new ValueIterator(data.iterator());
        while (i.hasNext())
        {
            Object value = i.next();
            if (value != null && 
                v.equals(value)) return true;
        }
        return false;
    }

    /**
     * @pre k is a key, possibly in the map
     * @post returns the value mapped to from k, or null
     */
    public Object get(Object k)
    {
        ComparableAssociation a =
            new ComparableAssociation((Comparable)k,null);
        ComparableAssociation result = (ComparableAssociation)data.get(a);
        return result.getValue();
    }

    /**
     * @pre k and v are non-null
     * @post inserts a mapping from k to v in the map
     */
    public Object put(Object k, Object v)
    {
        ComparableAssociation newValue =
            new ComparableAssociation((Comparable)k,v);
        ComparableAssociation oldValue =
            (ComparableAssociation)data.remove(newValue);
        data.add(newValue);
        if (oldValue == null) return null;
        else return oldValue.getValue();
    }
    
    /**
     * @pre k is non-null
     * @post removes any mapping from k to a value, from the mapping
     */
    public Object remove(Object k)
    {
        ComparableAssociation result = 
            ((ComparableAssociation)data.remove(
                       new ComparableAssociation((Comparable)k,null)));
        if (result == null) return null;
        else return result.getValue();
    }

    /**
     * @pre other is non-null
     * @post all the mappings of other are installed in this map,
     * overriding any conflicting maps
     */
    public void putAll(Map other)
    {
        Iterator i = other.entrySet().iterator();
        while (i.hasNext())
        {
            ComparableAssociation e = (ComparableAssociation)i.next();
            put(e.getKey(),e.getValue());
        }
    }

    /**
     * @post removes all map entries associated with this map
     */
    public void clear()
    {
        data.clear();
    }

    /**
     * @post returns a set of all keys associated with this map
     */
    public Set keySet()
    {
        Set result = new SetList();
        Iterator i = data.iterator();
        while (i.hasNext())
        {
            ComparableAssociation a = (ComparableAssociation)i.next();
            result.add(a.getKey());
        }
        return result;
    }

    /**
     * @post returns a structure that contains the range of the map
     */
    public Structure values()
    {
        Structure result = new SinglyLinkedList();
        Iterator i = new ValueIterator(data.iterator());
        while (i.hasNext())
        {
            result.add(i.next());
        }
        return result;
    }

    /**
     * @post returns a set of (key-value) pairs, generated from this map
     */
    public Set entrySet()
    {
        Set result = new SetList();
        Iterator i = data.iterator();
        while (i.hasNext())
        {
            ComparableAssociation a = (ComparableAssociation)i.next();
            result.add(a);
        }
        return result;
    }

    /**
     * @pre other is non-null
     * @post returns true iff maps this and other are entry-wise equal
     */
    public boolean equals(Object other)
    {
        MapList that = (MapList)other;
        return data.equals(that.data);
    }
    
    /**
     * @post returns a hash code associated with this structure
     */
    public int hashCode()
    {
        return data.hashCode();
    }
}
