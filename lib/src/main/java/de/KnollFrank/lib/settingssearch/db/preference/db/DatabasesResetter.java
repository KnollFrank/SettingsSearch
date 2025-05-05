package de.KnollFrank.lib.settingssearch.db.preference.db;

public class DatabasesResetter {

    public static void resetDatabase(final AppDatabase appDatabase) {
        appDatabase.searchablePreferenceDAO().removeAll();
        appDatabase.searchDatabaseStateDAO().setSearchDatabaseInitialized(false);
    }
}
