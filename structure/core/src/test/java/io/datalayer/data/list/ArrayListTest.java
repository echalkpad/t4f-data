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
package io.datalayer.data.list;

import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;

/**
 * Concrete tests for {@link ArrayList}.
 *
 */
public class ArrayListTest extends AbstractListTestCase {
    protected List createList() {
        return new ArrayList();
    }

    public void testResizeBeyondInitialCapacity() {
        List list = new ArrayList(1);

        list.add(VALUE_A);
        list.add(VALUE_A);
        list.add(VALUE_A);

        assertEquals(3, list.size());

        assertSame(VALUE_A, list.get(0));
        assertSame(VALUE_A, list.get(1));
        assertSame(VALUE_A, list.get(2));
    }

    public void testDeleteFromFirstElementOfListAtFullCapacity() {
        List list = new ArrayList(3);

        list.add(VALUE_A);
        list.add(VALUE_B);
        list.add(VALUE_C);

        list.delete(0);
    }

    public void testDeleteFromLastElementInArray() {
        List list = new ArrayList(1);

        list.add(VALUE_A);

        list.delete(0);
    }
}
