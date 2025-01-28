package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.Fragment2PreferenceFragmentConverter;
import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

public class SearchablePreferenceScreenGraphProvider {

    private final String rootPreferenceFragmentClassName;
    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider;
    private final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final PreferenceScreenGraphListener preferenceScreenGraphListener;
    private final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter;
    private final Fragment2PreferenceFragmentConverter fragment2PreferenceFragmentConverter;

    public SearchablePreferenceScreenGraphProvider(final String rootPreferenceFragmentClassName,
                                                   final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                                   final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
                                                   final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
                                                   final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                                   final PreferenceScreenGraphListener preferenceScreenGraphListener,
                                                   final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter,
                                                   final Fragment2PreferenceFragmentConverter fragment2PreferenceFragmentConverter) {
        this.rootPreferenceFragmentClassName = rootPreferenceFragmentClassName;
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceFragmentConnected2PreferenceProvider = preferenceFragmentConnected2PreferenceProvider;
        this.rootPreferenceFragmentOfActivityProvider = rootPreferenceFragmentOfActivityProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.preferenceScreenGraphListener = preferenceScreenGraphListener;
        this.preference2SearchablePreferenceConverter = preference2SearchablePreferenceConverter;
        this.fragment2PreferenceFragmentConverter = fragment2PreferenceFragmentConverter;
    }

    public Graph<PreferenceScreenWithHostClass, SearchablePreferenceEdge> getSearchablePreferenceScreenGraph() {
        final var preferenceScreenGraph = getPreferenceScreenGraph();
        preferenceScreenGraphAvailableListener.onPreferenceScreenGraphWithoutInvisibleAndNonSearchablePreferencesAvailable(preferenceScreenGraph);
        return transformGraph2POJOGraph(preferenceScreenGraph);
    }

    private Graph<PreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph() {
        final PreferenceScreenGraphProvider preferenceScreenGraphProvider =
                new PreferenceScreenGraphProvider(
                        preferenceScreenWithHostProvider,
                        preferenceFragmentConnected2PreferenceProvider,
                        rootPreferenceFragmentOfActivityProvider,
                        preferenceScreenGraphListener,
                        fragment2PreferenceFragmentConverter);
        return preferenceScreenGraphProvider.getPreferenceScreenGraph(rootPreferenceFragmentClassName);
    }

    private Graph<PreferenceScreenWithHostClass, SearchablePreferenceEdge> transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return MapFromPojoNodesRemover.removeMapFromPojoNodes(
                Graph2POJOGraphTransformer.transformGraph2POJOGraph(
                        preferenceScreenGraph,
                        preference2SearchablePreferenceConverter));
    }
}
