package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.io.File;
import java.util.stream.Stream;

record MergedPreferenceScreenDataFiles(File preferences,
                                       File predecessorIdByPreferenceId) {

    public boolean exists() {
        return Stream
                .of(preferences(), predecessorIdByPreferenceId())
                .allMatch(File::exists);
    }
}
