package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.File;

class PreferencesRoomDatabaseFactory {

    private PreferencesRoomDatabaseFactory() {
    }

    public static <C> PreferencesRoomDatabase createPreferencesRoomDatabase(
            final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
            final Context context) {
        final RoomDatabase.Builder<PreferencesRoomDatabase> databaseBuilder =
                createPreferencesRoomDatabaseBuilder(
                        preferencesDatabaseConfig,
                        context);
        maybeCreateFromAsset(databaseBuilder, preferencesDatabaseConfig, context);
        return databaseBuilder.build();
    }

    private static <C> RoomDatabase.Builder<PreferencesRoomDatabase> createPreferencesRoomDatabaseBuilder(
            final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
            final Context context) {
        return Room
                .databaseBuilder(
                        context.getApplicationContext(),
                        PreferencesRoomDatabase.class,
                        preferencesDatabaseConfig.databaseFileName())
                .setJournalMode(asRoomJournalMode(preferencesDatabaseConfig.journalMode()))
                // FK-TODO: remove allowMainThreadQueries()
                .allowMainThreadQueries();
    }

    private static RoomDatabase.JournalMode asRoomJournalMode(final PreferencesDatabaseConfig.JournalMode journalMode) {
        return switch (journalMode) {
            case AUTOMATIC -> RoomDatabase.JournalMode.AUTOMATIC;
            case TRUNCATE -> RoomDatabase.JournalMode.TRUNCATE;
            case WRITE_AHEAD_LOGGING -> RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING;
        };
    }

    private static <C> void maybeCreateFromAsset(final RoomDatabase.Builder<PreferencesRoomDatabase> databaseBuilder,
                                                 final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
                                                 final Context context) {
        final File databaseFile = context.getDatabasePath(preferencesDatabaseConfig.databaseFileName());
        if (!databaseFile.exists()) {
            preferencesDatabaseConfig
                    .prepackagedPreferencesDatabase()
                    .map(PrepackagedPreferencesDatabase::databaseAssetFile)
                    .ifPresent(databaseAssetFile -> databaseBuilder.createFromAsset(databaseAssetFile.getPath()));
        }
    }
}
