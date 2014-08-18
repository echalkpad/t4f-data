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
package io.datalayer.data.iterable;

import io.datalayer.data.predicate.Predicate;

/**
 * A {@link Predicate} that performs a boolean AND (&&) of two other predicates.
 *
 */
public final class AndPredicate implements Predicate {
    /** The left-hand argument. */
    private final Predicate _left;

    /** The right-hand argument. */
    private final Predicate _right;

    /**
     * Constructor.
     *
     * @param left The left-hand argument.
     * @param right The righ-hand argument.
     */
    public AndPredicate(Predicate left, Predicate right) {
        assert left != null : "left can't be null";
        assert right != null : "right can't be null";

        _left = left;
        _right = right;
    }

    public boolean evaluate(Object object) {
        return _left.evaluate(object) && _right.evaluate(object);
    }
}
