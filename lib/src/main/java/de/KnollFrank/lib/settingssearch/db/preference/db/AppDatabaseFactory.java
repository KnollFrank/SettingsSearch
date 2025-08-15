package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabase.Builder;

public class AppDatabaseFactory {

    public static AppDatabase createAppDatabase(final AppDatabaseConfig appDatabaseConfig, final Context context) {
        final Builder<AppDatabase> appDatabaseBuilder =
                Room
                        .databaseBuilder(
                                context.getApplicationContext(),
                                AppDatabase.class,
                                appDatabaseConfig.databaseFileName())
                        .setJournalMode(asRoomJournalMode(appDatabaseConfig.journalMode()))
                        // FK-TODO: remove allowMainThreadQueries()
                        .allowMainThreadQueries();
        appDatabaseConfig
                .prepackagedDatabaseAssetFile()
                .ifPresent(prepackagedDatabaseAssetFile -> appDatabaseBuilder.createFromAsset(prepackagedDatabaseAssetFile.getPath()));
        return appDatabaseBuilder.build();
    }

    private static RoomDatabase.JournalMode asRoomJournalMode(final AppDatabaseConfig.JournalMode journalMode) {
        return switch (journalMode) {
            case AUTOMATIC -> RoomDatabase.JournalMode.AUTOMATIC;
            case TRUNCATE -> RoomDatabase.JournalMode.TRUNCATE;
            case WRITE_AHEAD_LOGGING -> RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING;
        };
    }
}
