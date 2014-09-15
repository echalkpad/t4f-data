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
package io.datalayer.text.string.search;

/**
 * Iterator based on that defined in Design Patterns (Gamma et. al).
 */
public interface AosIterator {
    /**
     * (Re)positions the iterator to the first item.
     *
     * @throws UnsupportedOperationException if not implemented.
     */
    public void first();

    /**
     * (Re)positions the iterator to the last item.
     *
     * @throws UnsupportedOperationException if not implemented.
     */
    public void last();

    /**
     * Checks to see if the iterator refers to an item.
     *
     * @return <code>true</code> if the end has been reached; otherwise <code>false</code>.
     */
    public boolean isDone();

    /**
     * Positions to the next item.
     *
     * @throws UnsupportedOperationException if not implemented.
     */
    public void next();

    /**
     * Positions to the previous item.
     *
     * @throws UnsupportedOperationException if not implemented.
     */
    public void previous();

    /**
     * Obtains the value of the current item.
     *
     * @return The value of the current item.
     * @throws IteratorOutOfBoundsException if there is no current item.
     */
    public Object current() throws IteratorOutOfBoundsException;
}
