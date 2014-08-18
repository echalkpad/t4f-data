package io.datalayer.lucene.helper;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.LimitTokenCountAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class AosAnalyser {
    public static final Analyzer NO_LIMIT_TOKEN_COUNT_STANDARD_ANALYSER;
    public static final Analyzer NO_LIMIT_TOKEN_COUNT_SIMPLE_ANALYSER;
    public static final Analyzer NO_LIMIT_TOKEN_COUNT_WHITE_SPACE_ANALYSER;
    public static final Analyzer ONE_TOKEN_COUNT_WHITE_SPACE_ANALYSER;

    static {
        NO_LIMIT_TOKEN_COUNT_STANDARD_ANALYSER = new LimitTokenCountAnalyzer(new StandardAnalyzer(Version.LUCENE_46), Integer.MAX_VALUE);
        NO_LIMIT_TOKEN_COUNT_SIMPLE_ANALYSER = new LimitTokenCountAnalyzer(new SimpleAnalyzer(Version.LUCENE_46), Integer.MAX_VALUE);
        NO_LIMIT_TOKEN_COUNT_WHITE_SPACE_ANALYSER = new LimitTokenCountAnalyzer(new WhitespaceAnalyzer(Version.LUCENE_46), Integer.MAX_VALUE);
        ONE_TOKEN_COUNT_WHITE_SPACE_ANALYSER = new LimitTokenCountAnalyzer(new WhitespaceAnalyzer(Version.LUCENE_46), 1);
    }

}
