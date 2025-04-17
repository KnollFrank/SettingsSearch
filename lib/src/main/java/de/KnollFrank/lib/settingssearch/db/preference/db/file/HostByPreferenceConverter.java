package de.KnollFrank.lib.settingssearch.db.preference.db.file;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

// FK-TODO: remove class?
class HostByPreferenceConverter {

    public static Map<Integer, Class<? extends PreferenceFragmentCompat>> addIds(final Set<SearchablePreference> preferences) {
        return preferences
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreference::getId,
                                SearchablePreference::getHost));
    }
}
