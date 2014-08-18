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

import io.datalayer.data.hash.BucketingHashtable;
import io.datalayer.data.hash.Hashtable;
import io.datalayer.data.hash.LinearProbingHashtable;
import junit.framework.TestCase;

import org.junit.Ignore;

/**
 * Compare the performance of linear probing versus bucketing.
 */
@Ignore
public class HashtableCallCountingTest extends TestCase {
    private static final int TEST_SIZE = 1000;
    private static final int INITIAL_CAPACITY = 17;

    private int _counter;
    private Hashtable _hashtable;

    public void testLinearProbingWithResizing() {
        _hashtable = new LinearProbingHashtable(INITIAL_CAPACITY);
        runAll();
    }

    public void testLinearProbingNoResizing() {
        _hashtable = new LinearProbingHashtable(TEST_SIZE);
        runAll();
    }

    public void testBucketsLoadFactor100Percent() {
        _hashtable = new BucketingHashtable(INITIAL_CAPACITY, 1.0f);
        runAll();
    }

    public void testBucketsLoadFactor75Percent() {
        _hashtable = new BucketingHashtable(INITIAL_CAPACITY, 0.75f);
        runAll();
    }

    public void testBuckets50Percent() {
        _hashtable = new BucketingHashtable(INITIAL_CAPACITY, 0.50f);
        runAll();
    }

    public void testBuckets25Percent() {
        _hashtable = new BucketingHashtable(INITIAL_CAPACITY, 0.25f);
        runAll();
    }

    public void testBuckets150Percent() {
        _hashtable = new BucketingHashtable(INITIAL_CAPACITY, 1.50f);
        runAll();
    }

    public void testBuckets200Percent() {
        _hashtable = new BucketingHashtable(INITIAL_CAPACITY, 2.0f);
        runAll();
    }

    private void runAll() {
        runAdd();
        runContains();
    }

    private void runAdd() {
        _counter = 0;
        for (int i = 0; i < TEST_SIZE; ++i) {
            _hashtable.add(new Value(i));
        }
        reportCalls("add");
    }

    private void runContains() {
        _counter = 0;
        for (int i = 0; i < TEST_SIZE; ++i) {
            _hashtable.contains(new Value(i));
        }
        reportCalls("contains");
    }

    private void reportCalls(String method) {
        System.out.println(getName() + "(" + method + "): " + _counter + " calls");
    }

    private final class Value {
        private final String _value;

        public Value(int value) {
            _value = String.valueOf(Math.random() * TEST_SIZE);
        }

        public int hashCode() {
            return _value.hashCode();
        }

        public boolean equals(Object object) {
            ++_counter;
            return object != null && _value.equals(((Value) object)._value);
        }
    }
}
