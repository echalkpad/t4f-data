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

import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;
import io.datalayer.data.queue.BlockingQueue;
import io.datalayer.data.queue.ListFifoQueue;
import io.datalayer.data.queue.Queue;

/**
 * Uses a {@link BlockingQueue} to route {@link Call}s to {@link CustomerServiceAgent}s.
 *
 */
public class CallCenter {
    /** The calls. */
    private final Queue _calls = new BlockingQueue(new ListFifoQueue());

    /** The number of customer service agents to service calls. */
    private final int _numberOfAgents;

    /** The customer service agent threads when open; or empty if closed. */
    private final List _threads;

    /**
     * Constructor.
     *
     * @param numberOfAgents the number of customer service agents to service calls.
     */
    public CallCenter(int numberOfAgents) {
        _numberOfAgents = numberOfAgents;
        _threads = new ArrayList(numberOfAgents);
    }

    /**
     * Opens the call centre for servicing calls.
     */
    public void open() {
        assert _threads.isEmpty() : "Already open";

        System.out.println("Call center opening");

        for (int i = 0; i < _numberOfAgents; ++i) {
            Thread thread = new Thread(new CustomerServiceAgent(i, _calls));

            thread.start();
            _threads.add(thread);
        }

        System.out.println("Call center open");
    }

    /**
     * Closes the centre. Calls are no longer accepted.
     */
    public void close() {
        assert !_threads.isEmpty() : "Already closed";

        System.out.println("Call center closing");

        for (int i = 0; i < _numberOfAgents; ++i) {
            accept(CustomerServiceAgent.GO_HOME);
        }

        AosIterator i = _threads.iterator();
        for (i.first(); !i.isDone(); i.next()) {
            waitForTermination((Thread) i.current());
        }

        _threads.clear();

        System.out.println("Call center closed");
    }

    /**
     * Waits for a thread to terminate.
     *
     * @param thread The thread on which to wait.
     */
    private void waitForTermination(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            // Ignore
        }
    }

    /**
     * Process a call.
     *
     * @param call The call the accept.
     */
    public void accept(Call call) {
        assert !_threads.isEmpty() : "Not open";

        _calls.enqueue(call);

        System.out.println(call + " queued");
    }
}
