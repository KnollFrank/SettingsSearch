package de.KnollFrank.lib.settingssearch.results;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.getFragments;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceFromPOJOConverterTest.createSomePreferenceFragment;

import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.Matchers;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest;
import de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory;
import de.KnollFrank.lib.settingssearch.results.recyclerview.PreferenceViewHolder;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.search.IndexRange;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch.Type;
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
                                createSomePreferenceFragment(activity).getPreferenceManager(),
                                pojoEntityMap -> null);

                // When
                searchResultsDisplayer.displaySearchResults(
                        List.of(
                                new PreferenceMatch(
                                        POJOTestFactory.createSearchablePreferencePOJO(
                                                Optional.of(title),
                                                Optional.of("some summary"),
                                                Optional.of("searchable info also has a title"),
                                                Optional.empty()),
                                        Type.TITLE,
                                        new IndexRange(0, 5)))
                );

                // Then
                assertThat(
                        Matchers.recyclerViewHasItem(
                                searchResultsFragment.getRecyclerView(),
                                (final PreferenceViewHolder viewHolder) -> ((TextView) viewHolder.findViewById(android.R.id.title)).getText().toString().equals(title)),
                        is(true));
            });
        }
    }

    private static SearchResultsFragment getInitializedSearchResultsFragment(final TestActivity activity) {
        final SearchResultsFragment searchResultsFragment =
                new SearchResultsFragment(
                        Map.of(),
                        preference -> {
                        });
        PreferenceScreenWithHostClass2POJOConverterTest.initializeFragment(
                searchResultsFragment,
                getFragments(searchResultsFragment, activity));
        return searchResultsFragment;
    }
}
