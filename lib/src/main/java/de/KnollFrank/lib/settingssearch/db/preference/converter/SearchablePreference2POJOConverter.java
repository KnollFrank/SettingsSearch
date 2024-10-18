package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter.drawable2String;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class SearchablePreference2POJOConverter {

    public static SearchablePreferencePOJO convert2POJO(final SearchablePreference searchablePreference,
                                                        final IdGenerator idGenerator) {
        return new SearchablePreferencePOJO(
                idGenerator.nextId(),
                searchablePreference.getKey(),
                drawable2String(searchablePreference.getIcon()),
                searchablePreference.getLayoutResource(),
                toString(searchablePreference.getSummary()),
                toString(searchablePreference.getTitle()),
                searchablePreference.getWidgetLayoutResource(),
                searchablePreference.getFragment(),
                searchablePreference.isVisible(),
                SearchableInfoAttributeConverter.convert2POJO(searchablePreference.getSearchableInfo()),
                searchablePreference.getExtras(),
                convert2POJOs(
                        SearchablePreferenceCaster.cast(Preferences.getImmediateChildren(searchablePreference)),
                        idGenerator),
                Optional.of(searchablePreference));
    }

    public static List<SearchablePreferencePOJO> convert2POJOs(final List<SearchablePreference> searchablePreferences,
                                                               final IdGenerator idGenerator) {
        return searchablePreferences
                .stream()
                .map(searchablePreference -> convert2POJO(searchablePreference, idGenerator))
                .collect(Collectors.toList());
    }

    private static String toString(final CharSequence charSequence) {
        return Optional
                .ofNullable(charSequence)
                .map(CharSequence::toString)
                .orElse(null);
    }
}
