package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenGraphTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;
import de.KnollFrank.lib.settingssearch.graph.ComputePreferencesListener;

public class PreferencesDatabaseFactory {

    public static <C> PreferencesDatabase<C> createPreferencesDatabase(
            final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
            final C configuration,
            final Locale locale,
            final ConfigurationBundleConverter<C> configurationBundleConverter,
            final ComputePreferencesListener computePreferencesListener,
            final FragmentActivity activityContext) {
        final PreferencesRoomDatabase preferencesRoomDatabase =
                PreferencesRoomDatabaseFactory.createPreferencesRoomDatabase(
                        preferencesDatabaseConfig,
                        activityContext);
        processAndPersistGraph(
                preferencesRoomDatabase
                        .searchablePreferenceScreenGraphDAO()
                        .findGraphById(locale),
                preferencesDatabaseConfig
                        .prepackagedPreferencesDatabase()
                        .map(PrepackagedPreferencesDatabase::searchablePreferenceScreenGraphTransformer),
                preferencesRoomDatabase.searchablePreferenceScreenGraphDAO(),
                configuration,
                configurationBundleConverter,
                activityContext);
        return new PreferencesDatabase<>() {

            private final SearchablePreferenceScreenGraphRepository<C> searchablePreferenceScreenGraphRepository =
                    SearchablePreferenceScreenGraphRepository.of(
                            preferencesRoomDatabase.searchablePreferenceScreenGraphDAO(),
                            computePreferencesListener);

            @Override
            public SearchablePreferenceScreenGraphRepository<C> searchablePreferenceScreenGraphRepository() {
                return searchablePreferenceScreenGraphRepository;
            }
        };
    }

    private static <C> void processAndPersistGraph(final Optional<SearchablePreferenceScreenGraph> graph,
                                                   final Optional<SearchablePreferenceScreenGraphTransformer<C>> graphTransformer,
                                                   final SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO,
                                                   final C configuration,
                                                   final ConfigurationBundleConverter<C> configurationBundleConverter,
                                                   final FragmentActivity activityContext) {
        final InitialGraphTransformer<C> initialGraphTransformer =
                new InitialGraphTransformer<>(
                        graphTransformer,
                        searchablePreferenceScreenGraphDAO,
                        activityContext,
                        configurationBundleConverter);
        initialGraphTransformer.transformAndPersist(graph, configuration);
    }
}
