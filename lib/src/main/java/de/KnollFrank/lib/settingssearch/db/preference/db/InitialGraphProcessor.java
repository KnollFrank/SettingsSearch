package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.Optional;

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

    public void processAndPersist(final Optional<SearchablePreferenceScreenGraph> graph) {
        graph.ifPresent(
                _graph -> {
                    if (!_graph.processed()) {
                        searchablePreferenceScreenGraphDAO.persist(process(_graph));
                    }
                });
    }

    private SearchablePreferenceScreenGraph process(final SearchablePreferenceScreenGraph graph) {
        return graphProcessor
                .map(_graphProcessor -> _graphProcessor.processGraph(graph, activityContext))
                .orElse(graph)
                .asProcessedGraph();
    }
}
