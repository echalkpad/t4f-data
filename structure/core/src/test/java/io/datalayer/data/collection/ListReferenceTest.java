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
package io.datalayer.data.collection;

import io.datalayer.data.list.Item;
import io.datalayer.data.list.ItemContainer;
import io.datalayer.data.list.ItemReplacedContainer;
import junit.framework.Assert;

import org.junit.Test;

public class ListReferenceTest {
    
    @Test
    public void testReplaceReference() {
        
        Item item = new Item("item1");
        ItemContainer ic1 = new ItemContainer(item, new Item("item2"));
        ItemContainer ic2 = new ItemContainer(item, new Item("item2"));
        ItemReplacedContainer irc = new ItemReplacedContainer(ic1.getList());

        Assert.assertEquals(ic1.getList().get(0), irc.getList().get(0));
        Assert.assertSame(ic1.getList().get(0), irc.getList().get(0));
        
        Assert.assertNotSame(ic2.getList().get(0), irc.getList().get(0));
    
        Assert.assertNotSame(ic1.getList().get(0), ic2.getList().get(0));
    
    }

}
