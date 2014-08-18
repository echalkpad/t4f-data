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
package io.datalayer.lucene.demo.parser;

import io.datalayer.lucene.demo.parser.HTMLParser;
import io.datalayer.lucene.demo.parser.ParseException;
import io.datalayer.lucene.demo.parser.TokenMgrError;

import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

class ParserThread extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParserThread.class);

    private final HTMLParser parser;

    ParserThread(HTMLParser p) {
        parser = p;
    }

    @Override
    public void run() {

        try {
            try {
                parser.HTMLDocument();
            }
            catch (ParseException e) {
                LOGGER.info("Parse Aborted: " + e.getMessage());
            }
            catch (TokenMgrError e) {
                LOGGER.info("Parse Aborted: " + e.getMessage());
            }
            finally {
                parser.pipeOut.close();
                synchronized (parser) {
                    parser.summary.setLength(HTMLParser.SUMMARY_LENGTH);
                    parser.titleComplete = true;
                    parser.notifyAll();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
