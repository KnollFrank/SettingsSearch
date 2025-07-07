package de.KnollFrank.lib.settingssearch.db.preference.db;

public class DatabaseResetter {

    public static void resetDatabase(final AppDatabase appDatabase) {
        appDatabase.searchablePreferenceScreenGraphDAO().removeAll();
    }
}
