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
import java.util.ListIterator;

/**
 * Base class for the implementation of a list Iterator. 
 * The methods provided in this class have no executable bodies and will throw
 * errors if the user attempts to invoke them.  
 */
public abstract class AbstractListIterator<E> extends AbstractIterator<E> implements java.util.ListIterator<E>
{
    /**
     * Default constructor (for base class invocation).
     * Does nothing.  
     * Remind Sun (<a href="mailto:jdk-comments@java.sun.com">jdk-comments@java.sun.com</a>) that automatically implemented default
     * constructors are a silly thing.
     *
     * @post does nothing
     */
    public AbstractListIterator()
    {
    }

    public abstract E get();

    public void remove()
    {
        Assert.fail("remove not implemented.");
    }

    public void set(E o)
    {
        Assert.fail("set not implemented.");
    }

    public void add(E o)
    {
        Assert.fail("set not implemented.");
    }
}
