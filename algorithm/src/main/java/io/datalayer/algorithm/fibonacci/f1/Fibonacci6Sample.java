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

import java.math.BigDecimal;
import java.math.BigInteger;

public class Fibonacci6Sample {

    /** Using exact integer arithmetic, print N_Terms ascending
    members of the Fibonacci sequence, prefixed by their term numbers,
    and followed by the ratio of successive terms, to demonstrate the
    1.618...^n growth (the ratio approaches the golden ratio, (1 +
    sqrt(5))/2 = 1.6180339887498949, and reaches it (to 64-bit machine
    precision) at 41 terms: the fourth item on each line is the
    difference from the golden ratio).

    Java's BigDecimal package, which is intended for simple
    high-precision currency calculations, does not provide elementary
    functions, such as sqrt(), so we supply a 100D-string
    representation for the golden_ratio. */

    private static final int Fractional_Digits = 50; // no more than 100 (digits in golden_ratio below)
    private static final int N_Terms = 200; // reasonable for demonstration purposes

    static final BigDecimal golden_ratio = new BigDecimal("1.618033988749894848204586834365638117720309179805762862135448622705260462818902449707207204189391138");

    private static String pad(BigDecimal bd, int pad_len) 
    {
	return (pad(bd + "",pad_len));
    }

    private static String pad(BigInteger bi, int pad_len) 
    {
	return (pad(bi + "",pad_len));
    }

    private static String pad(int n, int pad_len) 
    {
	return (pad(n + "",pad_len));
    }

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
	BigInteger lo = BigInteger.valueOf(1L);
	BigInteger hi = BigInteger.valueOf(1L);
	int n = 1;

	System.out.print(pad(n,3));
	System.out.print("\t");
	System.out.println(pad(lo,47));

	while (n < N_Terms)
	{
	    n++;
	    System.out.print(pad(n,3));
	    System.out.print("\t");
	    System.out.print(pad(hi,47));
	    System.out.print("\t");
	    BigDecimal ratio = new BigDecimal(hi);
	    BigDecimal den = new BigDecimal(lo);
	    ratio = ratio.divide(den, Fractional_Digits, ratio.ROUND_HALF_DOWN);
	    System.out.print(pad(ratio,55));
	    System.out.print("\t");
	    System.out.println(pad(ratio.subtract(golden_ratio).setScale(Fractional_Digits,
								     ratio.ROUND_HALF_DOWN),55));
	    hi = lo.add(hi);
	    lo = hi.subtract(lo);
	}
    }
}
