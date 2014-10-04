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
package io.aos.string;

import io.aos.string.palindrome.StringPalindrome;

import org.junit.Test;

public class StringPalindromeTest {
    public static final String IS_PALINDROME = "ici";
    public static final String IS_NOT_PALINDROME = "pas palindrome";
    public static final StringPalindrome palindrome = new StringPalindrome();

    @Test
    public void test() {
        printPalindrome(IS_PALINDROME);
        printPalindrome(IS_NOT_PALINDROME);
    }

    private void printPalindrome(String string) {
        System.out.format("'%s' is palindrom?: %b\n", string, palindrome.isPalindrome(string));
    }

}
