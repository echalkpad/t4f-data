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
// (c) 1997-2006 duane a. bailey
package io.datalayer.data.structure5;
/**
 * A utility class that can be used to determine the version of software
 * currently being used. 
 * Simply run this class from the command prompt to see the version info.
 */
public class Version
{
    public final static String Id = "$Id: Version.java 36 2007-08-29 15:39:24Z bailey $";
    public final static String name = "structure";
    public final static String author = "duane a. bailey";

    public static void main(String... args)
    {
        int major;
        String date;
        String info;
        String s = Id;
        int s1 = s.indexOf(" ",s.indexOf(" ")+1);
        int s2 = s.indexOf(" ",s1+1);
        major = Integer.valueOf(s.substring(s1+1,s2));
        int c = s.indexOf(":",s.indexOf(":",s2)+1);
        date = s.substring(s2+1,c);
        int year = Integer.valueOf(date.substring(0,4));
        info = "package "+name+", version "+major+" ("+date+"), (c) 1997-"+year+" "+author;        
        if (args.length != 0)
        {
            if (args[0].equals("-d")) System.out.println(date);
            else if (args[0].equals("-M")) System.out.println(major);
            else if (args[0].equals("-p")) System.out.println(name);
            else if (args[0].equals("-a")) System.out.println(author);
        } else System.out.println(info);
    }
}





