package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class HostByPreferenceConverter {

    public static Map<Integer, Class<? extends PreferenceFragmentCompat>> addIds(final Set<SearchablePreference> preferences) {
        return preferences
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreference::getId,
                                SearchablePreference::getHost));
    }

    public static Map<SearchablePreference, Class<? extends PreferenceFragmentCompat>> removeIds(
            final Map<Integer, Class<? extends PreferenceFragmentCompat>> hostByPreferenceId,
            final Map<Integer, SearchablePreference> preferenceById) {
        return hostByPreferenceId
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                entry -> preferenceById.get(entry.getKey()),
                                Entry::getValue));
    }
}
