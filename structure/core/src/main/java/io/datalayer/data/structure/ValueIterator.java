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
// Implementation of value- iterators for driving Association iterators.
// (c) 1998, 2001 duane a. bailey
package io.datalayer.data.structure;
import java.util.Iterator;
/**
 * A private master iterator for filtering the value fields from
 * an Association-returning iterator.
 * Used to construct iterators over dictionaries.
 *
 * @version $Id: ValueIterator.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 */
class ValueIterator extends AbstractIterator
{
    /**
     * The underlying iterator.
     * The slave iterator provides the value iterator values which
     * are Associations.  The value iterator returns only the value-portion
     * of the Associations.     
     */
    protected Iterator slave;

    /**
     * Construct a new value iterator that filters the slave iterator,
     * an Association-returning iterator.
     *
     * @pre slave is an iterator returning Association elements
     * @post creates a new iterator returning associated values
     * 
     * @param slave The slave iterator.
     */
    public ValueIterator(Iterator slave)
    {
        this.slave = slave;
    }

    /**
     * Resets the slave iterator (and thus the value iterator) to the
     * first association in the structure.
     *
     * @post resets iterator to point to first value
     */
    public void reset()
    {
        ((AbstractIterator)slave).reset();
    }

    /**
     * Returns true if an association is available for generating a value.
     *
     * @post returns true if current element is valid
     * 
     * @return True if a valid value can be generated.
     */
    public boolean hasNext()
    {
        return slave.hasNext();
    }

    /**
     * Returns the current value, and increments the iterator.  
     *
     * @pre hasNext()
     * @post returns current value and increments iterator
     * 
     * @return The current value, before iterator is incremented.
     */
    public Object next()
    {
        Association pair = (Association)((AbstractIterator)slave).next();
        return pair.getValue();
    }

    /**
     * Returns the current value from the slave iterator.
     *
     * @pre current value is valid
     * @post returns current value
     * 
     * @return The current value associated with the iterator.
     */
    public Object get()
    {
        Association pair = (Association)((AbstractIterator)slave).get();
        return pair.getValue();
    }
}
