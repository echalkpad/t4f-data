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
package io.datalayer.data.predicate;

import io.datalayer.data.iterable.AndPredicate;
import io.datalayer.data.predicate.Predicate;
import junit.framework.TestCase;

/**
 * Test cases for {@link AndPredicate}.
 *
 */
public class AndPredicateTest extends TestCase {
    public void testTrueAndTrueIsTrue() {
        Predicate predicate = new AndPredicate(FixedPredicate.TRUE, FixedPredicate.TRUE);

        assertTrue(predicate.evaluate(this));
    }

    public void testTrueAndFalseIsFalse() {
        Predicate predicate = new AndPredicate(FixedPredicate.TRUE, FixedPredicate.FALSE);

        assertFalse(predicate.evaluate(this));
    }

    public void testFalseAndTrueIsFalse() {
        Predicate predicate = new AndPredicate(FixedPredicate.FALSE, FixedPredicate.TRUE);

        assertFalse(predicate.evaluate(this));
    }

    public void testFalseAndFalseIsFalse() {
        Predicate predicate = new AndPredicate(FixedPredicate.FALSE, FixedPredicate.FALSE);

        assertFalse(predicate.evaluate(this));
    }

    private static final class FixedPredicate implements Predicate {
        public static final Predicate TRUE = new FixedPredicate(true);
        public static final Predicate FALSE = new FixedPredicate(false);

        private final boolean _result;

        private FixedPredicate(boolean result) {
            _result = result;
        }

        public boolean evaluate(Object object) {
            return _result;
        }
    }
}
