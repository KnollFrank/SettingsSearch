package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;

class SearchablePreferenceCaster {

    public static SearchablePreference cast(final Preference preference) {
        return (SearchablePreference) preference;
    }

    public static List<SearchablePreference> cast(final List<Preference> preferences) {
        return preferences
                .stream()
                .map(SearchablePreferenceCaster::cast)
                .collect(Collectors.toList());
    }
}
