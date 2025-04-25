package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.common.collect.Iterables;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJODAO;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

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
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreferencePOJO preference = createSomeSearchablePreference(1, Optional.empty(), Optional.empty());

        // When
        dao.persist(preference);

        // Then the preference was persisted at all
        final Optional<SearchablePreferencePOJO> preferenceFromDb = dao.findPreferenceById(preference.getId());
        assertThat(preferenceFromDb.isPresent(), is(true));

        // And the preference was persisted correctly
        SearchablePreferencePOJOEquality.assertActualEqualsExpected(preferenceFromDb.orElseThrow(), preference);
    }

    @Test
    public void shouldGetPredecessorOfPersistedPreference() {
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreferencePOJO predecessor = createSomeSearchablePreference(1, Optional.empty(), Optional.empty());
        final SearchablePreferencePOJO preference = createSomeSearchablePreference(2, Optional.empty(), Optional.of(predecessor.getId()));
        dao.persist(predecessor, preference);
        final SearchablePreferencePOJO preferenceFromDb = dao.findPreferenceById(preference.getId()).orElseThrow();

        // When
        final SearchablePreferencePOJO predecessorFromDb = preferenceFromDb.getPredecessor(dao).orElseThrow();

        // Then
        assertThat(predecessorFromDb, is(predecessor));
        assertThat(predecessorFromDb.getPredecessor(dao), is(Optional.empty()));
    }

    @Test
    public void shouldGetChildrenOfPersistedPreference() {
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreferencePOJO parent = createSomeSearchablePreference(1, Optional.empty(), Optional.empty());
        final SearchablePreferencePOJO child = createSomeSearchablePreference(2, Optional.of(parent.getId()), Optional.empty());
        dao.persist(parent, child);
        final SearchablePreferencePOJO parentFromDb = dao.findPreferenceById(parent.getId()).orElseThrow();

        // When
        final List<SearchablePreferencePOJO> childrenFromDb = parentFromDb.getChildren(dao);

        // Then
        assertThat(childrenFromDb, contains(child));

        final SearchablePreferencePOJO childFromDb = Iterables.getOnlyElement(childrenFromDb);
        assertThat(childFromDb.getChildren(dao), is(empty()));
    }

    @Test
    public void shouldRemovePreference() {
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreferencePOJO preference = createSomeSearchablePreference(1, Optional.empty(), Optional.empty());

        // When
        dao.persist(preference);
        dao.remove(preference);

        // Then
        final boolean removed =
                dao
                        .findPreferenceById(preference.getId())
                        .isEmpty();
        assertThat(removed, is(true));
    }

    // FK-TODO: add test shouldFindChildPreferenceByKeyAndHost()
    @Test
    public void shouldFindPreferenceByKeyAndHost() {
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreferencePOJO preference = createSomeSearchablePreference(1, Optional.empty(), Optional.empty());
        dao.persist(preference);

        // When
        final Optional<SearchablePreferencePOJO> preferenceFromDb =
                dao.findPreferenceByKeyAndHost(
                        preference.getKey(),
                        preference.getHost());

        // Then
        assertThat(preferenceFromDb.isPresent(), is(true));
        SearchablePreferencePOJOEquality.assertActualEqualsExpected(preferenceFromDb.orElseThrow(), preference);
    }

    @Test
    public void shouldNotFindPreferenceByKeyAndHost() {
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();

        // When
        final Optional<SearchablePreferencePOJO> preferenceFromDb =
                dao.findPreferenceByKeyAndHost(
                        "nonExistingKey",
                        PrefsFragmentFirst.class);

        // Then
        assertThat(preferenceFromDb.isEmpty(), is(true));
    }

    private static SearchablePreferencePOJO createSomeSearchablePreference(
            final int id,
            final Optional<Integer> parentId,
            final Optional<Integer> predecessorId) {
        return POJOTestFactory.createSearchablePreferencePOJO(
                id,
                Optional.of("some title"),
                Optional.of("some summary"),
                Optional.of("some searchable info"),
                Optional.empty(),
                parentId,
                predecessorId);
    }
}
