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

package aos.lucene.tools;

import java.io.IOException;
import java.io.File;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Directory;

// From chapter 8
public class CreateSpellCheckerIndex {

  public static void main(String[] args) throws IOException {

    if (args.length != 3) {
      LOGGER.info("Usage: java lia.tools.SpellCheckerTest SpellCheckerIndexDir IndexDir IndexField");
      System.exit(1);
    }

    String spellCheckDir = args[0];
    String indexDir = args[1];
    String indexField = args[2];

    LOGGER.info("Now build SpellChecker index...");
    Directory dir = FSDirectory.open(new File(spellCheckDir));
    SpellChecker spell = new SpellChecker(dir);     //#A
    long startTime = System.currentTimeMillis();
    
    Directory dir2 = FSDirectory.open(new File(indexDir));
    IndexReader r = DirectoryReader.open(dir2);     //#B
    try {
      spell.indexDictionary(new LuceneDictionary(r, indexField));  //#C
    } finally {
      r.close();
    }
    dir.close();
    dir2.close();
    long endTime = System.currentTimeMillis();
    LOGGER.info("  took " + (endTime-startTime) + " milliseconds");
  }
}
/*
  #A Create SpellChecker on its directory
  #B Open IndexReader containing words to add to spell dictionary
  #C Add all words from the specified fields into the spell checker index
*/
