package de.KnollFrank.lib.preferencesearch;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import java.util.List;

import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.preferencesearch.test.TestActivity;

class PreferenceScreensMergerTestImplementation {

    public static void shouldDestructivelyMergeScreens(
            final List<Class<? extends PreferenceFragmentCompat>> screens2Merge,
            final Class<? extends PreferenceFragmentCompat> expectedMergedScreen) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(
                    fragmentActivity ->
                            shouldDestructivelyMergeScreens(
                                    fragmentActivity,
                                    screens2Merge,
                                    expectedMergedScreen));
        }
    }

    private static void shouldDestructivelyMergeScreens(
            final FragmentActivity fragmentActivity,
            final List<Class<? extends PreferenceFragmentCompat>> screens2Merge,
            final Class<? extends PreferenceFragmentCompat> expectedMergedScreen) {
        // Given
        final PreferenceScreensMerger preferenceScreensMerger = new PreferenceScreensMerger(fragmentActivity);
        final PreferenceFragments preferenceFragments =
                new PreferenceFragments(
                        fragmentActivity,
                        fragmentActivity.getSupportFragmentManager(),
                        TestActivity.FRAGMENT_CONTAINER_VIEW);
        final List<PreferenceScreen> screens =
                screens2Merge
                        .stream()
                        .map(preferenceFragment -> getPreferenceScreen(preferenceFragment, preferenceFragments))
                        .toList();

        // When
        final PreferenceScreen mergedPreferenceScreen =
                preferenceScreensMerger.destructivelyMergeScreens(screens);

        // Then
        assertThatPreferenceScreensAreEqual(
                mergedPreferenceScreen,
                getPreferenceScreen(expectedMergedScreen, preferenceFragments));
    }

    private static PreferenceScreen getPreferenceScreen(final Class<? extends PreferenceFragmentCompat> preferenceFragment,
                                                        final PreferenceFragments preferenceFragments) {
        return preferenceFragments
                .getPreferenceScreenOfFragment(preferenceFragment.getName())
                .get()
                .preferenceScreen;
    }

    private static void assertThatPreferenceScreensAreEqual(final PreferenceScreen actual,
                                                            final PreferenceScreen expected) {
        assertThat(getAllPreferences(actual), is(getAllPreferences(expected)));
    }

    private static List<String> getAllPreferences(final PreferenceScreen preferenceScreen) {
        return Preferences
                .getAllPreferences(preferenceScreen)
                .stream()
                .map(Preference::toString)
                .toList();
    }
}
