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

import io.datalayer.data.iterator.AosIterator;

/**
 * Convenience base class for {@link List} implementations.
 *
 */
public abstract class AbstractList implements List {
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append('[');

        if (!isEmpty()) {
            AosIterator i = iterator();
            for (i.first(); !i.isDone(); i.next()) {
                buffer.append(i.current()).append(", ");
            }

            buffer.setLength(buffer.length() - 2);
        }

        buffer.append(']');

        return buffer.toString();
    }

    public int hashCode() {
        int hashCode = 0;

        AosIterator i = iterator();
        for (i.first(); !i.isDone(); i.next()) {
            hashCode ^= i.current().hashCode();
        }

        return hashCode;
    }

    public boolean equals(Object object) {
        return object instanceof List ? equals((List) object) : false;
    }

    public boolean equals(List other) {
        if (other == null || size() != other.size()) {
            return false;
        }

        AosIterator i = iterator();
        AosIterator j = other.iterator();
        for (i.first(), j.first(); !i.isDone() && !j.isDone(); i.next(), j.next()) {
            if (!i.current().equals(j.current())) {
                break;
            }
        }

        return i.isDone() && j.isDone();
    }
}
