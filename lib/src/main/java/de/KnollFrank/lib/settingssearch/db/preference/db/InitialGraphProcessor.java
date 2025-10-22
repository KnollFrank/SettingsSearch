package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.PersistableBundleEquality;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;

class InitialGraphProcessor {

    private final Optional<SearchablePreferenceScreenGraphProcessor> graphProcessor;
    private final SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO;
    private final FragmentActivity activityContext;

    public InitialGraphProcessor(final Optional<SearchablePreferenceScreenGraphProcessor> graphProcessor,
                                 final SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO,
                                 final FragmentActivity activityContext) {
        this.graphProcessor = graphProcessor;
        this.searchablePreferenceScreenGraphDAO = searchablePreferenceScreenGraphDAO;
        this.activityContext = activityContext;
    }

    public void processAndPersist(final Optional<SearchablePreferenceScreenGraph> graph, final PersistableBundle configuration) {
        graph.ifPresent(
                _graph -> {
                    if (!PersistableBundleEquality.areBundlesEqual(_graph.configuration(), configuration)) {
                        searchablePreferenceScreenGraphDAO.persist(process(_graph, configuration));
                    }
                });
    }

    private SearchablePreferenceScreenGraph process(final SearchablePreferenceScreenGraph graph,
                                                    final PersistableBundle configuration) {
        return graphProcessor
                .map(_graphProcessor -> _graphProcessor.processGraph(graph, configuration, activityContext))
                .orElse(graph)
                .asGraphHavingConfiguration(configuration);
    }
}
