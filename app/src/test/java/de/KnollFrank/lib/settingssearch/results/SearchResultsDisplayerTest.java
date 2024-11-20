package de.KnollFrank.lib.settingssearch.results;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.getFragments;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceFromPOJOConverterTest.createSomePreferenceFragment;

import androidx.test.core.app.ActivityScenario;

import com.codepoetics.ambivalence.Either;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.Matchers;
import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest;
import de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;
import de.KnollFrank.lib.settingssearch.results.recyclerview.ViewHolder;
import de.KnollFrank.lib.settingssearch.search.IndexRange;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch.Type;
import de.KnollFrank.settingssearch.R;
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
                                                Optional.of(Either.ofRight(DrawableAndStringConverter.drawable2String(activity.getResources().getDrawable(R.drawable.smiley, null))))),
                                        Type.TITLE,
                                        new IndexRange(0, 5))),
                        "Title");

                // Then
                assertThat(
                        Matchers.recyclerViewHasItem(
                                searchResultsFragment.getRecyclerView(),
                                (final ViewHolder viewHolder) -> viewHolder.title.getText().equals(title)),
                        is(true));
            });
        }
    }

    private static SearchResultsFragment getInitializedSearchResultsFragment(final TestActivity activity) {
        final SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
        PreferenceScreenWithHostClass2POJOConverterTest.initializeFragment(
                searchResultsFragment,
                getFragments(searchResultsFragment, activity));
        return searchResultsFragment;
    }
}
