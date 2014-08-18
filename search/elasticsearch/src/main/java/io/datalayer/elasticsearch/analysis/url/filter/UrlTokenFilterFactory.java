package io.datalayer.elasticsearch.analysis.url.filter;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;
import org.elasticsearch.index.settings.IndexSettings;

public class UrlTokenFilterFactory extends AbstractTokenFilterFactory {

    private final String name;

    @Inject
    public UrlTokenFilterFactory(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
        this.name = "url_normalizer";
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new UrlTokenFilter(tokenStream);
    }

}
