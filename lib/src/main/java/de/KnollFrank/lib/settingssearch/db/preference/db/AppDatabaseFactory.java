package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.Locale;

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
        appDatabase
                .searchablePreferenceScreenGraphDAO()
                .findGraphById(locale)
                .ifPresent(
                        graph -> {
                            final InitialGraphProcessor initialGraphProcessor =
                                    new InitialGraphProcessor(
                                            appDatabaseConfig
                                                    .prepackagedAppDatabase()
                                                    .map(PrepackagedAppDatabase::graphProcessor),
                                            appDatabase.searchablePreferenceScreenGraphDAO(),
                                            activityContext);
                            initialGraphProcessor.processAndPersist(graph);
                        });
        return appDatabase;
    }

    private static RoomDatabase.JournalMode asRoomJournalMode(final AppDatabaseConfig.JournalMode journalMode) {
        return switch (journalMode) {
            case AUTOMATIC -> RoomDatabase.JournalMode.AUTOMATIC;
            case TRUNCATE -> RoomDatabase.JournalMode.TRUNCATE;
            case WRITE_AHEAD_LOGGING -> RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING;
        };
    }
}
