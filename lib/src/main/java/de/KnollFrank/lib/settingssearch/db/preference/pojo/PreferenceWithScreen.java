package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;

public record PreferenceWithScreen(
        @Embedded
        SearchablePreferenceEntity preference,
        // FK-TODO: extract constant for "screen_" and use in it here and in @Query of SearchablePreferenceScreenEntityDAO.
        @Embedded(prefix = "screen_")
        SearchablePreferenceScreenEntity screen) {
}