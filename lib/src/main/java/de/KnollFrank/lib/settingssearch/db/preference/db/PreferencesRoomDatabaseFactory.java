package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.Callable;

import de.KnollFrank.lib.settingssearch.common.AssetsUtils;

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
        maybeCreateFromInputStreamCallable(databaseBuilder, preferencesDatabaseConfig, context);
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

    private static <C> void maybeCreateFromInputStreamCallable(
            final RoomDatabase.Builder<PreferencesRoomDatabase> databaseBuilder,
            final PreferencesDatabaseConfig<C> preferencesDatabaseConfig,
            final Context context) {
        if (!existsDatabaseFile(preferencesDatabaseConfig.databaseFileName(), context)) {
            PreferencesRoomDatabaseFactory
                    .getInputStreamCallable(
                            preferencesDatabaseConfig.prepackagedPreferencesDatabase(),
                            context.getAssets())
                    .ifPresent(databaseBuilder::createFromInputStream);
        }
    }

    private static boolean existsDatabaseFile(final String databaseFileName, final Context context) {
        final File databaseFile = context.getDatabasePath(databaseFileName);
        return databaseFile.exists();
    }

    private static <C> Optional<Callable<InputStream>> getInputStreamCallable(
            final Optional<PrepackagedPreferencesDatabase<C>> prepackagedPreferencesDatabase,
            final AssetManager assetManager) {
        return prepackagedPreferencesDatabase
                .map(PrepackagedPreferencesDatabase::databaseAssetFile)
                .map(databaseAssetFile -> createInputStreamCallable(databaseAssetFile, assetManager));
    }

    private static Callable<InputStream> createInputStreamCallable(final File databaseAssetFile, final AssetManager assetManager) {
        return () -> AssetsUtils.open(databaseAssetFile, assetManager);
    }
}
