package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.io.File;
import java.util.Locale;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchDatabaseDirectoryIO;

public class FileDatabaseFactory {

    private final SearchDatabaseDirectoryIO searchDatabaseDirectoryIO;

    public FileDatabaseFactory(final SearchDatabaseDirectoryIO searchDatabaseDirectoryIO) {
        this.searchDatabaseDirectoryIO = searchDatabaseDirectoryIO;
    }

    public Database createFileDatabase(final Locale locale) {
        final File directory4Locale = searchDatabaseDirectoryIO.getAndMakeSearchDatabaseDirectory4Locale(locale);
        return new FileDatabase(getMergedPreferenceScreenDataFiles(directory4Locale));
    }

    private static MergedPreferenceScreenDataFiles getMergedPreferenceScreenDataFiles(final File directory) {
        return new MergedPreferenceScreenDataFiles(
                new File(directory, "preferences.json"),
                new File(directory, "preference_path_by_preference.json"));
    }
}
