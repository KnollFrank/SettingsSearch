package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;

public class SearchablePreferenceScreenGraphProvider {

    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final ComputePreferencesListener computePreferencesListener;
    private final GraphToPojoGraphTransformer graphToPojoGraphTransformer;
    private final PreferenceScreenGraphProvider preferenceScreenGraphProvider;
    private final Locale locale;

    public SearchablePreferenceScreenGraphProvider(final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                                   final ComputePreferencesListener computePreferencesListener,
                                                   final GraphToPojoGraphTransformer graphToPojoGraphTransformer,
                                                   final PreferenceScreenGraphProvider preferenceScreenGraphProvider,
                                                   final Locale locale) {
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.computePreferencesListener = computePreferencesListener;
        this.graphToPojoGraphTransformer = graphToPojoGraphTransformer;
        this.preferenceScreenGraphProvider = preferenceScreenGraphProvider;
        this.locale = locale;
    }

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> getSearchablePreferenceScreenGraph(final PreferenceScreenWithHost root) {
        computePreferencesListener.onStartComputePreferences();
        final var searchablePreferenceScreenGraph = _getSearchablePreferenceScreenGraph(root);
        computePreferencesListener.onFinishComputePreferences();
        return searchablePreferenceScreenGraph;
    }

    private Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> _getSearchablePreferenceScreenGraph(final PreferenceScreenWithHost root) {
        final var preferenceScreenGraph = preferenceScreenGraphProvider.getPreferenceScreenGraph(root);
        preferenceScreenGraphAvailableListener.onPreferenceScreenGraphAvailable(preferenceScreenGraph);
        return transformGraphToPojoGraph(preferenceScreenGraph);
    }

    private Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> transformGraphToPojoGraph(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return MapFromPojoNodesRemover.removeMapFromPojoNodes(
                graphToPojoGraphTransformer.transformGraphToPojoGraph(preferenceScreenGraph, locale));
    }
}
