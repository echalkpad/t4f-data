package io.datalayer.redis.jedis.commands;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
import redis.clients.util.SafeEncoder;

public class SortedSetCommandsTest extends JedisCommandTestBase {
    final byte[] bfoo = { 0x01, 0x02, 0x03, 0x04 };
    final byte[] bbar = { 0x05, 0x06, 0x07, 0x08 };
    final byte[] bcar = { 0x09, 0x0A, 0x0B, 0x0C };
    final byte[] ba = { 0x0A };
    final byte[] bb = { 0x0B };
    final byte[] bc = { 0x0C };

    @Test
    public void zadd() {
        long status = jedis.zadd("foo", 1d, "a");
        assertEquals(1, status);

        status = jedis.zadd("foo", 10d, "b");
        assertEquals(1, status);

        status = jedis.zadd("foo", 0.1d, "c");
        assertEquals(1, status);

        status = jedis.zadd("foo", 2d, "a");
        assertEquals(0, status);

        // Binary
        long bstatus = jedis.zadd(bfoo, 1d, ba);
        assertEquals(1, bstatus);

        bstatus = jedis.zadd(bfoo, 10d, bb);
        assertEquals(1, bstatus);

        bstatus = jedis.zadd(bfoo, 0.1d, bc);
        assertEquals(1, bstatus);

        bstatus = jedis.zadd(bfoo, 2d, ba);
        assertEquals(0, bstatus);

    }

