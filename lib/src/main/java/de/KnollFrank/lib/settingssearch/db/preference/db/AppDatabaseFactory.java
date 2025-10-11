package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;

public class AppDatabaseFactory {

    public static AppDatabase createAppDatabase(final AppDatabaseConfig appDatabaseConfig,
                                                final FragmentActivity activityContext,
                                                final Locale locale) {
        final RoomDatabase.Builder<AppDatabase> appDatabaseBuilder =
                Room
                        .databaseBuilder(
                                activityContext.getApplicationContext(),
                                AppDatabase.class,
                                appDatabaseConfig.databaseFileName())
                        .setJournalMode(asRoomJournalMode(appDatabaseConfig.journalMode()))
                        // FK-TODO: remove allowMainThreadQueries()
                        .allowMainThreadQueries();
        appDatabaseConfig
                .prepackagedAppDatabase()
                .map(PrepackagedAppDatabase::databaseAssetFile)
                .ifPresent(databaseAssetFile -> appDatabaseBuilder.createFromAsset(databaseAssetFile.getPath()));
        final AppDatabase appDatabase = appDatabaseBuilder.build();
        processAndPersistGraph(
                appDatabaseConfig
                        .prepackagedAppDatabase()
                        .map(PrepackagedAppDatabase::graphProcessor),
                appDatabase
                        .searchablePreferenceScreenGraphDAO()
                        .findGraphById(locale),
                activityContext,
                appDatabase.searchablePreferenceScreenGraphDAO());
        return appDatabase;
    }

    private static void processAndPersistGraph(final Optional<GraphProcessor> graphProcessor,
                                               final Optional<SearchablePreferenceScreenGraph> graph,
                                               final FragmentActivity activityContext,
                                               final SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO) {
        graph.ifPresent(
                _graph -> {
                    if (!_graph.processed()) {
                        searchablePreferenceScreenGraphDAO.persist(
                                graphProcessor
                                        .map(_graphProcessor -> _graphProcessor.processGraph(_graph, activityContext))
                                        .orElseGet(_graph::asProcessedGraph));
                    }
                });
    }

    private static RoomDatabase.JournalMode asRoomJournalMode(final AppDatabaseConfig.JournalMode journalMode) {
        return switch (journalMode) {
            case AUTOMATIC -> RoomDatabase.JournalMode.AUTOMATIC;
            case TRUNCATE -> RoomDatabase.JournalMode.TRUNCATE;
            case WRITE_AHEAD_LOGGING -> RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING;
        };
    }
}
