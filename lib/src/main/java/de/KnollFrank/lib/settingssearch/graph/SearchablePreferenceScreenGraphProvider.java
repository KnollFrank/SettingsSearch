package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;

public class SearchablePreferenceScreenGraphProvider {

    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final ComputePreferencesListener computePreferencesListener;
    private final PreferenceScreen2SearchablePreferenceScreenConverter preferenceScreen2SearchablePreferenceScreenConverter;
    private final PreferenceScreenGraphProvider preferenceScreenGraphProvider;
    private final PreferenceFragmentIdProvider preferenceFragmentIdProvider;

    public SearchablePreferenceScreenGraphProvider(final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                                   final ComputePreferencesListener computePreferencesListener,
                                                   final PreferenceScreen2SearchablePreferenceScreenConverter preferenceScreen2SearchablePreferenceScreenConverter,
                                                   final PreferenceScreenGraphProvider preferenceScreenGraphProvider,
                                                   final PreferenceFragmentIdProvider preferenceFragmentIdProvider) {
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.computePreferencesListener = computePreferencesListener;
        this.preferenceScreen2SearchablePreferenceScreenConverter = preferenceScreen2SearchablePreferenceScreenConverter;
        this.preferenceScreenGraphProvider = preferenceScreenGraphProvider;
        this.preferenceFragmentIdProvider = preferenceFragmentIdProvider;
    }

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> getSearchablePreferenceScreenGraph(final PreferenceScreenWithHost root) {
        computePreferencesListener.onStartComputePreferences();
        final var searchablePreferenceScreenGraph = _getSearchablePreferenceScreenGraph(root);
        computePreferencesListener.onFinishComputePreferences();
        return searchablePreferenceScreenGraph;
    }

    private Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> _getSearchablePreferenceScreenGraph(final PreferenceScreenWithHost root) {
        final var preferenceScreenGraph = preferenceScreenGraphProvider.getPreferenceScreenGraph(root);
        preferenceScreenGraphAvailableListener.onPreferenceScreenGraphWithoutInvisibleAndNonSearchablePreferencesAvailable(preferenceScreenGraph);
        return transformGraph2POJOGraph(preferenceScreenGraph);
    }

    private Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return MapFromPojoNodesRemover.removeMapFromPojoNodes(
                Graph2POJOGraphTransformer.transformGraph2POJOGraph(
                        preferenceScreenGraph,
                        preferenceScreen2SearchablePreferenceScreenConverter,
                        preferenceFragmentIdProvider));
    }
}
