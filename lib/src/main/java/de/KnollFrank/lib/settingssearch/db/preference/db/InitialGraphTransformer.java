package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.Objects;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenGraphTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

class InitialGraphTransformer<C> {

    private final Optional<SearchablePreferenceScreenGraphTransformer<C>> graphTransformer;
    private final SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO;
    private final FragmentActivity activityContext;
    private final ConfigurationBundleConverter<C> configurationBundleConverter;

    public InitialGraphTransformer(final Optional<SearchablePreferenceScreenGraphTransformer<C>> graphTransformer,
                                   final SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO,
                                   final FragmentActivity activityContext,
                                   final ConfigurationBundleConverter<C> configurationBundleConverter) {
        this.graphTransformer = graphTransformer;
        this.searchablePreferenceScreenGraphDAO = searchablePreferenceScreenGraphDAO;
        this.activityContext = activityContext;
        this.configurationBundleConverter = configurationBundleConverter;
    }

    public void transformAndPersist(final Optional<SearchablePreferenceScreenTree> graph, final C configuration) {
        graph.ifPresent(
                _graph -> {
                    if (!Objects.equals(configuration, configurationBundleConverter.convertBackward(_graph.configuration()))) {
                        searchablePreferenceScreenGraphDAO.persistOrReplace(process(_graph, configuration));
                    }
                });
    }

    private SearchablePreferenceScreenTree process(final SearchablePreferenceScreenTree graph,
                                                   final C configuration) {
        return graphTransformer
                .map(_graphTransformer -> _graphTransformer.transformGraph(graph, configuration, activityContext))
                .orElse(graph)
                .asGraphHavingConfiguration(configurationBundleConverter.convertForward(configuration));
    }
}
