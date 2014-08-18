package io.datalayer.zookeeper;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AosZookeeperServerFromConfigLauncher {
    private final static Logger LOGGER = LoggerFactory.getLogger(AosZookeeperServerFromConfigLauncher.class);

    public static void main(String... args) throws IOException, InterruptedException {

        Properties startupProperties = new Properties();
        startupProperties.load(AosZookeeperServerFromConfigLauncher.class.getClassLoader().getResourceAsStream("zoo.cfg"));

        QuorumPeerConfig quorumConfiguration = new QuorumPeerConfig();
        try {
            quorumConfiguration.parseProperties(startupProperties);
        } catch (Exception e) {
            LOGGER.error("ZooKeeper Properties parsing error.", e);
            throw new RuntimeException("ZooKeeper Properties parsing error.", e);
        }

        final ZooKeeperServerMain zooKeeperServer = new ZooKeeperServerMain();
        final ServerConfig configuration = new ServerConfig();
        configuration.readFrom(quorumConfiguration);

        new Thread() {
            public void run() {
                try {
                    zooKeeperServer.runFromConfig(configuration);
                    while (true) {
                        try {
                            Thread.sleep(Long.MAX_VALUE);
                        } catch (InterruptedException e) {
                            LOGGER.error("ZooKeeper Failed", e);
                            throw new RuntimeException("ZooKeeper Failed", e);
                        }
                    }
                } catch (IOException e) {
                    LOGGER.error("ZooKeeper Failed", e);
                    throw new RuntimeException("ZooKeeper Failed", e);
                }
            }
        }.start();

        LOGGER.info("Zookeeper Server is started.");

        TimeUnit.MINUTES.sleep(3);

    }

}
