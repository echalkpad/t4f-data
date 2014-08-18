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
 * A generice interface for comparing two object for order.
 *
 */
public interface Comparator {
    /**
     * Compares two objects for order.
     *
     * @param left The object on the 'left' of the comparison.
     * @param right The object on the 'right' of the comparison.
     * @return &lt;0, 0, &gt;0 if left&lt;right, left == right or left&gt;right respectively.
     * @throws ClassCastException if the type of either object prevents them from being compared.
     */
    public int compare(Object left, Object right) throws ClassCastException;
}
