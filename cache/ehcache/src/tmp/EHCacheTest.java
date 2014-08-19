package io.datalayer.cache.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.junit.Test;

public class EHCacheTest {

    @Test
    public void test() {

        CacheManager manager = CacheManager.create();

        Cache cache = new Cache("testCache", 50000, false, false, 8, 2);
        manager.addCache(cache);
        cache.put(new Element("name", "Aos"));

        System.out.println(cache.get("name").getObjectValue());
        System.out.println(cache.get("name").getObjectValue());
        System.out.println(cache.get("name").getObjectValue());
        System.out.println(cache.get("name").getObjectValue());
        System.out.println(cache.get("name").getObjectValue());
        System.out.println(cache.get("name").getHitCount());

    }

}
