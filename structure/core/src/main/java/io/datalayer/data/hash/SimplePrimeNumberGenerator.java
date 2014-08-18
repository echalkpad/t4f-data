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
package io.datalayer.data.hash;

/**
 * A deterministic {@link PrimeNumberGenerator} that uses successive division to test for primality.
 *
 */
public final class SimplePrimeNumberGenerator implements PrimeNumberGenerator {
    /** The single instance of the class. */
    public static final SimplePrimeNumberGenerator INSTANCE = new SimplePrimeNumberGenerator();

    /**
     * Constructor makred private to prevent instantiation.
     */
    private SimplePrimeNumberGenerator() {
    }

    public int generate(int candidate) {
        int prime = candidate;

        while (!isPrime(prime)) {
            ++prime;
        }

        return prime;
    }

    /**
     * Determines if a number is prime by checking the remainder after division.
     *
     * @param candidate The candidate prime.
     * @return <code>true</code> if the candidate is prime; otherwise <code>false</code>.
     */
    private boolean isPrime(int candidate) {
        for (int i = candidate / 2; i >= 2; --i) {
            if (candidate % i == 0) {
                return false;
            }
        }
        return true;
    }
}
