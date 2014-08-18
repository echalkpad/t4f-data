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

import io.datalayer.data.queue.Queue;

/**
 * Services {@link Call}s by pulling them off a {@link Queue}.
 *
 */
public class CustomerServiceAgent implements Runnable {
    /** Indicates it's time for the agent to finish. */
    public static final Call GO_HOME = new Call(-1, 0);

    /** The id of the agent. */
    private final int _id;

    /** The queue from which to pull calls. */
    private final Queue _calls;

    /**
     * Constructor.
     *
     * @param id The id of the agent.
     * @param calls The queue from which to pull calls.
     */
    public CustomerServiceAgent(int id, Queue calls) {
        assert calls != null : "calls can't be null";
        _id = id;
        _calls = calls;
    }

    public void run() {
        System.out.println(this + " clocked on");

        while (true) {
            System.out.println(this + " waiting");

            Call call = (Call) _calls.dequeue();
            System.out.println(this + " answering " + call);

            if (call == GO_HOME) {
                break;
            }

            call.answer();
        }

        System.out.println(this + " going home");
    }

    public String toString() {
        return "Agent " + _id;
    }
}
