package de.KnollFrank.lib.settingssearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.getPreferenceScreenByName;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
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
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.graph.DefaultSearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.settingssearch.test.TestActivity;

public class PreferenceScreensProvider1Test {

    @Test
    public void shouldGetConnectedPreferenceScreens() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final String rootPreferenceFragmentClassName = Fragment1ConnectedToFragment2AndFragment4.class.getName();
                final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider =
                        createSearchablePreferenceScreenGraphProvider(
                                rootPreferenceFragmentClassName,
                                activity);

                // When
                final Set<PreferenceScreenWithHostClass> preferenceScreens =
                        ConnectedSearchablePreferenceScreens
                                .fromSearchablePreferenceScreenGraph(searchablePreferenceScreenGraphProvider.getSearchablePreferenceScreenGraph())
                                .connectedSearchablePreferenceScreens();

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
                final String rootPreferenceFragmentClassName = Fragment1ConnectedToFragment2AndFragment4.class.getName();
                final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider =
                        createSearchablePreferenceScreenGraphProvider(
                                rootPreferenceFragmentClassName,
                                activity);

                // When
                final ConnectedSearchablePreferenceScreens connectedSearchablePreferenceScreens =
                        ConnectedSearchablePreferenceScreens.fromSearchablePreferenceScreenGraph(searchablePreferenceScreenGraphProvider.getSearchablePreferenceScreenGraph());

                // Then
                final Preference preferenceOfFragment2PointingToFragment3 =
                        getPreference(
                                connectedSearchablePreferenceScreens,
                                Fragment2ConnectedToFragment3.class,
                                Fragment3.class);
                final Preference preferenceOfFragment1PointingToFragment2 =
                        getPreference(
                                connectedSearchablePreferenceScreens,
                                Fragment1ConnectedToFragment2AndFragment4.class,
                                Fragment2ConnectedToFragment3.class);
                assertThat(
                        connectedSearchablePreferenceScreens.preferencePathByPreference().get(preferenceOfFragment2PointingToFragment3),
                        is(
                                new PreferencePath(
                                        ImmutableList.of(
                                                preferenceOfFragment1PointingToFragment2,
                                                preferenceOfFragment2PointingToFragment3))));
            });
        }
    }

    public static SearchablePreferenceScreenGraphProvider createSearchablePreferenceScreenGraphProvider(
            final String rootPreferenceFragmentClassName,
            final FragmentActivity activity) {
        return new DefaultSearchablePreferenceScreenGraphProvider(
                rootPreferenceFragmentClassName,
                new PreferenceScreenWithHostProvider(
                        FragmentsFactory.createFragments(activity),
                        PreferenceFragmentCompat::getPreferenceScreen),
                (preference, hostOfPreference) -> Optional.empty(),
                preferenceScreenGraph -> {
                },
                new SearchableInfoAndDialogInfoProvider(
                        preference -> Optional.empty(),
                        (preference, hostOfPreference) -> Optional.empty()));
    }

    private static Preference getPreference(
            final ConnectedSearchablePreferenceScreens connectedSearchablePreferenceScreens,
            final Class<? extends PreferenceFragmentCompat> hostOfPreference,
            final Class<? extends PreferenceFragmentCompat> fragmentPointedTo) {
        return getPreference(
                connectedSearchablePreferenceScreens.connectedSearchablePreferenceScreens(),
                (_hostOfPreference, preference) ->
                        hostOfPreference.equals(_hostOfPreference) &&
                                fragmentPointedTo.getName().equals(preference.getFragment()));
    }

    private static Preference getPreference(
            final Set<PreferenceScreenWithHostClass> preferenceScreenWithHostSet,
            final BiPredicate<Class<? extends PreferenceFragmentCompat>, Preference> predicate) {
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