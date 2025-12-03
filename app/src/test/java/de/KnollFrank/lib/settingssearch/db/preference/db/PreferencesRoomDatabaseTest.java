package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;

public abstract class PreferencesRoomDatabaseTest {

    protected PreferencesRoomDatabase preferencesRoomDatabase;

    @Before
    public void createPreferencesDatabase() {
        preferencesRoomDatabase =
                Room
                        .inMemoryDatabaseBuilder(
                                ApplicationProvider.getApplicationContext(),
                                PreferencesRoomDatabase.class)
                        .allowMainThreadQueries()
                        .build();
    }

    @After
    public void closePreferencesDatabase() {
        preferencesRoomDatabase.close();
    }
}
