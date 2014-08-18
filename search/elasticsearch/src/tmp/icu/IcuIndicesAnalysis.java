package io.aos.elasticsearch.analysis.icu;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.icu.ICUFoldingFilter;
import org.apache.lucene.analysis.icu.ICUTransformFilter;
import org.apache.lucene.analysis.icu.segmentation.ICUTokenizer;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.PreBuiltTokenFilterFactoryFactory;
import org.elasticsearch.index.analysis.PreBuiltTokenizerFactoryFactory;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;

import com.ibm.icu.text.Collator;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.Transliterator;

/**
 * Registers indices level analysis components so, if not explicitly configured, will be shared
 * among all indices.
 */
public class IcuIndicesAnalysis extends AbstractComponent {

    @Inject
    public IcuIndicesAnalysis(Settings settings, IndicesAnalysisService indicesAnalysisService) {
        super(settings);

        indicesAnalysisService.tokenizerFactories().put("icu_tokenizer", new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
            @Override
            public String name() {
                return "icu_tokenizer";
            }

            @Override
            public Tokenizer create(Reader reader) {
//                return new ICUTokenizer(reader);
                return null;
            }
        }));

        indicesAnalysisService.tokenFilterFactories().put("icu_normalizer", new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
            @Override
            public String name() {
                return "icu_normalizer";
            }

            @Override
            public TokenStream create(TokenStream tokenStream) {
                return new org.apache.lucene.analysis.icu.ICUNormalizer2Filter(tokenStream, Normalizer2.getInstance(null, "nfkc_cf", Normalizer2.Mode.COMPOSE));
            }
        }));


        indicesAnalysisService.tokenFilterFactories().put("icu_folding", new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
            @Override
            public String name() {
                return "icu_folding";
            }

            @Override
            public TokenStream create(TokenStream tokenStream) {
                return new ICUFoldingFilter(tokenStream);
            }
        }));

        indicesAnalysisService.tokenFilterFactories().put("icu_collation", new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
            @Override
            public String name() {
                return "icu_collation";
            }

            @Override
            public TokenStream create(TokenStream tokenStream) {
                return null;
//                return new ICUCollationKeyFilter(tokenStream, Collator.getInstance());
            }
        }));

        indicesAnalysisService.tokenFilterFactories().put("icu_transform", new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
            @Override
            public String name() {
                return "icu_transform";
            }

            @Override
            public TokenStream create(TokenStream tokenStream) {
                return new ICUTransformFilter(tokenStream, Transliterator.getInstance("Null", Transliterator.FORWARD));
            }
        }));
    }
}
