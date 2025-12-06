package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;

public record PreferenceWithScreen(
        // FK-TODO: extract constant for "pref_" and use in it here and in @Query of SearchablePreferenceScreenEntityDAO.
        @Embedded(prefix = "pref_")
        SearchablePreferenceEntity preference,
        @Embedded
        SearchablePreferenceScreenEntity screen) {
}