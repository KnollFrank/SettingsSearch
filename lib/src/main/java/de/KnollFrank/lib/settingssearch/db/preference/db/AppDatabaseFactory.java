package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphEntityDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.GraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphEntity;

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
        processAppDatabase(
                activityContext,
                locale,
                appDatabase,
                appDatabaseConfig
                        .prepackagedAppDatabase()
                        .map(PrepackagedAppDatabase::appDatabaseProcessor));
        return appDatabase;
    }

    private static void processAppDatabase(final FragmentActivity activityContext,
                                           final Locale locale,
                                           final AppDatabase appDatabase,
                                           final Optional<AppDatabaseProcessor> appDatabaseProcessor) {
        final SearchablePreferenceScreenGraphEntityDAO graphDAO = appDatabase.searchablePreferenceScreenGraphEntityDAO();
        graphDAO
                .findGraphById(locale)
                .map(GraphAndDbDataProvider::graph)
                .ifPresent(
                        graph -> {
                            if (!graph.processed()) {
                                appDatabaseProcessor.ifPresent(_appDatabaseProcessor -> _appDatabaseProcessor.processAppDatabase(appDatabase, locale, activityContext));
                                graphDAO.update(
                                        new SearchablePreferenceScreenGraphEntity(
                                                graph.id(),
                                                true));
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
