package de.KnollFrank.lib.settingssearch.results;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.getFragments;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest;
import de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory;
import de.KnollFrank.lib.settingssearch.results.recyclerview.PreferenceViewHolder;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.IndexRange;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;
import de.KnollFrank.lib.settingssearch.test.Matchers;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchResultsDisplayerTest {

    @Test
    public void shouldDisplaySearchResults() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final String title = "Title, title part";
                final SearchResultsFragment searchResultsFragment = getInitializedSearchResultsFragment(activity);
                final SearchResultsDisplayer searchResultsDisplayer =
                        SearchResultsDisplayerFactory.createSearchResultsDisplayer(
                                searchResultsFragment,
                                activity,
                                new DefaultSearchResultsSorter());

                // When
                searchResultsDisplayer.displaySearchResults(
                        Set.of(
                                new PreferenceMatch(
                                        POJOTestFactory.createSearchablePreferencePOJO(
                                                Optional.of(title),
                                                Optional.of("some summary"),
                                                Optional.of("searchable info also has a title"),
                                                Optional.empty()),
                                        Set.of(new IndexRange(0, 5)),
                                        Set.of(),
                                        Set.of()))
                );

                // Then
                assertThat(
                        Matchers.recyclerViewHasItem(
                                searchResultsFragment.getRecyclerView(),
                                (final PreferenceViewHolder viewHolder) -> getTitle(viewHolder).equals(title)),
                        is(true));
            });
        }
    }

    private static String getTitle(final PreferenceViewHolder viewHolder) {
        return viewHolder
                .<TextView>findViewById(android.R.id.title)
                .orElseThrow()
                .getText()
                .toString();
    }

    private static SearchResultsFragment getInitializedSearchResultsFragment(final TestActivity activity) {
        final SearchResultsFragment searchResultsFragment =
                new SearchResultsFragment(
                        preference -> {
                        },
                        preferencePath -> true,
                        new SearchResultsFragmentUI() {

                            @Override
                            public int getRootViewId() {
                                return de.KnollFrank.lib.settingssearch.R.layout.searchresults_fragment;
                            }

                            @Override
                            public RecyclerView getSearchResultsView(View rootView) {
                                return rootView.requireViewById(de.KnollFrank.lib.settingssearch.R.id.searchResults);
                            }
                        });
        PreferenceScreenWithHostClass2POJOConverterTest.initializeFragment(
                searchResultsFragment,
                getFragments(searchResultsFragment, activity));
        return searchResultsFragment;
    }
}
