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
package io.datalayer.text.string.search;

import junit.framework.TestCase;

/**
 * Test cases for {@link CallCountingCharSequence}.
 */
public class CallCountingCharSequenceTest extends TestCase {
    
    public void test() {

        String text = "Hello, World";
        CallCountingCharSequence sequence = new CallCountingCharSequence(text);

        assertEquals(0, sequence.getCallCount());
        assertEquals(text.length(), sequence.length());

        for (int i = text.length() - 1; i >= 0; --i) {
            assertEquals(text.charAt(i), sequence.charAt(i));
        }

        for (int i = text.length() - 1; i >= 0; --i) {
            assertEquals(text.charAt(i), sequence.charAt(i));
        }

        assertEquals(text.length() * 2, sequence.getCallCount());
    }

}
