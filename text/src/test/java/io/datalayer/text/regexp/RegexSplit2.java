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

package io.datalayer.text.regexp;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Test regex splitting with arbitrary regexs.
 * Remember to quote the input args.
 *
 * @author Ron Hitchens
 * @version $Id: RegexSplit2.java,v 1.1 2002/05/07 02:21:08 ron Exp $
 */
public class RegexSplit2
{
	public static void main (String [] argv)
	{
		Pattern pattern = null;

		try {
			pattern = Pattern.compile (argv [0]);
		} catch (PatternSyntaxException e) {
			System.out.println ("The regular expression '"
				+ argv [0] + "' contains a "
				+ "syntax error: " + e.getMessage());
		}

		String [] tokens = pattern.split (argv [1]);

		for (int i = 0; i < tokens.length; i++) {
			System.out.println ("" + i + ": " + tokens [i]);
		}
	}
}
