package de.KnollFrank.lib.settingssearch.graph;

import androidx.fragment.app.Fragment;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;

public class SearchablePreferenceScreenGraphProvider {

    private final Class<? extends Fragment> rootPreferenceFragmentClass;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final ComputePreferencesListener computePreferencesListener;
    private final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter;
    private final PreferenceScreenGraphProvider preferenceScreenGraphProvider;

    public SearchablePreferenceScreenGraphProvider(final Class<? extends Fragment> rootPreferenceFragmentClass,
                                                   final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                                   final ComputePreferencesListener computePreferencesListener,
                                                   final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter,
                                                   final PreferenceScreenGraphProvider preferenceScreenGraphProvider) {
        this.rootPreferenceFragmentClass = rootPreferenceFragmentClass;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.computePreferencesListener = computePreferencesListener;
        this.preference2SearchablePreferenceConverter = preference2SearchablePreferenceConverter;
        this.preferenceScreenGraphProvider = preferenceScreenGraphProvider;
    }

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> getSearchablePreferenceScreenGraph() {
        computePreferencesListener.onStartComputePreferences();
        final var searchablePreferenceScreenGraph = _getSearchablePreferenceScreenGraph();
        computePreferencesListener.onFinishComputePreferences();
        return searchablePreferenceScreenGraph;
    }

    private Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> _getSearchablePreferenceScreenGraph() {
        final var preferenceScreenGraph = preferenceScreenGraphProvider.getPreferenceScreenGraph(rootPreferenceFragmentClass);
        preferenceScreenGraphAvailableListener.onPreferenceScreenGraphWithoutInvisibleAndNonSearchablePreferencesAvailable(preferenceScreenGraph);
        return transformGraph2POJOGraph(preferenceScreenGraph);
    }

    private Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return MapFromPojoNodesRemover.removeMapFromPojoNodes(
                Graph2POJOGraphTransformer.transformGraph2POJOGraph(
                        preferenceScreenGraph,
                        preference2SearchablePreferenceConverter));
    }
}
