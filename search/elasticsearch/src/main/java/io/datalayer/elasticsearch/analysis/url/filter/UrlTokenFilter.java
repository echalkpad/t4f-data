package io.datalayer.elasticsearch.analysis.url.filter;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

public final class UrlTokenFilter extends TokenFilter {
    private TokenStream input;

    protected UrlTokenFilter(TokenStream input) {
        super(input);
        this.input = input;
    }

    @Override
    public boolean incrementToken() throws IOException {
        return false;
    }

}
