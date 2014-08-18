package io.datalayer.zookeeper;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.netflix.curator.RetryPolicy;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import com.netflix.curator.test.TestingCluster;

public class CuratorTest {
    
    private static final TestingCluster testingCluster = new TestingCluster(2);
    private static CuratorFramework curatorFramework;

    @BeforeClass
    public static void beforeClass() throws Exception {
        testingCluster.start();
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        testingCluster.close();
        curatorFramework.close();
    }

    public void test() throws IOException {
    }
    
}
