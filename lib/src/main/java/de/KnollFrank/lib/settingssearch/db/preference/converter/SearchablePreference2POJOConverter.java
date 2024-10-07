package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class SearchablePreference2POJOConverter {

    public static void convert2POJO(final SearchablePreference searchablePreference,
                                    final List<SearchablePreferencePOJO> result) {
        result.add(
                new SearchablePreferencePOJO(
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
                        SearchablePreferenceCaster
                                .cast(Preferences.getDirectChildren(searchablePreference))
                                .stream()
                                .map(
                                        child -> {
                                            convert2POJO(child, result);
                                            return result.size() - 1;
                                        })
                                .collect(Collectors.toList())));
    }

    public static void convert2POJOs(final List<SearchablePreference> searchablePreferences,
                                     final List<SearchablePreferencePOJO> result) {
        for (final SearchablePreference searchablePreference : searchablePreferences) {
            convert2POJO(searchablePreference, result);
        }
    }

    private static String toString(final CharSequence charSequence) {
        return Optional
                .ofNullable(charSequence)
                .map(CharSequence::toString)
                .orElse(null);
    }
}
