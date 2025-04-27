package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchDatabaseStateDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferencePOJODAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchDatabaseState;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ClassConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalEitherIntegerOrStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalIntegerConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalStringConverter;

@Database(
        entities = {SearchablePreferencePOJO.class, SearchDatabaseState.class},
        version = 1,
        exportSchema = false)
@TypeConverters(
        {
                ClassConverter.class,
                OptionalEitherIntegerOrStringConverter.class,
                OptionalStringConverter.class,
                OptionalIntegerConverter.class
        })
public abstract class AppDatabase extends RoomDatabase {

    private static volatile Optional<AppDatabase> instance = Optional.empty();

    public static synchronized AppDatabase getInstance(final Context context) {
        if (instance.isEmpty()) {
            instance = Optional.of(createInstance(context));
        }
        return instance.orElseThrow();
    }

    private static AppDatabase createInstance(final Context context) {
        return Room
                .databaseBuilder(
                        context,
                        AppDatabase.class,
                        "searchable_preferences")
                .allowMainThreadQueries()
                .build();
    }

    protected AppDatabase() {
    }

    public abstract SearchablePreferencePOJODAO searchablePreferenceDAO();

    public abstract SearchDatabaseStateDAO searchDatabaseStateDAO();
}
