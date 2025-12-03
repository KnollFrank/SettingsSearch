package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.common.Locales;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

public class PreferencesDatabaseFactory {

    public static <C> PreferencesDatabase<C> createPreferencesDatabase(
            final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
            final C configuration,
            final ConfigurationBundleConverter<C> configurationBundleConverter,
            final FragmentActivity activityContext) {
        return PreferencesRoomDatabaseFactory.createPreferencesRoomDatabase(
                preferencesDatabaseConfig,
                activityContext,
                Locales.getCurrentLanguageLocale(activityContext.getResources()),
                configuration,
                configurationBundleConverter);
    }
}
