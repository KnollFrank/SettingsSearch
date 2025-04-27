package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;

@RunWith(AndroidJUnit4.class)
public class SearchDatabaseStateDAOTest {

    // FK-TODO: BEGIN: DRY with SearchablePreferencePOJODAOTest
    private AppDatabase appDatabase;

    @Before
    public void createDb() {
        appDatabase =
                Room
                        .inMemoryDatabaseBuilder(
                                ApplicationProvider.getApplicationContext(),
                                AppDatabase.class)
                        .build();
    }

    @After
    public void closeDb() {
        appDatabase.close();
    }
    // FK-TODO: END: DRY with SearchablePreferencePOJODAOTest

    @Test
    public void shouldSetInitialized() {
        final SearchDatabaseStateDAO dao = appDatabase.searchDatabaseStateDAO();
        // search database is initially not initialized
        assertThat(dao.isInitialized(), is(false));
        dao.setInitialized(true);
        assertThat(dao.isInitialized(), is(true));
    }
}
