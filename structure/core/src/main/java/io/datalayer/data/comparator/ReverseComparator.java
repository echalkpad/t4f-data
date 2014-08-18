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



/**
 * A {@link Comparator} that reverses the order of another, underlying, {@link Comparator}. Useful for sorting in
 * reverse order without needing to create two {@link Comparator}s for each type.
 *
 */
public class ReverseComparator implements Comparator {
    /** The underlying comparator. */
    private final Comparator _comparator;

    /**
     * Constructor.
     *
     * @param comparator The underlying comparator.
     */
    public ReverseComparator(Comparator comparator) {
        assert comparator != null : "comparator can't be null";
        _comparator = comparator;
    }

    public int compare(Object left, Object right) throws ClassCastException {
        return _comparator.compare(right, left);
    }
}
