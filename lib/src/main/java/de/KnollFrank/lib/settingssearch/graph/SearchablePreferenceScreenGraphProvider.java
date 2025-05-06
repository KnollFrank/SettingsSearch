package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

import androidx.fragment.app.Fragment;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

public class SearchablePreferenceScreenGraphProvider {

    private final Class<? extends Fragment> rootPreferenceFragmentClass;
    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider;
    private final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final PreferenceScreenGraphListener preferenceScreenGraphListener;
    private final ComputePreferencesListener computePreferencesListener;
    private final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter;
    private final Context context;

    public SearchablePreferenceScreenGraphProvider(final Class<? extends Fragment> rootPreferenceFragmentClass,
                                                   final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                                   final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
                                                   final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
                                                   final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                                   final PreferenceScreenGraphListener preferenceScreenGraphListener,
                                                   final ComputePreferencesListener computePreferencesListener,
                                                   final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter,
                                                   final Context context) {
        this.rootPreferenceFragmentClass = rootPreferenceFragmentClass;
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceFragmentConnected2PreferenceProvider = preferenceFragmentConnected2PreferenceProvider;
        this.rootPreferenceFragmentOfActivityProvider = rootPreferenceFragmentOfActivityProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.preferenceScreenGraphListener = preferenceScreenGraphListener;
        this.computePreferencesListener = computePreferencesListener;
        this.preference2SearchablePreferenceConverter = preference2SearchablePreferenceConverter;
        this.context = context;
    }

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> getSearchablePreferenceScreenGraph() {
        computePreferencesListener.onStartComputePreferences();
        final var searchablePreferenceScreenGraph = _getSearchablePreferenceScreenGraph();
        computePreferencesListener.onFinishComputePreferences();
        return searchablePreferenceScreenGraph;
    }

    private Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> _getSearchablePreferenceScreenGraph() {
        final var preferenceScreenGraph = getPreferenceScreenGraph();
        preferenceScreenGraphAvailableListener.onPreferenceScreenGraphWithoutInvisibleAndNonSearchablePreferencesAvailable(preferenceScreenGraph);
        return transformGraph2POJOGraph(preferenceScreenGraph);
    }

    private Graph<PreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph() {
        // FK-TODO: make PreferenceScreenGraphProvider an constructor parameter
        return PreferenceScreenGraphProviderFactory
                .createPreferenceScreenGraphProvider(
                        preferenceScreenWithHostProvider,
                        preferenceFragmentConnected2PreferenceProvider,
                        rootPreferenceFragmentOfActivityProvider,
                        context,
                        preferenceScreenGraphListener)
                .getPreferenceScreenGraph(rootPreferenceFragmentClass);
    }

    private Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return MapFromPojoNodesRemover.removeMapFromPojoNodes(
                Graph2POJOGraphTransformer.transformGraph2POJOGraph(
                        preferenceScreenGraph,
                        preference2SearchablePreferenceConverter));
    }
}
