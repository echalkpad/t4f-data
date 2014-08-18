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
 * Uses a {@link ListSearcher} to insertion values into a {@link List} in sorted order.
 *
 */
public class ListInserter {
    /** The list searcher to use. */
    private final ListSearcher _searcher;

    /**
     * Constructor.
     *
     * @param searcher
     */
    public ListInserter(ListSearcher searcher) {
        assert searcher != null : "searcher can't be null";
        _searcher = searcher;
    }

    /**
     * Inserts a value into a list in sorted order.
     *
     * @param list The list into which the value will be inserted.
     * @param value The value to add.
     * @return Th eposition (0, 1, 2...) at which the value ws inserted.
     */
    public int insert(List list, Object value) {
        assert list != null : "list can't be null";

        int index = _searcher.search(list, value);

        if (index < 0) {
            index = -(index + 1);
        }

        list.insert(index, value);

        return index;
    }
}