    @Test
    public void zrange() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 10d, "b");
        jedis.zadd("foo", 0.1d, "c");
        jedis.zadd("foo", 2d, "a");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("c");
        expected.add("a");

        Set<String> range = jedis.zrange("foo", 0, 1);
        assertEquals(expected, range);

        expected.add("b");
        range = jedis.zrange("foo", 0, 100);
        assertEquals(expected, range);

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 10d, bb);
        jedis.zadd(bfoo, 0.1d, bc);
        jedis.zadd(bfoo, 2d, ba);

        Set<byte[]> bexpected = new LinkedHashSet<byte[]>();
        bexpected.add(bc);
        bexpected.add(ba);

        Set<byte[]> brange = jedis.zrange(bfoo, 0, 1);
        assertEquals(bexpected, brange);

        bexpected.add(bb);
        brange = jedis.zrange(bfoo, 0, 100);
        assertEquals(bexpected, brange);

    }

    @Test
    public void zrevrange() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 10d, "b");
        jedis.zadd("foo", 0.1d, "c");
        jedis.zadd("foo", 2d, "a");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("b");
        expected.add("a");

        Set<String> range = jedis.zrevrange("foo", 0, 1);
        assertEquals(expected, range);

        expected.add("c");
        range = jedis.zrevrange("foo", 0, 100);
        assertEquals(expected, range);

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 10d, bb);
        jedis.zadd(bfoo, 0.1d, bc);
        jedis.zadd(bfoo, 2d, ba);

        Set<byte[]> bexpected = new LinkedHashSet<byte[]>();
        bexpected.add(bb);
        bexpected.add(ba);

        Set<byte[]> brange = jedis.zrevrange(bfoo, 0, 1);
        assertEquals(bexpected, brange);

        bexpected.add(bc);
        brange = jedis.zrevrange(bfoo, 0, 100);
        assertEquals(bexpected, brange);

    }

    @Test
    public void zrem() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 2d, "b");

        long status = jedis.zrem("foo", "a");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("b");

        assertEquals(1, status);
        assertEquals(expected, jedis.zrange("foo", 0, 100));

        status = jedis.zrem("foo", "bar");

        assertEquals(0, status);

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 2d, bb);

        long bstatus = jedis.zrem(bfoo, ba);

        Set<byte[]> bexpected = new LinkedHashSet<byte[]>();
        bexpected.add(bb);

        assertEquals(1, bstatus);
        assertEquals(bexpected, jedis.zrange(bfoo, 0, 100));

        bstatus = jedis.zrem(bfoo, bbar);

        assertEquals(0, bstatus);

    }

    @Test
    public void zincrby() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 2d, "b");

        double score = jedis.zincrby("foo", 2d, "a");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("a");
        expected.add("b");

        assertEquals(3d, score, 0);
        assertEquals(expected, jedis.zrange("foo", 0, 100));

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 2d, bb);

        double bscore = jedis.zincrby(bfoo, 2d, ba);

        Set<byte[]> bexpected = new LinkedHashSet<byte[]>();
        bexpected.add(bb);
        bexpected.add(ba);

        assertEquals(3d, bscore, 0);
        assertEquals(bexpected, jedis.zrange(bfoo, 0, 100));

    }

    @Test
    public void zrank() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 2d, "b");

        long rank = jedis.zrank("foo", "a");
        assertEquals(0, rank);

        rank = jedis.zrank("foo", "b");
        assertEquals(1, rank);

        assertNull(jedis.zrank("car", "b"));

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 2d, bb);

        long brank = jedis.zrank(bfoo, ba);
        assertEquals(0, brank);

        brank = jedis.zrank(bfoo, bb);
        assertEquals(1, brank);

        assertNull(jedis.zrank(bcar, bb));

    }

    @Test
    public void zrevrank() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 2d, "b");

        long rank = jedis.zrevrank("foo", "a");
        assertEquals(1, rank);

        rank = jedis.zrevrank("foo", "b");
        assertEquals(0, rank);

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 2d, bb);

        long brank = jedis.zrevrank(bfoo, ba);
        assertEquals(1, brank);

        brank = jedis.zrevrank(bfoo, bb);
        assertEquals(0, brank);

    }

    @Test
    public void zrangeWithScores() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 10d, "b");
        jedis.zadd("foo", 0.1d, "c");
        jedis.zadd("foo", 2d, "a");

        Set<Tuple> expected = new LinkedHashSet<Tuple>();
        expected.add(new Tuple("c", 0.1d));
        expected.add(new Tuple("a", 2d));

        Set<Tuple> range = jedis.zrangeWithScores("foo", 0, 1);
        assertEquals(expected, range);

        expected.add(new Tuple("b", 10d));
        range = jedis.zrangeWithScores("foo", 0, 100);
        assertEquals(expected, range);

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 10d, bb);
        jedis.zadd(bfoo, 0.1d, bc);
        jedis.zadd(bfoo, 2d, ba);

        Set<Tuple> bexpected = new LinkedHashSet<Tuple>();
        bexpected.add(new Tuple(bc, 0.1d));
        bexpected.add(new Tuple(ba, 2d));

        Set<Tuple> brange = jedis.zrangeWithScores(bfoo, 0, 1);
        assertEquals(bexpected, brange);

        bexpected.add(new Tuple(bb, 10d));
        brange = jedis.zrangeWithScores(bfoo, 0, 100);
        assertEquals(bexpected, brange);

    }

    @Test
    public void zrevrangeWithScores() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 10d, "b");
        jedis.zadd("foo", 0.1d, "c");
        jedis.zadd("foo", 2d, "a");

        Set<Tuple> expected = new LinkedHashSet<Tuple>();
        expected.add(new Tuple("b", 10d));
        expected.add(new Tuple("a", 2d));

        Set<Tuple> range = jedis.zrevrangeWithScores("foo", 0, 1);
        assertEquals(expected, range);

        expected.add(new Tuple("c", 0.1d));
        range = jedis.zrevrangeWithScores("foo", 0, 100);
        assertEquals(expected, range);

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 10d, bb);
        jedis.zadd(bfoo, 0.1d, bc);
        jedis.zadd(bfoo, 2d, ba);

        Set<Tuple> bexpected = new LinkedHashSet<Tuple>();
        bexpected.add(new Tuple(bb, 10d));
        bexpected.add(new Tuple(ba, 2d));

        Set<Tuple> brange = jedis.zrevrangeWithScores(bfoo, 0, 1);
        assertEquals(bexpected, brange);

        bexpected.add(new Tuple(bc, 0.1d));
        brange = jedis.zrevrangeWithScores(bfoo, 0, 100);
        assertEquals(bexpected, brange);

    }

    @Test
    public void zcard() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 10d, "b");
        jedis.zadd("foo", 0.1d, "c");
        jedis.zadd("foo", 2d, "a");

        long size = jedis.zcard("foo");
        assertEquals(3, size);

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 10d, bb);
        jedis.zadd(bfoo, 0.1d, bc);
        jedis.zadd(bfoo, 2d, ba);

        long bsize = jedis.zcard(bfoo);
        assertEquals(3, bsize);

    }

    @Test
    public void zscore() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 10d, "b");
        jedis.zadd("foo", 0.1d, "c");
        jedis.zadd("foo", 2d, "a");

        Double score = jedis.zscore("foo", "b");
        assertEquals((Double) 10d, score);

        score = jedis.zscore("foo", "c");
        assertEquals((Double) 0.1d, score);

        score = jedis.zscore("foo", "s");
        assertNull(score);

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 10d, bb);
        jedis.zadd(bfoo, 0.1d, bc);
        jedis.zadd(bfoo, 2d, ba);

        Double bscore = jedis.zscore(bfoo, bb);
        assertEquals((Double) 10d, bscore);

        bscore = jedis.zscore(bfoo, bc);
        assertEquals((Double) 0.1d, bscore);

        bscore = jedis.zscore(bfoo, SafeEncoder.encode("s"));
        assertNull(bscore);

    }

    @Test
    public void zcount() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 10d, "b");
        jedis.zadd("foo", 0.1d, "c");
        jedis.zadd("foo", 2d, "a");

        long result = jedis.zcount("foo", 0.01d, 2.1d);

        assertEquals(2, result);

        result = jedis.zcount("foo", "(0.01", "+inf");

        assertEquals(3, result);
        
        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 10d, bb);
        jedis.zadd(bfoo, 0.1d, bc);
        jedis.zadd(bfoo, 2d, ba);

        long bresult = jedis.zcount(bfoo, 0.01d, 2.1d);

        assertEquals(2, bresult);

        bresult = jedis.zcount(bfoo, SafeEncoder.encode("(0.01"), SafeEncoder.encode("+inf"));

        assertEquals(3, bresult);
    }

    @Test
    public void zrangebyscore() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 10d, "b");
        jedis.zadd("foo", 0.1d, "c");
        jedis.zadd("foo", 2d, "a");

        Set<String> range = jedis.zrangeByScore("foo", 0d, 2d);

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("c");
        expected.add("a");

        assertEquals(expected, range);

        range = jedis.zrangeByScore("foo", 0d, 2d, 0, 1);

        expected = new LinkedHashSet<String>();
        expected.add("c");

        assertEquals(expected, range);

        range = jedis.zrangeByScore("foo", 0d, 2d, 1, 1);
        Set<String> range2 = jedis.zrangeByScore("foo", "-inf", "(2");
        assertEquals(expected, range2);

        expected = new LinkedHashSet<String>();
        expected.add("a");

        assertEquals(expected, range);

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 10d, bb);
        jedis.zadd(bfoo, 0.1d, bc);
        jedis.zadd(bfoo, 2d, ba);

        Set<byte[]> brange = jedis.zrangeByScore(bfoo, 0d, 2d);

        Set<byte[]> bexpected = new LinkedHashSet<byte[]>();
        bexpected.add(bc);
        bexpected.add(ba);

        assertEquals(bexpected, brange);

        brange = jedis.zrangeByScore(bfoo, 0d, 2d, 0, 1);

        bexpected = new LinkedHashSet<byte[]>();
        bexpected.add(bc);

        assertEquals(bexpected, brange);

        brange = jedis.zrangeByScore(bfoo, 0d, 2d, 1, 1);
        Set<byte[]> brange2 = jedis.zrangeByScore(bfoo, SafeEncoder
                .encode("-inf"), SafeEncoder.encode("(2"));
        assertEquals(bexpected, brange2);

        bexpected = new LinkedHashSet<byte[]>();
        bexpected.add(ba);

        assertEquals(bexpected, brange);

    }

    @Test
    public void zrevrangebyscore() {
        jedis.zadd("foo", 1.0d, "a");
        jedis.zadd("foo", 2.0d, "b");
        jedis.zadd("foo", 3.0d, "c");
        jedis.zadd("foo", 4.0d, "d");
        jedis.zadd("foo", 5.0d, "e");

        Set<String> range = jedis.zrevrangeByScore("foo", 3d,
                Double.NEGATIVE_INFINITY, 0, 1);
        Set<String> expected = new LinkedHashSet<String>();
        expected.add("c");

        assertEquals(expected, range);

        range = jedis.zrevrangeByScore("foo", 3.5d, Double.NEGATIVE_INFINITY,
                0, 2);
        expected = new LinkedHashSet<String>();
        expected.add("c");
        expected.add("b");

        assertEquals(expected, range);

        range = jedis.zrevrangeByScore("foo", 3.5d, Double.NEGATIVE_INFINITY,
                1, 1);
        expected = new LinkedHashSet<String>();
        expected.add("b");

        assertEquals(expected, range);

        range = jedis.zrevrangeByScore("foo", 4d, 2d);
        expected = new LinkedHashSet<String>();
        expected.add("d");
        expected.add("c");
        expected.add("b");

        assertEquals(expected, range);

        range = jedis.zrevrangeByScore("foo", "+inf", "(4");
        expected = new LinkedHashSet<String>();
        expected.add("e");

        assertEquals(expected, range);

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 10d, bb);
        jedis.zadd(bfoo, 0.1d, bc);
        jedis.zadd(bfoo, 2d, ba);

        Set<byte[]> brange = jedis.zrevrangeByScore(bfoo, 2d, 0d);

        Set<byte[]> bexpected = new LinkedHashSet<byte[]>();
        bexpected.add(bc);
        bexpected.add(ba);

        assertEquals(bexpected, brange);

        brange = jedis.zrevrangeByScore(bfoo, 2d, 0d, 0, 1);

        bexpected = new LinkedHashSet<byte[]>();
        bexpected.add(ba);

        assertEquals(bexpected, brange);

        Set<byte[]> brange2 = jedis.zrevrangeByScore(bfoo, SafeEncoder
                .encode("+inf"), SafeEncoder.encode("(2"));

        bexpected = new LinkedHashSet<byte[]>();
        bexpected.add(bb);

        assertEquals(bexpected, brange2);

        brange = jedis.zrevrangeByScore(bfoo, 2d, 0d, 1, 1);
        bexpected = new LinkedHashSet<byte[]>();
        bexpected.add(bc);

        assertEquals(bexpected, brange);
    }

    @Test
    public void zrangebyscoreWithScores() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 10d, "b");
        jedis.zadd("foo", 0.1d, "c");
        jedis.zadd("foo", 2d, "a");

        Set<Tuple> range = jedis.zrangeByScoreWithScores("foo", 0d, 2d);

        Set<Tuple> expected = new LinkedHashSet<Tuple>();
        expected.add(new Tuple("c", 0.1d));
        expected.add(new Tuple("a", 2d));

        assertEquals(expected, range);

        range = jedis.zrangeByScoreWithScores("foo", 0d, 2d, 0, 1);

        expected = new LinkedHashSet<Tuple>();
        expected.add(new Tuple("c", 0.1d));

        assertEquals(expected, range);

        range = jedis.zrangeByScoreWithScores("foo", 0d, 2d, 1, 1);

        expected = new LinkedHashSet<Tuple>();
        expected.add(new Tuple("a", 2d));

        assertEquals(expected, range);

        // Binary

        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 10d, bb);
        jedis.zadd(bfoo, 0.1d, bc);
        jedis.zadd(bfoo, 2d, ba);

        Set<Tuple> brange = jedis.zrangeByScoreWithScores(bfoo, 0d, 2d);

        Set<Tuple> bexpected = new LinkedHashSet<Tuple>();
        bexpected.add(new Tuple(bc, 0.1d));
        bexpected.add(new Tuple(ba, 2d));

        assertEquals(bexpected, brange);

        brange = jedis.zrangeByScoreWithScores(bfoo, 0d, 2d, 0, 1);

        bexpected = new LinkedHashSet<Tuple>();
        bexpected.add(new Tuple(bc, 0.1d));

        assertEquals(bexpected, brange);

        brange = jedis.zrangeByScoreWithScores(bfoo, 0d, 2d, 1, 1);

        bexpected = new LinkedHashSet<Tuple>();
        bexpected.add(new Tuple(ba, 2d));

        assertEquals(bexpected, brange);

    }

    @Test
    public void zrevrangebyscoreWithScores() {
        jedis.zadd("foo", 1.0d, "a");
        jedis.zadd("foo", 2.0d, "b");
        jedis.zadd("foo", 3.0d, "c");
        jedis.zadd("foo", 4.0d, "d");
        jedis.zadd("foo", 5.0d, "e");

        Set<Tuple> range = jedis.zrevrangeByScoreWithScores("foo", 3d,
                Double.NEGATIVE_INFINITY, 0, 1);
        Set<Tuple> expected = new LinkedHashSet<Tuple>();
        expected.add(new Tuple("c", 3.0d));

        assertEquals(expected, range);

        range = jedis.zrevrangeByScoreWithScores("foo", 3.5d,
                Double.NEGATIVE_INFINITY, 0, 2);
        expected = new LinkedHashSet<Tuple>();
        expected.add(new Tuple("c", 3.0d));
        expected.add(new Tuple("b", 2.0d));

        assertEquals(expected, range);

        range = jedis.zrevrangeByScoreWithScores("foo", 3.5d,
                Double.NEGATIVE_INFINITY, 1, 1);
        expected = new LinkedHashSet<Tuple>();
        expected.add(new Tuple("b", 2.0d));

        assertEquals(expected, range);

        range = jedis.zrevrangeByScoreWithScores("foo", 4d, 2d);
        expected = new LinkedHashSet<Tuple>();
        expected.add(new Tuple("d", 4.0d));
        expected.add(new Tuple("c", 3.0d));
        expected.add(new Tuple("b", 2.0d));

        assertEquals(expected, range);

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 10d, bb);
        jedis.zadd(bfoo, 0.1d, bc);
        jedis.zadd(bfoo, 2d, ba);

        Set<Tuple> brange = jedis.zrevrangeByScoreWithScores(bfoo, 2d, 0d);

        Set<Tuple> bexpected = new LinkedHashSet<Tuple>();
        bexpected.add(new Tuple(bc, 0.1d));
        bexpected.add(new Tuple(ba, 2d));

        assertEquals(bexpected, brange);

        brange = jedis.zrevrangeByScoreWithScores(bfoo, 2d, 0d, 0, 1);

        bexpected = new LinkedHashSet<Tuple>();
        bexpected.add(new Tuple(ba, 2d));

        assertEquals(bexpected, brange);

        brange = jedis.zrevrangeByScoreWithScores(bfoo, 2d, 0d, 1, 1);

        bexpected = new LinkedHashSet<Tuple>();
        bexpected.add(new Tuple(bc, 0.1d));

        assertEquals(bexpected, brange);
    }
    
    @Test
    public void zremrangeByRank() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 10d, "b");
        jedis.zadd("foo", 0.1d, "c");
        jedis.zadd("foo", 2d, "a");

        long result = jedis.zremrangeByRank("foo", 0, 0);

        assertEquals(1, result);

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("a");
        expected.add("b");

        assertEquals(expected, jedis.zrange("foo", 0, 100));

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 10d, bb);
        jedis.zadd(bfoo, 0.1d, bc);
        jedis.zadd(bfoo, 2d, ba);

        long bresult = jedis.zremrangeByRank(bfoo, 0, 0);

        assertEquals(1, bresult);

        Set<byte[]> bexpected = new LinkedHashSet<byte[]>();
        bexpected.add(ba);
        bexpected.add(bb);

        assertEquals(bexpected, jedis.zrange(bfoo, 0, 100));

    }

    @Test
    public void zremrangeByScore() {
        jedis.zadd("foo", 1d, "a");
        jedis.zadd("foo", 10d, "b");
        jedis.zadd("foo", 0.1d, "c");
        jedis.zadd("foo", 2d, "a");

        long result = jedis.zremrangeByScore("foo", 0, 2);

        assertEquals(2, result);

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("b");

        assertEquals(expected, jedis.zrange("foo", 0, 100));

        // Binary
        jedis.zadd(bfoo, 1d, ba);
        jedis.zadd(bfoo, 10d, bb);
        jedis.zadd(bfoo, 0.1d, bc);
        jedis.zadd(bfoo, 2d, ba);

        long bresult = jedis.zremrangeByScore(bfoo, 0, 2);

        assertEquals(2, bresult);

        Set<byte[]> bexpected = new LinkedHashSet<byte[]>();
        bexpected.add(bb);

        assertEquals(bexpected, jedis.zrange(bfoo, 0, 100));
    }

    @Test
    public void zunionstore() {
        jedis.zadd("foo", 1, "a");
        jedis.zadd("foo", 2, "b");
        jedis.zadd("bar", 2, "a");
        jedis.zadd("bar", 2, "b");

        long result = jedis.zunionstore("dst", "foo", "bar");

        assertEquals(2, result);

        Set<Tuple> expected = new LinkedHashSet<Tuple>();
        expected.add(new Tuple("b", new Double(4)));
        expected.add(new Tuple("a", new Double(3)));

        assertEquals(expected, jedis.zrangeWithScores("dst", 0, 100));

        // Binary
        jedis.zadd(bfoo, 1, ba);
        jedis.zadd(bfoo, 2, bb);
        jedis.zadd(bbar, 2, ba);
        jedis.zadd(bbar, 2, bb);

        long bresult = jedis.zunionstore(SafeEncoder.encode("dst"), bfoo, bbar);

        assertEquals(2, bresult);

        Set<Tuple> bexpected = new LinkedHashSet<Tuple>();
        bexpected.add(new Tuple(bb, new Double(4)));
        bexpected.add(new Tuple(ba, new Double(3)));

        assertEquals(bexpected, jedis.zrangeWithScores(SafeEncoder
                .encode("dst"), 0, 100));
    }

    @Test
    public void zunionstoreParams() {
        jedis.zadd("foo", 1, "a");
        jedis.zadd("foo", 2, "b");
        jedis.zadd("bar", 2, "a");
        jedis.zadd("bar", 2, "b");

        ZParams params = new ZParams();
        params.weights(2, 2);
        params.aggregate(ZParams.Aggregate.SUM);
        long result = jedis.zunionstore("dst", params, "foo", "bar");

        assertEquals(2, result);

        Set<Tuple> expected = new LinkedHashSet<Tuple>();
        expected.add(new Tuple("b", new Double(8)));
        expected.add(new Tuple("a", new Double(6)));

        assertEquals(expected, jedis.zrangeWithScores("dst", 0, 100));

        // Binary
        jedis.zadd(bfoo, 1, ba);
        jedis.zadd(bfoo, 2, bb);
        jedis.zadd(bbar, 2, ba);
        jedis.zadd(bbar, 2, bb);

        ZParams bparams = new ZParams();
        bparams.weights(2, 2);
        bparams.aggregate(ZParams.Aggregate.SUM);
        long bresult = jedis.zunionstore(SafeEncoder.encode("dst"), bparams,
                bfoo, bbar);

        assertEquals(2, bresult);

        Set<Tuple> bexpected = new LinkedHashSet<Tuple>();
        bexpected.add(new Tuple(bb, new Double(8)));
        bexpected.add(new Tuple(ba, new Double(6)));

        assertEquals(bexpected, jedis.zrangeWithScores(SafeEncoder
                .encode("dst"), 0, 100));
    }

    @Test
    public void zinterstore() {
        jedis.zadd("foo", 1, "a");
        jedis.zadd("foo", 2, "b");
        jedis.zadd("bar", 2, "a");

        long result = jedis.zinterstore("dst", "foo", "bar");

        assertEquals(1, result);

        Set<Tuple> expected = new LinkedHashSet<Tuple>();
        expected.add(new Tuple("a", new Double(3)));

        assertEquals(expected, jedis.zrangeWithScores("dst", 0, 100));

        // Binary
        jedis.zadd(bfoo, 1, ba);
        jedis.zadd(bfoo, 2, bb);
        jedis.zadd(bbar, 2, ba);

        long bresult = jedis.zinterstore(SafeEncoder.encode("dst"), bfoo, bbar);

        assertEquals(1, bresult);

        Set<Tuple> bexpected = new LinkedHashSet<Tuple>();
        bexpected.add(new Tuple(ba, new Double(3)));

        assertEquals(bexpected, jedis.zrangeWithScores(SafeEncoder
                .encode("dst"), 0, 100));
    }

    @Test
    public void zintertoreParams() {
        jedis.zadd("foo", 1, "a");
        jedis.zadd("foo", 2, "b");
        jedis.zadd("bar", 2, "a");

        ZParams params = new ZParams();
        params.weights(2, 2);
        params.aggregate(ZParams.Aggregate.SUM);
        long result = jedis.zinterstore("dst", params, "foo", "bar");

        assertEquals(1, result);

        Set<Tuple> expected = new LinkedHashSet<Tuple>();
        expected.add(new Tuple("a", new Double(6)));

        assertEquals(expected, jedis.zrangeWithScores("dst", 0, 100));

        // Binary
        jedis.zadd(bfoo, 1, ba);
        jedis.zadd(bfoo, 2, bb);
        jedis.zadd(bbar, 2, ba);

        ZParams bparams = new ZParams();
        bparams.weights(2, 2);
        bparams.aggregate(ZParams.Aggregate.SUM);
        long bresult = jedis.zinterstore(SafeEncoder.encode("dst"), bparams,
                bfoo, bbar);

        assertEquals(1, bresult);

        Set<Tuple> bexpected = new LinkedHashSet<Tuple>();
        bexpected.add(new Tuple(ba, new Double(6)));

        assertEquals(bexpected, jedis.zrangeWithScores(SafeEncoder
                .encode("dst"), 0, 100));
    }

    @Test
    public void tupleCompare() {
        Tuple t1 = new Tuple("foo", 1d);
        Tuple t2 = new Tuple("bar", 2d);

        assertEquals(-1, t1.compareTo(t2));
        assertEquals(1, t2.compareTo(t1));
        assertEquals(0, t2.compareTo(t2));
    }
}