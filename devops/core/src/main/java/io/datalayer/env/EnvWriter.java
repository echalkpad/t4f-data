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
package io.datalayer.env;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Use this if you need to mimick the command line -Dproperty=value.
 * 
 * @see http://stackoverflow.com/questions/318239/how-do-i-set-environment-variables-from-java
 */
public class EnvWriter {
    private Map<String, String> env;
    
    public EnvWriter() {
        this.env = new HashMap<String, String>();
        this.env.putAll(EnvReader.read());
    }
    
    public EnvWriter add(String key, String value) {
        env.put(key, value);
        return this;
    }
    
    public EnvWriter add(Map<String, String> map) {
        env.putAll(map);
        return this;
    }

    @SuppressWarnings("unchecked")
    public void write() throws Exception {
        
        try {

            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");

            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> envField = (Map<String, String>) theEnvironmentField.get(null);
            envField.putAll(env);
            
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> ciEnvField = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            ciEnvField.putAll(env);

        } 
        
        catch (NoSuchFieldException e) {
            try {
                Class<?>[] classes = Collections.class.getDeclaredClasses();
                Map<String, String> sysEnv = System.getenv();
                for (Class<?> cl : classes) {
                    if ("java.util.Collections$UnmodifiableMap".equals(cl .getName())) {
                        Field field = cl.getDeclaredField("m");
                        field.setAccessible(true);
                        Object obj = field.get(sysEnv);
                        Map<String, String> map = (Map<String, String>) obj;
                        map.clear();
                        map.putAll(env);
                    }
                }
            }
            catch (Exception e1) {
                throw e1;
            }
        }
        
        catch (Exception e) {
            throw e;
        }
    
    }

}
