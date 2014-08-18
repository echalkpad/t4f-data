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

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * #A Create SpellCheck from existing spell check index #B Sets the string
 * distance metric used to rank the suggestions #C Generate respelled candidates
 */
public class SpellCheckerExample {

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            LOGGER.info("Usage: java lia.tools.SpellCheckerTest SpellCheckerIndexDir wordToRespell");
            System.exit(1);
        }

        String spellCheckDir = args[0];
        String wordToRespell = args[1];

        Directory dir = FSDirectory.open(new File(spellCheckDir));
        if (!IndexReader.indexExists(dir)) {
            LOGGER.info("\nERROR: No spellchecker index at path \"" + spellCheckDir
                    + "\"; please run CreateSpellCheckerIndex first\n");
            System.exit(1);
        }
        SpellChecker spell = new SpellChecker(dir); // #A

        spell.setStringDistance(new LevensteinDistance()); // #B
        // spell.setStringDistance(new JaroWinklerDistance());

        String[] suggestions = spell.suggestSimilar(wordToRespell, 5); // #C
        LOGGER.info(suggestions.length + " suggestions for '" + wordToRespell + "':");
        for (String suggestion : suggestions)
            LOGGER.info("  " + suggestion);
    }
}
