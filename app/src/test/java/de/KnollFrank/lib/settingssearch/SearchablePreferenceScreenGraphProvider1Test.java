package de.KnollFrank.lib.settingssearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.getPreferenceScreenByName;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

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

import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
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
                final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider =
                        createSearchablePreferenceScreenGraphProvider(
                                Fragment1ConnectedToFragment2AndFragment4.class,
                                activity);

                // When
                final Set<PreferenceScreenWithHostClass> preferenceScreens =
                        searchablePreferenceScreenGraphProvider
                                .getSearchablePreferenceScreenGraph()
                                .vertexSet();

                // Then
                MatcherAssert.assertThat(
                        preferenceScreens,
                        Matchers.hasItems(
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
                final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider =
                        createSearchablePreferenceScreenGraphProvider(
                                Fragment1ConnectedToFragment2AndFragment4.class,
                                activity);

                // When
                final Graph<PreferenceScreenWithHostClass, SearchablePreferenceEdge> pojoGraph =
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
            final Class<? extends Fragment> rootPreferenceFragmentClass,
            final FragmentActivity activity) {
        final InstantiateAndInitializeFragment instantiateAndInitializeFragment = InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment(activity);
        return new SearchablePreferenceScreenGraphProvider(
                rootPreferenceFragmentClass,
                new PreferenceScreenWithHostProvider(
                        instantiateAndInitializeFragment,
                        PreferenceFragmentCompat::getPreferenceScreen,
                        fragment -> Optional.empty()),
                (preference, hostOfPreference) -> Optional.empty(),
                classNameOfActivity -> Optional.empty(),
                preferenceScreenGraph -> {
                },
                preferenceScreenWithHost -> {
                },
                new Preference2SearchablePreferenceConverter(
                        (preference, hostOfPreference) -> Optional.empty(),
                        new SearchableInfoAndDialogInfoProvider(
                                preference -> Optional.empty(),
                                (preference, hostOfPreference) -> Optional.empty()),
                        new IdGenerator()),
                activity);
    }

    private static SearchablePreference getPreference(
            final Class<? extends PreferenceFragmentCompat> hostOfPreference,
            final Class<? extends PreferenceFragmentCompat> fragmentPointedTo,
            final Set<PreferenceScreenWithHostClass> preferenceScreenWithHostSet) {
        return getPreference(
                preferenceScreenWithHostSet,
                (_hostOfPreference, preference) ->
                        hostOfPreference.equals(_hostOfPreference) && preference.getFragment().equals(Optional.of(fragmentPointedTo.getName())));
    }

    private static SearchablePreference getPreference(
            final Set<PreferenceScreenWithHostClass> preferenceScreenWithHostSet,
            final BiPredicate<Class<? extends PreferenceFragmentCompat>, SearchablePreference> predicate) {
        return preferenceScreenWithHostSet
                .stream()
                .flatMap(
                        preferenceScreenWithHost ->
                                SearchablePreferences
                                        .getPreferencesRecursively(preferenceScreenWithHost.preferenceScreen().preferences())
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
                    List.of(
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
                    List.of(Fragment3.class));
        }
    }

    public static class Fragment3 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment(
                    this,
                    "third screen",
                    List.of(Fragment4.class));
        }
    }

    public static class Fragment4 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment(
                    this,
                    "fourth screen",
                    List.of());
        }
    }
}