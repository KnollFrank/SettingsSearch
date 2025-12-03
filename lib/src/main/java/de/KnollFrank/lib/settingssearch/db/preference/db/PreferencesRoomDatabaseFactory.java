package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

public class PreferencesRoomDatabaseFactory {

    public static <C> PreferencesRoomDatabase createPreferencesRoomDatabase(
            final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
            final C configuration,
            final Locale locale,
            final ConfigurationBundleConverter<C> configurationBundleConverter,
            final FragmentActivity activityContext) {
        final RoomDatabase.Builder<PreferencesRoomDatabase> preferencesDatabaseBuilder =
                Room
                        .databaseBuilder(
                                activityContext.getApplicationContext(),
                                PreferencesRoomDatabase.class,
                                preferencesDatabaseConfig.databaseFileName())
                        .setJournalMode(asRoomJournalMode(preferencesDatabaseConfig.journalMode()))
                        // FK-TODO: remove allowMainThreadQueries()
                        .allowMainThreadQueries();
        preferencesDatabaseConfig
                .prepackagedPreferencesDatabase()
                .map(PrepackagedPreferencesDatabase::databaseAssetFile)
                .ifPresent(databaseAssetFile -> preferencesDatabaseBuilder.createFromAsset(databaseAssetFile.getPath()));
        final PreferencesRoomDatabase preferencesRoomDatabase = preferencesDatabaseBuilder.build();
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
        return preferencesRoomDatabase;
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

    private static RoomDatabase.JournalMode asRoomJournalMode(final PreferencesDatabaseConfig.JournalMode journalMode) {
        return switch (journalMode) {
            case AUTOMATIC -> RoomDatabase.JournalMode.AUTOMATIC;
            case TRUNCATE -> RoomDatabase.JournalMode.TRUNCATE;
            case WRITE_AHEAD_LOGGING -> RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING;
        };
    }
}
