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
 * Call center simulator command-line application.
 *
 */
public final class CallCenterSimulator {
    /** The number of expected command-line arguments. */
    private static final int NUMBER_OF_ARGS = 4;

    /** Index to the number of agents command-line argument. */
    private static final int NUMBER_OF_AGENTS_ARG = 0;

    /** Index to the number of calls command-line argument. */
    private static final int NUMBER_OF_CALLS_ARG = 1;

    /** Index to the maximum call duration command-line argument. */
    private static final int MAX_CALL_DURATION_ARG = 2;

    /** Index to the maximum call interval command-line argument. */
    private static final int MAX_CALL_INTERVAL_ARG = 3;

    /**
     * Constructor marked private to prevent instantiation.
     */
    private CallCenterSimulator() {
    }

    /**
     * Prgram mainline.
     *
     * @param args Command-line arguments.
     */
    public static void main(String... args) {
        assert args != null : "args can't be null";

        if (args.length != NUMBER_OF_ARGS) {
            System.out.println("Usage: CallGenerator <numberOfAgents> <numberOfCalls>"
                               + "<maxCallDuration> <maxCallInterval>");
            System.exit(-1);
        }

        CallCenter callCenter = new CallCenter(Integer.parseInt(args[NUMBER_OF_AGENTS_ARG]));

        CallGenerator callGenerator = new CallGenerator(callCenter,
                                                    Integer.parseInt(args[NUMBER_OF_CALLS_ARG]),
                                                    Integer.parseInt(args[MAX_CALL_DURATION_ARG]),
                                                    Integer.parseInt(args[MAX_CALL_INTERVAL_ARG]));

        callCenter.open();
        try {
            callGenerator.generateCalls();
        } finally {
            callCenter.close();
        }
    }
}
