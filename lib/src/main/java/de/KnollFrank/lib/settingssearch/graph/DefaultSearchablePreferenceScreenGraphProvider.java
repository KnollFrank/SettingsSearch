package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.graph.Host2HostClassTransformer.transformHost2HostClass;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;

public class DefaultSearchablePreferenceScreenGraphProvider implements SearchablePreferenceScreenGraphProvider {

    private final String rootPreferenceFragmentClassName;
    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider;

    public DefaultSearchablePreferenceScreenGraphProvider(final String rootPreferenceFragmentClassName,
                                                          final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                                          final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                                          final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                                          final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider) {
        this.rootPreferenceFragmentClassName = rootPreferenceFragmentClassName;
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.searchableInfoAndDialogInfoProvider = searchableInfoAndDialogInfoProvider;
    }

    @Override
    public Graph<PreferenceScreenWithHostClass, PreferenceEdge> getSearchablePreferenceScreenGraph() {
        final var preferenceScreenGraph =
                new PreferenceScreenGraphProvider(preferenceScreenWithHostProvider, preferenceConnected2PreferenceFragmentProvider)
                        .getPreferenceScreenGraph(rootPreferenceFragmentClassName);
        preferenceScreenGraphAvailableListener.onPreferenceScreenGraphWithoutInvisibleAndNonSearchablePreferencesAvailable(preferenceScreenGraph);
        return transformHost2HostClass(
                transformPreferences2SearchablePreferences(
                        preferenceScreenGraph));
    }

    private Graph<PreferenceScreenWithHost, PreferenceEdge> transformPreferences2SearchablePreferences(final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return new Preferences2SearchablePreferencesTransformer(searchableInfoAndDialogInfoProvider)
                .transformPreferences2SearchablePreferences(preferenceScreenGraph);
    }
}