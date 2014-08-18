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
package io.datalayer.algorithm.bsearch;

import io.datalayer.algorithm.list.ListSearcher;
import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.list.List;

/**
 * A {@link ListSearcher} that uses recursion.
 *
 */
public class RecursiveBinaryListSearcher implements ListSearcher {
    /** The strategy to use for key comparison. */
    private final Comparator _comparator;

    /**
     * Constructor.
     *
     * @param comparator The strategy to use for value comparison.
     */
    public RecursiveBinaryListSearcher(Comparator comparator) {
        assert comparator != null : "comparator can't be null";
        _comparator = comparator;
    }

    public int search(List list, Object value) {
        assert list != null : "list can't be null";

        return search(list, value, 0, list.size() - 1);
    }

    private int search(List list, Object value, int lowerIndex, int upperIndex) {
        assert list != null : "list can't be null";

        if (lowerIndex > upperIndex) {
            return -(lowerIndex + 1);
        }

        int index = lowerIndex + (upperIndex - lowerIndex) / 2;

        int cmp = _comparator.compare(value, list.get(index));

        if (cmp < 0) {
            index = search(list, value, lowerIndex, index - 1);
        } else if (cmp > 0) {
            index = search(list, value, index + 1, upperIndex);
        }

        return index;
    }
}
