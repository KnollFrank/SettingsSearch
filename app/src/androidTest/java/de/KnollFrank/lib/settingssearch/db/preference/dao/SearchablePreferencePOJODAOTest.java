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

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJODAO;

@RunWith(AndroidJUnit4.class)
public class SearchablePreferencePOJODAOTest {

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

    @Test
    public void shouldPersistPreference() {
        // Given
        final SearchablePreferencePOJODAO searchablePreferencePOJODAO = appDatabase.searchablePreferenceDAO();
        final SearchablePreferencePOJO preference = createSomeSearchablePreference(1, -1);

        // When
        searchablePreferencePOJODAO.persist(preference);

        // Then the preference was persisted at all
        final Optional<SearchablePreferencePOJO> preferenceFromDb = searchablePreferencePOJODAO.findPreferenceById(preference.getId());
        assertThat(preferenceFromDb.isPresent(), is(true));

        // And the preference was persisted correctly
        SearchablePreferencePOJOEquality.assertActualEqualsExpected(preferenceFromDb.orElseThrow(), preference);
    }

    @Test
    public void shouldGetPredecessorOfPersistedPreference() {
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreferencePOJO predecessor = createSomeSearchablePreference(1, -1);
        final SearchablePreferencePOJO preference = createSomeSearchablePreference(2, predecessor.getId());

        // When
        dao.persist(predecessor, preference);

        // Then
        final SearchablePreferencePOJO preferenceFromDb = dao.findPreferenceById(preference.getId()).orElseThrow();
        final SearchablePreferencePOJO predecessorFromDb = preferenceFromDb.getPredecessor(dao).orElseThrow();
        assertThat(predecessorFromDb, is(predecessor));
        assertThat(predecessor.getPredecessor(dao), is(Optional.empty()));
    }

    @Test
    public void shouldRemovePreference() {
        // Given
        final SearchablePreferencePOJODAO searchablePreferencePOJODAO = appDatabase.searchablePreferenceDAO();
        final SearchablePreferencePOJO preference = createSomeSearchablePreference(1, -1);

        // When
        searchablePreferencePOJODAO.persist(preference);
        searchablePreferencePOJODAO.remove(preference);

        // Then
        final boolean removed =
                searchablePreferencePOJODAO
                        .findPreferenceById(preference.getId())
                        .isEmpty();
        assertThat(removed, is(true));
    }

    private static SearchablePreferencePOJO createSomeSearchablePreference(final int id, final int predecessorId) {
        return POJOTestFactory.createSearchablePreferencePOJO(
                id,
                Optional.of("some title"),
                Optional.of("some summary"),
                Optional.of("some searchable info"),
                Optional.empty(),
                predecessorId);
    }
}
