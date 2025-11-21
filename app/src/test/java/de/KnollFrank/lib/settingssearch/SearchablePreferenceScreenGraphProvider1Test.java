package de.KnollFrank.lib.settingssearch;

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
import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.DefaultPreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleTestFactory;
import de.KnollFrank.lib.settingssearch.graph.Graph2POJOGraphTransformer;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenGraphProviderFactory;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenGraphProvider1Test extends PreferencesDatabaseTest {

    @Test
    public void shouldGetSearchablePreferenceScreenGraph() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final var result =
                        createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider(
                                activity,
                                Fragment1ConnectedToFragment2AndFragment4.class);

                // When
                final Set<SearchablePreferenceScreen> preferenceScreens =
                        result
                                .searchablePreferenceScreenGraphProvider()
                                .getSearchablePreferenceScreenGraph(result.preferenceScreenWithHost())
                                .vertexSet();

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
    public void shouldGetPreferencePath() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final var result =
                        createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider(
                                activity,
                                Fragment1ConnectedToFragment2AndFragment4.class);

                // When
                final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph =
                        result
                                .searchablePreferenceScreenGraphProvider()
                                .getSearchablePreferenceScreenGraph(
                                        result.preferenceScreenWithHost());

                // Then
                makeGetPreferencePathWorkOnGraph(pojoGraph, preferencesDatabase, Locale.GERMAN);
                final SearchablePreference preferenceOfFragment2PointingToFragment3 =
                        getPreference(
                                Fragment2ConnectedToFragment3ConnectedToFragment4.class,
                                Fragment3ConnectedToFragment4.class,
                                pojoGraph.vertexSet());
                final SearchablePreference preferenceOfFragment1PointingToFragment2 =
                        getPreference(
                                Fragment1ConnectedToFragment2AndFragment4.class,
                                Fragment2ConnectedToFragment3ConnectedToFragment4.class,
                                pojoGraph.vertexSet());
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

    public static SearchablePreferenceScreenGraphProvider createSearchablePreferenceScreenGraphProvider(
            final FragmentActivity activity,
            final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider) {
        return new SearchablePreferenceScreenGraphProvider(
                preferenceScreenGraph -> {
                },
                emptyComputePreferencesListener(),
                new Graph2POJOGraphTransformer(
                        new PreferenceScreen2SearchablePreferenceScreenConverter(
                                new Preference2SearchablePreferenceConverter(
                                        (preference, hostOfPreference) -> Optional.empty(),
                                        new SearchableInfoAndDialogInfoProvider(
                                                preference -> Optional.empty(),
                                                (preference, hostOfPreference) -> Optional.empty()))),
                        new DefaultPreferenceFragmentIdProvider()),
                PreferenceScreenGraphProviderFactory.createPreferenceScreenGraphProvider(
                        preferenceScreenWithHostProvider,
                        (preference, hostOfPreference) -> Optional.empty(),
                        classNameOfActivity -> Optional.empty(),
                        activity,
                        preferenceScreenWithHost -> {
                        }),
                Locale.GERMAN);
    }

    private SearchablePreference getPreference(
            final Class<? extends PreferenceFragmentCompat> hostOfPreference,
            final Class<? extends PreferenceFragmentCompat> fragmentPointedTo,
            final Set<SearchablePreferenceScreen> preferenceScreenWithHostSet) {
        return getPreference(
                preferenceScreenWithHostSet,
                preference -> hostOfPreference.equals(preference.getHost().host()) && preference.getFragment().equals(Optional.of(fragmentPointedTo.getName())));
    }

    private SearchablePreference getPreference(
            final Set<SearchablePreferenceScreen> searchablePreferenceScreens,
            final Predicate<SearchablePreference> predicate) {
        return searchablePreferenceScreens
                .stream()
                .flatMap(
                        searchablePreferenceScreen ->
                                searchablePreferenceScreen
                                        .allPreferencesOfPreferenceHierarchy()
                                        .stream()
                                        .filter(predicate))
                .collect(MoreCollectors.onlyElement());
    }

    public static SearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHost createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider(
            final FragmentActivity activity,
            final Class<? extends Fragment> root) {
        final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider =
                new PreferenceScreenWithHostProvider(
                        InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment(activity),
                        new PrincipalAndProxyProvider(ImmutableBiMap.of()));
        return new SearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHost(
                createSearchablePreferenceScreenGraphProvider(
                        activity,
                        preferenceScreenWithHostProvider),
                preferenceScreenWithHostProvider
                        .getPreferenceScreenWithHostOfFragment(
                                root,
                                Optional.empty())
                        .orElseThrow());
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

    public static void makeGetPreferencePathWorkOnGraph(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
                                                        final PreferencesDatabase preferencesDatabase,
                                                        final Locale locale) {
        preferencesDatabase.searchablePreferenceScreenGraphDAO().persist(
                new SearchablePreferenceScreenGraph(
                        graph,
                        locale,
                        PersistableBundleTestFactory.createSomePersistableBundle()));
    }
}