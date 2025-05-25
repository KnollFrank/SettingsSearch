package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory.createSearchablePreference;

import com.google.common.collect.Iterables;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.search.IndexRange;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.test.SearchablePreferenceEquality;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceDAOTest extends AppDatabaseTest {

    @Test
    public void shouldPersistAndFindPreferenceById() {
        // Given
        final SearchablePreferenceDAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreference preference =
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
        final Optional<SearchablePreference> preferenceFromDb = dao.findPreferenceById(preference.getId());
        assertThat(preferenceFromDb.isPresent(), is(true));

        // And the preference was persisted correctly
        SearchablePreferenceEquality.assertActualEqualsExpected(preferenceFromDb.orElseThrow(), preference);
    }

    @Test
    public void shouldUpdateSummaryOfPreference() {
        // Given
        final Optional<String> oldSummary = Optional.of("old summary");
        final Optional<String> newSummary = Optional.of("new summary");

        final SearchablePreferenceDAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreference preference =
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
        final SearchablePreferenceDAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreference predecessor =
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        final SearchablePreference preference =
                createSomeSearchablePreference(
                        2,
                        Optional.empty(),
                        Optional.of(predecessor.getId()),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        dao.persist(predecessor, preference);
        final SearchablePreference preferenceFromDb = dao.findPreferenceById(preference.getId()).orElseThrow();

        // When
        final SearchablePreference predecessorFromDb = preferenceFromDb.getPredecessor().orElseThrow();

        // Then
        assertThat(predecessorFromDb, is(predecessor));
        assertThat(predecessorFromDb.getPredecessor(), is(Optional.empty()));
    }

    @Test
    public void shouldGetChildrenOfPersistedPreference() {
        // Given
        final SearchablePreferenceDAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreference parent =
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        final SearchablePreference child =
                createSomeSearchablePreference(
                        2,
                        Optional.of(parent.getId()),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        dao.persist(parent, child);
        final SearchablePreference parentFromDb = dao.findPreferenceById(parent.getId()).orElseThrow();

        // When
        final Set<SearchablePreference> childrenFromDb = parentFromDb.getChildren();

        // Then
        assertThat(childrenFromDb, contains(child));

        final SearchablePreference childFromDb = Iterables.getOnlyElement(childrenFromDb);
        assertThat(childFromDb.getChildren(), is(empty()));
    }

    @Test
    public void shouldRemovePreference() {
        // Given
        final SearchablePreferenceDAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreference preference =
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
    public void shouldSearchWithinTitleSummarySearchableInfo_title() {
        final String needle = "title";
        shouldSearchAndFindWithinTitleSummarySearchableInfo(
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some " + needle),
                        Optional.empty(),
                        Optional.empty()),
                needle);
    }

    @Test
    public void shouldSearchWithinTitleSummarySearchableInfo() {
        final SearchablePreference preference = createSomeSearchablePreference(
                1,
                Optional.empty(),
                Optional.empty(),
                Optional.of("Title, title part"),
                Optional.of("title in summary"),
                Optional.of("searchable info also has a title"));
        // Given
        final SearchablePreferenceDAO dao = appDatabase.searchablePreferenceDAO();
        dao.persist(preference);

        // When
        final Set<PreferenceMatch> preferenceMatches =
                dao.searchWithinTitleSummarySearchableInfo(
                        "title",
                        _preference -> true);

        // Then
        assertThat(
                Iterables.getOnlyElement(preferenceMatches),
                is(
                        new PreferenceMatch(
                                preference,
                                Set.of(
                                        new IndexRange(0, 5),
                                        new IndexRange(7, 12)),
                                Set.of(new IndexRange(0, 5)),
                                Set.of(new IndexRange(27, 32)))));
    }

    @Test
    public void shouldSearchWithinTitleSummarySearchableInfo_excludePreferenceFromSearchResults() {
        // Given
        final SearchablePreferenceDAO dao = appDatabase.searchablePreferenceDAO();
        final String needle = "title";
        final SearchablePreference someSearchablePreference =
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some " + needle),
                        Optional.empty(),
                        Optional.empty());
        dao.persist(someSearchablePreference);
        final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate =
                excludePreferenceFromSearchResults(someSearchablePreference);

        // When
        final Set<PreferenceMatch> preferenceMatches =
                dao.searchWithinTitleSummarySearchableInfo(
                        needle,
                        includePreferenceInSearchResultsPredicate);

        // Then
        assertThat(getPreferences(preferenceMatches), not(contains(someSearchablePreference)));
    }

    @Test
    public void shouldGetMaxId() {
        // Given
        final SearchablePreferenceDAO dao = appDatabase.searchablePreferenceDAO();
        final int maxId = 4711;
        final SearchablePreference preference =
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
        final SearchablePreferenceDAO dao = appDatabase.searchablePreferenceDAO();

        // When
        final Optional<Integer> maxId = dao.getMaxId();

        // Then
        assertThat(maxId, is(Optional.empty()));
    }

    private static IncludePreferenceInSearchResultsPredicate excludePreferenceFromSearchResults(final SearchablePreference preference2Exclude) {
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

    @Test
    public void shouldSearchWithinTitleSummarySearchableInfo_summary() {
        final String needle = "summary";
        shouldSearchAndFindWithinTitleSummarySearchableInfo(
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some " + needle),
                        Optional.empty()),
                needle);
    }

    @Test
    public void shouldSearchWithinTitleSummarySearchableInfo_searchableInfo() {
        final String needle = "searchableInfo";
        shouldSearchAndFindWithinTitleSummarySearchableInfo(
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some " + needle)),
                needle);
    }

    @Test
    public void shouldSearchWithinTitleSummarySearchableInfo_nonMatchingNeedle_findNothing() {
        final SearchablePreference preference = createSomeSearchablePreference(
                1,
                Optional.empty(),
                Optional.empty(),
                Optional.of("some title"),
                Optional.empty(),
                Optional.empty());
        // Given
        final SearchablePreferenceDAO dao = appDatabase.searchablePreferenceDAO();
        dao.persist(preference);

        // When
        final Set<PreferenceMatch> preferenceMatches =
                dao.searchWithinTitleSummarySearchableInfo(
                        "this can't be found",
                        _preference -> true);

        // Then
        assertThat(preferenceMatches, is(empty()));
    }

    @Test
    public void shouldSearchWithinTitleSummarySearchableInfo_emptyDatabase_findNothing() {
        // Given
        final SearchablePreferenceDAO dao = appDatabase.searchablePreferenceDAO();

        // When
        final Set<PreferenceMatch> preferenceMatches =
                dao.searchWithinTitleSummarySearchableInfo(
                        "this can't be found",
                        _preference -> true);

        // Then
        assertThat(preferenceMatches, is(empty()));
    }

    private void shouldSearchAndFindWithinTitleSummarySearchableInfo(final SearchablePreference preference,
                                                                     final String needle) {
        // Given
        final SearchablePreferenceDAO dao = appDatabase.searchablePreferenceDAO();
        dao.persist(preference);

        // When
        final Set<PreferenceMatch> preferenceMatches =
                dao.searchWithinTitleSummarySearchableInfo(
                        needle,
                        _preference -> true);

        // Then
        assertThat(getPreferences(preferenceMatches), contains(preference));
    }

    private static Set<SearchablePreference> getPreferences(final Set<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .collect(Collectors.toSet());
    }

    private static SearchablePreference createSomeSearchablePreference(
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
