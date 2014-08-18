package io.datalayer.elasticsearch.analysis.url;

import java.util.Collection;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.plugins.AbstractPlugin;

import com.google.common.collect.ImmutableList;

public class UrlAnalysisPlugin extends AbstractPlugin {

    @Override
    public String name() {
        return "analysis-url";
    }

    @Override
    public String description() {
        return "Analysis for URL";
    }

    @Override
    public Collection<Class<? extends Module>> modules() {
        return ImmutableList.<Class<? extends Module>>of(UrlIndicesAnalysisModule.class);
    }

    /**
     * Automatically called with the analysis module.
     */
    public void onModule(AnalysisModule module) {
        module.addProcessor(new UrlAnalysisBinderProcessor());
    }

}
