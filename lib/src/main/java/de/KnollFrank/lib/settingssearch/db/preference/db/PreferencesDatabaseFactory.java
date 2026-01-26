package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenTreeDao;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TreeProcessorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

public class PreferencesDatabaseFactory {

    private PreferencesDatabaseFactory() {
    }

    public static <C> PreferencesDatabase<C> createPreferencesDatabase(
            final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
            final C configuration,
            final Locale locale,
            final TreeProcessorFactory<C> treeProcessorFactory,
            final ConfigurationBundleConverter<C> configurationBundleConverter,
            final FragmentActivity activityContext) {
        final PreferencesRoomDatabase preferencesRoomDatabase =
                PreferencesRoomDatabaseFactory.createPreferencesRoomDatabase(
                        preferencesDatabaseConfig,
                        activityContext);
        processAndPersistTree(
                preferencesRoomDatabase
                        .searchablePreferenceScreenTreeDao()
                        .findTreeById(locale),
                preferencesDatabaseConfig
                        .prepackagedPreferencesDatabase()
                        .map(PrepackagedPreferencesDatabase::searchablePreferenceScreenTreeTransformer),
                preferencesRoomDatabase.searchablePreferenceScreenTreeDao(),
                configuration,
                configurationBundleConverter,
                activityContext);
        return new PreferencesDatabase<>() {

            private final SearchablePreferenceScreenTreeRepository<C> searchablePreferenceScreenTreeRepository =
                    new SearchablePreferenceScreenTreeRepository<>(
                            preferencesRoomDatabase.searchablePreferenceScreenTreeDao(),
                            TreeProcessorManagerFactory.createTreeProcessorManager(
                                    preferencesRoomDatabase.treeProcessorDescriptionEntityDao(),
                                    treeProcessorFactory,
                                    configurationBundleConverter));

            @Override
            public SearchablePreferenceScreenTreeRepository<C> searchablePreferenceScreenTreeRepository() {
                return searchablePreferenceScreenTreeRepository;
            }
        };
    }

    private static <C> void processAndPersistTree(final Optional<SearchablePreferenceScreenTree<PersistableBundle>> tree,
                                                  final Optional<SearchablePreferenceScreenTreeTransformer<C>> treeTransformer,
                                                  final SearchablePreferenceScreenTreeDao searchablePreferenceScreenTreeDao,
                                                  final C configuration,
                                                  final ConfigurationBundleConverter<C> configurationBundleConverter,
                                                  final FragmentActivity activityContext) {
        final InitialTreeTransformer<C> initialTreeTransformer =
                new InitialTreeTransformer<>(
                        treeTransformer,
                        searchablePreferenceScreenTreeDao,
                        activityContext,
                        configurationBundleConverter);
        initialTreeTransformer.transformAndPersist(tree, configuration);
    }
}
