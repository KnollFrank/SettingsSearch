package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class SearchablePreferencePOJOConverter {

    public static SearchablePreferencePOJO convert2POJO(final SearchablePreference searchablePreference) {
        return new SearchablePreferencePOJO(
                searchablePreference.getKey(),
                // FK-FIXME: replace 0 with real value
                0,
                searchablePreference.getLayoutResource(),
                toString(searchablePreference.getSummary()),
                toString(searchablePreference.getTitle()),
                searchablePreference.getWidgetLayoutResource(),
                searchablePreference.getFragment(),
                searchablePreference.isVisible(),
                searchablePreference.getSearchableInfo().orElse(null),
                convert2POJOs(cast(Preferences.getDirectChildren(searchablePreference))));
    }

    private static String toString(final CharSequence charSequence) {
        return Optional
                .ofNullable(charSequence)
                .map(CharSequence::toString)
                .orElse(null);
    }

    static List<SearchablePreferencePOJO> convert2POJOs(final List<SearchablePreference> searchablePreferences) {
        return searchablePreferences
                .stream()
                .map(SearchablePreferencePOJOConverter::convert2POJO)
                .collect(Collectors.toList());
    }

    static List<SearchablePreference> cast(final List<Preference> preferences) {
        return preferences
                .stream()
                .map(SearchablePreferencePOJOConverter::cast)
                .collect(Collectors.toList());
    }

    static SearchablePreference cast(final Preference preference) {
        return (SearchablePreference) preference;
    }
}
