package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SearchablePreferenceScreenAndAllPreferences {

    @Embedded
    private final SearchablePreferenceScreenEntity searchablePreferenceScreenEntity;

    @Relation(
            parentColumn = "id",
            entityColumn = "searchablePreferenceScreenId")
    private final List<SearchablePreferenceEntity> allPreferences;

    public SearchablePreferenceScreenAndAllPreferences(final SearchablePreferenceScreenEntity searchablePreferenceScreenEntity,
                                                       final List<SearchablePreferenceEntity> allPreferences) {
        this.searchablePreferenceScreenEntity = searchablePreferenceScreenEntity;
        this.allPreferences = allPreferences;
    }

    public SearchablePreferenceScreenEntity searchablePreferenceScreen() {
        return searchablePreferenceScreenEntity;
    }

    public Set<SearchablePreferenceEntity> allPreferences() {
        return new HashSet<>(allPreferences);
    }
}