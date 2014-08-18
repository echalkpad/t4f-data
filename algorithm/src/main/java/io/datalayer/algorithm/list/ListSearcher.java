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
package io.datalayer.algorithm.list;

import io.datalayer.data.list.List;


/**
 * A generic interface for searching a {@link List}.
 *
 */
public interface ListSearcher {
    /**
     * Searches a list for a specified key.
     *
     * @param list The to search.
     * @param value The value for which to search.
     * @return The position (0, 1, 2...) of the value if found; otherwise <code>-(insertion point + 1)</code>.
     */
    public int search(List list, Object value);
}
