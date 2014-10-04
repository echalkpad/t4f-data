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

import java.text.Normalizer.Form;

import sun.text.Normalizer;

public class StringNormalizerTest {

    /**
     * Look real implementation in aos.util.formatter.AOSStringFormatter;
     * 
     * @param args
     */
    public void test() {
        String inputString = "�l� ??&#@phant%?,?.";
        System.out.println(normalizeAccentuedCharUnicode(inputString));
        System.out.println("R = "
                + enCodeStringUnderscoreLetterNumber(inputString));
        System.out.println("X = " + filterFromPageNameHtml(inputString));
    }

    static String filterFromPageNameHtml(String inputString) {

        String outputString = normalizeAccentuedCharUnicode(inputString);
        outputString = outputString.replaceAll(" ", "_");
        outputString = enCodeStringUnderscoreLetterNumber(outputString);

        return outputString;

    }

    static public String normalizeAccentuedCharUnicode(String s) {
        String strUnicode = Normalizer.normalize(s, Form.NFKD, 0).replaceAll(
                "[^\\p{ASCII}]", "");
        return strUnicode;
    }

    static public String formatStringFromLetterNumber(String s) {
        String pageName = Normalizer.normalize(s, Form.NFKD, 0).replaceAll(
                "[^\\p{ASCII}]", "");
        pageName = pageName.replaceAll(" ", "_");
        pageName = enCodeStringUnderscoreLetterNumber(pageName);
        return pageName;
    }

    static String enCodeStringUnderscoreLetterNumber(String strInput) {
        String outputString = "";
        char[] charArray = strInput.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char charValue = charArray[i];
            int charCodeValue = (int) charValue;
            System.out.println("char = " + charValue + " val = "
                    + charCodeValue);

            /*
             * Valid code to unicode and filename (48-57) (65-90) (97 - 122) 95
             * to do it's possible use a pattern class !
             */

            if ((charCodeValue >= 48 && charCodeValue <= 57)

            || (charCodeValue >= 65 && charCodeValue <= 90)

            || (charCodeValue >= 97 && charCodeValue <= 122)
                    || (charCodeValue == 95))

            {

                outputString += charValue;

            }

        }

        return outputString;

    }

    static void printResult(String inputString) {

        char[] charArray = inputString.toCharArray();

        for (int i = 0; i < charArray.length; i++) {

            char temp = charArray[i];

            int val = (int) temp;

            System.out.println("char = " + temp + " val = " + val);
        }
    }

}
