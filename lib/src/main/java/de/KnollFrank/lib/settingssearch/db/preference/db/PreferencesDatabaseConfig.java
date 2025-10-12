package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.util.Optional;

public record PreferencesDatabaseConfig(String databaseFileName,
                                        Optional<PrepackagedPreferencesDatabase> prepackagedPreferencesDatabase,
                                        JournalMode journalMode) {

    public enum JournalMode {
        AUTOMATIC,
        TRUNCATE,
        WRITE_AHEAD_LOGGING
    }
}
