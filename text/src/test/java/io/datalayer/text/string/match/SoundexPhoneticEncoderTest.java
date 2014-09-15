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
package io.datalayer.text.string.match;

import junit.framework.TestCase;

/**
 * Test cases for {@link SoundexPhoneticEncoder}.
 *
 */
public class SoundexPhoneticEncoderTest extends TestCase {
    private SoundexPhoneticEncoder _encoder;

    protected void setUp() throws Exception {
        super.setUp();

        _encoder = SoundexPhoneticEncoder.INSTANCE;
    }

    public void testFirstLetterIsAlwaysUsed() {
        for (char c = 'A'; c <= 'Z'; ++c) {
            String result = _encoder.encode(c + "-");

            assertNotNull(result);
            assertEquals(4, result.length());

            assertEquals(c, result.charAt(0));
        }
    }

    public void testVowelsAreIgnored() {
        assertAllEquals('0', new char[] {'A', 'E', 'I', 'O', 'U', 'H', 'W', 'Y'});
    }

    public void testLettersRepresentedByOne() {
        assertAllEquals('1', new char[] {'B', 'F', 'P', 'V'});
    }

    public void testLettersRepresentedByTwo() {
        assertAllEquals('2', new char[] {'C', 'G', 'J', 'K', 'Q', 'S', 'X', 'Z'});
    }

    public void testLettersRepresentedByThree() {
        assertAllEquals('3', new char[] {'D', 'T'});
    }

    public void testLettersRepresentedByFour() {
        assertAllEquals('4', new char[] {'L'});
    }

    public void testLettersRepresentedByFive() {
        assertAllEquals('5', new char[] {'M', 'N'});
    }

    public void testLettersRepresentedBySix() {
        assertAllEquals('6', new char[] {'R'});
    }

    public void testDuplicateCodesAreDropped() {
        assertEquals("B100", _encoder.encode("BFPV"));
        assertEquals("C200", _encoder.encode("CGJKQSXZ"));
        assertEquals("D300", _encoder.encode("DDT"));
        assertEquals("L400", _encoder.encode("LLL"));
        assertEquals("M500", _encoder.encode("MNMN"));
        assertEquals("R600", _encoder.encode("RRR"));
    }

    public void testSomeRealStrings() {
        assertEquals("S530", _encoder.encode("Smith"));
        assertEquals("S530", _encoder.encode("Smythe"));
        assertEquals("M235", _encoder.encode("McDonald"));
        assertEquals("M235", _encoder.encode("MacDonald"));
        assertEquals("H620", _encoder.encode("Harris"));
        assertEquals("H620", _encoder.encode("Harrys"));
    }

    private void assertAllEquals(char expectedValue, char[] chars) {
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            String result = _encoder.encode("-" + c);

            assertNotNull(result);
            assertEquals(4, result.length());

            assertEquals("-" + expectedValue + "00", result);
        }
    }
}
