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

import java.nio.CharBuffer;
import java.util.regex.Pattern;

/**
 * Test regex splitting.  Splitting CharBuffers does not work reliably on
 * JDK 1.4.0.  Fixed in 1.4.1 release.
 */
public class Split
{
	private static final String input = "GET /z.html HTTP/1.0\r\n";

	public static void main (String [] argv)
	{
		Pattern spacePat = Pattern.compile (" ");
		StringBuffer sb = new StringBuffer (input);
		CharBuffer cb = CharBuffer.wrap (sb.toString());
		String [] tokens = null;

		try {
			System.out.println ("Splitting StringBuffer");
			tokens = spacePat.split (sb);
			System.out.println ("split OK");
		} catch (Exception e) {
			System.out.println ("Caught: " + e);
			e.printStackTrace();
		}
		System.out.println ("");

		try {
			System.out.println ("Splitting CharBuffer");
			tokens = spacePat.split (cb);
			System.out.println ("split OK");
		} catch (Exception e) {
			System.out.println ("Caught: " + e);
			e.printStackTrace();
		}
		System.out.println ("");

		try {
			System.out.println ("Splitting CharBuffer.toString()");
			tokens = spacePat.split (cb.toString());
			System.out.println ("split OK");
		} catch (Exception e) {
			System.out.println ("Caught: " + e);
			e.printStackTrace();
		}
	}
}
