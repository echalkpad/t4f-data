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
package io.datalayer.lucene.filter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.lucene.queryparser.xml.DOMUtils;
import org.apache.lucene.queryparser.xml.FilterBuilder;
import org.apache.lucene.queryparser.xml.ParserException;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NumericRangeFilter;
import org.w3c.dom.Element;

/**
 * #A Extract field, time unit, from and to
 * 
 * #B Parse date/times
 * 
 * #C Create NumericRangeFilter
 */
public class TimeAgoFilterBuilder implements FilterBuilder {

    private static HashMap<String, Integer> timeUnits = new HashMap<String, Integer>();
    static {
        timeUnits.put("days", Calendar.DAY_OF_YEAR);
        timeUnits.put("months", Calendar.MONTH);
        timeUnits.put("years", Calendar.YEAR);
    }

    @Override
    public Filter getFilter(Element element) throws ParserException {

        String fieldName = DOMUtils.getAttributeWithInheritanceOrFail(element, "fieldName");
        String timeUnit = DOMUtils.getAttribute(element, "timeUnit", "days");
        Integer calUnit = timeUnits.get(timeUnit);
        if (calUnit == null) {
            throw new ParserException("Illegal time unit:" + timeUnit + " - must be days, months or years");
        }
        int agoStart = DOMUtils.getAttribute(element, "from", 0);
        int agoEnd = DOMUtils.getAttribute(element, "to", 0);
        if (agoStart < agoEnd) {
            int oldAgoStart = agoStart;
            agoStart = agoEnd;
            agoEnd = oldAgoStart;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar start = Calendar.getInstance();
        start.add(calUnit, agoStart * -1);

        Calendar end = Calendar.getInstance();
        end.add(calUnit, agoEnd * -1);

        return NumericRangeFilter.newIntRange(fieldName, Integer.valueOf(sdf.format(start.getTime())),
                Integer.valueOf(sdf.format(end.getTime())), true, true);
    }

}
