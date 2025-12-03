package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.Objects;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

class InitialGraphTransformer<C> {

    private final Optional<SearchablePreferenceScreenGraphTransformer<C>> graphTransformer;
    private final SearchablePreferenceScreenGraphRepository<C> graphRepository;
    private final FragmentActivity activityContext;
    private final ConfigurationBundleConverter<C> configurationBundleConverter;

    public InitialGraphTransformer(final Optional<SearchablePreferenceScreenGraphTransformer<C>> graphTransformer,
                                   final SearchablePreferenceScreenGraphRepository<C> graphRepository,
                                   final FragmentActivity activityContext,
                                   final ConfigurationBundleConverter<C> configurationBundleConverter) {
        this.graphTransformer = graphTransformer;
        this.graphRepository = graphRepository;
        this.activityContext = activityContext;
        this.configurationBundleConverter = configurationBundleConverter;
    }

    public void transformAndPersist(final Optional<SearchablePreferenceScreenGraph> graph, final C configuration) {
        graph.ifPresent(
                _graph -> {
                    if (!Objects.equals(configuration, configurationBundleConverter.convertBackward(_graph.configuration()))) {
                        graphRepository.persist(process(_graph, configuration));
                    }
                });
    }

    private SearchablePreferenceScreenGraph process(final SearchablePreferenceScreenGraph graph,
                                                    final C configuration) {
        return graphTransformer
                .map(_graphTransformer -> _graphTransformer.transformGraph(graph, configuration, activityContext))
                .orElse(graph)
                .asGraphHavingConfiguration(configurationBundleConverter.convertForward(configuration));
    }
}
