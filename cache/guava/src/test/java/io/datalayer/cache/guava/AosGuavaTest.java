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
package io.datalayer.cache.guava;

import static org.junit.Assert.fail;
import io.datalayer.cache.guava.AosGuava;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Sets;

public class AosGuavaTest {
    private static final String KEY_1 = "key1";
    private static final String VALUE_1 = "value1";
    private static final String KEY_2 = "key2";
    private static final String VALUE_2 = "value2";

    private Map<String, Set<String>> cacheToEmit;
    private LoadingCache<String, Set<String>> cache;

    @Test
    public void test1() throws ExecutionException {
        AosGuava aosGuava = new AosGuava();
        try {
            aosGuava.get(KEY_1);
            fail();
        } catch (ExecutionException e) {

        }
        aosGuava.put(KEY_1, VALUE_1);
        aosGuava.get(KEY_1);
        aosGuava.put(KEY_2, VALUE_2);
        aosGuava.get(KEY_2);
    }

    @Test
    public void test2() throws InterruptedException {

        cacheToEmit = new ConcurrentHashMap<String, Set<String>>();

        cache = CacheBuilder.newBuilder() //
                .maximumSize(10) //
                .expireAfterWrite(1, TimeUnit.SECONDS) //
                .removalListener(new RemovalListener<String, Set<String>>() {

                    @Override
                    public void onRemoval(RemovalNotification<String, Set<String>> removalNotification) {

                        System.out.println("Removed key: " + removalNotification.getKey());

                    }
                }).build(new CacheLoader<String, Set<String>>() {

                    public Set<String> load(String visitorId) {

                        return Collections.synchronizedSet(new HashSet<String>());
                    }
                });

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        ScheduledFuture<Void> task = executorService.schedule(new Callable<Void>() {
            @Override
            public Void call() {
                cache.cleanUp();
                return null;
            }
        }, 1, TimeUnit.SECONDS);

        String[] keys = new String[] { "visitorId1", "visitorId2", "visitorId3" };

        Set<String> value0 = Sets.newHashSet("value0");
        cache.put(keys[0], value0);
        Set<String> pingSet0 = null;
        try {
            pingSet0 = cache.get(keys[0]);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println(keys[0] + ": " + pingSet0);

        Set<String> pingSet1 = null;
        try {
            pingSet1 = cache.get(keys[1], new Callable<Set<String>>() {

                @Override
                public Set<String> call() throws Exception {

                    return Collections.synchronizedSet(new HashSet<String>());
                }
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println(keys[1] + ": " + pingSet1);

        Set<String> pingSet2 = null;
        try {
            pingSet2 = cache.get(keys[2], new Callable<Set<String>>() {

                @Override
                public Set<String> call() throws Exception {

                    return Collections.synchronizedSet(new HashSet<String>());
                }
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println(keys[2] + ": " + pingSet2);

        while (cache.asMap().size() > 0) {
            TimeUnit.SECONDS.sleep(1);
            cache.cleanUp();
        }

    }
}
