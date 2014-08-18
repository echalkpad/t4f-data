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
package io.datalayer.cassandra.support;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.db.commitlog.CommitLog;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.service.CassandraDaemon;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AosEmbeddedCassandra {
    private static final Logger log = LoggerFactory.getLogger(AosEmbeddedCassandra.class);

    private static final String TMP = "tmp";

    private final String yamlFile;
    static CassandraDaemon cassandraDaemon;

    public AosEmbeddedCassandra() {
        this("/cassandra.yaml");
    }

    public AosEmbeddedCassandra(String yamlFile) {
        this.yamlFile = yamlFile;
    }

    static ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Set embedded cassandra up and spawn it in a new thread.
     * 
     * @throws TTransportException
     * @throws IOException
     * @throws InterruptedException
     */
    public void setup() throws TTransportException, IOException, InterruptedException, ConfigurationException {
        // delete tmp dir first
        rmdir(TMP);
        // make a tmp dir and copy cassandra.yaml and log4j.properties to it
        copy("/log4j.properties", TMP);
        copy(yamlFile, TMP);
        // yamlFile may be user defined; make sure to grab the copy directly
        // from TMP
        System.setProperty("cassandra.config", "file:" + TMP + "/" + yamlFile.substring(yamlFile.lastIndexOf("/") + 1));
        System.setProperty("log4j.configuration", "file:" + TMP + "/log4j.properties");
        System.setProperty("cassandra-foreground", "true");

        cleanupAndLeaveDirs();
        loadSchemaFromYaml();
        // loadYamlTables();
        log.info("Starting executor");

        executor.execute(new CassandraRunner());
        log.info("Started executor");
        try {
            TimeUnit.SECONDS.sleep(3);
            log.info("Done sleeping");
        }
        catch (InterruptedException e) {
            throw new AssertionError(e);
        }
    }

    public static void teardown() throws IOException {
        // if ( cassandraDaemon != null )
        // cassandraDaemon.stop();
        executor.shutdown();
        executor.shutdownNow();
        log.info("Teardown complete");
        rmdir(TMP);
    }

    private static void rmdir(String dir) throws IOException {
        File dirFile = new File(dir);
        if (dirFile.exists()) {
            FileUtils.deleteRecursive(new File(dir));
        }
    }

    /**
     * Copies a resource from within the jar to a directory.
     * 
     * @param resource
     * @param directory
     * @throws IOException
     */
    private static void copy(String resource, String directory) throws IOException {
        mkdir(directory);
        InputStream is = AosEmbeddedCassandra.class.getResourceAsStream(resource);
        String fileName = resource.substring(resource.lastIndexOf("/") + 1);
        File file = new File(directory + System.getProperty("file.separator") + fileName);
        OutputStream out = new FileOutputStream(file);
        byte buf[] = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        is.close();
    }

    /**
     * Creates a directory
     * 
     * @param dir
     * @throws IOException
     */
    private static void mkdir(String dir) throws IOException {
        FileUtils.createDirectory(dir);
    }

    public static void cleanupAndLeaveDirs() throws IOException {
        mkdirs();
        cleanup();
        mkdirs();
        CommitLog.instance.resetUnsafe(); // cleanup screws w/ CommitLog, this
                                          // brings it back to safe state
    }

    public static void cleanup() throws IOException {
        // clean up commitlog
        String[] directoryNames = { DatabaseDescriptor.getCommitLogLocation(), };
        for (String dirName : directoryNames) {
            File dir = new File(dirName);
            if (!dir.exists())
                throw new RuntimeException("No such directory: " + dir.getAbsolutePath());
            FileUtils.deleteRecursive(dir);
        }

        // clean up data directory which are stored as data directory/table/data
        // files
        for (String dirName : DatabaseDescriptor.getAllDataFileLocations()) {
            File dir = new File(dirName);
            if (!dir.exists())
                throw new RuntimeException("No such directory: " + dir.getAbsolutePath());
            FileUtils.deleteRecursive(dir);
        }
    }

    public static void mkdirs() {
        DatabaseDescriptor.createAllDirectories();
    }

    public static void loadSchemaFromYaml() {
        AosEmbeddedSchemaLoader.loadSchema();

    }

    class CassandraRunner implements Runnable {

        @Override
        public void run() {

            cassandraDaemon = new CassandraDaemon();

            cassandraDaemon.activate();

        }

    }

}
