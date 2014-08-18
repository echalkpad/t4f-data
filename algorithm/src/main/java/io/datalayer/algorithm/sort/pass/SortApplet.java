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
package io.datalayer.algorithm.sort.pass;



import io.datalayer.algorithm.sort.pass.impl.base.SortingAlgorithm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A simple applet class to demonstrate a sort algorithm. You can specify a
 * sorting algorithm using the "alg" attribute. 
 * When you click on the applet, a thread is forked which animates the sorting algorithm.
 */
public class SortApplet extends java.applet.Applet implements Runnable, MouseListener {
    private static final long serialVersionUID = 1L;

    public static int INITIAL_WIDTH = 200;

    private Thread kicker;
    int arr[];  // The array that is being sorted.
    public int h1 = -1; // The high water mark.
    public int h2 = -1; // The low water mark.
    String algName; // The name of the algorithm.
    SortingAlgorithm algorithm;  // The sorting algorithm (or null).
    Dimension initialSize = null;

    /**
     * Fill the array with random numbers from 0..n-1.
     */
    void scramble() {
        initialSize = getSize();
        int a[] = new int[initialSize.height / 2];
        double f = initialSize.width / (double) a.length;

        for (int i = a.length; --i >= 0;) {
            a[i] = (int) (i * f);
        }
        for (int i = a.length; --i >= 0;) {
            int j = (int) (i * Math.random());
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
        arr = a;
    }

    /**
     * Pause a while.
     * 
     * @see SortingAlgorithm
     */
    void pause() {
        pause(-1, -1);
    }

    /**
     * Pause a while, and draw the high water mark.
     * 
     * @see SortingAlgorithm
     */
    void pause(int H1) {
        pause(H1, -1);
    }

    /**
     * Pause a while, and draw the low&high water marks.
     * 
     * @see SortingAlgorithm
     */
    public void pause(int H1, int H2) {
        h1 = H1;
        h2 = H2;
        if (kicker != null) {
            repaint();
        }
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Initialize the applet.
     */
    public void init() {
        String at = getParameter("alg");
        if (at == null) {
            at = "BubbleSort";
        }
        algName = at + "Algorithm";
        scramble();
        resize(100, 100);
        addMouseListener(this);
    }

    public void start() {
        h1 = h2 = -1;
        scramble();
        repaint();
        showStatus(getParameter("alg"));
    }

    /**
     * Deallocate resources of applet.
     */
    public void destroy() {
        removeMouseListener(this);
    }

    /**
     * Paint the array of numbers as a list of horizontal lines of varying
     * lengths.
     */
    public void paint(Graphics g) {
        int a[] = arr;
        int y = 0;
        int deltaY = 0, deltaX = 0, evenY = 0, evenX = 0;

        Dimension currentSize = getSize();
        int currentHeight = currentSize.height;
        int currentWidth = currentSize.width;

        // Check to see if the applet has been resized since it
        // started running. If so, need the deltas to make sure
        // the applet is centered in its containing panel.
        // The evenX and evenY are because the high and low
        // watermarks are calculated from the top, but the rest
        // of the lines are calculated from the bottom, which
        // can lead to a discrepancy if the window is not an
        // even size.
        if (!currentSize.equals(initialSize)) {
            evenY = (currentHeight - initialSize.height) % 2;
            evenX = (currentWidth - initialSize.width) % 2;
            deltaY = (currentHeight - initialSize.height) / 2;
            deltaX = (currentWidth - initialSize.width) / 2;

            if (deltaY < 0) {
                deltaY = 0;
                evenY = 0;
            }
            if (deltaX < 0) {
                deltaX = 0;
                evenX = 0;
            }
        }

        // Erase old lines
        g.setColor(getBackground());
        y = currentHeight - deltaY - 1;
        for (int i = a.length; --i >= 0; y -= 2) {
            g.drawLine(deltaX + arr[i], y, currentWidth, y);
        }

        // Draw new lines
        g.setColor(Color.black);
        y = currentHeight - deltaY - 1;
        for (int i = a.length; --i >= 0; y -= 2) {
            g.drawLine(deltaX, y, deltaX + arr[i], y);
        }

        if (h1 >= 0) {
            g.setColor(Color.red);
            y = deltaY + evenY + h1 * 2 + 1;
            g.drawLine(deltaX, y, deltaX + initialSize.width, y);
        }
        if (h2 >= 0) {
            g.setColor(Color.blue);
            y = deltaY + evenY + h2 * 2 + 1;
            g.drawLine(deltaX, y, deltaX + initialSize.width, y);
        }
    }

    /**
     * Update without erasing the background.
     */
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * Run the sorting algorithm. This method is called by class Thread once the
     * sorting algorithm is started.
     * 
     * @see java.lang.Thread#run
     * @see SortApplet#mouseUp
     */
    public void run() {
        try {
            if (algorithm == null) {
                algorithm = (SortingAlgorithm) Class
                        .forName("io.aos.algo.sort.impl." + algName).newInstance();
                algorithm.setParent(this);
            }
            algorithm.init();
            algorithm.sort(arr);
        } catch (Exception e) {
        }
    }

    /**
     * Stop the applet. Kill any sorting algorithm that is still sorting.
     */
    public synchronized void stop() {
        if (algorithm != null) {
            try {
                algorithm.stop();
            } catch (IllegalThreadStateException e) {
                // ignore this exception
            }
            kicker = null;
        }
    }

    /**
     * For a Thread to actually do the sorting. This routine makes sure we do
     * not simultaneously start several sorts if the user repeatedly clicks on
     * the sort item. It needs to be synchronized with the stop() method because
     * they both manipulate the common kicker variable.
     */
    private synchronized void startSort() {
        if (kicker == null || !kicker.isAlive()) {
            kicker = new Thread(this);
            kicker.start();
        }
    }

    public void mouseClicked(MouseEvent e) {
        showStatus(getParameter("alg"));
    }

    public void mousePressed(MouseEvent e) {
    }

    /**
     * The user clicked in the applet. Start the clock!
     */
    public void mouseReleased(MouseEvent e) {
        startSort();
        e.consume();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public String getAppletInfo() {
        return "Title: SortDemo \nAuthor: James Gosling 1.17f, 10 Apr 1995 \nA simple applet class to demonstrate a sort algorithm.  \nYou can specify a sorting algorithm using the 'alg' attribute.  \nWhen you click on the applet, a thread is forked which animates \nthe sorting algorithm.";
    }

    public String[][] getParameterInfo() {
        String[][] info = { {
                "alg",
                "string",
                "The name of the algorithm to run.  You can choose from the provided algorithms or suppply your own, as long as the classes are runnable as threads and their names end in 'Algorithm.'  BubbleSort is the default.  Example:  Use 'QSort' to run the QSortAlgorithm class." } };
        return info;
    }

}
