package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

public class DAOProviderFactory {

    public static DAOProvider getDAOProvider(final Context context) {
        return AppDatabaseFactory.getInstance(context);
    }
}
