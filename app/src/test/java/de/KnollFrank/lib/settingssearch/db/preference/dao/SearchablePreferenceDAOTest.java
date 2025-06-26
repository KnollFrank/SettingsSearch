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

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.test.SearchablePreferenceEquality;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceDAOTest extends AppDatabaseTest {

    @Test
    public void shouldPersistAndFindPreferenceById() {
        // Given
        final SearchablePreferenceEntityDAO dao = appDatabase.searchablePreferenceEntityDAO();
        final SearchablePreferenceEntity preference =
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));

        // When
        dao.persist(preference);

        // Then the preference was persisted at all
        final Optional<SearchablePreferenceEntity> preferenceFromDb = dao.findPreferenceById(preference.getId());
        assertThat(preferenceFromDb.isPresent(), is(true));

        // And the preference was persisted correctly
        SearchablePreferenceEquality.assertActualEqualsExpected(preferenceFromDb.orElseThrow(), preference);
    }

    @Test
    public void shouldUpdateSummaryOfPreference() {
        // Given
        final Optional<String> oldSummary = Optional.of("old summary");
        final Optional<String> newSummary = Optional.of("new summary");

        final SearchablePreferenceEntityDAO dao = appDatabase.searchablePreferenceEntityDAO();
        final SearchablePreferenceEntity preference =
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        oldSummary,
                        Optional.empty());
        dao.persist(preference);

        // When
        preference.setSummary(newSummary);
        dao.update(preference);

        // Then
        final Optional<String> summaryFromDb =
                dao
                        .findPreferenceById(preference.getId())
                        .orElseThrow()
                        .getSummary();
        assertThat(summaryFromDb, is(newSummary));
    }

    @Test
    public void shouldGetPredecessorOfPersistedPreference() {
        // Given
        final SearchablePreferenceEntityDAO dao = appDatabase.searchablePreferenceEntityDAO();
        final SearchablePreferenceEntity predecessor =
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        final SearchablePreferenceEntity preference =
                createSomeSearchablePreference(
                        2,
                        Optional.empty(),
                        Optional.of(predecessor.getId()),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        dao.persist(predecessor, preference);
        final SearchablePreferenceEntity preferenceFromDb = dao.findPreferenceById(preference.getId()).orElseThrow();

        // When
        final SearchablePreferenceEntity predecessorFromDb = preferenceFromDb.getPredecessor(dao).orElseThrow();

        // Then
        assertThat(predecessorFromDb, is(predecessor));
        assertThat(predecessorFromDb.getPredecessor(dao), is(Optional.empty()));
    }

    @Test
    public void shouldGetChildrenOfPersistedPreference() {
        // Given
        final SearchablePreferenceEntityDAO dao = appDatabase.searchablePreferenceEntityDAO();
        final SearchablePreferenceEntity parent =
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        final SearchablePreferenceEntity child =
                createSomeSearchablePreference(
                        2,
                        Optional.of(parent.getId()),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        dao.persist(parent, child);
        final SearchablePreferenceEntity parentFromDb = dao.findPreferenceById(parent.getId()).orElseThrow();

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
        final SearchablePreferenceEntityDAO dao = appDatabase.searchablePreferenceEntityDAO();
        final SearchablePreferenceEntity preference =
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));

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

    @Test
    public void shouldGetMaxId() {
        // Given
        final SearchablePreferenceEntityDAO dao = appDatabase.searchablePreferenceEntityDAO();
        final int maxId = 4711;
        final SearchablePreferenceEntity preference =
                createSomeSearchablePreference(
                        maxId,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty());
        dao.persist(preference);

        // When
        final Optional<Integer> maxIdActual = dao.getMaxId();

        // Then
        assertThat(maxIdActual, is(Optional.of(maxId)));
    }

    @Test
    public void shouldGetMaxId_emptyDatabase() {
        // Given
        final SearchablePreferenceEntityDAO dao = appDatabase.searchablePreferenceEntityDAO();

        // When
        final Optional<Integer> maxId = dao.getMaxId();

        // Then
        assertThat(maxId, is(Optional.empty()));
    }

    private static IncludePreferenceInSearchResultsPredicate excludePreferenceFromSearchResults(final SearchablePreferenceEntity preference2Exclude) {
        return new IncludePreferenceInSearchResultsPredicate() {

            @Override
            public boolean includePreferenceInSearchResults(final SearchablePreference preference) {
                return !isPreference2Exclude(preference);
            }

            private boolean isPreference2Exclude(final SearchablePreference preference) {
                return preference.getId() == preference2Exclude.getId();
            }
        };
    }

    private static SearchablePreferenceEntity createSomeSearchablePreference(
            final int id,
            final Optional<Integer> parentId,
            final Optional<Integer> predecessorId,
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
