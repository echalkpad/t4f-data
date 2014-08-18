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
package com.wideplay.warp.persist.internal;

import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;

/**
 * Cglib class naming policy for Warp Persist.
 *
 * @author crazybob@google.com (Bob Lee)
 * @author Robbie Vanbrabant
 */
public class WarpPersistNamingPolicy implements NamingPolicy {

  public String getClassName(String prefix, String source, Object key,
                             Predicate names) {
    StringBuffer sb = new StringBuffer();
    sb.append(
        (prefix != null) ?
            (
                prefix.startsWith("java") ?
                    "$" + prefix : prefix
            )
            : "net.sf.cglib.empty.Object"
    );
    sb.append("$$");
    sb.append(source.substring(source.lastIndexOf('.') + 1));
    sb.append("ByWarpPersist$$");
    sb.append(Integer.toHexString(key.hashCode()));
    String base = sb.toString();
    String attempt = base;
    int index = 2;
    while (names.evaluate(attempt)) {
      attempt = base + "_" + index++;
    }

    return attempt;
  }
}
