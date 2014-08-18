package io.aos.elasticsearch.analysis.icu;

import java.util.Collection;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.plugins.AbstractPlugin;

import com.google.common.collect.ImmutableList;

public class AnalysisICUPlugin extends AbstractPlugin {

    @Override
    public String name() {
        return "analysis-icu";
    }

    @Override
    public String description() {
        return "UTF related ICU analysis support";
    }

    @Override
    public Collection<Class<? extends Module>> modules() {
        return ImmutableList.<Class<? extends Module>>of(IcuIndicesAnalysisModule.class);
    }

    /**
     * Automatically called with the analysis module.
     */
    public void onModule(AnalysisModule module) {
        module.addProcessor(new IcuAnalysisBinderProcessor());
    }
}
