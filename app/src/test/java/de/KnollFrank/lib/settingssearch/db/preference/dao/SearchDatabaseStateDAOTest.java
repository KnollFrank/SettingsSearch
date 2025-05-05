package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;

@RunWith(RobolectricTestRunner.class)
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
