package de.KnollFrank.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.preferencesearch.PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment;
import static de.KnollFrank.lib.preferencesearch.PreferenceScreensProviderTestHelper.getPreferenceScreenByName;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.fragment.Fragments;
import de.KnollFrank.preferencesearch.test.TestActivity;

public class PreferenceScreensProvider1Test {

    @Test
    public void shouldGetConnectedPreferenceScreens() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final Fragments fragments = FragmentsFactory.createFragments(activity);
                final PreferenceScreensProvider preferenceScreensProvider =
                        new PreferenceScreensProvider(new PreferenceScreenWithHostProvider(fragments));
                final PreferenceFragmentCompat root =
                        (PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(
                                Fragment1ConnectedToFragment2AndFragment4.class.getName());

                // When
                final Set<PreferenceScreenWithHost> preferenceScreens =
                        preferenceScreensProvider
                                .getConnectedPreferenceScreens(root)
                                .connectedPreferenceScreens;

                // Then
                assertThat(
                        preferenceScreens,
                        is(
                                ImmutableSet.of(
                                        getPreferenceScreenByName(preferenceScreens, "first screen"),
                                        getPreferenceScreenByName(preferenceScreens, "second screen"),
                                        getPreferenceScreenByName(preferenceScreens, "third screen"),
                                        getPreferenceScreenByName(preferenceScreens, "fourth screen"))));
            });
        }
    }

    @Test
    public void shouldGetPreferencePath() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final Fragments fragments = FragmentsFactory.createFragments(activity);
                final PreferenceScreensProvider preferenceScreensProvider =
                        new PreferenceScreensProvider(new PreferenceScreenWithHostProvider(fragments));
                final PreferenceFragmentCompat root =
                        (PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(
                                Fragment1ConnectedToFragment2AndFragment4.class.getName());

                // When
                final ConnectedPreferenceScreens connectedPreferenceScreens =
                        preferenceScreensProvider.getConnectedPreferenceScreens(root);

                // Then
                final Set<Preference> preferences = getPreferences(connectedPreferenceScreens.connectedPreferenceScreens);
                final Preference preferencePointingToFragment3 =
                        getPreferencePointingToFragment(
                                preferences,
                                Fragment3.class);
                assertThat(
                        connectedPreferenceScreens.preferencePathByPreference.get(preferencePointingToFragment3),
                        is(
                                new PreferencePath(
                                        ImmutableList.of(
                                                getPreferencePointingToFragment(
                                                        preferences,
                                                        Fragment2ConnectedToFragment3.class),
                                                preferencePointingToFragment3))));
            });
        }
    }

    private static Set<Preference> getPreferences(final Set<PreferenceScreenWithHost> preferenceScreenWithHostSet) {
        return preferenceScreenWithHostSet
                .stream()
                .flatMap(
                        preferenceScreenWithHost ->
                                Preferences
                                        .getAllChildren(preferenceScreenWithHost.preferenceScreen)
                                        .stream())
                .collect(Collectors.toSet());
    }

    private static Preference getPreferencePointingToFragment(final Set<Preference> preferences,
                                                              final Class<? extends Fragment> fragment) {
        return preferences
                .stream()
                .filter(preference -> fragment.getName().equals(preference.getFragment()))
                .findFirst()
                .get();
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

    public static class Fragment3 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            configureConnectedPreferencesOfFragment(
                    this,
                    "third screen",
                    ImmutableList.of(Fragment4.class));
        }
    }

    public static class Fragment4 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            configureConnectedPreferencesOfFragment(
                    this,
                    "fourth screen",
                    ImmutableList.of(Fragment3.class));
        }
    }
}