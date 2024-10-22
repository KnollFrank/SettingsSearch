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

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MoreCollectors;

import org.jgrapht.Graph;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassPOJOWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.graph.DefaultSearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.graph.Graph2POJOGraphTransformer;
import de.KnollFrank.lib.settingssearch.graph.HostClassAndMapFromNodesRemover;
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
                        searchablePreferenceScreenGraphProvider
                                .getSearchablePreferenceScreenGraph()
                                .vertexSet();

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
                final Graph<PreferenceScreenWithHostClass, PreferenceEdge> entityGraph =
                        searchablePreferenceScreenGraphProvider.getSearchablePreferenceScreenGraph();
                final Graph<PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> pojoGraph =
                        Graph2POJOGraphTransformer.transformGraph2POJOGraph(entityGraph);
                final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap =
                        getPojoEntityMap(pojoGraph);
                final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference =
                        PreferencePathByPojoPreferenceProvider.getPreferencePathByPojoPreference(
                                HostClassAndMapFromNodesRemover.removeHostClassAndMapFromNodes(pojoGraph));

                // Then
                final Preference preferenceOfFragment2PointingToFragment3 =
                        getPreference(
                                Fragment2ConnectedToFragment3.class,
                                Fragment3.class,
                                entityGraph.vertexSet());
                final Preference preferenceOfFragment1PointingToFragment2 =
                        getPreference(
                                Fragment1ConnectedToFragment2AndFragment4.class,
                                Fragment2ConnectedToFragment3.class,
                                entityGraph.vertexSet());
                assertThat(
                        preferencePathByPreference.get(entity2Pojo(pojoEntityMap, preferenceOfFragment2PointingToFragment3)),
                        is(
                                new PreferencePath(
                                        Stream
                                                .of(
                                                        preferenceOfFragment1PointingToFragment2,
                                                        preferenceOfFragment2PointingToFragment3)
                                                .map(preference -> entity2Pojo(pojoEntityMap, preference))
                                                .collect(Collectors.toList()))));
            });
        }
    }

    private static SearchablePreferencePOJO entity2Pojo(final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap,
                                                        final Preference entity) {
        return pojoEntityMap.inverse().get(entity);
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
            final Class<? extends PreferenceFragmentCompat> hostOfPreference,
            final Class<? extends PreferenceFragmentCompat> fragmentPointedTo,
            final Set<PreferenceScreenWithHostClass> preferenceScreenWithHostSet) {
        return getPreference(
                preferenceScreenWithHostSet,
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
                                        .getChildrenRecursively(preferenceScreenWithHost.preferenceScreen())
                                        .stream()
                                        .filter(preference -> predicate.test(preferenceScreenWithHost.host(), preference)))
                .collect(MoreCollectors.onlyElement());
    }

    private static BiMap<SearchablePreferencePOJO, SearchablePreference> getPojoEntityMap(
            final Graph<PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> pojoGraph) {
        return Maps.mergeBiMaps(
                pojoGraph
                        .vertexSet()
                        .stream()
                        .map(PreferenceScreenWithHostClassPOJOWithMap::pojoEntityMap)
                        .collect(Collectors.toList()));
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