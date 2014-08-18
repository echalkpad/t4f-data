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
// The version of this release.
// (c) 2001 duane a. bailey
package io.datalayer.data.structure;
/**
 * A utility class that can be used to determine the version of software
 * currently being used.
 */
public class Version
{
    public final static int major = 2;
    public final static int minor = 9120000;
    public final static String name = "structure";
    public final static int year = 2001;
    public final static String author = "duane a. bailey";
    public final static String info = "package "+name+", version "+major+"."+minor+", (c) "+year+" "+author;

    public static void main(String... args)
    {
        System.out.println(info);
    }
}
