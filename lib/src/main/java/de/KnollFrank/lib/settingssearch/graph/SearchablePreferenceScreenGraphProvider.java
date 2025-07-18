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
    private final Graph2POJOGraphTransformer graph2POJOGraphTransformer;
    private final PreferenceScreenGraphProvider preferenceScreenGraphProvider;
    private final Locale locale;

    public SearchablePreferenceScreenGraphProvider(final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                                   final ComputePreferencesListener computePreferencesListener,
                                                   final Graph2POJOGraphTransformer graph2POJOGraphTransformer,
                                                   final PreferenceScreenGraphProvider preferenceScreenGraphProvider,
                                                   final Locale locale) {
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.computePreferencesListener = computePreferencesListener;
        this.graph2POJOGraphTransformer = graph2POJOGraphTransformer;
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
        preferenceScreenGraphAvailableListener.onPreferenceScreenGraphWithoutInvisibleAndNonSearchablePreferencesAvailable(preferenceScreenGraph);
        return transformGraph2POJOGraph(preferenceScreenGraph);
    }

    private Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return MapFromPojoNodesRemover.removeMapFromPojoNodes(
                graph2POJOGraphTransformer.transformGraph2POJOGraph(preferenceScreenGraph, locale));
    }
}
