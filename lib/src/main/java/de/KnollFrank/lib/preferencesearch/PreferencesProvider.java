package de.KnollFrank.lib.preferencesearch;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class PreferencesProvider {

    private final String preferenceFragment;
    private final PreferenceScreensProvider preferenceScreensProvider;
    private final Context context;

    public PreferencesProvider(final String preferenceFragment,
                               final PreferenceScreensProvider preferenceScreensProvider,
                               final Context context) {
        this.preferenceFragment = preferenceFragment;
        this.preferenceScreensProvider = preferenceScreensProvider;
        this.context = context;
    }

    public List<PreferenceWithHost> getPreferences() {
        return preferenceScreensProvider
                .getPreferenceScreens(instantiatePreferenceFragment())
                .stream()
                .map(preferenceScreenWithHost ->
                        asPreferenceWithHostList(
                                PreferenceProvider.getPreferences(preferenceScreenWithHost.preferenceScreen),
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

    private PreferenceFragmentCompat instantiatePreferenceFragment() {
        return (PreferenceFragmentCompat) Fragment.instantiate(context, preferenceFragment);
    }
}
