package io.datalayer.elasticsearch.analysis.url.tokenizer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see http://www.supermind.org/blog/1078/extracting-second-level-domains-and-top-level-domains-tld-from-a-url-in-java
 */
public class DomainExtractor {

    private StringBuilder sb = new StringBuilder();
    private Pattern pattern;

    public void init() {

        try {

            ArrayList<String> terms = new ArrayList<String>();

            BufferedReader br =
                    new BufferedReader(new InputStreamReader(getClass()
                            .getResourceAsStream("effective_tld_names.txt")));
            String s = null;
            while ((s = br.readLine()) != null) {
                s = s.trim();
                if (s.length() == 0 || s.startsWith("//") || s.startsWith("!"))
                    continue;
                terms.add(s);
            }
            Collections.sort(terms, new StringLengthComparator());
            for (String t : terms) {
                add(t);
            }
            compile();
            br.close();
        } 
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
        
    }

    protected void add(String s) {
        s = s.replace(".", "\\.");
        s = "\\." + s;
        if (s.startsWith("*")) {
            s = s.replace("*", ".+");
            sb.append(s).append("|");
        } 
        else {
            sb.append(s).append("|");
        }
    }

    public void compile() {
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.insert(0, "[^.]+?(");
        sb.append(")$");
        pattern = Pattern.compile(sb.toString());
        sb = null;
    }

    public String extractTLD(String host) {
        Matcher m = pattern.matcher(host);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    public String extract2LD(String host) {
        Matcher m = pattern.matcher(host);
        if (m.find()) {
            return m.group(0);
        }
        return null;
    }

    public static class StringLengthComparator implements Comparator<String> {

        public int compare(String s1, String s2) {
            if (s1.length() > s2.length()) {
                return -1;
            }
            if (s1.length() < s2.length()) {
                return 1;
            }
            return 0;
        }

    }
    
}
