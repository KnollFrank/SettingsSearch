package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import androidx.room.Room;

import java.util.Optional;

public class AppDatabaseFactory {

    private static volatile Optional<AppDatabase> appDatabase = Optional.empty();

    public static synchronized AppDatabase getInstance(final Context context) {
        if (appDatabase.isEmpty()) {
            appDatabase = Optional.of(createInstance(context));
        }
        return appDatabase.orElseThrow();
    }

    private static AppDatabase createInstance(final Context context) {
        return Room
                .databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "searchable_preferences")
                // FK-TODO: remove allowMainThreadQueries()
                .allowMainThreadQueries()
                .build();
    }
}
