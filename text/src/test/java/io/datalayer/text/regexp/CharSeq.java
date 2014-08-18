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

import java.nio.CharBuffer;

/**
 * Demonstrate behavior of java.lang.CharSequence as implemented
 * by String, StringBuffer and CharBuffer.
 *
 * Created: April, 2002
 * @author Ron Hitchens (ron@ronsoft.com)
 * @version $Id: CharSeq.java,v 1.1 2002/05/07 02:21:08 ron Exp $
 */
public class CharSeq
{
	public static void main (String [] argv)
	{
		StringBuffer stringBuffer = new StringBuffer ("Hello World");
		CharBuffer charBuffer = CharBuffer.allocate (20);
		CharSequence charSequence = "Hello World";

		// derived directly from a String
		printCharSequence (charSequence);

		// derived from a StringBuffer
		charSequence = stringBuffer;
		printCharSequence (charSequence);

		// change StringBuffer
		stringBuffer.setLength (0);
		stringBuffer.append ("Goodbye cruel world");
		// same "immutable" CharSequence yields different result
		printCharSequence (charSequence);

		// derive CharSequence from CharBuffer.
		charSequence = charBuffer;
		charBuffer.put ("xxxxxxxxxxxxxxxxxxxx");
		charBuffer.clear();

		charBuffer.put ("Hello World");
		charBuffer.flip();
		printCharSequence (charSequence);

		charBuffer.mark();
		charBuffer.put ("Seeya");
		charBuffer.reset();
		printCharSequence (charSequence);

		charBuffer.clear();
		printCharSequence (charSequence);
		// changing underlying CharBuffer is reflected in the
		// read-only CharSequnce interface.
	}

	private static void printCharSequence (CharSequence cs)
	{
		System.out.println ("length=" + cs.length()
			+ ", content='" + cs.toString() + "'");
	}
}
