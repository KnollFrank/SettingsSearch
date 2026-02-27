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
        final var worker =
                new PreferencesRoomDatabaseFactoryWorker<>(
                        preferencesDatabaseConfig,
                        context);
        return worker.createPreferencesRoomDatabase();
    }

    private static class PreferencesRoomDatabaseFactoryWorker<C> {

        private final PreferencesDatabaseConfig<C> preferencesDatabaseConfig;
        private final Context context;

        public PreferencesRoomDatabaseFactoryWorker(final PreferencesDatabaseConfig<C> preferencesDatabaseConfig, final Context context) {
            this.preferencesDatabaseConfig = preferencesDatabaseConfig;
            this.context = context;
        }

        public PreferencesRoomDatabase createPreferencesRoomDatabase() {
            final RoomDatabase.Builder<PreferencesRoomDatabase> databaseBuilder = createPreferencesRoomDatabaseBuilder();
            maybeCreateFromInputStreamCallable(databaseBuilder);
            return databaseBuilder.build();
        }

        private RoomDatabase.Builder<PreferencesRoomDatabase> createPreferencesRoomDatabaseBuilder() {
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

        private void maybeCreateFromInputStreamCallable(final RoomDatabase.Builder<PreferencesRoomDatabase> databaseBuilder) {
            if (!getDatabaseFile().exists()) {
                getInputStreamCallable().ifPresent(databaseBuilder::createFromInputStream);
            }
        }

        private File getDatabaseFile() {
            return context.getDatabasePath(preferencesDatabaseConfig.databaseFileName());
        }

        private Optional<Callable<InputStream>> getInputStreamCallable() {
            return preferencesDatabaseConfig
                    .prepackagedPreferencesDatabase()
                    .map(PrepackagedPreferencesDatabase::databaseAssetFile)
                    .map(databaseAssetFile -> createInputStreamCallable(databaseAssetFile, context.getAssets()));
        }

        private static Callable<InputStream> createInputStreamCallable(final File databaseAssetFile, final AssetManager assetManager) {
            return () -> AssetsUtils.open(databaseAssetFile, assetManager);
        }
    }
}
