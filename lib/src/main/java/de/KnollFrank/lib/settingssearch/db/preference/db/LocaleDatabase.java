package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.LocaleDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.Locale;

@Database(
        entities = {Locale.class},
        version = 1,
        exportSchema = false)
public abstract class LocaleDatabase extends RoomDatabase {

    private static volatile Optional<LocaleDatabase> INSTANCE = Optional.empty();

    public static synchronized LocaleDatabase getInstance(final Context context) {
        if (INSTANCE.isEmpty()) {
            INSTANCE = Optional.of(createInstance(context));
        }
        return INSTANCE.orElseThrow();
    }

    private static LocaleDatabase createInstance(final Context context) {
        return Room
                .databaseBuilder(
                        context,
                        LocaleDatabase.class,
                        "locale_database")
                // FK-TODO: remove allowMainThreadQueries()
                .allowMainThreadQueries()
                .build();
    }

    protected LocaleDatabase() {
    }

    public abstract LocaleDAO localeDAO();
}
