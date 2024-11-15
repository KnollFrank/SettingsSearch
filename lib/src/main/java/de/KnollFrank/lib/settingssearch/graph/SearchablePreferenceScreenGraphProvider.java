package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.graph.Host2HostClassTransformer.transformHost2HostClass;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.search.provider.IconProvider;

public class SearchablePreferenceScreenGraphProvider {

    private final String rootPreferenceFragmentClassName;
    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider;
    private final IconProvider iconProvider;
    private final PreferenceScreenGraphListener preferenceScreenGraphListener;

    public SearchablePreferenceScreenGraphProvider(final String rootPreferenceFragmentClassName,
                                                   final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                                   final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                                   final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                                   final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider,
                                                   final IconProvider iconProvider,
                                                   final PreferenceScreenGraphListener preferenceScreenGraphListener) {
        this.rootPreferenceFragmentClassName = rootPreferenceFragmentClassName;
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.searchableInfoAndDialogInfoProvider = searchableInfoAndDialogInfoProvider;
        this.iconProvider = iconProvider;
        this.preferenceScreenGraphListener = preferenceScreenGraphListener;
    }

    public Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> getSearchablePreferenceScreenGraph() {
        final var preferenceScreenGraph = getPreferenceScreenGraph();
        preferenceScreenGraphAvailableListener.onPreferenceScreenGraphWithoutInvisibleAndNonSearchablePreferencesAvailable(preferenceScreenGraph);
        return transformGraph2POJOGraph(preferenceScreenGraph);
    }

    private Graph<PreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph() {
        final PreferenceScreenGraphProvider preferenceScreenGraphProvider =
                new PreferenceScreenGraphProvider(
                        preferenceScreenWithHostProvider,
                        preferenceConnected2PreferenceFragmentProvider,
                        preferenceScreenGraphListener);
        return preferenceScreenGraphProvider.getPreferenceScreenGraph(rootPreferenceFragmentClassName);
    }

    private Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return MapFromPojoNodesRemover.removeMapFromPojoNodes(
                Graph2POJOGraphTransformer.transformGraph2POJOGraph(
                        transformHost2HostClass(
                                transformPreferences2SearchablePreferences(
                                        preferenceScreenGraph))));
    }

    private Graph<PreferenceScreenWithHost, PreferenceEdge> transformPreferences2SearchablePreferences(final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        final Preferences2SearchablePreferencesTransformer transformer =
                new Preferences2SearchablePreferencesTransformer(
                        searchableInfoAndDialogInfoProvider,
                        iconProvider);
        return transformer.transformPreferences2SearchablePreferences(preferenceScreenGraph);
    }
}
