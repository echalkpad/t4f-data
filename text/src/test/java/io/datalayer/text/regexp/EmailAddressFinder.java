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
import java.util.regex.Matcher;

/**
 * Validates email addresses.
 *
 * Regular expression found in the Regular Expression Library
 * at regxlib.com.  Quoting from the site,
 * "Email validator that adheres directly to the specification 
 * for email address naming.  It allows for everything from 
 * ipaddress and country-code domains, to very rare characters 
 * in the username."
 * 
 * @author Michael Daudel (mgd@ronsoft.com) (original)
 * @author Ron Hitchens (ron@ronsoft.com) (hacked)
 * @version $Id: EmailAddressFinder.java,v 1.2 2002/05/07 02:21:08 ron Exp $
 */
public class EmailAddressFinder
{
	public static void main (String[] argv)
	{
		if (argv.length < 1) {
			System.out.println ("usage: emailaddress ...");
		}

		// Compile the email address detector pattern
		Pattern pattern = Pattern.compile (
			"([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]"
			+ "{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))"
			+ "([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)", 
			Pattern.MULTILINE);

		// Make a Matcher object for the pattern
		Matcher matcher = pattern.matcher ("");

		// loop through the args and find the addrs in each one
		for (int i = 0; i < argv.length; i++) {
			boolean matched = false;

			System.out.println ("");
			System.out.println ("Looking at " + argv [i] + " ...");

			// reset the Matcher to look at the current arg string
			matcher.reset (argv [i]);

			// loop while matches are encountered
			while (matcher.find()) 
			{
				// found one
				System.out.println ("\t" + matcher.group());

				matched = true;
			}

			if ( ! matched) {
				System.out.println ("\tNo email addresses found");
			}
		}
	}
}
