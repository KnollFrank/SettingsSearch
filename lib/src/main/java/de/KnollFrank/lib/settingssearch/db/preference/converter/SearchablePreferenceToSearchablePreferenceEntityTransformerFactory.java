package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

class SearchablePreferenceToSearchablePreferenceEntityTransformerFactory {

    public static SearchablePreferenceToSearchablePreferenceEntityTransformer createTransformer(final Set<SearchablePreferenceEntity> preferences) {
        return new SearchablePreferenceToSearchablePreferenceEntityTransformer(getPreferenceById(preferences));
    }

    private static Map<Integer, SearchablePreferenceEntity> getPreferenceById(final Set<SearchablePreferenceEntity> preferences) {
        return preferences
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreferenceEntity::id,
                                Function.identity()));
    }
}
