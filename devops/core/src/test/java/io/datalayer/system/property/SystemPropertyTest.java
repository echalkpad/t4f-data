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
package io.datalayer.system.property;

import java.io.File;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class SystemPropertyTest {

    @Test
    public void testGetProperty() {
        Assert.assertNull(System.getProperty("io.aos.var"));
        // Launch with -Dio.aos.var=xxx to have a non null
        // Assert.assertNotNull(System.getProperty("io.aos.var"));
        Assert.assertNotNull(System.getProperty("java.class.path"));
    }

    @Test
    public void testSetGetProperty() {
        System.setProperty("p1", "v1");
        Assert.assertEquals("v1", System.getProperty("p1"));
    }

    /**
     * Get a list of all the system properties and their values Not available in
     * unsigned Applets, only applications and signed Applets.
     */
    // java.runtime.name=Java(TM) SE Runtime Environment
    // sun.boot.library.path=/Library/Java/JavaVirtualMachines/jdk1.7.0_08.jdk/Contents/Home/jre/lib
    // java.vm.version=23.4-b01
    // user.country.format=GB
    // gopherProxySet=false
    // java.vm.vendor=Oracle Corporation
    // java.vendor.url=http://java.oracle.com/
    // path.separator=:
    // java.vm.name=Java HotSpot(TM) 64-Bit Server VM
    // file.encoding.pkg=sun.io
    // user.country=US
    // sun.java.launcher=SUN_STANDARD
    // sun.os.patch.level=unknown
    // java.vm.specification.name=Java Virtual Machine Specification
    // user.dir=/Users/eric/wrk/t4f/t4f-essentials.git/devops
    // java.runtime.version=1.7.0_08-ea-b04
    // java.awt.graphicsenv=sun.awt.CGraphicsEnvironment
    // java.endorsed.dirs=/Library/Java/JavaVirtualMachines/jdk1.7.0_08.jdk/Contents/Home/jre/lib/endorsed
    // os.arch=x86_64
    // java.io.tmpdir=/var/folders/4j/1c_msh0d00j5wm9mdp0nhzf80000gp/T/
    // line.separator=
    //
    // java.vm.specification.vendor=Oracle Corporation
    // os.name=Mac OS X
    // sun.jnu.encoding=US-ASCII
    // java.library.path=/Users/eric/Library/Java/Extensions:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java:.
    // java.specification.name=Java Platform API Specification
    // java.class.version=51.0
    // sun.management.compiler=HotSpot 64-Bit Tiered Compilers
    // os.version=10.7.4
    // http.nonProxyHosts=local|*.local|169.254/16|*.169.254/16
    // user.home=/Users/eric
    // user.timezone=
    // java.awt.printerjob=sun.lwawt.macosx.CPrinterJob
    // file.encoding=UTF-8
    // java.specification.version=1.7
    // user.name=eric
    // java.class.path=/Users/eric/wrk/t4f/t4f-essentials.git/devops/target/test-classes:/Users/eric/wrk/t4f/t4f-essentials.git/devops/target/classes:/Users/eric/.m2/repository/commons-configuration/commons-configuration/1.9/commons-configuration-1.9.jar:/Users/eric/.m2/repository/commons-lang/commons-lang/2.5/commons-lang-2.5.jar:/Users/eric/.m2/repository/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar:/Users/eric/.m2/repository/junit/junit/4.10/junit-4.10.jar:/Users/eric/.m2/repository/org/hamcrest/hamcrest-core/1.1/hamcrest-core-1.1.jar:/Users/eric/.m2/repository/commons-configuration/commons-configuration/1.9/commons-configuration-1.9-tests.jar:/Users/eric/.m2/repository/commons-jxpath/commons-jxpath/1.3/commons-jxpath-1.3.jar:/Users/eric/.m2/repository/xml-resolver/xml-resolver/1.2/xml-resolver-1.2.jar:/Users/eric/.m2/repository/org/slf4j/slf4j-log4j12/1.6.1/slf4j-log4j12-1.6.1.jar:/Users/eric/.m2/repository/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar:/Users/eric/.m2/repository/log4j/log4j/1.2.16/log4j-1.2.16.jar:/Users/eric/wrk/opt/eclipse-juno-java/configuration/org.eclipse.osgi/bundles/207/2/.cp/:/Users/eric/wrk/opt/eclipse-juno-java/configuration/org.eclipse.osgi/bundles/206/2/.cp/
    // java.vm.specification.version=1.7
    // sun.arch.data.model=64
    // java.home=/Library/Java/JavaVirtualMachines/jdk1.7.0_08.jdk/Contents/Home/jre
    // sun.java.command=org.eclipse.jdt.internal.junit.runner.RemoteTestRunner
    // -version 3 -port 61558 -testLoaderClass
    // org.eclipse.jdt.internal.junit4.runner.JUnit4TestLoader -loaderpluginname
    // org.eclipse.jdt.junit4.runtime -test
    // aos.system.property.SystemPropertyTest:testProperties
    // java.specification.vendor=Oracle Corporation
    // user.language=en
    // awt.toolkit=sun.lwawt.macosx.LWCToolkit
    // java.vm.info=mixed mode
    // java.version=1.7.0_08-ea
    // java.ext.dirs=/Users/eric/Library/Java/Extensions:/Library/Java/JavaVirtualMachines/jdk1.7.0_08.jdk/Contents/Home/jre/lib/ext:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java
    // sun.boot.class.path=/Library/Java/JavaVirtualMachines/jdk1.7.0_08.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_08.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_08.jdk/Contents/Home/jre/lib/sunrsasign.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_08.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_08.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_08.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_08.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_08.jdk/Contents/Home/jre/lib/JObjC.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_08.jdk/Contents/Home/jre/classes
    // java.vendor=Oracle Corporation
    // file.separator=/
    // java.vendor.url.bug=http://bugreport.sun.com/bugreport/
    // sun.cpu.endian=little
    // sun.io.unicode.encoding=UnicodeBig
    // socksNonProxyHosts=local|*.local|169.254/16|*.169.254/16
    // ftp.nonProxyHosts=local|*.local|169.254/16|*.169.254/16
    // sun.cpu.isalist=
    //
    @Test
    @SuppressWarnings("unchecked")
    public void testProperties() {
        Properties properties = System.getProperties();
        for (Enumeration<String> e = (Enumeration<String>) properties.propertyNames(); e.hasMoreElements();) {
            String key = e.nextElement();
            System.out.println(key + "=" + properties.getProperty(key));
        }
    }

    @Test
    public void testFileSeparator() {
        System.out.println(File.separator);
    }

    /**
     * Is Java "Vended"?
     */
    @Test
    public void testJavaVendor() {
        System.out.println(System.getProperty("java.vendor"));
    }

}
