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
package io.datalayer.algorithm.fibonacci.f1;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Fibonacci4Sample {

    /** Print ascending members of the Fibonacci sequence that are
    representable as 64-bit signed integers, prefixed by their term
    numbers, and followed by the ratio of successive terms, to
    demonstrate the 1.618...^n growth (the ratio approaches the golden
    ratio, (1 + sqrt(5))/2 = 1.6180339887498949, and reaches it (to
    machine precision) at 41 terms: the fourth item on each line is
    the difference from the golden ratio).

    This program also shows the hoops that one must jump through in
    Java to get right-adjusted fixed-width output fields, a feature
    that is utterly trivial in C, C++, Pascal, and Fortran. The comma
    separators in the Java output are, however, a nice touch. :^) */

    static final double golden_ratio = (1.0 + Math.sqrt(5.0))/2.0;

    private static String pad(String s, int pad_len)
    {
	if (s.length() >= pad_len)
	    return (s);
	else
	{
	    int nblanks = pad_len - s.length();
	    StringBuffer blanks = new StringBuffer(nblanks);

	    blanks.setLength(nblanks);
	    for (int k = 0; k < blanks.length(); ++k)
		blanks.setCharAt(k,' ');
	    return (blanks + s);
	}
    }

    public static void main(String... args)
    {
	long lo = 1L;
	long hi = 1L;
	long n = 1L;
	double ratio;
	DecimalFormat myFormat = (DecimalFormat)NumberFormat.getInstance();

	myFormat.setMinimumIntegerDigits(1);
	myFormat.setMaximumIntegerDigits(2);
	myFormat.setMinimumFractionDigits(0);
	myFormat.setMaximumFractionDigits(0);
	System.out.print(myFormat.format(n));

	System.out.print("\t");

	myFormat.setMinimumIntegerDigits(1);
	myFormat.setMaximumIntegerDigits(19);
	System.out.println(pad(myFormat.format(lo),25));

	while (hi > 0L)
	{
	    n++;

	    myFormat.setMinimumIntegerDigits(1);
	    myFormat.setMaximumIntegerDigits(2);
	    myFormat.setMinimumFractionDigits(0);
	    myFormat.setMaximumFractionDigits(0);
	    System.out.print(myFormat.format(n));

	    System.out.print("\t");

	    myFormat.setMinimumIntegerDigits(1);
	    myFormat.setMaximumIntegerDigits(19);

	    System.out.print(pad(myFormat.format(hi),25));

	    System.out.print("\t");

	    ratio = (double)hi/(double)lo;

	    myFormat.setMinimumIntegerDigits(1);
	    myFormat.setMaximumIntegerDigits(1);
	    myFormat.setMinimumFractionDigits(16);
	    myFormat.setMaximumFractionDigits(16);
	    System.out.print(myFormat.format(ratio));

	    System.out.print("\t");

	    myFormat.setMinimumIntegerDigits(1);
	    myFormat.setMaximumIntegerDigits(1);
	    myFormat.setMinimumFractionDigits(16);
	    myFormat.setMaximumFractionDigits(16);
	    System.out.println(pad(myFormat.format(ratio - golden_ratio),19));

	    hi = lo + hi;
	    lo = hi - lo;
	}
    }
}
