package de.KnollFrank.lib.preferencesearch.provider;

import android.content.Context;

import androidx.fragment.app.Fragment;
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

public class MergedPreferenceScreenProvider {

    private final String preferenceFragment;
    private final PreferenceScreensProvider preferenceScreensProvider;
    private final Context context;

    private static final Map<String, MergedPreferenceScreen> mergedPreferenceScreenByFragment = new HashMap<>();

    public MergedPreferenceScreenProvider(final String preferenceFragment,
                                          final PreferenceScreensProvider preferenceScreensProvider,
                                          final Context context) {
        this.preferenceFragment = preferenceFragment;
        this.preferenceScreensProvider = preferenceScreensProvider;
        this.context = context;
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
                new PreferenceScreensMerger(context)
                        .destructivelyMergeScreens(getPreferenceScreens(screens));
        return new MergedPreferenceScreen(preferenceScreen, hostByPreference);
    }

    private List<PreferenceScreenWithHost> getPreferenceScreenWithHostList() {
        return new ArrayList<>(preferenceScreensProvider.getConnectedPreferenceScreens(instantiatePreferenceFragment()));
    }

    private PreferenceFragmentCompat instantiatePreferenceFragment() {
        return (PreferenceFragmentCompat) Fragment.instantiate(context, preferenceFragment);
    }

    private static List<PreferenceScreen> getPreferenceScreens(final List<PreferenceScreenWithHost> screens) {
        return screens
                .stream()
                .map(preferenceScreenWithHost -> preferenceScreenWithHost.preferenceScreen)
                .collect(Collectors.toList());
    }
}
