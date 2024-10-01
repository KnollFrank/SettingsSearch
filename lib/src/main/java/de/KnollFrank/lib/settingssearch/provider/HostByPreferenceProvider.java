package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Preferences;

class HostByPreferenceProvider {

    public static Map<Preference, PreferenceFragmentCompat> getHostByPreference(
            final Collection<SearchablePreferenceScreenWithHost> searchablePreferenceScreenWithHostList) {
        return Maps.merge(
                searchablePreferenceScreenWithHostList
                        .stream()
                        .map(HostByPreferenceProvider::getHostByPreference)
                        .collect(Collectors.toList()));
    }

    private static Map<Preference, PreferenceFragmentCompat> getHostByPreference(final SearchablePreferenceScreenWithHost searchablePreferenceScreenWithHost) {
        return Preferences
                .getAllChildren(searchablePreferenceScreenWithHost.searchablePreferenceScreen().searchablePreferenceScreen())
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                preference -> searchablePreferenceScreenWithHost.host()));
    }
}
