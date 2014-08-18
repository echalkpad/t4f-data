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
package io.datalayer.system.cpu;

import io.datalayer.system.info.SystemInformation;
import io.datalayer.system.info.SystemInformation.CPUUsageSnapshot;

import java.util.ArrayList;

/**
 * This class shows a sample API for recording and reporting periodic CPU usage
 * shapshots. See <code>CPUmon</code> class for a usage example.
 */
public class CpuUsageThread extends Thread {

    /**
     * Any client interested in receiving CPU usage events should implement this
     * interface and call {@link #addUsageEventListener} to add itself as the
     * event listener.
     */
    public static interface IUsageEventListener {
        void accept(SystemInformation.CPUUsageSnapshot event);

    } // end of nested interface

    /**
     * Default value for the data sampling interval [in milliseconds]. Currently
     * the value is 500 ms.
     */
    public static final int DEFAULT_SAMPLING_INTERVAL = 500;

    /**
     * Factory method for obtaining the CPU usage profiling thread singleton.
     * The first call constructs the thread, whose sampling interval will
     * default to {@link #DEFAULT_SAMPLING_INTERVAL} and can be adjusted via
     * {@link #setSamplingInterval}.
     */
    public static synchronized CpuUsageThread getCPUThreadUsageThread() {
        if (s_singleton == null) {
            s_singleton = new CpuUsageThread(DEFAULT_SAMPLING_INTERVAL);
        }

        return s_singleton;
    }

    /**
     * Sets the CPU usage sampling interval.
     * 
     * @param samplingInterval
     *            new sampling interval [in milliseconds].
     * @return previous value of the sampling interval.
     * 
     * @throws IllegalArgumentException
     *             if 'samplingInterval' is not positive.
     */
    public synchronized long setSamplingInterval(final long samplingInterval) {
        if (samplingInterval <= 0)
            throw new IllegalArgumentException("must be positive: samplingInterval");

        final long old = m_samplingInterval;
        m_samplingInterval = samplingInterval;

        return old;
    }

    /**
     * Adds a new CPU usage event listener. No uniqueness check is performed.
     */
    public synchronized void addUsageEventListener(final IUsageEventListener listener) {
        if (listener != null)
            m_listeners.add(listener);
    }

    /**
     * Removes a CPU usage event listener [previously added via
     * {@link addUsageEventListener}].
     */
    public synchronized void removeUsageEventListener(final IUsageEventListener listener) {
        if (listener != null)
            m_listeners.remove(listener);
    }

    /**
     * Records and broadcasts periodic CPU usage events. Follows the standard
     * interruptible thread termination model.
     */
    public void run() {
        while (!isInterrupted()) {
            final SystemInformation.CPUUsageSnapshot snapshot = SystemInformation.makeCPUUsageSnapshot();
            notifyListeners(snapshot);

            final long sleepTime;
            synchronized (this) {
                sleepTime = m_samplingInterval;
            }

            // for simplicity, this assumes that all listeners take a short time
            // to process
            // their accept()s; if that is not the case, you might want to
            // compensate for
            // that by adjusting the value of sleepTime:
            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                return;
            }
        }

        // reset the singleton field [Threads are not restartable]:
        synchronized (CpuUsageThread.class) {
            s_singleton = null;
        }
    }

    /**
     * Protected constructor used by {@link getCPUThreadUsageThread} singleton
     * factory method. The created thread will be a daemon thread.
     */
    protected CpuUsageThread(final long samplingInterval) {
        setName(getClass().getName() + " [interval: " + samplingInterval + " ms]");
        setDaemon(true);

        setSamplingInterval(samplingInterval);

        m_listeners = new ArrayList();
    }

    /**
     * Effects the listener notification.
     */
    private void notifyListeners(final SystemInformation.CPUUsageSnapshot event) {
        final ArrayList /* <IUsageEventListener> */listeners;
        synchronized (this) {
            listeners = (ArrayList) m_listeners.clone();
        }

        for (int i = 0; i < listeners.size(); ++i) {
            ((IUsageEventListener) listeners.get(i)).accept(event);
        }
    }

    private long m_samplingInterval; // assertion: non-negative
    private final ArrayList /* <IUsageEventListener> */m_listeners;

    private static CpuUsageThread s_singleton;

}
