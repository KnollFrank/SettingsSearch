package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SearchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchy {

    @Embedded
    private final SearchablePreferenceScreenEntity searchablePreferenceScreen;

    @Relation(
            parentColumn = "id",
            entityColumn = "searchablePreferenceScreenId")
    private final List<SearchablePreferenceEntity> allPreferencesOfPreferenceHierarchy;

    public SearchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchy(final SearchablePreferenceScreenEntity searchablePreferenceScreen,
                                                                            final List<SearchablePreferenceEntity> allPreferencesOfPreferenceHierarchy) {
        this.searchablePreferenceScreen = searchablePreferenceScreen;
        this.allPreferencesOfPreferenceHierarchy = allPreferencesOfPreferenceHierarchy;
    }

    public SearchablePreferenceScreenEntity searchablePreferenceScreen() {
        return searchablePreferenceScreen;
    }

    public Set<SearchablePreferenceEntity> allPreferencesOfPreferenceHierarchy() {
        return new HashSet<>(allPreferencesOfPreferenceHierarchy);
    }
}