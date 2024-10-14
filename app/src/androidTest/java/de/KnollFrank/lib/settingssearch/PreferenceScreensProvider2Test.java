package de.KnollFrank.lib.settingssearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProvider1Test.createSearchablePreferenceScreenGraphProvider;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.getPreferenceScreenByName;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import org.junit.Test;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreensProvider;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.settingssearch.test.TestActivity;

public class PreferenceScreensProvider2Test {

    @Test
    public void shouldIgnoreNonPreferenceFragments() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(PreferenceScreensProvider2Test::shouldIgnoreNonPreferenceFragments);
        }
    }

    private static void shouldIgnoreNonPreferenceFragments(final FragmentActivity activity) {
        // Given
        final String rootPreferenceFragmentClassName = FragmentConnectedToNonPreferenceFragment.class.getName();
        final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider =
                createSearchablePreferenceScreenGraphProvider(
                        rootPreferenceFragmentClassName,
                        activity);

        // When
        final Set<PreferenceScreenWithHostClass> preferenceScreens =
                PreferenceScreensProvider
                        .getConnectedPreferenceScreens(searchablePreferenceScreenGraphProvider)
                        .connectedSearchablePreferenceScreens();

        // Then
        assertThat(
                preferenceScreens,
                is(ImmutableSet.of(getPreferenceScreenByName(preferenceScreens, "first screen"))));
    }

    // FK-TODO: remove
    public static PreferenceManager getPreferenceManager(final String rootPreferenceFragmentClassName,
                                                         final Fragments fragments) {
        return ((PreferenceFragmentCompat) fragments
                .instantiateAndInitializeFragment(
                        rootPreferenceFragmentClassName,
                        Optional.empty()))
                .getPreferenceManager();
    }

    public static class FragmentConnectedToNonPreferenceFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            configureConnectedPreferencesOfFragment(
                    this,
                    "first screen",
                    ImmutableList.of(NonPreferenceFragment.class));
        }
    }

    public static class NonPreferenceFragment extends Fragment {
    }
}