package de.KnollFrank.lib.preferencesearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.preferencesearch.common.Maps;
import de.KnollFrank.lib.preferencesearch.common.Preferences;

class HostByPreferenceProvider {

    public static Map<Preference, Class<? extends PreferenceFragmentCompat>> getHostByPreference(final List<PreferenceScreenWithHost> preferenceScreenWithHostList) {
        return Maps.merge(
                preferenceScreenWithHostList
                        .stream()
                        .map(HostByPreferenceProvider::getHostByPreference)
                        .collect(Collectors.toList()));
    }

    private static Map<Preference, Class<? extends PreferenceFragmentCompat>> getHostByPreference(final PreferenceScreenWithHost preferenceScreenWithHost) {
        return Preferences
                .getAllChildren(preferenceScreenWithHost.preferenceScreen)
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                preference -> preferenceScreenWithHost.host));
    }
}
