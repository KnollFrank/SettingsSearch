package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;

public abstract class LocaleDatabaseTest {

    protected LocaleDatabase localeDatabase;

    @Before
    public void createAppDatabase() {
        localeDatabase =
                Room
                        .inMemoryDatabaseBuilder(
                                ApplicationProvider.getApplicationContext(),
                                LocaleDatabase.class)
                        .allowMainThreadQueries()
                        .build();
    }

    @After
    public void closeAppDatabase() {
        localeDatabase.close();
    }
}
