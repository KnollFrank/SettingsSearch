package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityTestFactory.createSearchablePreference;

import com.google.common.collect.Iterables;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.test.SearchablePreferenceEntityEquality;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceDaoTest extends PreferencesRoomDatabaseTest {

    @Test
    public void shouldPersistAndFindPreferenceById() {
        // Given
        final SearchablePreferenceEntityDao dao = preferencesRoomDatabase.searchablePreferenceEntityDao();
        final SearchablePreferenceEntity preference =
                createSomeSearchablePreference(
                        "1",
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));

        // When
        dao.persist(List.of(preference));

        // Then the preference was persisted at all
        final Optional<SearchablePreferenceEntity> preferenceFromDb = dao.findPreferenceById(preference.id());
        assertThat(preferenceFromDb.isPresent(), is(true));

        // And the preference was persisted correctly
        SearchablePreferenceEntityEquality.assertActualEqualsExpected(preferenceFromDb.orElseThrow(), preference);
    }

    @Test
    public void shouldGetPredecessorOfPersistedPreference() {
        // Given
        final SearchablePreferenceEntityDao dao = preferencesRoomDatabase.searchablePreferenceEntityDao();
        final SearchablePreferenceEntity predecessor =
                createSomeSearchablePreference(
                        "1",
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        final SearchablePreferenceEntity preference =
                createSomeSearchablePreference(
                        "2",
                        Optional.empty(),
                        Optional.of(predecessor.id()),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        dao.persist(List.of(predecessor, preference));
        final SearchablePreferenceEntity preferenceFromDb = dao.findPreferenceById(preference.id()).orElseThrow();

        // When
        final SearchablePreferenceEntity predecessorFromDb = preferenceFromDb.getPredecessor(dao).orElseThrow();

        // Then
        assertThat(predecessorFromDb, is(predecessor));
        assertThat(predecessorFromDb.getPredecessor(dao), is(Optional.empty()));
    }

    @Test
    public void shouldGetChildrenOfPersistedPreference() {
        // Given
        final SearchablePreferenceEntityDao dao = preferencesRoomDatabase.searchablePreferenceEntityDao();
        final SearchablePreferenceEntity parent =
                createSomeSearchablePreference(
                        "1",
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        final SearchablePreferenceEntity child =
                createSomeSearchablePreference(
                        "2",
                        Optional.of(parent.id()),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        dao.persist(List.of(parent, child));
        final SearchablePreferenceEntity parentFromDb = dao.findPreferenceById(parent.id()).orElseThrow();

        // When
        final Set<SearchablePreferenceEntity> childrenFromDb = parentFromDb.getChildren(dao);

        // Then
        assertThat(childrenFromDb, contains(child));

        final SearchablePreferenceEntity childFromDb = Iterables.getOnlyElement(childrenFromDb);
        assertThat(childFromDb.getChildren(dao), is(empty()));
    }

    @Test
    public void shouldRemovePreference() {
        // Given
        final SearchablePreferenceEntityDao dao = preferencesRoomDatabase.searchablePreferenceEntityDao();
        final SearchablePreferenceEntity preference =
                createSomeSearchablePreference(
                        "1",
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));

        // When
        dao.persist(List.of(preference));
        dao.remove(List.of(preference));

        // Then
        final boolean removed =
                dao
                        .findPreferenceById(preference.id())
                        .isEmpty();
        assertThat(removed, is(true));
    }

    private static SearchablePreferenceEntity createSomeSearchablePreference(
            final String id,
            final Optional<String> parentId,
            final Optional<String> predecessorId,
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo) {
        return createSearchablePreference(
                id,
                title,
                summary,
                searchableInfo,
                Optional.empty(),
                parentId,
                predecessorId);
    }
}
