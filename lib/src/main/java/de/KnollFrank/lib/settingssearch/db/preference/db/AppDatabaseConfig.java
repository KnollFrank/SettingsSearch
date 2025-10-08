package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.util.Optional;

public record AppDatabaseConfig(String databaseFileName,
                                Optional<PrepackagedAppDatabase> prepackagedAppDatabase,
                                JournalMode journalMode) {

    public enum JournalMode {
        AUTOMATIC,
        TRUNCATE,
        WRITE_AHEAD_LOGGING
    }
}
