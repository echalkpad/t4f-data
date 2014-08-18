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
package io.datalayer.algorithm.sort.pass.impl;

import io.datalayer.algorithm.sort.pass.impl.base.SortingAlgorithm;

/**
 * A bi-directional bubble sort demonstration algorithm
 */
public class BubbleBidirSortAlgorithm extends SortingAlgorithm {
    public void sort(int a[]) throws Exception {
        int j;
        int limit = a.length;
        int st = -1;
        while (st < limit) {
            st++;
            limit--;
            boolean swapped = false;
            for (j = st; j < limit; j++) {
                if (stopRequested) {
                    return;
                }
                if (a[j] > a[j + 1]) {
                    int T = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = T;
                    swapped = true;
                }
                pause(st, limit);
            }
            if (!swapped) {
                return;
            } else
                swapped = false;
            for (j = limit; --j >= st;) {
                if (stopRequested) {
                    return;
                }
                if (a[j] > a[j + 1]) {
                    int T = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = T;
                    swapped = true;
                }
                pause(st, limit);
            }
            if (!swapped) {
                return;
            }
        }
    }
}
