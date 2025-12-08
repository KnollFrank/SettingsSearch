package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;

public record PreferenceWithScreen(
        @Embedded
        SearchablePreferenceEntity preference,
        @Embedded(prefix = SCREEN_PREFIX)
        SearchablePreferenceScreenEntity screen) {

    public static final String SCREEN_PREFIX = "screen_";
}