package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.Objects;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

class InitialGraphProcessor<C> {

    private final Optional<SearchablePreferenceScreenGraphProcessor<C>> graphProcessor;
    private final SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO;
    private final FragmentActivity activityContext;
    private final ConfigurationBundleConverter<C> configurationBundleConverter;

    public InitialGraphProcessor(final Optional<SearchablePreferenceScreenGraphProcessor<C>> graphProcessor,
                                 final SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO,
                                 final FragmentActivity activityContext,
                                 final ConfigurationBundleConverter<C> configurationBundleConverter) {
        this.graphProcessor = graphProcessor;
        this.searchablePreferenceScreenGraphDAO = searchablePreferenceScreenGraphDAO;
        this.activityContext = activityContext;
        this.configurationBundleConverter = configurationBundleConverter;
    }

    public void processAndPersist(final Optional<SearchablePreferenceScreenGraph> graph, final C configuration) {
        graph.ifPresent(
                _graph -> {
                    if (!Objects.equals(configuration, configurationBundleConverter.doBackward(_graph.configuration()))) {
                        searchablePreferenceScreenGraphDAO.persist(process(_graph, configuration));
                    }
                });
    }

    private SearchablePreferenceScreenGraph process(final SearchablePreferenceScreenGraph graph,
                                                    final C configuration) {
        return graphProcessor
                .map(_graphProcessor -> _graphProcessor.processGraph(graph, configuration, activityContext))
                .orElse(graph)
                .asGraphHavingConfiguration(configurationBundleConverter.doForward(configuration));
    }
}
