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
package io.datalayer.data.structure;
import java.util.Iterator;

public class ArrayIterator extends AbstractIterator
{
    protected Object data[];
    protected int head, count;
    protected int current, remaining;

    public ArrayIterator(Object source[])
    {
        this(source,0,source.length);
    }

    public ArrayIterator(Object source[], int first, int size)
    {
        data = source;
        head = first;
        count = size;
        reset();
    }

    public void reset()
    {
        current = head;
        remaining = count;
    }

    public boolean hasNext()
    {
        return remaining > 0;
    }

    public Object next()
    {
        Object temp = data[current];
        current = (current+1)%data.length;
        remaining--;
        return temp;
    }

    public Object get()
    {
        return data[current];
    }
}
