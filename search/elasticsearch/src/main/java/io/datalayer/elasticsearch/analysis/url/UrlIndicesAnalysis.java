package io.datalayer.elasticsearch.analysis.url;

import io.datalayer.elasticsearch.analysis.url.tokenizer.UrlTokenizer;

import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.PreBuiltTokenizerFactoryFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;

/**
 * Registers indices level analysis components so, if not explicitly configured, will be shared
 * among all indices.
 */
public class UrlIndicesAnalysis extends AbstractComponent {

    @Inject
    public UrlIndicesAnalysis(Settings settings, IndicesAnalysisService indicesAnalysisService) {

        super(settings);

        indicesAnalysisService.tokenizerFactories().put("url_tokenizer", new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
            
            @Override
            public String name() {
                return "url_tokenizer";
            }

            @Override
            public Tokenizer create(Reader reader) {
//                return new UrlTokenizer(reader);
                return null;
           }
            
        }));

     }

}
