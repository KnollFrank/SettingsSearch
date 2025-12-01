package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.common.Locales;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

public class DAOProviderFactory {

    public static <C> DAOProvider<C> createDAOProvider(
            final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
            final C configuration,
            final ConfigurationBundleConverter<C> configurationBundleConverter,
            final FragmentActivity activityContext) {
        return PreferencesDatabaseFactory.createPreferencesDatabase(
                preferencesDatabaseConfig,
                activityContext,
                Locales.getCurrentLanguageLocale(activityContext.getResources()),
                configuration,
                configurationBundleConverter);
    }
}
