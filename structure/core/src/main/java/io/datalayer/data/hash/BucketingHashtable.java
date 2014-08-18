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
package io.datalayer.data.hash;

import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.LinkedList;
import io.datalayer.data.list.List;

/**
 * A {@link Hashtable} that uses buckets.
 *
 */
public class BucketingHashtable implements Hashtable {
    /** The threshold load factor after which resizing occurs. */
    private final float _loadFactor;

    /** The buckets for holding values. */
    private List[] _buckets;

    /** The number of values in the table. */
    private int _size;

    /**
     * Constructor.
     *
     * @param initialCapacity The initial number of buckets.
     * @param loadFactor The threshold load factor after which resizing occurs.
     */
    public BucketingHashtable(int initialCapacity, float loadFactor) {
        assert initialCapacity > 0 : "initialCapacity can't be < 1";
        assert loadFactor > 0 : "loadFactor can't be <= 0";

        _loadFactor = loadFactor;
        _buckets = new List[initialCapacity];
    }

    public void add(Object value) {
        List bucket = bucketFor(value);

        if (!bucket.contains(value)) {
            bucket.add(value);
            ++_size;
            maintainLoad();
        }
    }

    public boolean contains(Object value) {
        List bucket = _buckets[bucketIndexFor(value)];
        return bucket != null && bucket.contains(value);
    }

    public int size() {
        return _size;
    }

    /**
     * Obtains a bucket for a specified value.
     *
     * @param value The value for which a bucket is required.
     * @return The bucket.
     */
    private List bucketFor(Object value) {
        int bucketIndex = bucketIndexFor(value);

        List bucket = _buckets[bucketIndex];
        if (bucket == null) {
            bucket = new LinkedList();
            _buckets[bucketIndex] = bucket;
        }

        return bucket;
    }

    /**
     * Obtains the index to the bucket appropriate for a specified value.
     *
     * @param value The value for which the index is desired.
     * @return The bucket index.
     */
    private int bucketIndexFor(Object value) {
        assert value != null : "value can't be null";
        return Math.abs(value.hashCode() % _buckets.length);
    }

    /**
     * Resizes the table to satisfy the load factor requirements.
     */
    private void maintainLoad() {
        if (loadFactorExceeded()) {
            resize();
        }
    }

    /**
     * Determines if the load factor threshold has been exceeded.
     *
     * @return <code>true</code> if the threshold has been exceeded; otherwise <code>false</code>.
     */
    private boolean loadFactorExceeded() {
        return size() > _buckets.length * _loadFactor;
    }

    /**
     * Re-sizes the table.
     */
    private void resize() {
        BucketingHashtable copy = new BucketingHashtable(_buckets.length * 2, _loadFactor);

        for (int i = 0; i < _buckets.length; ++i) {
            if (_buckets[i] != null) {
                copy.addAll(_buckets[i].iterator());
            }
        }

        _buckets = copy._buckets;
    }

    /**
     * Adds all values from a bucket into the table.
     *
     * @param values The values.
     */
    private void addAll(AosIterator values) {
        assert values != null : "values can't be null";

        for (values.first(); !values.isDone(); values.next()) {
            add(values.current());
        }
    }
}
