package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.graph.Host2HostClassTransformer.transformHost2HostClass;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.ConnectedSearchablePreferenceScreens;
import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostFactory;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;

public class PreferenceScreensProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final IsPreferenceSearchable isPreferenceSearchable;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider;
    private final PreferenceManager preferenceManager;

    public PreferenceScreensProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                     final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                     final IsPreferenceSearchable isPreferenceSearchable,
                                     final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                     final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider,
                                     final PreferenceManager preferenceManager) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.searchableInfoAndDialogInfoProvider = searchableInfoAndDialogInfoProvider;
        this.preferenceManager = preferenceManager;
    }

    public ConnectedSearchablePreferenceScreens getConnectedPreferenceScreens(final PreferenceFragmentCompat root) {
        return ConnectedSearchablePreferenceScreens.fromSearchablePreferenceScreenGraph(
                getSearchablePreferenceScreenGraph(root),
                preferenceManager);
    }

    private Graph<PreferenceScreenWithHostClass, PreferenceEdge> getSearchablePreferenceScreenGraph(final PreferenceFragmentCompat root) {
        final var preferenceScreenGraph = createPreferenceScreenGraph(root);
        preferenceScreenGraphAvailableListener.onPreferenceScreenGraphWithoutInvisibleAndNonSearchablePreferencesAvailable(preferenceScreenGraph);
        // FK-TODO: transformHost2HostClass() und removeMapFromNodes() in einem einzelnen Schritt durchführen
        return transformHost2HostClass(
                MapFromNodesRemover.removeMapFromNodes(
                        transformPreferences2SearchablePreferences(preferenceScreenGraph)));
    }

    private Graph<PreferenceScreenWithHost, PreferenceEdge> createPreferenceScreenGraph(final PreferenceFragmentCompat root) {
        return new PreferenceScreenGraphProvider(preferenceScreenWithHostProvider, preferenceConnected2PreferenceFragmentProvider)
                .getPreferenceScreenGraph(
                        PreferenceScreenWithHostFactory.createPreferenceScreenWithHost(
                                root));
    }

    private Graph<SearchablePreferenceScreenWithMapAndHost, PreferenceEdge> transformPreferences2SearchablePreferences(final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return new Preferences2SearchablePreferencesTransformer(
                isPreferenceSearchable,
                searchableInfoAndDialogInfoProvider)
                .transformPreferences2SearchablePreferences(preferenceScreenGraph);
    }
}
