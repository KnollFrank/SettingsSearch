package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

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

    public PreferenceScreensProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                     final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                     final IsPreferenceSearchable isPreferenceSearchable,
                                     final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                     final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.searchableInfoAndDialogInfoProvider = searchableInfoAndDialogInfoProvider;
    }

    public ConnectedPreferenceScreens getConnectedPreferenceScreens(final PreferenceFragmentCompat root) {
        return new ConnectedPreferenceScreens(getPreferenceScreenGraph(root));
    }

    private Graph<SearchablePreferenceScreenWithMapAndHost, PreferenceEdge> getPreferenceScreenGraph(final PreferenceFragmentCompat root) {
        final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph =
                new PreferenceScreenGraphProvider(preferenceScreenWithHostProvider, preferenceConnected2PreferenceFragmentProvider)
                        .getPreferenceScreenGraph(
                                PreferenceScreenWithHostFactory.createPreferenceScreenWithHost(
                                        root));
        preferenceScreenGraphAvailableListener.onPreferenceScreenGraphWithoutInvisibleAndNonSearchablePreferencesAvailable(preferenceScreenGraph);
        return new Preferences2SearchablePreferencesTransformer(
                isPreferenceSearchable,
                searchableInfoAndDialogInfoProvider)
                .transformPreferences2SearchablePreferences(preferenceScreenGraph);
    }
}
