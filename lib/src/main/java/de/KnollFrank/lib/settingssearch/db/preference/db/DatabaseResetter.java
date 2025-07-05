package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import java.util.Collection;

public class DatabaseResetter {

    public static void resetDatabases(final Context context) {
        resetDatabases(getDatabases(context));
    }

    public static void resetDatabases(final Collection<AppDatabase> appDatabases) {
        appDatabases.forEach(DatabaseResetter::resetDatabase);
    }

    public static void resetDatabase(final AppDatabase appDatabase) {
        appDatabase.searchablePreferenceScreenGraphEntityDAO().removePersistedGraph();
    }

    private static Collection<AppDatabase> getDatabases(final Context context) {
        return AppDatabaseProviderFactory
                .createAppDatabaseProvider(context)
                .getAppDatabases(context);
    }
}
