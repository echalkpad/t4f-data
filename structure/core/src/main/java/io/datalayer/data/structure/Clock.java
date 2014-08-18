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
// A simple clock for use in timing things.
// (c) 1998, 2001 duane a. bailey
package io.datalayer.data.structure;

/**
 * A simple object for measuring time.
 * This Clock allows one to measure time between events.  Events may be
 * from several milliseconds to several seconds apart.  When measuring
 * events that take considerable time (> 1/10th of a second) it is important
 * to counteract the effect of scheduling of other system processes.
 *
 * A Clock has the following operations:
 *  <ul>
 *  <li> reset - clear the clock and reset accumulated time to zero
 *  <li> start - start the clock and accumulate time
 *  <li> stop  - stop the clock (and therefore stop accumulating time)
 *  <li> read  - read a stopped clock to get accumulated time in milliseconds.
 * </ul>
 *
 * typical use:
 * <pre>
 *    Clock timer = new Clock();
 *    timer.start();
 *    for (i = 0; i < 100000; i++);
 *    timer.stop();
 *    System.out.println("Time to count to 100000: "+timer.read()+" seconds.");
 * </pre>
 *
 * @version $Id: Clock.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 */
public class Clock
{
    // we use a native-code library for structures
    /**
     * An indication of whether or not the clock is running
     */
    protected boolean running;  // is the clock on?
    /**
     * The millisecond that the clock started.
     */
    protected long strt;        // starting millisecond count
    /**
     * The total number of milliseconds elapsed.
     */
    protected long accum;       // total milliseconds

    /**
     * Constructs a stopwatch for timing events to the milliseconds.
     *
     * @post returns a stopped clock
     */
    public Clock()
    {
        running = false;
        strt = 0;
        accum = 0;
    }

    /**
     * Start the clock running.
     *
     * @post clock is stopped
     * @pre starts clock, begins measuring possibly accumulated time
     */
    public void start()
    {
        running = true;
        strt = System.currentTimeMillis();
    }

    /**
     * Stop the clock.  Time does not accumulate.
     *
     * @pre clock is running
     * @post stops clock, and accumulates time
     */
    public void stop()
    {
        running = false;
        accum += (System.currentTimeMillis()-strt);
    }

    /**
     * Read the value on the stop watch.
     *
     * @pre clock is stopped
     * @post returns the accumulated time on the clock
     * 
     * @return A double representing the number of seconds elapsed.
     */
    public double read()
    {
        return (double)accum/(double)1000.0;
    }

    /**
     * Resets the time on the clock to zero.
     *
     * @post stops running clock and clears the accumulated time
     */
    public void reset()
    {
        running = false;
        accum = 0;
    }

    /**
     * Generates string representation of clock.
     *
     * @post returns a string representation of the clock
     * 
     * @return A string representing this clock.
     */
    public String toString()
    {
        return "<Clock: "+read()+" seconds>";
    }
}

