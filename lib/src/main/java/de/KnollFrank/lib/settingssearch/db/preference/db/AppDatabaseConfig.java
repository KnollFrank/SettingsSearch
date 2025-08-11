package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.io.File;
import java.util.Optional;

public record AppDatabaseConfig(String databaseFileName,
                                Optional<File> prepackagedDatabaseAssetFile,
                                JournalMode journalMode) {

    public enum JournalMode {
        AUTOMATIC,
        TRUNCATE,
        WRITE_AHEAD_LOGGING
    }
}
