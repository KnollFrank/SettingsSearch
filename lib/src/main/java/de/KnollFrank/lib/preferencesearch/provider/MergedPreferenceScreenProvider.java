package de.KnollFrank.lib.preferencesearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.ConnectedPreferenceScreens;
import de.KnollFrank.lib.preferencesearch.MergedPreferenceScreen;
import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.preferencesearch.PreferenceScreensProvider;
import de.KnollFrank.lib.preferencesearch.fragment.Fragments;
import de.KnollFrank.lib.preferencesearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoAttribute;

public class MergedPreferenceScreenProvider {

    private final Fragments fragments;
    private final PreferenceDialogs preferenceDialogs;
    private final PreferenceScreensProvider preferenceScreensProvider;
    private final PreferenceScreensMerger preferenceScreensMerger;
    private final IsPreferenceSearchable isPreferenceSearchable;
    private final SearchableInfoAttribute searchableInfoAttribute;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final boolean cacheMergedPreferenceScreens;

    private static final Map<String, MergedPreferenceScreen> mergedPreferenceScreenByFragment = new HashMap<>();

    public MergedPreferenceScreenProvider(final Fragments fragments,
                                          final PreferenceDialogs preferenceDialogs,
                                          final PreferenceScreensProvider preferenceScreensProvider,
                                          final PreferenceScreensMerger preferenceScreensMerger,
                                          final IsPreferenceSearchable isPreferenceSearchable,
                                          final SearchableInfoAttribute searchableInfoAttribute,
                                          final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                          final boolean cacheMergedPreferenceScreens) {
        this.fragments = fragments;
        this.preferenceDialogs = preferenceDialogs;
        this.preferenceScreensProvider = preferenceScreensProvider;
        this.preferenceScreensMerger = preferenceScreensMerger;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.searchableInfoAttribute = searchableInfoAttribute;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.cacheMergedPreferenceScreens = cacheMergedPreferenceScreens;
    }

    public MergedPreferenceScreen getMergedPreferenceScreen(final String preferenceFragment) {
        if (!cacheMergedPreferenceScreens) {
            return computeMergedPreferenceScreen(preferenceFragment);
        }
        if (!mergedPreferenceScreenByFragment.containsKey(preferenceFragment)) {
            mergedPreferenceScreenByFragment.put(preferenceFragment, computeMergedPreferenceScreen(preferenceFragment));
        }
        return mergedPreferenceScreenByFragment.get(preferenceFragment);
    }

    private MergedPreferenceScreen computeMergedPreferenceScreen(final String preferenceFragment) {
        return getMergedPreferenceScreen(
                (PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(
                        preferenceFragment,
                        Optional.empty()));
    }

    private MergedPreferenceScreen getMergedPreferenceScreen(final PreferenceFragmentCompat preferenceFragment) {
        final ConnectedPreferenceScreens screens = getConnectedPreferenceScreens(preferenceFragment);
        // MUST compute A (which just reads screens) before B (which modifies screens)
        // A:
        final Map<Preference, PreferenceFragmentCompat> hostByPreference =
                HostByPreferenceProvider.getHostByPreference(screens.connectedPreferenceScreens);
        final Map<Preference, String> searchableInfoByPreference =
                new SearchableInfoByPreferenceProvider(preferenceDialogs, preferenceDialogAndSearchableInfoProvider)
                        .getSearchableInfoByPreference(screens.connectedPreferenceScreens);
        // B:
        final PreferenceScreen preferenceScreen = destructivelyMergeScreens(screens.connectedPreferenceScreens);
        return new MergedPreferenceScreen(
                preferenceScreen,
                hostByPreference,
                searchableInfoByPreference,
                screens.preferencePathByPreference,
                searchableInfoAttribute);
    }

    private ConnectedPreferenceScreens getConnectedPreferenceScreens(final PreferenceFragmentCompat preferenceFragment) {
        final ConnectedPreferenceScreens screens = preferenceScreensProvider.getConnectedPreferenceScreens(preferenceFragment);
        removeInvisiblePreferences(screens.connectedPreferenceScreens);
        removeNonSearchablePreferences(screens.connectedPreferenceScreens);
        return screens;
    }

    private static void removeInvisiblePreferences(final Set<PreferenceScreenWithHost> screens) {
        PreferencesRemover.removePreferences(screens, MergedPreferenceScreenProvider::isInvisible);
    }

    private static boolean isInvisible(final Preference preference,
                                       final PreferenceFragmentCompat host) {
        return !preference.isVisible();
    }

    private void removeNonSearchablePreferences(final Set<PreferenceScreenWithHost> screens) {
        PreferencesRemover.removePreferences(screens, this::isNonSearchable);
    }

    private boolean isNonSearchable(final Preference preference,
                                    final PreferenceFragmentCompat host) {
        return !isPreferenceSearchable.isPreferenceOfHostSearchable(preference, host);
    }

    private PreferenceScreen destructivelyMergeScreens(final Set<PreferenceScreenWithHost> screens) {
        return preferenceScreensMerger.destructivelyMergeScreens(getPreferenceScreens(new ArrayList<>(screens)));
    }

    private static List<PreferenceScreen> getPreferenceScreens(final List<PreferenceScreenWithHost> screens) {
        return screens
                .stream()
                .map(preferenceScreenWithHost -> preferenceScreenWithHost.preferenceScreen)
                .collect(Collectors.toList());
    }
}
