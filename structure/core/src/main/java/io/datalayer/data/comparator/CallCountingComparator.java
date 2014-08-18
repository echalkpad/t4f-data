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
 * A {@link Comparator} that count the calls made to {@link #compare(Object, Object)}.
 *
 */
public final class CallCountingComparator implements Comparator {
    /** The comparator to delegate to while counting calls. */
    private final Comparator _comparator;

    /** The number of calls made to this objects {@link #compare(Object, Object)} method. */
    private int _callCount;

    /**
     * Constructor.
     * @param comparator the comparator to delegate to while counting calls.
     */
    public CallCountingComparator(Comparator comparator) {
        assert comparator != null : "comparator cannot be null";

        _comparator = comparator;
        _callCount = 0;
    }

    /**
     * Delegates to the {@link Comparator} being wrapped.
     * @param left the left operand.
     * @param right the right operand.
     * @return the result from the wrapped object.
     */
    public int compare(Object left, Object right) throws ClassCastException {
        ++_callCount;
        return _comparator.compare(left, right);
    }

    /**
     * @return the number of calls made to this objects {@link #compare(Object, Object)} method.
     */
    public int getCallCount() {
        return _callCount;
    }
}
