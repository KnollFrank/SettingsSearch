package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class SearchablePreference2POJOConverter {

    private static int uniqueId = 0;

    public static SearchablePreferencePOJO convert2POJO(final SearchablePreference searchablePreference) {
        return new SearchablePreferencePOJO(
                uniqueId++,
                searchablePreference.getKey(),
                // FK-FIXME: replace 0 with real value
                0,
                searchablePreference.getLayoutResource(),
                toString(searchablePreference.getSummary()),
                toString(searchablePreference.getTitle()),
                searchablePreference.getWidgetLayoutResource(),
                searchablePreference.getFragment(),
                searchablePreference.isVisible(),
                SearchableInfoAttributeConverter.convert2POJO(searchablePreference.getSearchableInfo()),
                convert2POJOs(SearchablePreferenceCaster.cast(Preferences.getDirectChildren(searchablePreference))));
    }

    public static List<SearchablePreferencePOJO> convert2POJOs(final List<SearchablePreference> searchablePreferences) {
        return searchablePreferences
                .stream()
                .map(SearchablePreference2POJOConverter::convert2POJO)
                .collect(Collectors.toList());
    }

    private static String toString(final CharSequence charSequence) {
        return Optional
                .ofNullable(charSequence)
                .map(CharSequence::toString)
                .orElse(null);
    }
}
