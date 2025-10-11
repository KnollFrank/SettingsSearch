package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.common.Utils;

public class DAOProviderFactory {

    public static DAOProvider createDAOProvider(final AppDatabaseConfig appDatabaseConfig, final FragmentActivity activityContext) {
        return AppDatabaseFactory.createAppDatabase(
                appDatabaseConfig,
                activityContext,
                Utils.getCurrentLanguageLocale(activityContext.getResources()));
    }
}
