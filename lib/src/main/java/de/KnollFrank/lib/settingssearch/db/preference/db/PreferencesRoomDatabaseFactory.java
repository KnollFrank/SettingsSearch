package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

class PreferencesRoomDatabaseFactory {

    private PreferencesRoomDatabaseFactory() {
    }

    public static <C> PreferencesRoomDatabase createPreferencesRoomDatabase(
            final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
            final Context context) {
        final RoomDatabase.Builder<PreferencesRoomDatabase> preferencesDatabaseBuilder =
                Room
                        .databaseBuilder(
                                context.getApplicationContext(),
                                PreferencesRoomDatabase.class,
                                preferencesDatabaseConfig.databaseFileName())
                        .setJournalMode(asRoomJournalMode(preferencesDatabaseConfig.journalMode()))
                        // FK-TODO: remove allowMainThreadQueries()
                        .allowMainThreadQueries();
        preferencesDatabaseConfig
                .prepackagedPreferencesDatabase()
                .map(PrepackagedPreferencesDatabase::databaseAssetFile)
                .ifPresent(databaseAssetFile -> preferencesDatabaseBuilder.createFromAsset(databaseAssetFile.getPath()));
        return preferencesDatabaseBuilder.build();
    }

    private static RoomDatabase.JournalMode asRoomJournalMode(final PreferencesDatabaseConfig.JournalMode journalMode) {
        return switch (journalMode) {
            case AUTOMATIC -> RoomDatabase.JournalMode.AUTOMATIC;
            case TRUNCATE -> RoomDatabase.JournalMode.TRUNCATE;
            case WRITE_AHEAD_LOGGING -> RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING;
        };
    }
}
