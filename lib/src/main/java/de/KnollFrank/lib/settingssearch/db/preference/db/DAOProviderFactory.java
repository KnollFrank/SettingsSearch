package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.common.Utils;

public class DAOProviderFactory {

    public static DAOProvider createDAOProvider(final PreferencesDatabaseConfig preferencesDatabaseConfig, final FragmentActivity activityContext) {
        return PreferencesDatabaseFactory.createPreferencesDatabase(
                preferencesDatabaseConfig,
                activityContext,
                Utils.getCurrentLanguageLocale(activityContext.getResources()));
    }
}
