package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

public class PreferencesDatabaseFactory {

    public static <C> PreferencesDatabase<C> createPreferencesDatabase(
            final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
            final C configuration,
            final Locale locale,
            final ConfigurationBundleConverter<C> configurationBundleConverter,
            final FragmentActivity activityContext) {
        return PreferencesRoomDatabaseFactory.createPreferencesRoomDatabase(
                preferencesDatabaseConfig,
                configuration,
                locale,
                configurationBundleConverter,
                activityContext);
    }
}
