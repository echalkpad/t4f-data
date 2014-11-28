package io.aos.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSClient;


public class AosDfsClient {

    public static void main(String[] args) throws IOException {
        DFSClient dfsClient = new DFSClient(new Configuration());
    }

}
