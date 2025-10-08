package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

public class DAOProviderFactory {

    public static DAOProvider createDAOProvider(final AppDatabaseConfig appDatabaseConfig, final FragmentActivity activityContext) {
        return AppDatabaseFactory.createAppDatabase(appDatabaseConfig, activityContext);
    }
}
