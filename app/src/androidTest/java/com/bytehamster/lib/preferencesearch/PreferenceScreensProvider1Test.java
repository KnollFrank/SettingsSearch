package com.bytehamster.lib.preferencesearch;

import static com.bytehamster.lib.preferencesearch.PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment;
import static com.bytehamster.lib.preferencesearch.PreferenceScreensProviderTestHelper.getPreferenceScreenByName;
import static com.bytehamster.preferencesearch.test.TestActivity.FRAGMENT_CONTAINER_VIEW;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.bytehamster.preferencesearch.test.TestActivity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import org.junit.Test;

import java.util.Set;

public class PreferenceScreensProvider1Test {

    @Test
    public void shouldGetPreferenceScreens() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(PreferenceScreensProvider1Test::shouldGetPreferenceScreens);
        }
    }

    private static void shouldGetPreferenceScreens(final FragmentActivity activity) {
        // Given
        final PreferenceScreensProvider preferenceScreensProvider =
                new PreferenceScreensProvider(
                        new PreferenceFragments(activity, FRAGMENT_CONTAINER_VIEW));
        final PreferenceFragmentCompat root = new Fragment1ConnectedToFragment2AndFragment4();

        // When
        final Set<PreferenceScreenWithHost> preferenceScreens = preferenceScreensProvider.getPreferenceScreens(root);

        // Then
        assertThat(
                preferenceScreens,
                is(
                        ImmutableSet.of(
                                getPreferenceScreenByName(preferenceScreens, "first screen"),
                                getPreferenceScreenByName(preferenceScreens, "second screen"),
                                getPreferenceScreenByName(preferenceScreens, "third screen"),
                                getPreferenceScreenByName(preferenceScreens, "fourth screen"))));
    }

    public static class Fragment1ConnectedToFragment2AndFragment4 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            configureConnectedPreferencesOfFragment(
                    this,
                    "first screen",
                    ImmutableList.of(
                            Fragment2ConnectedToFragment3.class,
                            Fragment4.class));
        }
    }

    public static class Fragment2ConnectedToFragment3 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            configureConnectedPreferencesOfFragment(
                    this,
                    "second screen",
                    ImmutableList.of(Fragment3.class));
        }
    }

    public static class Fragment3 extends BaseSearchPreferenceFragment {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            configureConnectedPreferencesOfFragment(
                    this,
                    "third screen",
                    ImmutableList.of());
        }
    }

    public static class Fragment4 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            configureConnectedPreferencesOfFragment(
                    this,
                    "fourth screen",
                    ImmutableList.of());
        }
    }
}