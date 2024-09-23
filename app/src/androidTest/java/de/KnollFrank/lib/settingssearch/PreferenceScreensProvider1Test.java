package de.KnollFrank.lib.settingssearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.getPreferenceScreenByName;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.MoreCollectors;

import org.junit.Test;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.settingssearch.test.TestActivity;

public class PreferenceScreensProvider1Test {

    @Test
    public void shouldGetConnectedPreferenceScreens() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final Fragments fragments = FragmentsFactory.createFragments(activity);
                final PreferenceScreensProvider preferenceScreensProvider =
                        new PreferenceScreensProvider(
                                new PreferenceScreenWithHostProvider(fragments),
                                preference -> Optional.empty());
                final PreferenceFragmentCompat root =
                        (PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(
                                Fragment1ConnectedToFragment2AndFragment4.class.getName(),
                                Optional.empty());

                // When
                final Set<PreferenceScreenWithHost> preferenceScreens =
                        preferenceScreensProvider
                                .getConnectedPreferenceScreens(root)
                                .getConnectedPreferenceScreens();

                // Then
                assertThat(
                        preferenceScreens,
                        hasItems(
                                getPreferenceScreenByName(preferenceScreens, "first screen"),
                                getPreferenceScreenByName(preferenceScreens, "second screen"),
                                getPreferenceScreenByName(preferenceScreens, "third screen"),
                                getPreferenceScreenByName(preferenceScreens, "fourth screen")));
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
                        new PreferenceScreensProvider(
                                new PreferenceScreenWithHostProvider(fragments),
                                preference -> Optional.empty());
                final PreferenceFragmentCompat root =
                        (PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(
                                Fragment1ConnectedToFragment2AndFragment4.class.getName(),
                                Optional.empty());

                // When
                final ConnectedPreferenceScreens connectedPreferenceScreens =
                        preferenceScreensProvider.getConnectedPreferenceScreens(root);

                // Then
                final Preference preferenceOfFragment2PointingToFragment3 =
                        getPreference(
                                connectedPreferenceScreens,
                                Fragment2ConnectedToFragment3.class,
                                Fragment3.class);
                final Preference preferenceOfFragment1PointingToFragment2 =
                        getPreference(
                                connectedPreferenceScreens,
                                Fragment1ConnectedToFragment2AndFragment4.class,
                                Fragment2ConnectedToFragment3.class);
                assertThat(
                        connectedPreferenceScreens.preferencePathByPreference.get(preferenceOfFragment2PointingToFragment3),
                        is(
                                new PreferencePath(
                                        ImmutableList.of(
                                                preferenceOfFragment1PointingToFragment2,
                                                preferenceOfFragment2PointingToFragment3))));
            });
        }
    }

    private static Preference getPreference(
            final ConnectedPreferenceScreens connectedPreferenceScreens,
            final Class<? extends PreferenceFragmentCompat> hostOfPreference,
            final Class<? extends PreferenceFragmentCompat> fragmentPointedTo) {
        return getPreference(
                connectedPreferenceScreens.getConnectedPreferenceScreens(),
                (_hostOfPreference, preference) ->
                        hostOfPreference.equals(_hostOfPreference.getClass()) &&
                                fragmentPointedTo.getName().equals(preference.getFragment()));
    }

    private static Preference getPreference(
            final Set<PreferenceScreenWithHost> preferenceScreenWithHostSet,
            final BiPredicate<PreferenceFragmentCompat, Preference> predicate) {
        return preferenceScreenWithHostSet
                .stream()
                .flatMap(
                        preferenceScreenWithHost ->
                                Preferences
                                        .getAllChildren(preferenceScreenWithHost.preferenceScreen())
                                        .stream()
                                        .filter(preference -> predicate.test(preferenceScreenWithHost.host(), preference)))
                .collect(MoreCollectors.onlyElement());
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
                    ImmutableList.of());
        }
    }
}