package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.io.File;

public class FileDatabaseFactory {

    public static Database createFileDatabase(final File directory) {
        return new FileDatabase(getMergedPreferenceScreenDataFiles(directory));
    }

    private static MergedPreferenceScreenDataFiles getMergedPreferenceScreenDataFiles(final File directory) {
        return new MergedPreferenceScreenDataFiles(
                new File(directory, "preferences.json"),
                new File(directory, "preference_path_by_preference.json"),
                new File(directory, "host_by_preference.json"));
    }
}
