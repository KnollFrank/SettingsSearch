package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SearchablePreferenceScreenAndAllPreferences {

    @Embedded
    private final SearchablePreferenceScreen searchablePreferenceScreen;

    @Relation(
            parentColumn = "id",
            entityColumn = "searchablePreferenceScreenId")
    private final List<SearchablePreference> allPreferences;

    public SearchablePreferenceScreenAndAllPreferences(final SearchablePreferenceScreen searchablePreferenceScreen,
                                                       final List<SearchablePreference> allPreferences) {
        this.searchablePreferenceScreen = searchablePreferenceScreen;
        this.allPreferences = allPreferences;
    }

    public SearchablePreferenceScreen searchablePreferenceScreen() {
        return searchablePreferenceScreen;
    }

    public Set<SearchablePreference> allPreferences() {
        return new HashSet<>(allPreferences);
    }
}