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
package io.datalayer.data.comparator;

import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;

/**
 * A {@link Comparator} that combines other {@link Comparator}s to produce a compund result.
 *
 */
public class CompoundComparator implements Comparator {
    /** The underlying comparators. */
    private final List _comparators = new ArrayList();

    /**
     * Adds a comparator to the end of the collection.
     *
     * @param comparator The comparator to add.
     */
    public void addComparator(Comparator comparator) {
        assert comparator != null : "comparator can't be null";
        assert comparator != this : "can't add comparator to itself";

        _comparators.add(comparator);
    }

    public int compare(Object left, Object right) throws ClassCastException {
        int result = 0;
        AosIterator i = _comparators.iterator();

        for (i.first(); !i.isDone(); i.next()) {
            result = ((Comparator) i.current()).compare(left, right);
            if (result != 0) {
                break;
            }
        }

        return result;
    }
}
