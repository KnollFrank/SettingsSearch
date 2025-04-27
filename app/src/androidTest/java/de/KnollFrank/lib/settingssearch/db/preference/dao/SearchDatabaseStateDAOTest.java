package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SearchDatabaseStateDAOTest extends AppDatabaseTest {

    @Test
    public void test_searchDatabaseInitialized() {
        final SearchDatabaseStateDAO dao = appDatabase.searchDatabaseStateDAO();
        // search database is initially not initialized
        assertThat(dao.isSearchDatabaseInitialized(), is(false));
        dao.setSearchDatabaseInitialized(true);
        assertThat(dao.isSearchDatabaseInitialized(), is(true));
    }
}
