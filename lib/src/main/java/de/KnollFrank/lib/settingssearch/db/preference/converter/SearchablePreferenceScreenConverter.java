package de.KnollFrank.lib.settingssearch.db.preference.converter;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreenConverter {

    public static SearchablePreferenceScreenEntity toEntity(final SearchablePreferenceScreen searchablePreferenceScreen) {
        return new SearchablePreferenceScreenEntity(
                searchablePreferenceScreen.id(),
                searchablePreferenceScreen.host(),
                searchablePreferenceScreen.title(),
                searchablePreferenceScreen.summary(),
                searchablePreferenceScreen.allPreferences(),
                searchablePreferenceScreen.parentId());
    }

    public static SearchablePreferenceScreen fromEntity(final SearchablePreferenceScreenEntity searchablePreferenceScreenEntity) {
        return new SearchablePreferenceScreen(
                searchablePreferenceScreenEntity.getId(),
                searchablePreferenceScreenEntity.getHost(),
                searchablePreferenceScreenEntity.getTitle(),
                searchablePreferenceScreenEntity.getSummary(),
                searchablePreferenceScreenEntity.getAllPreferences(),
                searchablePreferenceScreenEntity.getParentId());
    }
}
