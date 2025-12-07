package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;

public record PreferenceWithScreen(
        @Embedded
        SearchablePreferenceEntity preference,
        // FK-TODO: extract constant for "screen_" and use in it here and in @Query of SearchablePreferenceScreenEntityDAO.
        @Embedded(prefix = SCREEN_PREFIX)
        SearchablePreferenceScreenEntity screen) {

    public static final String SCREEN_PREFIX = "screen_";
}