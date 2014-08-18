package io.aos.elasticsearch.analysis;

import static org.elasticsearch.common.settings.ImmutableSettings.Builder.EMPTY_SETTINGS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import io.aos.elasticsearch.analysis.url.UrlAnalysisBinderProcessor;
import io.aos.elasticsearch.analysis.url.filter.UrlTokenFilterFactory;
import io.aos.elasticsearch.analysis.url.tokenizer.UrlTokenizerFactory;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.inject.ModulesBuilder;
import org.elasticsearch.common.settings.SettingsModule;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.EnvironmentModule;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.IndexNameModule;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.index.settings.IndexSettingsModule;
import org.elasticsearch.indices.analysis.IndicesAnalysisModule;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleUrlAnalysisTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleUrlAnalysisTest.class);

    @Test
    public void testUrlAnalysisInjection() throws IOException {
        
        Index index = new Index("test");

        Injector parentInjector = new ModulesBuilder().add( //
                new SettingsModule(EMPTY_SETTINGS), //
                new EnvironmentModule( //
                        new Environment(EMPTY_SETTINGS)), //
                        new IndicesAnalysisModule()) //
                .createInjector();

        Injector injector = new ModulesBuilder().add( //
                new IndexSettingsModule(index, EMPTY_SETTINGS), //
                new IndexNameModule(index), //
                new AnalysisModule(EMPTY_SETTINGS, //
                        parentInjector.getInstance( //
                                IndicesAnalysisService.class)) //
                .addProcessor(new UrlAnalysisBinderProcessor())) //
                .createChildInjector(parentInjector);

        AnalysisService analysisService = injector.getInstance(AnalysisService.class);

        TokenizerFactory tokenizerFactory = analysisService.tokenizer("url_tokenizer");
        assertThat(tokenizerFactory, instanceOf(UrlTokenizerFactory.class));

        TokenFilterFactory filterFactory = analysisService.tokenFilter("url_normalizer");
        assertThat(filterFactory, instanceOf(UrlTokenFilterFactory.class));
        
        String lower = "http://aos.io/about";
        String upper = "http://aos.io/about";
        TokenStream tsLower = tokenizerFactory.create(new StringReader(lower));
        TokenStream tsUpper = tokenizerFactory.create(new StringReader(upper));
        assertCollation(tsUpper, tsLower, 0, true, false);
        
        TokenStream tsLower2 = filterFactory.create(new KeywordTokenizer(new StringReader(lower)));
        TokenStream tsUpper2 = filterFactory.create(new KeywordTokenizer(new StringReader(upper)));
        assertCollation(tsUpper2, tsLower2, 0, false, false);
    
    }
    
    private void assertCollation(TokenStream stream1, TokenStream stream2, int comparison, boolean isIncrement1, boolean isIncrement2) throws IOException {
        CharTermAttribute term1 = stream1.addAttribute(CharTermAttribute.class);
        CharTermAttribute term2 = stream2.addAttribute(CharTermAttribute.class);
        assertThat(stream1.incrementToken(), equalTo(isIncrement1));
        assertThat(stream2.incrementToken(), equalTo(isIncrement1));
        LOGGER.info("Term1=" + term1.toString());
        LOGGER.info("Term1=" + term2.toString());
        assertThat(Integer.signum(term1.toString().compareTo(term2.toString())), equalTo(Integer.signum(comparison)));
        assertThat(stream1.incrementToken(), equalTo(isIncrement2));
        assertThat(stream2.incrementToken(), equalTo(isIncrement2));
    }

}
