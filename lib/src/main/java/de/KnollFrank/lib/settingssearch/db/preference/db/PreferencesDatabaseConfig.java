package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.util.Optional;

// FK-TODO: warum nicht "File databaseFileName" statt String?
public record PreferencesDatabaseConfig<C>(String databaseFileName,
                                           Optional<PrepackagedPreferencesDatabase<C>> prepackagedPreferencesDatabase,
                                           JournalMode journalMode) {

    public enum JournalMode {
        AUTOMATIC,
        TRUNCATE,
        WRITE_AHEAD_LOGGING
    }
}
