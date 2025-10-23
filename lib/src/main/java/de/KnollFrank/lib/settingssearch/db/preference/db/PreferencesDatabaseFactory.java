package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

public class PreferencesDatabaseFactory {

    public static <C> PreferencesDatabase createPreferencesDatabase(final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
                                                                    final FragmentActivity activityContext,
                                                                    final Locale locale,
                                                                    final C configuration,
                                                                    final ConfigurationBundleConverter<C> configurationBundleConverter) {
        final RoomDatabase.Builder<PreferencesDatabase> preferencesDatabaseBuilder =
                Room
                        .databaseBuilder(
                                activityContext.getApplicationContext(),
                                PreferencesDatabase.class,
                                preferencesDatabaseConfig.databaseFileName())
                        .setJournalMode(asRoomJournalMode(preferencesDatabaseConfig.journalMode()))
                        // FK-TODO: remove allowMainThreadQueries()
                        .allowMainThreadQueries();
        preferencesDatabaseConfig
                .prepackagedPreferencesDatabase()
                .map(PrepackagedPreferencesDatabase::databaseAssetFile)
                .ifPresent(databaseAssetFile -> preferencesDatabaseBuilder.createFromAsset(databaseAssetFile.getPath()));
        final PreferencesDatabase preferencesDatabase = preferencesDatabaseBuilder.build();
        processAndPersistGraph(
                preferencesDatabase
                        .searchablePreferenceScreenGraphDAO()
                        .findGraphById(locale),
                preferencesDatabaseConfig
                        .prepackagedPreferencesDatabase()
                        .map(PrepackagedPreferencesDatabase::searchablePreferenceScreenGraphProcessor),
                preferencesDatabase.searchablePreferenceScreenGraphDAO(),
                configuration,
                configurationBundleConverter,
                activityContext);
        return preferencesDatabase;
    }

    private static <C> void processAndPersistGraph(final Optional<SearchablePreferenceScreenGraph> graph,
                                                   final Optional<SearchablePreferenceScreenGraphProcessor<C>> graphProcessor,
                                                   final SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO,
                                                   final C configuration,
                                                   final ConfigurationBundleConverter<C> configurationBundleConverter,
                                                   final FragmentActivity activityContext) {
        final InitialGraphProcessor<C> initialGraphProcessor =
                new InitialGraphProcessor<>(
                        graphProcessor,
                        searchablePreferenceScreenGraphDAO,
                        activityContext,
                        configurationBundleConverter);
        initialGraphProcessor.processAndPersist(graph, configuration);
    }

    private static RoomDatabase.JournalMode asRoomJournalMode(final PreferencesDatabaseConfig.JournalMode journalMode) {
        return switch (journalMode) {
            case AUTOMATIC -> RoomDatabase.JournalMode.AUTOMATIC;
            case TRUNCATE -> RoomDatabase.JournalMode.TRUNCATE;
            case WRITE_AHEAD_LOGGING -> RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING;
        };
    }
}
