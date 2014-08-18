package io.datalayer.elasticsearch.analysis.url;

import org.elasticsearch.common.inject.AbstractModule;

public class UrlIndicesAnalysisModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UrlIndicesAnalysis.class).asEagerSingleton();
    }
    
}
