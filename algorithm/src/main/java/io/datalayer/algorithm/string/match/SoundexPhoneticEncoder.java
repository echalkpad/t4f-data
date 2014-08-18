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
package io.datalayer.algorithm.string.match;

/**
 * Encodes strings according to the original Soundex algorithm.
 *
 */
public final class SoundexPhoneticEncoder implements PhoneticEncoder {
    /** The single instance of the class. */
    public static final SoundexPhoneticEncoder INSTANCE = new SoundexPhoneticEncoder();

    /**                                          ABCDEFGHIJKLMNOPQRSTUVWXYZ */
    private static final char[] CHARACTER_MAP = "01230120022455012623010202".toCharArray();

    /**
     * Constructor marked private to prevent instantiation.
     */
    private SoundexPhoneticEncoder() {
    }

    public String encode(CharSequence string) {
        assert string != null : "string can't be null";
        assert string.length() > 0 : "string can't be empty";

        char[] result = {'0', '0', '0', '0'};

        result[0] = Character.toUpperCase(string.charAt(0));

        int stringIndex = 1;
        int resultIndex = 1;

        while (stringIndex < string.length() && resultIndex < result.length) {
            char c = map(string.charAt(stringIndex));

            if (c != '0' && c != result[resultIndex - 1]) {
                result[resultIndex] = c;
                ++resultIndex;
            }

            ++stringIndex;
        }

        return String.valueOf(result);
    }

    /**
     * Maps a single character according to the soundex rules.
     *
     * @param c The character to map.
     * @return The encoding; or <code>'0'</code> if the character should be ignored.
     */
    private static char map(char c) {
        int index = Character.toUpperCase(c) - 'A';
        return isValid(index) ? CHARACTER_MAP[index] : '0';
    }

    /**
     * Determins if the specified index is valid for the translate table.
     *
     * @param index The index.
     * @return <code>true</code> if value; otherwise <code>false</code>.
     */
    private static boolean isValid(int index) {
        return index >= 0 && index < CHARACTER_MAP.length;
    }
}
