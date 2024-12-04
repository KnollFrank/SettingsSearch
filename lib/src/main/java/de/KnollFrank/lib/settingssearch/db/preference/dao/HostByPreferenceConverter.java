package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class HostByPreferenceConverter {

    public static Map<Integer, Class<? extends PreferenceFragmentCompat>> addIds(final Set<SearchablePreferencePOJO> preferences) {
        return preferences
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreferencePOJO::getId,
                                SearchablePreferencePOJO::getHost));
    }

    public static Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> removeIds(
            final Map<Integer, Class<? extends PreferenceFragmentCompat>> hostByPreferenceId,
            final Map<Integer, SearchablePreferencePOJO> preferenceById) {
        return hostByPreferenceId
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                entry -> preferenceById.get(entry.getKey()),
                                Entry::getValue));
    }
}
