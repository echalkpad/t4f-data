package io.datalayer.data.map;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class JdkMapTest {

    @Test
    public void test() {

        Map<String, String> map = new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;
            {
                put("key1", "value1");
                put("key2", "value2");
                put("key3", "value3");
            }
        };

        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }

    }

    @Test(expected = ConcurrentModificationException.class)
    public void testRemove() {

        Map<String, String> map = new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;
            {
                put("key1", "value1");
                put("key2", "value2");
                put("key3", "value3");
            }
        };

        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
            map.remove(entry.getKey());
        }

    }

}
