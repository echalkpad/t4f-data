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
package aos.lucene.search.msc;

import java.io.IOException;

import org.apache.lucene.queryParser.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.*;
import org.apache.lucene.search.*;
import org.apache.lucene.util.*;

import io.aos.lucene.search.ext.queryparser.NumericQueryParserTest.NumericDateRangeQueryParser;

import javax.servlet.http.*;
import javax.servlet.*;

// From chapter 6
public class SearchServletFragment extends HttpServlet {

  private IndexSearcher searcher;

  protected void doGet(HttpServletRequest request,
                       HttpServletResponse response) 
      throws ServletException, IOException {
    
    QueryParser parser = new NumericDateRangeQueryParser(Version.LUCENE_46,
                                                  "contents",
        new StandardAnalyzer(Version.LUCENE_46));
    
    parser.setLocale(request.getLocale());
    parser.setDateResolution(DateTools.Resolution.DAY);

    Query query = null;
    try {
      query = parser.parse(request.getParameter("q"));
    } catch (ParseException e) {
      e.printStackTrace(System.err);  
    }

    TopDocs docs = searcher.search(query, 10);        
  }
  /*
    1 Handle exception
    2 Perfom search and render results
  */
}
