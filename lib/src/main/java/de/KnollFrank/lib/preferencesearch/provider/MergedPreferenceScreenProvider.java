package de.KnollFrank.lib.preferencesearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.MergedPreferenceScreen;
import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.preferencesearch.PreferenceScreensProvider;
import de.KnollFrank.lib.preferencesearch.fragment.Fragments;

public class MergedPreferenceScreenProvider {

    private final String preferenceFragment;
    private final Fragments fragments;
    private final PreferenceScreensProvider preferenceScreensProvider;
    private final PreferenceScreensMerger preferenceScreensMerger;

    private static final Map<String, MergedPreferenceScreen> mergedPreferenceScreenByFragment = new HashMap<>();

    public MergedPreferenceScreenProvider(final String preferenceFragment,
                                          final Fragments fragments,
                                          final PreferenceScreensProvider preferenceScreensProvider,
                                          final PreferenceScreensMerger preferenceScreensMerger) {
        // FK-TODO: move preferenceFragment as a parameter to method getMergedPreferenceScreen()
        this.preferenceFragment = preferenceFragment;
        this.fragments = fragments;
        this.preferenceScreensProvider = preferenceScreensProvider;
        this.preferenceScreensMerger = preferenceScreensMerger;
    }

    public MergedPreferenceScreen getMergedPreferenceScreen() {
        if (!mergedPreferenceScreenByFragment.containsKey(preferenceFragment)) {
            mergedPreferenceScreenByFragment.put(preferenceFragment, _getMergedPreferenceScreen());
        }
        return mergedPreferenceScreenByFragment.get(preferenceFragment);
    }

    private MergedPreferenceScreen _getMergedPreferenceScreen() {
        final List<PreferenceScreenWithHost> screens = getPreferenceScreenWithHostList();
        // MUST compute A (which just reads screens) before B (which destructs screens)
        // A:
        final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference = HostByPreferenceProvider.getHostByPreference(screens);
        // B:
        final PreferenceScreen preferenceScreen =
                preferenceScreensMerger.destructivelyMergeScreens(
                        getPreferenceScreens(screens));
        return new MergedPreferenceScreen(preferenceScreen, hostByPreference);
    }

    private List<PreferenceScreenWithHost> getPreferenceScreenWithHostList() {
        return new ArrayList<>(preferenceScreensProvider.getConnectedPreferenceScreens(instantiatePreferenceFragment()));
    }

    private PreferenceFragmentCompat instantiatePreferenceFragment() {
        return (PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(preferenceFragment);
    }

    private static List<PreferenceScreen> getPreferenceScreens(final List<PreferenceScreenWithHost> screens) {
        return screens
                .stream()
                .map(preferenceScreenWithHost -> preferenceScreenWithHost.preferenceScreen)
                .collect(Collectors.toList());
    }
}
