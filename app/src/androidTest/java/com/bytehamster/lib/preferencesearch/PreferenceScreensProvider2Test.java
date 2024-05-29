package com.bytehamster.lib.preferencesearch;

import static com.bytehamster.lib.preferencesearch.PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment;
import static com.bytehamster.lib.preferencesearch.PreferenceScreensProviderTestHelper.getPreferenceScreenByName;
import static com.bytehamster.preferencesearch.test.TestActivity.FRAGMENT_CONTAINER_VIEW;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.bytehamster.preferencesearch.test.TestActivity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import org.junit.Test;

import java.util.Set;

public class PreferenceScreensProvider2Test {

    @Test
    public void shouldIgnoreNonPreferenceFragments() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(PreferenceScreensProvider2Test::shouldIgnoreNonPreferenceFragments);
        }
    }

    private static void shouldIgnoreNonPreferenceFragments(final FragmentActivity activity) {
        // Given
        final PreferenceScreensProvider preferenceScreensProvider =
                new PreferenceScreensProvider(
                        new PreferenceFragments(activity, FRAGMENT_CONTAINER_VIEW));
        final PreferenceFragmentCompat root = new FragmentConnectedToNonPreferenceFragment();

        // When
        final Set<PreferenceScreenWithHost> preferenceScreens = preferenceScreensProvider.getPreferenceScreens(root);

        // Then
        assertThat(
                preferenceScreens,
                is(ImmutableSet.of(getPreferenceScreenByName(preferenceScreens, "first screen"))));
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