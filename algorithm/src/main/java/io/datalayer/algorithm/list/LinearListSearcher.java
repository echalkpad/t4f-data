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

import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.List;

/**
 * A {@link ListSearcher} that performs a linear scan.
 *
 */
public class LinearListSearcher implements ListSearcher {
    /** The strategy to use for key comparison. */
    private final Comparator _comparator;

    /**
     * Constructor.
     *
     * @param comparator The strategy to use for key comparison.
     */
    public LinearListSearcher(Comparator comparator) {
        assert comparator != null : "comparator can't be null";
        _comparator = comparator;
    }

    public int search(List list, Object value) {
        assert list != null : "list can't be null";

        int index = 0;
        AosIterator i = list.iterator();

        for (i.first(); !i.isDone(); i.next()) {
            int cmp = _comparator.compare(value, i.current());
            if (cmp == 0) {
                return index;
            } else if (cmp < 0) {
                break;
            }

            ++index;
        }

        return -(index + 1);
    }
}
