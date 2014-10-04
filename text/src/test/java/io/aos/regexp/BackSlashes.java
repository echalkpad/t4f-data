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

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Demonstrate behavior of backslashes in regex patterns.
 *
 * Created: April, 2002
 * @author Ron Hitchens (ron@ronsoft.com)
 * @version $Id: BackSlashes.java,v 1.1 2002/04/10 18:41:27 ron Exp $
 */
public class BackSlashes
{
	public static void main (String [] argv)
	{
		// substitute "a\b" for XYZ or ABC in input
		String rep = "a\\\\b";
		String input = "> XYZ <=> ABC <";
		Pattern pattern = Pattern.compile ("ABC|XYZ");
		Matcher matcher = pattern.matcher (input);

		System.out.println (matcher.replaceFirst (rep));
		System.out.println (matcher.replaceAll (rep));

		// change all newlines in input to escaped, DOS-like CR/LF
		rep = "\\\\r\\\\n";
		input = "line 1\nline 2\nline 3\n";
		pattern = Pattern.compile ("\\n");
		matcher = pattern.matcher (input);

		System.out.println ("");
		System.out.println ("Before:");
		System.out.println (input);

		System.out.println ("After (dos-ified, escaped):");
		System.out.println (matcher.replaceAll (rep));
	}
}
