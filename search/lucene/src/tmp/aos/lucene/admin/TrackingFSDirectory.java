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
package aos.lucene.admin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.store.BufferedIndexInput;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 * Drop-in replacement for FSDirectory that tracks open files.
 * 
 * #A Holds all open file names #B Returns total open file count #C Opens
 * tracking input #D Opens tracking output #E Tracks eventual close
 */
public class TrackingFSDirectory extends SimpleFSDirectory {

    private final Set<String> openOutputs = new HashSet<String>();
    private final Set<String> openInputs = new HashSet<String>();

    public TrackingFSDirectory(File path) throws IOException {
        super(path);
    }

    synchronized public int getFileDescriptorCount() {
        return openOutputs.size() + openInputs.size();
    }

    synchronized private void report(String message) {
        LOGGER.info(System.currentTimeMillis() + ": " + message + "; total " + getFileDescriptorCount());
    }

    synchronized public IndexInput openInput(String name) throws IOException {
        return openInput(name, BufferedIndexInput.BUFFER_SIZE);
    }

    synchronized public IndexInput openInput(String name, int bufferSize) throws IOException {
        openInputs.add(name);
        report("Open Input: " + name);
        return new TrackingFSIndexInput(name, bufferSize);
    }

    synchronized public IndexOutput createOutput(String name) throws IOException {
        openOutputs.add(name);
        report("Open Output: " + name);
        File file = new File(getFile(), name);
        if (file.exists() && !file.delete())
            throw new IOException("Cannot overwrite: " + file);
        return new TrackingFSIndexOutput(name);
    }

    protected class TrackingFSIndexInput extends SimpleFSIndexInput {
        String name;

        public TrackingFSIndexInput(String name, int bufferSize) throws IOException {
            super(new File(getFile(), name), bufferSize, getReadChunkSize());
            this.name = name;
        }

        boolean cloned = false;

        @Override
        public Object clone() {
            TrackingFSIndexInput clone = (TrackingFSIndexInput) super.clone();
            clone.cloned = true;
            return clone;
        }

        @Override
        public void close() throws IOException {
            super.close();
            if (!cloned) {
                synchronized (TrackingFSDirectory.this) {
                    openInputs.remove(name);
                }
            }
            report("Close Input: " + name);
        }
    }

    protected class TrackingFSIndexOutput extends SimpleFSIndexOutput {
        String name;

        public TrackingFSIndexOutput(String name) throws IOException {
            super(new File(getFile(), name));
            this.name = name;
        }

        public void close() throws IOException {
            super.close();
            synchronized (TrackingFSDirectory.this) {
                openOutputs.remove(name);
            }
            report("Close Output: " + name);
        }
    }
}
