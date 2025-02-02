package de.KnollFrank.lib.settingssearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.getPreferenceScreenByName;
import static de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenGraphProvider1Test.createSearchablePreferenceScreenGraphProvider;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenGraphProvider2Test {

    @Test
    public void shouldIgnoreNonPreferenceFragments() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(SearchablePreferenceScreenGraphProvider2Test::shouldIgnoreNonPreferenceFragments);
        }
    }

    private static void shouldIgnoreNonPreferenceFragments(final FragmentActivity activity) {
        // Given
        final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider =
                createSearchablePreferenceScreenGraphProvider(
                        FragmentConnectedToNonPreferenceFragment.class,
                        activity);

        // When
        final Set<PreferenceScreenWithHostClass> preferenceScreens =
                searchablePreferenceScreenGraphProvider
                        .getSearchablePreferenceScreenGraph()
                        .vertexSet();

        // Then
        assertThat(
                preferenceScreens,
                is(Set.of(getPreferenceScreenByName(preferenceScreens, "first screen"))));
    }

    public static class FragmentConnectedToNonPreferenceFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            configureConnectedPreferencesOfFragment(
                    this,
                    "first screen",
                    List.of(NonPreferenceFragment.class));
        }
    }

    public static class NonPreferenceFragment extends Fragment {
    }
}