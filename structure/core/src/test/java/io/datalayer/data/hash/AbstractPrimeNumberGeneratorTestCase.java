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

import io.datalayer.data.hash.PrimeNumberGenerator;
import junit.framework.TestCase;

/**
 * Abstract base class for tesing implementations of {@link PrimeNumberGenerator}.
 *
 */
public abstract class AbstractPrimeNumberGeneratorTestCase extends TestCase {
    private PrimeNumberGenerator _generator;

    protected void setUp() throws Exception {
        super.setUp();

        _generator = createGenerator();
    }

    protected abstract PrimeNumberGenerator createGenerator();

    public void test() {
        assertEquals(2, _generator.generate(2));
        assertEquals(3, _generator.generate(3));
        assertEquals(5, _generator.generate(4));
        assertEquals(5, _generator.generate(5));
        assertEquals(7, _generator.generate(6));
        assertEquals(7, _generator.generate(7));
        assertEquals(11, _generator.generate(8));
        assertEquals(11, _generator.generate(9));
        assertEquals(11, _generator.generate(10));
        assertEquals(11, _generator.generate(11));
        assertEquals(101, _generator.generate(100));
        assertEquals(101, _generator.generate(101));
        assertEquals(4099, _generator.generate(4096));
        assertEquals(4099, _generator.generate(4097));
        assertEquals(4099, _generator.generate(4098));
        assertEquals(4099, _generator.generate(4099));
        assertEquals(65537, _generator.generate(65535));
        assertEquals(65537, _generator.generate(65536));
        assertEquals(65537, _generator.generate(65537));
    }
}
