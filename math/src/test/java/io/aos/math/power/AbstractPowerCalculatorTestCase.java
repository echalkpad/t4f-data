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
package io.aos.math.power;

import io.aos.math.power.PowerCalculator;
import junit.framework.TestCase;

/**
 * Abstract base class for testing implementations of {@link PowerCalculator}.
 */
public abstract class AbstractPowerCalculatorTestCase extends TestCase {
    protected abstract PowerCalculator createCalculator();

    public void testAnythingRaisedToThePowerOfZeroIsOne() {
        PowerCalculator calculator = createCalculator();

        assertEquals(1, calculator.calculate(0, 0));
        assertEquals(1, calculator.calculate(1, 0));
        assertEquals(1, calculator.calculate(27, 0));
        assertEquals(1, calculator.calculate(143, 0));
    }

    public void testAnythingRaisedToThePowerOfOneIsItself() {
        PowerCalculator calculator = createCalculator();

        assertEquals(0, calculator.calculate(0, 1));
        assertEquals(1, calculator.calculate(1, 1));
        assertEquals(27, calculator.calculate(27, 1));
        assertEquals(143, calculator.calculate(143, 1));
    }

    public void testSquare() {
        PowerCalculator calculator = createCalculator();

        assertEquals(0, calculator.calculate(0, 2));
        assertEquals(1, calculator.calculate(1, 2));
        assertEquals(4, calculator.calculate(2, 2));
        assertEquals(9, calculator.calculate(3, 2));
        assertEquals(16, calculator.calculate(4, 2));
    }

    public void testCube() {
        PowerCalculator calculator = createCalculator();

        assertEquals(0, calculator.calculate(0, 3));
        assertEquals(1, calculator.calculate(1, 3));
        assertEquals(8, calculator.calculate(2, 3));
        assertEquals(27, calculator.calculate(3, 3));
    }
}
