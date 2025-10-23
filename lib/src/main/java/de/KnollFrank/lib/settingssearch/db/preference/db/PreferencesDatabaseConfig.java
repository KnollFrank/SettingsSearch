package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.util.Optional;

public record PreferencesDatabaseConfig<C>(String databaseFileName,
                                           Optional<PrepackagedPreferencesDatabase<C>> prepackagedPreferencesDatabase,
                                           JournalMode journalMode) {

    public enum JournalMode {
        AUTOMATIC,
        TRUNCATE,
        WRITE_AHEAD_LOGGING
    }
}
