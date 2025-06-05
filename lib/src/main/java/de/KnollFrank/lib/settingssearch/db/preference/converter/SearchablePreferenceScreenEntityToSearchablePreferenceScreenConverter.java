package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Set;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter {

    private final Function<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> getAllPreferences;
    private final SearchablePreferenceEntityToSearchablePreferenceConverter preferenceConverter;

    public SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter(
            final Function<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> getAllPreferences,
            final SearchablePreferenceEntityToSearchablePreferenceConverter preferenceConverter) {
        this.getAllPreferences = getAllPreferences;
        this.preferenceConverter = preferenceConverter;
    }

    public SearchablePreferenceScreen fromEntity(final SearchablePreferenceScreenEntity entity) {
        return new SearchablePreferenceScreen(
                entity.getId(),
                entity.getHost(),
                entity.getTitle(),
                entity.getSummary(),
                preferenceConverter.fromEntities(getAllPreferences.apply(entity)));
    }
}
