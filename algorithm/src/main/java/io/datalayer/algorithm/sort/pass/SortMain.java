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

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class SortMain {
	private static SortApplet sortApplet;
	private static SortStub sortStub;

	public static void main(String... args) {

		Frame sortFrame = new Frame("Applet as an Application");

		sortApplet = new SortApplet();
		sortFrame.add(new Panel().add(sortApplet));
		sortFrame.addNotify();

		sortStub = (new SortMain()).new SortStub(args);
		sortApplet.setStub(sortStub);
		sortApplet.init();

		Dimension d = new Dimension(SortApplet.INITIAL_WIDTH, SortApplet.INITIAL_WIDTH);
        sortApplet.setSize(d);
		sortFrame.setSize(d);
	
		sortFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent w) {
				sortStub.setActive(false);
				sortApplet.stop();
				sortApplet.destroy();
				System.exit(0);
			}
		});
		
	    sortFrame.setVisible(true);
        sortStub.setActive(true);
        sortApplet.start();

	}

	class SortStub implements AppletStub {
	    
		private boolean active = false;
		private Hashtable<String, String> ht = new Hashtable<String, String>();
		private AsteroidsAppletContext context;

		public SortStub(String[] args) {
			context = new AsteroidsAppletContext();
			if ((args.length & 1) != 0)
				return;
			for (int i = 0; i < args.length; i += 2)
				ht.put(args[i], args[i + 1]);
		}

		public boolean isActive() {
			return active;
		}

		public URL getDocumentBase() {
			URL u = null;
			try {
				u = new URL("file:/C:./x.html");
			} catch (MalformedURLException e) {
			}
			return u;
		}

		public URL getCodeBase() {
			URL u = null;
			try {
				u = new URL("file:/C:./");
			} catch (MalformedURLException e) {
			}

			return u;
		}

		public String getParameter(String name) {
			return (String) ht.get(name);
		}

		public AppletContext getAppletContext() {
			return context;
		}

		public void appletResize(int width, int height) {
		}

		public void setActive(boolean active) {
			this.active = active;
		}
	}

	class AsteroidsAppletContext implements AppletContext {
		
		public AudioClip getAudioClip(URL url) {
			return Applet.newAudioClip(url);
		}

		public Image getImage(URL url) {
			Toolkit tk = Toolkit.getDefaultToolkit();
			return tk.getImage(url);
		}

		public Applet getApplet(String name) {
            throw new UnsupportedOperationException();
		}

		public Enumeration getApplets() {
            throw new UnsupportedOperationException();
		}

		public void showDocument(URL url) {
			System.out.println("Showing document " + url);
		}

		public void showDocument(URL url, String frame) {
			try {
				showDocument(new URL(url.toString() + frame));
			} catch (MalformedURLException e) {
			}
		}

		public void showStatus(String message) {
			System.out.println(message);
		}

		public void setStream(String key, InputStream stream) throws IOException {
		    throw new UnsupportedOperationException();
		}

		public InputStream getStream(String key) {
            throw new UnsupportedOperationException();
		}

		public Iterator<String> getStreamKeys() {
            throw new UnsupportedOperationException();
		}

	}

}
