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
package io.datalayer.algorithm.queue;

/**
 * Represents a telephone call.
 *
 */
public class Call {
    /** The id of the call. */
    private final int _id;

    /** The duration for the call. */
    private final int _duration;

    /** The time at which this call was started. */
    private final long _startTime;

    /**
     * Constructor.
     *
     * @param id The id of the call.
     * @param duration The duration for the call.
     */
    public Call(int id, int duration) {
        assert duration >= 0 : "duration can't be < 0";

        _id = id;
        _duration = duration;
        _startTime = System.currentTimeMillis();
    }

    /**
     * Answers the call.
     */
    public void answer() {
        System.out.println(this + " answered; waited " + (System.currentTimeMillis() - _startTime) + " milliseconds");

        try {
            Thread.sleep(_duration);
        } catch (InterruptedException e) {
            // Ignore
        }
    }

    public String toString() {
        return "Call " + _id;
    }
}
