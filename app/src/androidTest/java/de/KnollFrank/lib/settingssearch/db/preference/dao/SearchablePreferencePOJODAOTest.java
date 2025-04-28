package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferencePOJOEquality.assertActualEqualsExpected;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.common.collect.Iterables;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.provider.IncludeSearchablePreferencePOJOInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.search.IndexRange;
import de.KnollFrank.lib.settingssearch.search.SearchablePreferencePOJOMatch;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

@RunWith(AndroidJUnit4.class)
public class SearchablePreferencePOJODAOTest extends AppDatabaseTest {

    @Test
    public void shouldPersistPreference() {
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreferencePOJO preference =
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
        final Optional<SearchablePreferencePOJO> preferenceFromDb = dao.findPreferenceById(preference.getId());
        assertThat(preferenceFromDb.isPresent(), is(true));

        // And the preference was persisted correctly
        assertActualEqualsExpected(preferenceFromDb.orElseThrow(), preference);
    }

    @Test
    public void shouldUpdateSummaryOfPreference() {
        // Given
        final Optional<String> oldSummary = Optional.of("old summary");
        final Optional<String> newSummary = Optional.of("new summary");

        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreferencePOJO preference =
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
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreferencePOJO predecessor =
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        final SearchablePreferencePOJO preference =
                createSomeSearchablePreference(
                        2,
                        Optional.empty(),
                        Optional.of(predecessor.getId()),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        dao.persist(predecessor, preference);
        final SearchablePreferencePOJO preferenceFromDb = dao.findPreferenceById(preference.getId()).orElseThrow();

        // When
        final SearchablePreferencePOJO predecessorFromDb = preferenceFromDb.getPredecessor().orElseThrow();

        // Then
        assertThat(predecessorFromDb, is(predecessor));
        assertThat(predecessorFromDb.getPredecessor(), is(Optional.empty()));
    }

    @Test
    public void shouldGetChildrenOfPersistedPreference() {
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreferencePOJO parent =
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        final SearchablePreferencePOJO child =
                createSomeSearchablePreference(
                        2,
                        Optional.of(parent.getId()),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        dao.persist(parent, child);
        final SearchablePreferencePOJO parentFromDb = dao.findPreferenceById(parent.getId()).orElseThrow();

        // When
        final List<SearchablePreferencePOJO> childrenFromDb = parentFromDb.getChildren();

        // Then
        assertThat(childrenFromDb, contains(child));

        final SearchablePreferencePOJO childFromDb = Iterables.getOnlyElement(childrenFromDb);
        assertThat(childFromDb.getChildren(), is(empty()));
    }

    @Test
    public void shouldRemovePreference() {
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreferencePOJO preference =
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
    public void shouldFindPreferenceByKeyAndHost() {
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        final SearchablePreferencePOJO preference =
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some title"),
                        Optional.of("some summary"),
                        Optional.of("some searchable info"));
        dao.persist(preference);

        // When
        final Optional<SearchablePreferencePOJO> preferenceFromDb =
                dao.findPreferenceByKeyAndHost(
                        preference.getKey(),
                        preference.getHost());

        // Then
        assertThat(preferenceFromDb.isPresent(), is(true));
        assertActualEqualsExpected(preferenceFromDb.orElseThrow(), preference);
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
        final SearchablePreferencePOJO preference = createSomeSearchablePreference(
                1,
                Optional.empty(),
                Optional.empty(),
                Optional.of("Title, title part"),
                Optional.of("title in summary"),
                Optional.of("searchable info also has a title"));
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        dao.persist(preference);

        // When
        final Set<SearchablePreferencePOJOMatch> preferenceMatches =
                dao.searchWithinTitleSummarySearchableInfo(
                        "title",
                        _preference -> true);

        // Then
        assertThat(
                Iterables.getOnlyElement(preferenceMatches),
                is(
                        new SearchablePreferencePOJOMatch(
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
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        final String needle = "title";
        final SearchablePreferencePOJO someSearchablePreference =
                createSomeSearchablePreference(
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("some " + needle),
                        Optional.empty(),
                        Optional.empty());
        dao.persist(someSearchablePreference);
        final IncludeSearchablePreferencePOJOInSearchResultsPredicate includePreferenceInSearchResultsPredicate =
                excludePreferenceFromSearchResults(someSearchablePreference);

        // When
        final Set<SearchablePreferencePOJOMatch> preferenceMatches =
                dao.searchWithinTitleSummarySearchableInfo(
                        needle,
                        includePreferenceInSearchResultsPredicate);

        // Then
        assertThat(getPreferences(preferenceMatches), not(contains(someSearchablePreference)));
    }

    @Test
    public void shouldGetMaxId() {
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        final int maxId = 4711;
        final SearchablePreferencePOJO preference =
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
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();

        // When
        final Optional<Integer> maxId = dao.getMaxId();

        // Then
        assertThat(maxId, is(Optional.empty()));
    }

    private static IncludeSearchablePreferencePOJOInSearchResultsPredicate excludePreferenceFromSearchResults(final SearchablePreferencePOJO preference2Exclude) {
        return new IncludeSearchablePreferencePOJOInSearchResultsPredicate() {

            @Override
            public boolean includePreferenceInSearchResults(final SearchablePreferencePOJO preference) {
                return !isPreference2Exclude(preference);
            }

            private boolean isPreference2Exclude(final SearchablePreferencePOJO preference) {
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
        final SearchablePreferencePOJO preference = createSomeSearchablePreference(
                1,
                Optional.empty(),
                Optional.empty(),
                Optional.of("some title"),
                Optional.empty(),
                Optional.empty());
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        dao.persist(preference);

        // When
        final Set<SearchablePreferencePOJOMatch> preferenceMatches =
                dao.searchWithinTitleSummarySearchableInfo(
                        "this can't be found",
                        _preference -> true);

        // Then
        assertThat(preferenceMatches, is(empty()));
    }

    @Test
    public void shouldSearchWithinTitleSummarySearchableInfo_emptyDatabase_findNothing() {
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();

        // When
        final Set<SearchablePreferencePOJOMatch> preferenceMatches =
                dao.searchWithinTitleSummarySearchableInfo(
                        "this can't be found",
                        _preference -> true);

        // Then
        assertThat(preferenceMatches, is(empty()));
    }

    private void shouldSearchAndFindWithinTitleSummarySearchableInfo(final SearchablePreferencePOJO preference,
                                                                     final String needle) {
        // Given
        final SearchablePreferencePOJODAO dao = appDatabase.searchablePreferenceDAO();
        dao.persist(preference);

        // When
        final Set<SearchablePreferencePOJOMatch> preferenceMatches =
                dao.searchWithinTitleSummarySearchableInfo(
                        needle,
                        _preference -> true);

        // Then
        assertThat(getPreferences(preferenceMatches), contains(preference));
    }

    private static Set<SearchablePreferencePOJO> getPreferences(final Set<SearchablePreferencePOJOMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(SearchablePreferencePOJOMatch::preference)
                .collect(Collectors.toSet());
    }

    private static SearchablePreferencePOJO createSomeSearchablePreference(
            final int id,
            final Optional<Integer> parentId,
            final Optional<Integer> predecessorId,
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo) {
        return POJOTestFactory.createSearchablePreferencePOJO(
                id,
                title,
                summary,
                searchableInfo,
                Optional.empty(),
                parentId,
                predecessorId);
    }
}
