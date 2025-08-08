package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase.Builder;

import java.util.Optional;

public class AppDatabaseFactory {

    private static volatile Optional<AppDatabase> appDatabase = Optional.empty();

    public static synchronized AppDatabase getInstance(final AppDatabaseConfig appDatabaseConfig, final Context context) {
        if (appDatabase.isEmpty()) {
            appDatabase = Optional.of(createInstance(appDatabaseConfig, context));
        }
        return appDatabase.orElseThrow();
    }

    private static AppDatabase createInstance(final AppDatabaseConfig appDatabaseConfig, final Context context) {
        final Builder<AppDatabase> appDatabaseBuilder =
                Room
                        .databaseBuilder(
                                context.getApplicationContext(),
                                AppDatabase.class,
                                appDatabaseConfig.databaseFileName())
                        // FK-TODO: remove allowMainThreadQueries()
                        .allowMainThreadQueries();
        appDatabaseConfig
                .prepackagedDatabaseAssetFile()
                .ifPresent(prepackagedDatabaseAssetFile -> appDatabaseBuilder.createFromAsset(prepackagedDatabaseAssetFile.getPath()));
        return appDatabaseBuilder.build();
    }
}
