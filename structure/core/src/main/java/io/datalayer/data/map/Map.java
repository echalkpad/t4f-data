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
package io.datalayer.data.map;

import io.datalayer.data.iterable.Iterable;

/**
 * A generic interface for maps.
 *
 */
public interface Map extends Iterable {
    /**
     * Obtains the value for a given key.
     *
     * @param key The key for which the value should be obtained.
     * @return The value; or <code>null</code> if none.
     */
    public Object get(Object key);

    /**
     * Adds/replaces a key/value pair in the map.
     *
     * @param key The key to set.
     * @param value The value to set.
     * @return The old value; or <code>null</code> if none.
     */
    public Object set(Object key, Object value);

    /**
     * Removes a key/value pair from the map.
     *
     * @param key The key to delete.
     * @return The associated value; or <code>null</code> if the key didn't exist.
     */
    public Object delete(Object key);

    /**
     * Determines if a key exists in the map.
     *
     * @param key The key for which to search.
     * @return <code>true</code> if found; otherwise <code>false</code>.
     */
    public boolean contains(Object key);

    /**
     * Deletes all entries from the map. The size of the map will be reset to zero (0).
     */
    public void clear();

    /**
     * Obtains the size of the map.
     *
     * @return The number of values in the map.
     */
    public int size();

    /**
     * Determines if the map is empty or not.
     *
     * @return <code>true</code> if the map is empty (<code>size() == 0</code>); otherwise returns <code>false</code>.
     */
    public boolean isEmpty();

    /**
     * A key/value pair.
     */
    public static interface Entry {
        /**
         * Obtains the key.
         *
         * @return The key.
         */
        public Object getKey();

        /**
         * Obtains the value.
         *
         * @return The value.
         */
        public Object getValue();
    }
}
