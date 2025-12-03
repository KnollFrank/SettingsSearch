package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

public class DAOProviderManager<C> {

    private Optional<PreferencesDatabase<C>> daoProvider = Optional.empty();
    private static final Object LOCK = new Object();

    public void initDAOProvider(final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
                                final C configuration,
                                final ConfigurationBundleConverter<C> configurationBundleConverter,
                                final FragmentActivity activityContext) {
        if (daoProvider.isEmpty()) {
            synchronized (LOCK) {
                if (daoProvider.isEmpty()) {
                    daoProvider =
                            Optional.of(
                                    PreferencesDatabaseFactory.createPreferencesDatabase(
                                            preferencesDatabaseConfig,
                                            configuration,
                                            configurationBundleConverter,
                                            activityContext));
                }
            }
        }
    }

    public PreferencesDatabase<C> getDAOProvider() {
        return daoProvider.orElseThrow(() -> new IllegalStateException("DAOProviderManager is not initialized. Call initDAOProvider() first."));
    }
}
