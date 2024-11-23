package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class HostByPreferenceConverter {

    public static Map<Integer, Class<? extends PreferenceFragmentCompat>> addIds(
            final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
        return hostByPreference
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey().getId(),
                                Entry::getValue));
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
