package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;

abstract class AppDatabaseTest {

    protected AppDatabase appDatabase;

    @Before
    public void createAppDatabase() {
        appDatabase =
                Room
                        .inMemoryDatabaseBuilder(
                                ApplicationProvider.getApplicationContext(),
                                AppDatabase.class)
                        .build();
    }

    @After
    public void closeAppDatabase() {
        appDatabase.close();
    }
}
