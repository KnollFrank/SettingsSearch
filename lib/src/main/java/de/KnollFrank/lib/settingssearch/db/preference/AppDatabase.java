package de.KnollFrank.lib.settingssearch.db.preference;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferencePOJODAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

@Database(entities = {SearchablePreferencePOJO.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = createInstance(context);
        }
        return instance;
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

    public abstract SearchablePreferencePOJODAO searchablePreferencePOJODAO();
}
