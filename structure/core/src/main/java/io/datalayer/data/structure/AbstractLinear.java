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
abstract public class AbstractLinear extends AbstractStructure
    implements Linear
{
    /**
     * Determine if there are elements within the linear.
     *
     * @post return true iff the linear structure is empty
     * @return true if the linear structure is empty; false otherwise
     */
    public boolean empty()
    {
        return isEmpty();
    }

    /**
     * Removes value from the linear structure.
     * Not implemented (by default) for linear classes.
     *
     * @param value value matching the value to be removed
     * @pre value is non-null
     * @post value is removed from linear structure, if it was there
     * @return returns the value that was replaced, or null if none.
     */
    public Object remove(Object o)
    {
        Assert.fail("Method not implemented.");
        // never reaches this statement:
        return null;
    }
}
