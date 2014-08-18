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
// An implemention of an association that can be marked "reserved".
// Keys are not comparable.
// (c) 2006 duane a. bailey

package io.datalayer.data.structure5;

/**
 * Implements an association that can be marked "reserved".  Reserved
 * associations can not be read without exception.  Unreserved associations
 * act normally.
 *
 * @version $Id: HashAssociation.java 22 2006-08-21 19:27:26Z bailey $
 * @author 2006 duane a. bailey
 * @see Hashtable, Association
 */
public class HashAssociation<K,V> extends Association<K,V>
{
    protected boolean reserved;
    public HashAssociation(K key, V value)
    {
        super(key,value);
        reserved = false;
    }

    public HashAssociation(K key)
    {
        this(key,null);
    }

    public V getValue()
    {
        Assert.pre(!reserved, "Reserved HashAssociations may not be read.");
        return super.getValue();
    }

    public K getKey()
    {
        Assert.pre(!reserved, "Reserved HashAssociations may not be read.");
        return super.getKey();
    }

    public V setValue(V value)
    {
        Assert.pre(!reserved, "Reserved HashAssociations may not be written.");
        return super.setValue(value);
    }

    public boolean reserved()
    {
        return reserved;
    }

    public void reserve()
    {
        Assert.pre(!reserved,"HashAssociation reserved twice.");
        reserved = true;
    }
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        if (reserved()) {
            s.append("<ReservedAssociation: RESERVED>");
        } else {
            s.append("<ReservedAssociation: "+getKey()+"="+getValue()+">");
        }
        return s.toString();
    }
}

