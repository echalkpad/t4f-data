package io.datalayer.redis.jedis.commands;

import io.datalayer.redis.jedis.HostAndPortUtil;
import io.datalayer.redis.jedis.HostAndPortUtil.HostAndPort;

import org.junit.Test;

import redis.clients.jedis.BinaryJedis;

public class ConnectionHandlingCommandsTest extends JedisCommandTestBase {
    protected static HostAndPort hnp = HostAndPortUtil.getRedisServers().get(0);

    @Test
    public void quit() {
        assertEquals("OK", jedis.quit());
    }

    @Test
    public void binary_quit() {
        BinaryJedis bj = new BinaryJedis(hnp.host);
        assertEquals("OK", bj.quit());
    }
}