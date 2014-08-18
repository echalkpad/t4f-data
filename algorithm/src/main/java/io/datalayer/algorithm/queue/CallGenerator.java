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
 * Exercises a {@link CallCenter} by randomly generating {@link Call}s.
 *
 */
public class CallGenerator {
    /** The call centre. */
    private final CallCenter _callCenter;

    /** The number of calls to generate. */
    private final int _numberOfCalls;

    /** The maximum duration for a call. */
    private final int _maxCallDuration;

    /** The maximum duration between calls. */
    private final int _maxCallInterval;

    /**
     * Constructor.
     *
     * @param callCenter The call center to use.
     * @param numberOfCalls The number of calls to generate.
     * @param maxCallDuration The maximum The maximum duration for a call.
     * @param maxCallInterval The maximum duration between calls.
     */
    public CallGenerator(CallCenter callCenter, int numberOfCalls, int maxCallDuration, int maxCallInterval) {
        assert callCenter != null : "callCenter can't be null";
        assert numberOfCalls > 0 : "numberOfCalls can't be < 1";
        assert maxCallDuration > 0 : "maxCallDuration can't be < 1";
        assert maxCallInterval > 0 : "maxCallInterval can't be < 1";

        _callCenter = callCenter;
        _numberOfCalls = numberOfCalls;
        _maxCallDuration = maxCallDuration;
        _maxCallInterval = maxCallInterval;
    }

    /**
     * Generates calls with a random duration.
     */
    public void generateCalls() {
        for (int i = 0; i < _numberOfCalls; ++i) {
            sleep();
            _callCenter.accept(new Call(i, (int) (Math.random() * _maxCallDuration)));
        }
    }

    /**
     * Sleeps for a random duration.
     */
    private void sleep() {
        try {
            Thread.sleep((int) (Math.random() * _maxCallInterval));
        } catch (InterruptedException e) {
            // Ignore
        }
    }
}
