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

import java.lang.reflect.Method;
import java.text.DecimalFormat;


// ----------------------------------------------------------------------------
/**
 * A demo of {@link SystemInformation} and {@link CpuUsageThread} functionality.
 * This class starts an instance of CPU usage monitoring thread, registers itself
 * as a CPU usage event listener, and launches another application.
 * Usage:
 * <PRE>
 *   java -Djava.library.path=(silib native lib dir) CPUmon AnotherApp ...args...
 * </PRE>
 *
 * @author (C) 2002, Vladimir Roubtsov
 */
public class CpuMonitor implements CpuUsageThread.IUsageEventListener
{
    // public: ................................................................
    
    
    public static void main (final String [] args) throws Exception
    {
        if (args.length == 0)
        {
            throw new IllegalArgumentException ("usage: CPUmon <app_main_class> <app_main_args...>");
        }
        
        final CpuUsageThread monitor = CpuUsageThread.getCPUThreadUsageThread ();
        final CpuMonitor _this = new CpuMonitor ();
        
        final Class app = Class.forName (args [0]);
        final Method appmain = app.getMethod ("main", new Class [] {String[].class});
        final String [] appargs = new String [args.length - 1];
        System.arraycopy (args, 1, appargs, 0, appargs.length);
        
        monitor.addUsageEventListener (_this);
        monitor.start ();
        appmain.invoke (null, new Object [] {appargs});
    }
        
    public CpuMonitor ()
    {
        m_PID = SystemInformation.getProcessID ();
        
        m_format = new DecimalFormat ();
        m_format.setMaximumFractionDigits (1);
    }
    
    /**
     * Implements {@link CpuUsageThread.IUsageEventListener}. Simply
     * prints the current process PID and CPU usage since last snapshot
     * to System.out.
     */
    public void accept (final SystemInformation.CPUUsageSnapshot event)
    {
        if (m_prevSnapshot != null)
        {
            System.out.println ("[PID: " + m_PID + "] CPU usage: " +
                m_format.format (100.0 * SystemInformation.getProcessCPUUsage (m_prevSnapshot, event)) + "%");
        }
       
        m_prevSnapshot = event;
    }

    // protected: .............................................................

    // package: ...............................................................

    // private: ...............................................................
    
    
    private final int m_PID; // process ID
    private final DecimalFormat m_format;
    private SystemInformation.CPUUsageSnapshot m_prevSnapshot;

} // end of class
// ----------------------------------------------------------------------------
