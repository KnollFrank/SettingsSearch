package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferencePOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;

public class SearchablePreferenceScreenGraphProvider {

    private final String rootPreferenceFragmentClassName;
    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final PreferenceScreenGraphListener preferenceScreenGraphListener;
    private final Preference2SearchablePreferencePOJOConverter preference2SearchablePreferencePOJOConverter;

    public SearchablePreferenceScreenGraphProvider(final String rootPreferenceFragmentClassName,
                                                   final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                                   final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                                   final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                                   final PreferenceScreenGraphListener preferenceScreenGraphListener,
                                                   final Preference2SearchablePreferencePOJOConverter preference2SearchablePreferencePOJOConverter) {
        this.rootPreferenceFragmentClassName = rootPreferenceFragmentClassName;
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.preferenceScreenGraphListener = preferenceScreenGraphListener;
        this.preference2SearchablePreferencePOJOConverter = preference2SearchablePreferencePOJOConverter;
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
                        preferenceScreenGraph,
                        preference2SearchablePreferencePOJOConverter));
    }
}
