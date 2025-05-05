package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import java.util.Collection;

public class DatabasesResetter {

    public static void resetDatabases(final Context context) {
        resetDatabases(AppDatabaseProvider.getAppDatabases(context));
    }

    public static void resetDatabases(final Collection<AppDatabase> appDatabases) {
        appDatabases.forEach(DatabasesResetter::resetDatabase);
    }

    public static void resetDatabase(final AppDatabase appDatabase) {
        appDatabase.searchablePreferenceDAO().removeAll();
        appDatabase.searchDatabaseStateDAO().setSearchDatabaseInitialized(false);
    }
}
