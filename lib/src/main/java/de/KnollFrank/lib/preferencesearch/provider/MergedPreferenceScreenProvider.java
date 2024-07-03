package de.KnollFrank.lib.preferencesearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.MergedPreferenceScreen;
import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.preferencesearch.PreferenceScreensProvider;
import de.KnollFrank.lib.preferencesearch.fragment.Fragments;
import de.KnollFrank.lib.preferencesearch.search.PreferenceSummaryProvider;

public class MergedPreferenceScreenProvider {

    private final Fragments fragments;
    private final PreferenceScreensProvider preferenceScreensProvider;
    private final PreferenceScreensMerger preferenceScreensMerger;
    private final SearchablePreferencePredicate searchablePreferencePredicate;
    private final boolean cacheMergedPreferenceScreens;

    private static final Map<String, MergedPreferenceScreen> mergedPreferenceScreenByFragment = new HashMap<>();

    public MergedPreferenceScreenProvider(final Fragments fragments,
                                          final PreferenceScreensProvider preferenceScreensProvider,
                                          final PreferenceScreensMerger preferenceScreensMerger,
                                          final SearchablePreferencePredicate searchablePreferencePredicate,
                                          final boolean cacheMergedPreferenceScreens) {
        this.fragments = fragments;
        this.preferenceScreensProvider = preferenceScreensProvider;
        this.preferenceScreensMerger = preferenceScreensMerger;
        this.searchablePreferencePredicate = searchablePreferencePredicate;
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
        return getMergedPreferenceScreen((PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(preferenceFragment));
    }

    private MergedPreferenceScreen getMergedPreferenceScreen(final PreferenceFragmentCompat preferenceFragment) {
        final Set<PreferenceScreenWithHost> screens = getConnectedPreferenceScreens(preferenceFragment);
        // MUST compute A (which just reads screens) before B (which modifies screens)
        // A:
        final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference =
                HostByPreferenceProvider.getHostByPreference(screens);
        // B:
        final PreferenceScreen preferenceScreen = destructivelyMergeScreens(screens);
        return new MergedPreferenceScreen(
                preferenceScreen,
                hostByPreference,
                PreferenceSummaryProvider.getSummaryResetterByPreference(preferenceScreen));
    }

    private Set<PreferenceScreenWithHost> getConnectedPreferenceScreens(final PreferenceFragmentCompat preferenceFragment) {
        final Set<PreferenceScreenWithHost> screens = preferenceScreensProvider.getConnectedPreferenceScreens(preferenceFragment);
        removeInvisiblePreferences(screens);
        removeNonSearchablePreferences(screens);
        return screens;
    }

    private static void removeInvisiblePreferences(final Set<PreferenceScreenWithHost> screens) {
        PreferencesRemover.removePreferences(screens, MergedPreferenceScreenProvider::isInvisible);
    }

    private static boolean isInvisible(final Preference preference,
                                       final Class<? extends PreferenceFragmentCompat> host) {
        return !preference.isVisible();
    }

    private void removeNonSearchablePreferences(final Set<PreferenceScreenWithHost> screens) {
        PreferencesRemover.removePreferences(screens, this::isNonSearchable);
    }

    private boolean isNonSearchable(final Preference preference,
                                    final Class<? extends PreferenceFragmentCompat> host) {
        return !searchablePreferencePredicate.isPreferenceOfHostSearchable(preference, host);
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
