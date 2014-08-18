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
// Implementation of key- iterators for driving Association iterators.
// (c) 1998, 2001 duane a. bailey
package io.datalayer.data.structure5;
import java.util.Iterator;

/**
 * A private master iterator for filtering the key fields from
 * an Association-returning iterator. This iterator returns
 * objects of the {@link java.lang.Comparable} type, and is
 * publically available throught the {@link io.datalayer.data.structure.Hashtable#keys()} 
 * method.
 * <P>
 * Typical use:
 * <P>
 * <pre>
 *      Hashtable h = new Hashtable();
 *      // ...hashtable gets built up...
 *      Iterator hi = h.keys();
 *      while (hi.{@link #hasNext() hasNext()})
 *      {
 *          System.out.println(ai.{@link #next() next()});
 *      }
 * </pre>
 * 
 * @version $Id: KeyIterator.java 29 2006-11-03 16:56:32Z bailey $
 * @author, 2001 duane a. bailey
 */
class KeyIterator<K,V> extends AbstractIterator<K>
{
    /**
     * The underlying iterator.
     * The slave iterator provides the key iterator values which
     * are Associations.  The key iterator returns only the key-portion
     * of the Associations.     
     */
    protected Iterator<Association<K,V>> slave;

    /**
     * Construct a new key iterator that filters the slave iterator,
     * an Association-returning iterator.
     *
     * @pre slave is a fully reset iterator over Association elements
     * @post creates a new iterator that returns keys of slave iterator
     * 
     * @param slave The slave iterator.
     */
    @SuppressWarnings("unchecked")
    public <T extends Association<K,V>> KeyIterator(Iterator<T> slave)
    {
        this.slave = (AbstractIterator<Association<K,V>>)slave;
    }

    /**
     * Resets the slave iterator (and thus the key iterator) to the
     * first association in the structure.
     *
     * @post resets iterator to point to first key
     */
    public void reset()
    {
        ((AbstractIterator)slave).reset();
    }

    /**
     * Returns true if an association is available for generating a key.
     *
     * @post returns true if current element is valid
     * 
     * @return True if a valid key can be generated.
     */
    public boolean hasNext()
    {
        return slave.hasNext();
    }

    /**
     * Returns the current key, and increments the iterator.    
     *
     * @pre hasNext()
     * @post returns current value and increments iterator
     * 
     * @return The current key, before iterator is incremented.
     */
    public K next()
    {
        Association<K,V> pair = slave.next();
        return pair.getKey();
    }

    /**
     * Returns the current key from the slave iterator.
     *
     * @pre current value is valid
     * @post returns current value
     * 
     * @return The current key associated with the iterator.
     */
    public K get()
    {
        Association<K,V> pair = ((AbstractIterator<Association<K,V>>)slave).get();
        return pair.getKey();
    }
}
 
