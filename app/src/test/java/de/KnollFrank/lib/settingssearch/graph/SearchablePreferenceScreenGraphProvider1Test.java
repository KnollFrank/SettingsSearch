package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.getPreferenceScreenByTitle;
import static de.KnollFrank.lib.settingssearch.search.PreferenceSearcherTest.emptyComputePreferencesListener;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.MoreCollectors;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.InstantiateAndInitializeFragmentFactory;
import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper;
import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.DefaultPreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceToSearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenWithinGraph;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings({"UnstableApiUsage"})
public class SearchablePreferenceScreenGraphProvider1Test extends PreferencesRoomDatabaseTest {

    @Test
    public void shouldGetSearchablePreferenceScreenGraph() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final var result =
                        createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider(
                                Fragment1ConnectedToFragment2AndFragment4.class,
                                activity);

                // When
                final Set<SearchablePreferenceScreen> preferenceScreens =
                        result
                                .searchablePreferenceScreenGraphProvider()
                                .getSearchablePreferenceScreenGraph(result.preferenceScreenWithHost())
                                .graph()
                                .nodes();

                // Then
                MatcherAssert.assertThat(
                        preferenceScreens,
                        Matchers.hasItems(
                                getPreferenceScreenByTitle(preferenceScreens, "first screen"),
                                getPreferenceScreenByTitle(preferenceScreens, "second screen"),
                                getPreferenceScreenByTitle(preferenceScreens, "third screen"),
                                getPreferenceScreenByTitle(preferenceScreens, "fourth screen")));
            });
        }
    }

    @Test
    public void test_getSearchablePreferenceScreenGraph_addEdgePredicate() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final var result =
                        createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider(
                                Fragment1ConnectedToFragment2AndFragment4.class,
                                new AddEdgeToGraphPredicate() {

                                    @Override
                                    public boolean shallAddEdgeToGraph(final PreferenceEdge edge,
                                                                       final PreferenceScreenWithHost sourceNodeOfEdge,
                                                                       final PreferenceScreenWithHost targetNodeOfEdge) {
                                        return !shallNotAddEdgeToGraph(sourceNodeOfEdge, targetNodeOfEdge);
                                    }

                                    private boolean shallNotAddEdgeToGraph(final PreferenceScreenWithHost sourceNodeOfEdge,
                                                                           final PreferenceScreenWithHost targetNodeOfEdge) {
                                        return sourceNodeOfEdge.host() instanceof Fragment1ConnectedToFragment2AndFragment4 &&
                                                targetNodeOfEdge.host() instanceof Fragment2ConnectedToFragment3ConnectedToFragment4;
                                    }
                                },
                                activity);

                // When
                final Set<SearchablePreferenceScreen> preferenceScreens =
                        result
                                .searchablePreferenceScreenGraphProvider()
                                .getSearchablePreferenceScreenGraph(result.preferenceScreenWithHost())
                                .graph()
                                .nodes();

                // Then
                MatcherAssert.assertThat(
                        preferenceScreens,
                        Matchers.containsInAnyOrder(
                                getPreferenceScreenByTitle(preferenceScreens, "first screen"),
                                getPreferenceScreenByTitle(preferenceScreens, "fourth screen")));
            });
        }
    }

    @Test
    public void shouldGetPreferencePath() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final var result =
                        createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider(
                                Fragment1ConnectedToFragment2AndFragment4.class,
                                activity);

                // When
                final Tree<SearchablePreferenceScreen, SearchablePreference> pojoGraph =
                        result
                                .searchablePreferenceScreenGraphProvider()
                                .getSearchablePreferenceScreenGraph(
                                        result.preferenceScreenWithHost());

                // Then
                final Set<SearchablePreferenceScreenWithinGraph> searchablePreferenceScreens =
                        pojoGraph
                                .graph()
                                .nodes()
                                .stream()
                                .map(searchablePreferenceScreen ->
                                             new SearchablePreferenceScreenWithinGraph(
                                                     searchablePreferenceScreen,
                                                     pojoGraph))
                                .collect(Collectors.toSet());
                final SearchablePreferenceOfHostWithinGraph preferenceOfFragment2PointingToFragment3 =
                        getPreference(
                                Fragment2ConnectedToFragment3ConnectedToFragment4.class,
                                Fragment3ConnectedToFragment4.class,
                                searchablePreferenceScreens);
                final SearchablePreferenceOfHostWithinGraph preferenceOfFragment1PointingToFragment2 =
                        getPreference(
                                Fragment1ConnectedToFragment2AndFragment4.class,
                                Fragment2ConnectedToFragment3ConnectedToFragment4.class,
                                searchablePreferenceScreens);
                assertThat(
                        preferenceOfFragment2PointingToFragment3.getPreferencePath(),
                        is(
                                new PreferencePath(
                                        List.of(
                                                preferenceOfFragment1PointingToFragment2,
                                                preferenceOfFragment2PointingToFragment3))));
            });
        }
    }

    private static SearchablePreferenceScreenGraphProvider createSearchablePreferenceScreenGraphProvider(
            final FragmentActivity activity,
            final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
            final AddEdgeToGraphPredicate addEdgeToGraphPredicate) {
        return new SearchablePreferenceScreenGraphProvider(
                preferenceScreenGraph -> {
                },
                emptyComputePreferencesListener(),
                new GraphToPojoGraphTransformer(
                        new PreferenceScreenToSearchablePreferenceScreenConverter(
                                new PreferenceToSearchablePreferenceConverter(
                                        (preference, hostOfPreference) -> Optional.empty(),
                                        new SearchableInfoAndDialogInfoProvider(
                                                preference -> Optional.empty(),
                                                (preference, hostOfPreference) -> Optional.empty()))),
                        new DefaultPreferenceFragmentIdProvider()),
                PreferenceScreenGraphProviderFactory.createPreferenceScreenGraphProvider(
                        preferenceScreenWithHostProvider,
                        (preference, hostOfPreference) -> Optional.empty(),
                        classNameOfActivity -> Optional.empty(),
                        addEdgeToGraphPredicate,
                        activity,
                        preferenceScreenWithHost -> {
                        }),
                Locale.GERMAN);
    }

    private SearchablePreferenceOfHostWithinGraph getPreference(
            final Class<? extends PreferenceFragmentCompat> hostOfPreference,
            final Class<? extends PreferenceFragmentCompat> fragmentPointedTo,
            final Set<SearchablePreferenceScreenWithinGraph> preferenceScreenWithHostSet) {
        return getPreference(
                preferenceScreenWithHostSet,
                searchablePreferenceWithinGraph ->
                        hostOfPreference.equals(searchablePreferenceWithinGraph.hostOfPreference().host()) &&
                                searchablePreferenceWithinGraph.searchablePreference().getFragment().equals(Optional.of(fragmentPointedTo.getName())));
    }

    private SearchablePreferenceOfHostWithinGraph getPreference(
            final Set<SearchablePreferenceScreenWithinGraph> searchablePreferenceScreens,
            final Predicate<SearchablePreferenceOfHostWithinGraph> predicate) {
        return searchablePreferenceScreens
                .stream()
                .flatMap(
                        searchablePreferenceScreenWithinGraph ->
                                searchablePreferenceScreenWithinGraph
                                        .getAllPreferencesOfPreferenceHierarchy()
                                        .stream()
                                        .filter(predicate))
                .collect(MoreCollectors.onlyElement());
    }

    public static SearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHost createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider(
            final Class<? extends Fragment> root,
            final FragmentActivity activity) {
        return createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider(
                root,
                (edge, sourceNodeOfEdge, targetNodeOfEdge) -> true,
                activity);
    }

    private static SearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHost createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider(
            final Class<? extends Fragment> root,
            final AddEdgeToGraphPredicate addEdgeToGraphPredicate,
            final FragmentActivity activity) {
        final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider = createPreferenceScreenWithHostProvider(activity);
        return new SearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHost(
                createSearchablePreferenceScreenGraphProvider(
                        activity,
                        preferenceScreenWithHostProvider,
                        addEdgeToGraphPredicate),
                preferenceScreenWithHostProvider
                        .getPreferenceScreenWithHostOfFragment(
                                root,
                                Optional.empty())
                        .orElseThrow());
    }

    public static PreferenceScreenWithHostProvider createPreferenceScreenWithHostProvider(final FragmentActivity activity) {
        return new PreferenceScreenWithHostProvider(
                InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment(activity),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()));
    }

    public record SearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHost(
            SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider,
            PreferenceScreenWithHost preferenceScreenWithHost) {
    }

    public static class Fragment1ConnectedToFragment2AndFragment4 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment(
                    this,
                    "first screen",
                    List.of(
                            Fragment2ConnectedToFragment3ConnectedToFragment4.class,
                            Fragment4.class));
        }
    }

    public static class Fragment2ConnectedToFragment3ConnectedToFragment4 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            PreferenceScreensProviderTestHelper.configureConnectedPreferencesOfFragment(
                    this,
                    "second screen",
                    List.of(Fragment3ConnectedToFragment4.class));
        }
    }

    public static class Fragment3ConnectedToFragment4 extends PreferenceFragmentCompat {

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