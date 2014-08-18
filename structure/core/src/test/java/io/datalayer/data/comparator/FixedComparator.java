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

import io.datalayer.data.comparator.Comparator;

/**
 * A {@link Comparator} that always returns the same result irrespective of the arguments.
 * 
 */
public class FixedComparator implements Comparator {
    private final int _result;

    /**
     * Constructor.
     * @param result The result ro return from {@link #compare(Object, Object)}.
     */
    public FixedComparator(int result) {
        _result = result;
    }

    public int compare(Object left, Object right) throws ClassCastException {
        return _result;
    }
}
