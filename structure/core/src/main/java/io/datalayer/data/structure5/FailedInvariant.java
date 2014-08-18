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
package io.datalayer.data.structure5;
/**
 * This error is thrown by the Assert class in the event of a failed
 * invariant test. Errors are thrown rather than exceptions because
 * failed invariants are assumed to be an indication of such
 * an egregious program failure that recovery is impossible.
 *
 *
 * @version $Id: FailedInvariant.java 22 2006-08-21 19:27:26Z bailey $
 * @author, 2001 duane a. bailey
 * @see Assert#fail
 */
class FailedInvariant extends FailedAssertion
{
    final static long serialVersionUID = 0L;
    /**
     * Constructs an error indicating failure to meet an invariant
     *
     * @post Constructs a new failed invariant error
     * @param reason String describing failed condition.
     */
    public FailedInvariant(String reason)
    {
        super("\nInvariant that failed: " + reason);
    }
}
