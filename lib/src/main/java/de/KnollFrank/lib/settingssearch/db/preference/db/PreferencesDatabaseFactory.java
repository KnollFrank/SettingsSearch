package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenTreeDAO;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

public class PreferencesDatabaseFactory {

    private PreferencesDatabaseFactory() {
    }

    public static <C> PreferencesDatabase<C> createPreferencesDatabase(
            final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
            final C configuration,
            final Locale locale,
            final ConfigurationBundleConverter<C> configurationBundleConverter,
            final FragmentActivity activityContext) {
        final PreferencesRoomDatabase preferencesRoomDatabase =
                PreferencesRoomDatabaseFactory.createPreferencesRoomDatabase(
                        preferencesDatabaseConfig,
                        activityContext);
        processAndPersistTree(
                preferencesRoomDatabase
                        .searchablePreferenceScreenTreeDAO()
                        .findTreeById(locale),
                preferencesDatabaseConfig
                        .prepackagedPreferencesDatabase()
                        .map(PrepackagedPreferencesDatabase::searchablePreferenceScreenTreeTransformer),
                preferencesRoomDatabase.searchablePreferenceScreenTreeDAO(),
                configuration,
                configurationBundleConverter,
                activityContext);
        return new PreferencesDatabase<>() {

            private final SearchablePreferenceScreenTreeRepository<C> searchablePreferenceScreenTreeRepository =
                    SearchablePreferenceScreenTreeRepository.of(
                            preferencesRoomDatabase.searchablePreferenceScreenTreeDAO(),
                            configurationBundleConverter);

            @Override
            public SearchablePreferenceScreenTreeRepository<C> searchablePreferenceScreenTreeRepository() {
                return searchablePreferenceScreenTreeRepository;
            }
        };
    }

    private static <C> void processAndPersistTree(final Optional<SearchablePreferenceScreenTree<PersistableBundle>> tree,
                                                  final Optional<SearchablePreferenceScreenTreeTransformer<C>> treeTransformer,
                                                  final SearchablePreferenceScreenTreeDAO searchablePreferenceScreenTreeDAO,
                                                  final C configuration,
                                                  final ConfigurationBundleConverter<C> configurationBundleConverter,
                                                  final FragmentActivity activityContext) {
        final InitialTreeTransformer<C> initialTreeTransformer =
                new InitialTreeTransformer<>(
                        treeTransformer,
                        searchablePreferenceScreenTreeDAO,
                        activityContext,
                        configurationBundleConverter);
        initialTreeTransformer.transformAndPersist(tree, configuration);
    }
}
