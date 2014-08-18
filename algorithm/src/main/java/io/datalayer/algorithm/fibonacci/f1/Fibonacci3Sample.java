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


public class Fibonacci3Sample {
    
    /** Print ascending members of the Fibonacci sequence that are
    representable as 64-bit signed integers, prefixed by their term
    numbers, and followed by the ratio of successive terms, to
    demonstrate the 1.618...^n growth (the ratio approaches the golden
    ratio, (1 + sqrt(5))/2 = 1.6180339887498949, and reaches it (to
    machine precision) at 41 terms: the fourth item on each line is
    the difference from the golden ratio). */

    static final double golden_ratio = (1.0 + Math.sqrt(5.0))/2.0;

    public static void main(String... args)
    {
	long lo = 1L;
	long hi = 1L;
	long n = 1L;
	double ratio;
	System.out.print(n);

	System.out.print("\t");

	System.out.println(lo);

	while (hi > 0L)
	{
	    n++;

	    System.out.print(n);
	    System.out.print("\t");
	    System.out.print(hi);
	    System.out.print("\t");

	    ratio = (double)hi/(double)lo;

	    System.out.print(ratio);
	    System.out.print("\t");
	    System.out.println(ratio - golden_ratio);

	    hi = lo + hi;
	    lo = hi - lo;
	}
    }
}
