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
package io.aos.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test the appendReplacement() and appendTail() methods of the
 * java.util.regex.Matcher class. Created: Dec 28, 2001
 */
public class RegexAppend {
    public static void main(String[] argv) {
        String input = "Thanks, thanks very much";
        String regex = "([Tt])hanks";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        StringBuffer sb = new StringBuffer();

        // loop while matches are encountered
        while (matcher.find()) {
            if (matcher.group(1).equals("T")) {
                matcher.appendReplacement(sb, "Thank you");
            } else {
                matcher.appendReplacement(sb, "thank you");
            }
        }

        // complete the transfer to the StringBuffer
        matcher.appendTail(sb);

        // print the result
        System.out.println(sb.toString());

        // Let's try that again, using the $n escape in the replacement
        sb.setLength(0);
        matcher.reset();

        String replacement = "$1hank you";

        // loop while matches are encountered
        while (matcher.find()) {
            matcher.appendReplacement(sb, replacement);
        }

        // complete the transfer to the StringBuffer
        matcher.appendTail(sb);

        // print the result
        System.out.println(sb.toString());

        // And once more, the easy way (because this example is simple)
        System.out.println(matcher.replaceAll(replacement));

        // One last time, using only the String
        System.out.println(input.replaceAll(regex, replacement));

    }
}
