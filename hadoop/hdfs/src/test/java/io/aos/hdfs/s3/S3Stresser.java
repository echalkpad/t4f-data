package io.aos.hdfs.s3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class S3Stresser {

    private static Path S3_BASE_PATH = new Path("s3n://qbit-test/harness");
    private static int NUMBER_OF_THREADS = 1000;
    private static String FILE_NAME = "000005_0.lzo";
    private static Path SRC_PATH = new Path(FILE_NAME);

    public static void main(String... args) throws InterruptedException, IOException {

        final FileSystem fs = FileSystem.get(S3_BASE_PATH.toUri(), new Configuration());

        ThreadGroup tg = new ThreadGroup(S3Stresser.class.getName());
        List<Thread> threadList = new ArrayList<Thread>();

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            Thread t = new Thread(tg, new S3Stresser().new S3StresserRunnable(fs, new Path(S3_BASE_PATH,
                    new Integer(i).toString())));
            threadList.add(t);
            t.start();
        }

        Thread.sleep(Long.MAX_VALUE);

    }

    class S3StresserRunnable implements Runnable {

        FileSystem s3Fs;
        Path s3Folder;
        Path s3File;

        public S3StresserRunnable(FileSystem fs, Path folder) {

            this.s3Fs = fs;
            this.s3Folder = folder;
            this.s3File = new Path(folder, FILE_NAME);
        }

        @Override
        public void run() {

            try {

                while (true) {
                    System.out.println("Creating folder " + s3Folder.toString());
                    if (!s3Fs.mkdirs(s3Folder)) {
                        System.err.println("!!! Failed to create folder " + s3Folder + " ...");
                        System.exit(-1);
                    }
                    if (!s3Fs.exists(s3Folder)) {
                        System.err.println("!!! Folder " + s3Folder + " does not exist, but it should...");
                        System.exit(-1);
                    } else {
                        System.out.println("Cool, folder " + s3Folder + " exists.");
                    }
                    // System.out.println("Copying source file to folder " +
                    // s3Folder);
                    // s3Fs.copyFromLocalFile(SRC_PATH, s3File);
                    // System.out.println("Copy done.");
                    // if (!s3Fs.exists(s3File)) {
                    // System.err.println("!!! File  " + s3File
                    // + " does not exist, but it should...");
                    // }
                    // System.out.println("Deleting folder " +
                    // s3Folder.toString());
                    if (!s3Fs.delete(s3Folder, true)) {
                        System.err.println("!!! Failed to delete folder " + s3Folder + " ...");
                        System.exit(-1);
                    }
                    if (s3Fs.exists(s3Folder)) {
                        System.err.println("!!! Folder " + s3Folder + " exists, but it shouldn't...");
                        System.exit(-1);
                    } else {
                        System.out.println("Cool, folder " + s3Folder + " does not exist.");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }

        }

    }

}
