package de.KnollFrank.lib.preferencesearch;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.PreferenceGroups;

public class PreferencesProvider {

    private final String preferenceFragment;
    private final PreferenceScreensProvider preferenceScreensProvider;
    private final Context context;
    private static final Map<String, PreferenceScreenWithHosts> preferenceScreenWithHostsByFragment = new HashMap<>();

    public PreferencesProvider(final String preferenceFragment,
                               final PreferenceScreensProvider preferenceScreensProvider,
                               final Context context) {
        this.preferenceFragment = preferenceFragment;
        this.preferenceScreensProvider = preferenceScreensProvider;
        this.context = context;
    }

    public PreferenceScreenWithHosts getPreferenceScreenWithHosts() {
        if (!preferenceScreenWithHostsByFragment.containsKey(preferenceFragment)) {
            preferenceScreenWithHostsByFragment.put(preferenceFragment, _getPreferenceScreenWithHosts());
        }
        return preferenceScreenWithHostsByFragment.get(preferenceFragment);
    }

    private PreferenceScreenWithHosts _getPreferenceScreenWithHosts() {
        final List<PreferenceScreenWithHost> screens = getScreens();
        final List<PreferenceWithHost> preferenceWithHostList = getPreferenceWithHostList(screens);
        final PreferenceScreen preferenceScreen =
                new PreferenceScreensMerger(context)
                        .destructivelyMergeScreens(getPreferenceScreens(screens));
        return new PreferenceScreenWithHosts(preferenceScreen, preferenceWithHostList);
    }

    private List<PreferenceScreenWithHost> getScreens() {
        return new ArrayList<>(preferenceScreensProvider.getPreferenceScreens(instantiatePreferenceFragment()));
    }

    private PreferenceFragmentCompat instantiatePreferenceFragment() {
        return (PreferenceFragmentCompat) Fragment.instantiate(context, preferenceFragment);
    }

    private List<PreferenceWithHost> getPreferenceWithHostList(final List<PreferenceScreenWithHost> preferenceScreenWithHostList) {
        return preferenceScreenWithHostList
                .stream()
                .map(preferenceScreenWithHost ->
                        asPreferenceWithHostList(
                                PreferenceGroups.getAllChildren(preferenceScreenWithHost.preferenceScreen),
                                preferenceScreenWithHost.host))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static List<PreferenceWithHost> asPreferenceWithHostList(
            final List<Preference> preferences,
            final Class<? extends PreferenceFragmentCompat> host) {
        return preferences
                .stream()
                .map(preference -> new PreferenceWithHost(preference, host))
                .collect(Collectors.toList());
    }

    private static List<PreferenceScreen> getPreferenceScreens(final List<PreferenceScreenWithHost> screens) {
        return screens
                .stream()
                .map(preferenceScreenWithHost -> preferenceScreenWithHost.preferenceScreen)
                .collect(Collectors.toList());
    }
}
