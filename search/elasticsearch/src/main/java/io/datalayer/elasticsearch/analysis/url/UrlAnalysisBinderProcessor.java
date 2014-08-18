package io.datalayer.elasticsearch.analysis.url;

import io.datalayer.elasticsearch.analysis.url.filter.UrlTokenFilterFactory;
import io.datalayer.elasticsearch.analysis.url.tokenizer.UrlTokenizerFactory;

import org.elasticsearch.index.analysis.AnalysisModule;

public class UrlAnalysisBinderProcessor extends AnalysisModule.AnalysisBinderProcessor {

    @Override
    public void processTokenizers(TokenizersBindings tokenizersBindings) {
        tokenizersBindings.processTokenizer("url_tokenizer", UrlTokenizerFactory.class);
    }

    @Override
    public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
        tokenFiltersBindings.processTokenFilter("url_normalizer", UrlTokenFilterFactory.class);
    }

}
