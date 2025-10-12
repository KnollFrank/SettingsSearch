package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;

public abstract class PreferencesDatabaseTest {

    protected PreferencesDatabase preferencesDatabase;

    @Before
    public void createPreferencesDatabase() {
        preferencesDatabase =
                Room
                        .inMemoryDatabaseBuilder(
                                ApplicationProvider.getApplicationContext(),
                                PreferencesDatabase.class)
                        .allowMainThreadQueries()
                        .build();
    }

    @After
    public void closePreferencesDatabase() {
        preferencesDatabase.close();
    }
}
