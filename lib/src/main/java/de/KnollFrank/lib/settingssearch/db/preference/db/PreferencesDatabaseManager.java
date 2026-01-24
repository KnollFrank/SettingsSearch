package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Locales;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TreeProcessorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

public class PreferencesDatabaseManager<C> {

    private Optional<PreferencesDatabase<C>> preferencesDatabase = Optional.empty();
    private static final Object LOCK = new Object();

    public void initPreferencesDatabase(final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
                                        final C configuration,
                                        final TreeProcessorFactory<C> treeProcessorFactory,
                                        final ConfigurationBundleConverter<C> configurationBundleConverter,
                                        final FragmentActivity activityContext) {
        if (preferencesDatabase.isEmpty()) {
            synchronized (LOCK) {
                if (preferencesDatabase.isEmpty()) {
                    preferencesDatabase =
                            Optional.of(
                                    PreferencesDatabaseFactory.createPreferencesDatabase(
                                            preferencesDatabaseConfig,
                                            configuration,
                                            Locales.getCurrentLanguageLocale(activityContext.getResources()),
                                            treeProcessorFactory,
                                            configurationBundleConverter,
                                            activityContext));
                }
            }
        }
    }

    public PreferencesDatabase<C> getPreferencesDatabase() {
        return preferencesDatabase.orElseThrow(() -> new IllegalStateException("PreferencesDatabaseManager is not initialized. Call initPreferencesDatabase() first."));
    }
}
