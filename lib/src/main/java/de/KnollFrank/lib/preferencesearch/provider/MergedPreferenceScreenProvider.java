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

public class MergedPreferenceScreenProvider {

    private final Fragments fragments;
    private final PreferenceScreensProvider preferenceScreensProvider;
    private final PreferenceScreensMerger preferenceScreensMerger;
    private final MergedPreferenceScreenProviderListener mergedPreferenceScreenProviderListener;

    private static final Map<String, MergedPreferenceScreen> mergedPreferenceScreenByFragment = new HashMap<>();

    public MergedPreferenceScreenProvider(final Fragments fragments,
                                          final PreferenceScreensProvider preferenceScreensProvider,
                                          final PreferenceScreensMerger preferenceScreensMerger,
                                          final MergedPreferenceScreenProviderListener mergedPreferenceScreenProviderListener) {
        this.fragments = fragments;
        this.preferenceScreensProvider = preferenceScreensProvider;
        this.preferenceScreensMerger = preferenceScreensMerger;
        this.mergedPreferenceScreenProviderListener = mergedPreferenceScreenProviderListener;
    }

    public MergedPreferenceScreen getMergedPreferenceScreen(final String preferenceFragment) {
        if (!mergedPreferenceScreenByFragment.containsKey(preferenceFragment)) {
            mergedPreferenceScreenByFragment.put(preferenceFragment, _getMergedPreferenceScreen(preferenceFragment));
        }
        return mergedPreferenceScreenByFragment.get(preferenceFragment);
    }

    private MergedPreferenceScreen _getMergedPreferenceScreen(final String preferenceFragment) {
        mergedPreferenceScreenProviderListener.onStartGetMergedPreferenceScreen(preferenceFragment);
        final MergedPreferenceScreen mergedPreferenceScreen = getMergedPreferenceScreen(instantiateAndInitializeFragment(preferenceFragment));
        mergedPreferenceScreenProviderListener.onFinishGetMergedPreferenceScreen(preferenceFragment);
        return mergedPreferenceScreen;
    }

    private PreferenceFragmentCompat instantiateAndInitializeFragment(final String preferenceFragment) {
        return (PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(preferenceFragment);
    }

    private MergedPreferenceScreen getMergedPreferenceScreen(final PreferenceFragmentCompat preferenceFragment) {
        final Set<PreferenceScreenWithHost> screens = preferenceScreensProvider.getConnectedPreferenceScreens(preferenceFragment);
        // MUST compute A (which just reads screens) before B (which modifies screens)
        // A:
        final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference = HostByPreferenceProvider.getHostByPreference(screens);
        // B:
        final PreferenceScreen preferenceScreen =
                preferenceScreensMerger.destructivelyMergeScreens(
                        getPreferenceScreens(new ArrayList<>(screens)));
        return new MergedPreferenceScreen(preferenceScreen, hostByPreference);
    }

    private static List<PreferenceScreen> getPreferenceScreens(final List<PreferenceScreenWithHost> screens) {
        return screens
                .stream()
                .map(preferenceScreenWithHost -> preferenceScreenWithHost.preferenceScreen)
                .collect(Collectors.toList());
    }
}
