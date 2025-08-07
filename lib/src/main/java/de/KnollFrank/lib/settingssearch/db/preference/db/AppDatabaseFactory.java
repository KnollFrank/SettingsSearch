package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.room.Room;
import androidx.room.RoomDatabase.Builder;

import java.io.File;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.AssetsUtils;

public class AppDatabaseFactory {

    private static volatile Optional<AppDatabase> appDatabase = Optional.empty();

    public static synchronized AppDatabase getInstance(final Context context) {
        if (appDatabase.isEmpty()) {
            appDatabase = Optional.of(createInstance(context));
        }
        return appDatabase.orElseThrow();
    }

    private static AppDatabase createInstance(final Context context) {
        final Builder<AppDatabase> appDatabaseBuilder =
                Room
                        .databaseBuilder(
                                context.getApplicationContext(),
                                AppDatabase.class,
                                "searchable_preferences.db")
                        // FK-TODO: remove allowMainThreadQueries()
                        .allowMainThreadQueries();
        addDatabaseFileIfExists(
                appDatabaseBuilder,
                new File("database/searchable_preferences_prepackaged.db"),
                context.getAssets());
        return appDatabaseBuilder.build();
    }

    private static void addDatabaseFileIfExists(final Builder<AppDatabase> appDatabaseBuilder,
                                                final File databaseFile,
                                                final AssetManager assetManager) {
        if (AssetsUtils.assetExists(databaseFile, assetManager)) {
            appDatabaseBuilder.createFromAsset(databaseFile.getPath());
        }
    }
}
