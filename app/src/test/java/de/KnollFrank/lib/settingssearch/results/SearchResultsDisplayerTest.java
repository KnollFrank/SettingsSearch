package de.KnollFrank.lib.settingssearch.results;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceScreenConverterTest.getInstantiateAndInitializeFragment;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory.createSearchablePreference;
import static de.KnollFrank.lib.settingssearch.test.Matchers.recyclerViewHasItem;
import static de.KnollFrank.lib.settingssearch.test.Matchers.recyclerViewHasItemCount;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceScreenConverterTest;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.results.recyclerview.DefaultPreferencePathDisplayer;
import de.KnollFrank.lib.settingssearch.results.recyclerview.PreferenceViewHolder;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.IndexRange;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchResultsDisplayerTest extends AppDatabaseTest {

    @Test
    public void shouldDisplaySearchResults() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final String title = "Title, title part";
                final SearchResultsFragment searchResultsFragment = getInitializedSearchResultsFragment(activity);
                final SearchResultsDisplayer searchResultsDisplayer =
                        new SearchResultsDisplayer(
                                searchResultsFragment,
                                new DefaultMarkupsFactory(activity),
                                preference -> true,
                                new LexicographicalSearchResultsSorter());
                final SearchablePreference preference =
                        createSearchablePreference(
                                Optional.of(title),
                                Optional.of("some summary"),
                                Optional.of("searchable info also has a title"),
                                Optional.empty());

                // When
                searchResultsDisplayer.displaySearchResults(
                        Set.of(
                                new PreferenceMatch(
                                        preference,
                                        Set.of(new IndexRange(0, 5)),
                                        Set.of(),
                                        Set.of())));

                // Then
                assertThat(
                        searchResultsFragment.getRecyclerView(),
                        recyclerViewHasItem(withTitle(title)));
            });
        }
    }

    private static BoundedMatcher<PreferenceViewHolder, PreferenceViewHolder> withTitle(final String title) {
        return new BoundedMatcher<>(PreferenceViewHolder.class) {

            @Override
            public void describeTo(final Description description) {
                description.appendText("has title " + title);
            }

            @Override
            protected boolean matchesSafely(final PreferenceViewHolder viewHolder) {
                return getTitle(viewHolder).equals(title);
            }

            private static String getTitle(final PreferenceViewHolder viewHolder) {
                return viewHolder
                        .<TextView>findViewById(android.R.id.title)
                        .orElseThrow()
                        .getText()
                        .toString();
            }
        };
    }

    @Test
    public void shouldDisplayFilteredSearchResults() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final SearchResultsFilter searchResultsFilterRemovingAllSearchResults = preference -> false;

                final SearchResultsFragment searchResultsFragment = getInitializedSearchResultsFragment(activity);
                final SearchResultsDisplayer searchResultsDisplayer =
                        new SearchResultsDisplayer(
                                searchResultsFragment,
                                new DefaultMarkupsFactory(activity),
                                searchResultsFilterRemovingAllSearchResults,
                                new LexicographicalSearchResultsSorter());

                // When
                searchResultsDisplayer.displaySearchResults(
                        Set.of(
                                new PreferenceMatch(
                                        createSearchablePreference(
                                                Optional.of("Title, title part"),
                                                Optional.of("some summary"),
                                                Optional.of("searchable info also has a title"),
                                                Optional.empty()),
                                        Set.of(new IndexRange(0, 5)),
                                        Set.of(),
                                        Set.of()))
                                                           );

                // Then
                assertThat(
                        searchResultsFragment.getRecyclerView(),
                        recyclerViewHasItemCount(equalTo(0)));
            });
        }
    }

    private SearchResultsFragment getInitializedSearchResultsFragment(final FragmentActivity activity) {
        final SearchResultsFragment searchResultsFragment =
                new SearchResultsFragment(
                        preferencePathPointer -> {
                        },
                        preferencePath -> true,
                        new DefaultPreferencePathDisplayer(),
                        new SearchResultsFragmentUI() {

                            @Override
                            public int getRootViewId() {
                                return de.KnollFrank.lib.settingssearch.R.layout.searchresults_fragment;
                            }

                            @Override
                            public RecyclerView getSearchResultsView(View rootView) {
                                return rootView.requireViewById(de.KnollFrank.lib.settingssearch.R.id.searchResults);
                            }
                        },
                        appDatabase.searchablePreferenceEntityDAO());
        PreferenceScreen2SearchablePreferenceScreenConverterTest.initializeFragment(
                searchResultsFragment,
                getInstantiateAndInitializeFragment(searchResultsFragment, activity));
        return searchResultsFragment;
    }
}
