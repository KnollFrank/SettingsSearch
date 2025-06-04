package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SearchablePreferenceScreenAndAllPreferences {

    @Embedded
    private final SearchablePreferenceScreenEntity searchablePreferenceScreen;

    @Relation(
            parentColumn = "id",
            entityColumn = "searchablePreferenceScreenId")
    private final List<SearchablePreferenceEntity> allPreferences;

    public SearchablePreferenceScreenAndAllPreferences(final SearchablePreferenceScreenEntity searchablePreferenceScreen,
                                                       final List<SearchablePreferenceEntity> allPreferences) {
        this.searchablePreferenceScreen = searchablePreferenceScreen;
        this.allPreferences = allPreferences;
    }

    public SearchablePreferenceScreenEntity searchablePreferenceScreen() {
        return searchablePreferenceScreen;
    }

    public Set<SearchablePreferenceEntity> allPreferences() {
        return new HashSet<>(allPreferences);
    }
}