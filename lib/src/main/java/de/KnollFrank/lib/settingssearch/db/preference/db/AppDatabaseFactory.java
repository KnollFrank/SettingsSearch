package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.atomic.AtomicBoolean;

public class AppDatabaseFactory {

    public static AppDatabase createAppDatabase(
            final AppDatabaseConfig appDatabaseConfig,
            final FragmentActivity activityContext) {
        final RoomDatabase.Builder<AppDatabase> appDatabaseBuilder =
                Room.databaseBuilder(
                                activityContext.getApplicationContext(),
                                AppDatabase.class,
                                appDatabaseConfig.databaseFileName())
                        .setJournalMode(asRoomJournalMode(appDatabaseConfig.journalMode()))
                        // FK-TODO: remove allowMainThreadQueries()
                        .allowMainThreadQueries();
        final AtomicBoolean databaseCreated = new AtomicBoolean(false);
        // FK-TODO: extract method
        appDatabaseBuilder.addCallback(
                new RoomDatabase.Callback() {

                    @Override
                    public void onCreate(@NonNull final SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        databaseCreated.set(true);
                    }
                });
        appDatabaseConfig
                .prepackagedAppDatabase()
                .map(PrepackagedAppDatabase::databaseAssetFile)
                .ifPresent(databaseAssetFile -> appDatabaseBuilder.createFromAsset(databaseAssetFile.getPath()));
        final AppDatabase appDatabase = appDatabaseBuilder.build();
        if (databaseCreated.get()) {
            appDatabaseConfig
                    .prepackagedAppDatabase()
                    .map(PrepackagedAppDatabase::appDatabaseProcessor)
                    .ifPresent(appDatabaseProcessor -> appDatabaseProcessor.processAppDatabase(appDatabase, activityContext));
        }
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
