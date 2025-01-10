package de.KnollFrank.lib.settingssearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.MoreCollectors;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

import de.KnollFrank.lib.settingssearch.common.PreferencePOJOs;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferencePOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.graph.HostClassFromPojoNodesRemover;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenGraphProvider1Test {

    @Test
    public void shouldGetSearchablePreferenceScreenGraph() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final String rootPreferenceFragmentClassName = Fragment1ConnectedToFragment2AndFragment4.class.getName();
                final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider =
                        createSearchablePreferenceScreenGraphProvider(
                                rootPreferenceFragmentClassName,
                                activity);

                // When
                final Set<PreferenceScreenWithHostClassPOJO> preferenceScreens =
                        searchablePreferenceScreenGraphProvider
                                .getSearchablePreferenceScreenGraph()
                                .vertexSet();

                // Then
                MatcherAssert.assertThat(
                        preferenceScreens,
                        Matchers.hasItems(
                                PreferenceScreensProviderTestHelper.getPreferenceScreenByName(preferenceScreens, "first screen"),
                                PreferenceScreensProviderTestHelper.getPreferenceScreenByName(preferenceScreens, "second screen"),
                                PreferenceScreensProviderTestHelper.getPreferenceScreenByName(preferenceScreens, "third screen"),
                                PreferenceScreensProviderTestHelper.getPreferenceScreenByName(preferenceScreens, "fourth screen")));
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
                final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph =
                        searchablePreferenceScreenGraphProvider.getSearchablePreferenceScreenGraph();
                final Map<SearchablePreference, PreferencePath> preferencePathByPreference =
                        PreferencePathByPojoPreferenceProvider.getPreferencePathByPojoPreference(
                                HostClassFromPojoNodesRemover.removeHostClassFromNodes(pojoGraph));

                // Then
                final SearchablePreference preferenceOfFragment2PointingToFragment3 =
                        getPreference(
                                Fragment2ConnectedToFragment3.class,
                                Fragment3.class,
                                pojoGraph.vertexSet());
                final SearchablePreference preferenceOfFragment1PointingToFragment2 =
                        getPreference(
                                Fragment1ConnectedToFragment2AndFragment4.class,
                                Fragment2ConnectedToFragment3.class,
                                pojoGraph.vertexSet());
                assertThat(
                        preferencePathByPreference.get(preferenceOfFragment2PointingToFragment3),
                        is(
                                new PreferencePath(
                                        List.of(
                                                preferenceOfFragment1PointingToFragment2,
                                                preferenceOfFragment2PointingToFragment3))));

            });
        }
    }

    public static SearchablePreferenceScreenGraphProvider createSearchablePreferenceScreenGraphProvider(
            final String rootPreferenceFragmentClassName,
            final FragmentActivity activity) {
        return new SearchablePreferenceScreenGraphProvider(
                rootPreferenceFragmentClassName,
                new PreferenceScreenWithHostProvider(
                        FragmentsFactory.createFragments(activity),
                        PreferenceFragmentCompat::getPreferenceScreen),
                (preference, hostOfPreference) -> Optional.empty(),
                preferenceScreenGraph -> {
                },
                preferenceScreenWithHost -> {
                },
                new Preference2SearchablePreferencePOJOConverter(
                        (preference, hostOfPreference) -> Optional.empty(),
                        new SearchableInfoAndDialogInfoProvider(
                                preference -> Optional.empty(),
                                (preference, hostOfPreference) -> Optional.empty()),
                        new IdGenerator()));
    }

    private static SearchablePreference getPreference(
            final Class<? extends PreferenceFragmentCompat> hostOfPreference,
            final Class<? extends PreferenceFragmentCompat> fragmentPointedTo,
            final Set<PreferenceScreenWithHostClassPOJO> preferenceScreenWithHostSet) {
        return getPreference(
                preferenceScreenWithHostSet,
                (_hostOfPreference, preference) ->
                        hostOfPreference.equals(_hostOfPreference) && preference.getFragment().equals(Optional.of(fragmentPointedTo.getName())));
    }

    private static SearchablePreference getPreference(
            final Set<PreferenceScreenWithHostClassPOJO> preferenceScreenWithHostSet,
            final BiPredicate<Class<? extends PreferenceFragmentCompat>, SearchablePreference> predicate) {
        return preferenceScreenWithHostSet
                .stream()
                .flatMap(
                        preferenceScreenWithHost ->
                                PreferencePOJOs
                                        .getPreferencesRecursively(preferenceScreenWithHost.preferenceScreen().children())
                                        .stream()
                                        .filter(preference -> predicate.test(preferenceScreenWithHost.host(), preference)))
                .collect(MoreCollectors.onlyElement());
    }

    public static class Fragment1ConnectedToFragment2AndFragment4 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment(
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
            PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment(
                    this,
                    "second screen",
                    ImmutableList.of(Fragment3.class));
        }
    }

    public static class Fragment3 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment(
                    this,
                    "third screen",
                    ImmutableList.of(Fragment4.class));
        }
    }

    public static class Fragment4 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment(
                    this,
                    "fourth screen",
                    ImmutableList.of());
        }
    }
}