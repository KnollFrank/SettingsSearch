package de.KnollFrank.lib.settingssearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.PreferenceScreensProviderTestHelper.getPreferenceScreenByName;
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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.DefaultPreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGeneratorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.graph.Graph2POJOGraphTransformer;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenGraphProviderFactory;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenGraphProvider1Test extends AppDatabaseTest {

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
                final Set<SearchablePreferenceScreenEntity> preferenceScreens =
                        result
                                .searchablePreferenceScreenGraphProvider()
                                .getSearchablePreferenceScreenGraph(result.preferenceScreenWithHost())
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
                final var result =
                        createSearchablePreferenceScreenGraphProviderAndPreferenceScreenWithHostProvider(
                                activity,
                                Fragment1ConnectedToFragment2AndFragment4.class);

                // When
                final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> pojoGraph =
                        result
                                .searchablePreferenceScreenGraphProvider()
                                .getSearchablePreferenceScreenGraph(
                                        result.preferenceScreenWithHost());

                // Then
                makeGetPreferencePathWorkOnGraph(pojoGraph, appDatabase);
                final SearchablePreferenceEntity preferenceOfFragment2PointingToFragment3 =
                        getPreference(
                                Fragment2ConnectedToFragment3.class,
                                Fragment3.class,
                                pojoGraph.vertexSet());
                final SearchablePreferenceEntity preferenceOfFragment1PointingToFragment2 =
                        getPreference(
                                Fragment1ConnectedToFragment2AndFragment4.class,
                                Fragment2ConnectedToFragment3.class,
                                pojoGraph.vertexSet());
                assertThat(
                        preferenceOfFragment2PointingToFragment3.getPreferencePath(appDatabase.searchablePreferenceEntityDAO()),
                        is(
                                new PreferenceEntityPath(
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
                                                (preference, hostOfPreference) -> Optional.empty()),
                                        IdGeneratorFactory.createIdGeneratorStartingAt(1))),
                        new DefaultPreferenceFragmentIdProvider()),
                PreferenceScreenGraphProviderFactory.createPreferenceScreenGraphProvider(
                        preferenceScreenWithHostProvider,
                        (preference, hostOfPreference) -> Optional.empty(),
                        classNameOfActivity -> Optional.empty(),
                        activity,
                        preferenceScreenWithHost -> {
                        }));
    }

    private SearchablePreferenceEntity getPreference(
            final Class<? extends PreferenceFragmentCompat> hostOfPreference,
            final Class<? extends PreferenceFragmentCompat> fragmentPointedTo,
            final Set<SearchablePreferenceScreenEntity> preferenceScreenWithHostSet) {
        return getPreference(
                preferenceScreenWithHostSet,
                preference -> hostOfPreference.equals(preference.getHost(appDatabase.searchablePreferenceEntityDAO()).getHost()) && preference.getFragment().equals(Optional.of(fragmentPointedTo.getName())));
    }

    private SearchablePreferenceEntity getPreference(
            final Set<SearchablePreferenceScreenEntity> searchablePreferenceScreens,
            final Predicate<SearchablePreferenceEntity> predicate) {
        return searchablePreferenceScreens
                .stream()
                .flatMap(
                        searchablePreferenceScreen ->
                                searchablePreferenceScreen
                                        .getAllPreferences(appDatabase.searchablePreferenceScreenEntityDAO())
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
                        PreferenceFragmentCompat::getPreferenceScreen,
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

    public static void makeGetPreferencePathWorkOnGraph(final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> graph,
                                                        final AppDatabase appDatabase) {
        appDatabase.searchablePreferenceScreenGraphDAO().persist(graph);
    }

    public static void makeGetPreferencePathWorkOnPreferences(final Collection<SearchablePreferenceEntity> preferences,
                                                              final AppDatabase appDatabase) {
        appDatabase.searchablePreferenceEntityDAO().persist(preferences);
    }
}