package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

public class AppDatabaseFactory {

    public static AppDatabase createAppDatabase(final AppDatabaseConfig appDatabaseConfig,
                                                final FragmentActivity activityContext) {
        return createAppDatabase(
                appDatabaseConfig,
                activityContext,
                isDatabaseNew(
                        appDatabaseConfig.databaseFileName(),
                        activityContext.getApplicationContext()));
    }

    private static AppDatabase createAppDatabase(final AppDatabaseConfig appDatabaseConfig,
                                                 final FragmentActivity activityContext,
                                                 final boolean isDatabaseNew) {
        final RoomDatabase.Builder<AppDatabase> appDatabaseBuilder =
                Room.databaseBuilder(
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
        if (isDatabaseNew) {
            appDatabaseConfig
                    .prepackagedAppDatabase()
                    .map(PrepackagedAppDatabase::appDatabaseProcessor)
                    .ifPresent(appDatabaseProcessor -> appDatabaseProcessor.processAppDatabase(appDatabase, activityContext));
        }
        return appDatabase;
    }

    private static boolean isDatabaseNew(final String databaseFileName, final Context context) {
        return !databaseExists(databaseFileName, context);
    }

    private static boolean databaseExists(final String databaseFileName, final Context context) {
        return context
                .getDatabasePath(databaseFileName)
                .exists();
    }

    private static RoomDatabase.JournalMode asRoomJournalMode(final AppDatabaseConfig.JournalMode journalMode) {
        return switch (journalMode) {
            case AUTOMATIC -> RoomDatabase.JournalMode.AUTOMATIC;
            case TRUNCATE -> RoomDatabase.JournalMode.TRUNCATE;
            case WRITE_AHEAD_LOGGING -> RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING;
        };
    }
}
