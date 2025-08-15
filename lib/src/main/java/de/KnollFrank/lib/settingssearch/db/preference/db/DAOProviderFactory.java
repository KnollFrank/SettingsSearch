package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

public class DAOProviderFactory {

    public static DAOProvider getDAOProvider(final AppDatabaseConfig appDatabaseConfig, final Context context) {
        return AppDatabaseFactory.createAppDatabase(appDatabaseConfig, context);
    }
}
