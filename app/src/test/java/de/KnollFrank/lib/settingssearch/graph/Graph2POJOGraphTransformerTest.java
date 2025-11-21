package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceScreenConverterTest.getInstantiateAndInitializeFragment;
import static de.KnollFrank.lib.settingssearch.graph.MapFromPojoNodesRemover.removeMapFromPojoNodes;
import static de.KnollFrank.lib.settingssearch.graph.PojoGraphs.getPreferences;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleTestFactory;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class Graph2POJOGraphTransformerTest extends PreferencesDatabaseTest {

    @Test
    public void shouldTransformGraph2POJOGraph() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceFragmentCompat preferenceFragment = new PreferenceFragmentTemplate(getAddPreferences2Screen());
                final InstantiateAndInitializeFragment instantiateAndInitializeFragment = getInstantiateAndInitializeFragment(preferenceFragment, activity);
                final Graph<PreferenceScreenWithHost, PreferenceEdge> entityGraph =
                        PojoGraphTestFactory.createSomeEntityPreferenceScreenGraph(preferenceFragment, instantiateAndInitializeFragment, activity);
                final Locale locale = Locale.GERMAN;
                final String twoNodeScreen1Id = "graph-screen1";
                final String twoNodeScreen2Id = "graph-screen2";
                final SearchablePreferenceScreenGraphTestFactory.Data _data =
                        new SearchablePreferenceScreenGraphTestFactory.Data(
                                addLocaleToId(locale, twoNodeScreen2Id + "-0"),
                                addLocaleToId(locale, twoNodeScreen1Id + "-1"),
                                "parentKey",
                                addLocaleToId(locale, twoNodeScreen1Id + "-0"),
                                addLocaleToId(locale, twoNodeScreen1Id + "-0-0"),
                                addLocaleToId(locale, twoNodeScreen1Id + "-0-1"),
                                addLocaleToId(locale, "singleNodeGraph-screen1"),
                                addLocaleToId(locale, twoNodeScreen1Id),
                                addLocaleToId(locale, twoNodeScreen2Id));
                final Graph2POJOGraphTransformer graph2POJOGraphTransformer =
                        createGraph2POJOGraphTransformer(
                                new PreferenceFragmentIdProvider() {

                                    @Override
                                    public String getId(final PreferenceFragmentCompat preferenceFragment) {
                                        if (PreferenceFragmentWithSinglePreference.class.equals(preferenceFragment.getClass())) {
                                            return twoNodeScreen2Id;
                                        }
                                        if (PreferenceFragmentTemplate.class.equals(preferenceFragment.getClass())) {
                                            return twoNodeScreen1Id;
                                        }
                                        throw new IllegalArgumentException();
                                    }
                                });

                // When
                final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph =
                        removeMapFromPojoNodes(
                                graph2POJOGraphTransformer.transformGraph2POJOGraph(
                                        entityGraph,
                                        locale));

                // Then
                // check graph:
                PojoGraphEquality.assertActualEqualsExpected(
                        pojoGraph,
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraph(
                                        preferenceFragment.getClass(),
                                        locale,
                                        _data)
                                .pojoGraph());
                {
                    final var data = getPreferenceAndExpectedPredecessorOfPreference(pojoGraph, _data);
                    final SearchablePreference preference = data.preference();
                    final SearchablePreference expectedPredecessorOfPreference = data.expectedPredecessorOfPreference();
                    // check predecessor:
                    assertThat("predecessor of " + preference, preference.getPredecessor(), is(Optional.of(expectedPredecessorOfPreference)));
                }
            });
        }
    }

    @Test(expected = IllegalStateException.class)
    public void test_transformGraph2POJOGraph_nonUniqueId_fail() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceFragmentIdProvider preferenceFragmentIdProviderCreatingNonUniqueId =
                        new PreferenceFragmentIdProvider() {

                            @Override
                            public String getId(final PreferenceFragmentCompat preferenceFragment) {
                                return "non unique id";
                            }
                        };
                final Graph2POJOGraphTransformer graph2POJOGraphTransformer =
                        createGraph2POJOGraphTransformer(preferenceFragmentIdProviderCreatingNonUniqueId);

                // When
                graph2POJOGraphTransformer.transformGraph2POJOGraph(
                        createSomeEntityGraph(activity),
                        Locale.GERMAN);
            });
        }
    }

    @Test
    public void shouldCreateAndPersistTwoGraphsForDifferentLocales() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceFragmentCompat preferenceFragment = new PreferenceFragmentTemplate(getAddPreferences2Screen());
                final InstantiateAndInitializeFragment instantiateAndInitializeFragment = getInstantiateAndInitializeFragment(preferenceFragment, activity);
                final Graph<PreferenceScreenWithHost, PreferenceEdge> entityGraph =
                        PojoGraphTestFactory.createSomeEntityPreferenceScreenGraph(preferenceFragment, instantiateAndInitializeFragment, activity);
                final Graph2POJOGraphTransformer graph2POJOGraphTransformer =
                        createGraph2POJOGraphTransformer(
                                new PreferenceFragmentIdProvider() {

                                    @Override
                                    public String getId(final PreferenceFragmentCompat preferenceFragment) {
                                        if (PreferenceFragmentWithSinglePreference.class.equals(preferenceFragment.getClass())) {
                                            return "graph-screen2";
                                        }
                                        if (PreferenceFragmentTemplate.class.equals(preferenceFragment.getClass())) {
                                            return "graph-screen1";
                                        }
                                        throw new IllegalArgumentException();
                                    }
                                });

                // When
                final SearchablePreferenceScreenGraph germanPojoGraph = transformGraph2POJOGraph(entityGraph, graph2POJOGraphTransformer, Locale.GERMAN);
                preferencesDatabase.searchablePreferenceScreenGraphDAO().persist(germanPojoGraph);

                // And
                final SearchablePreferenceScreenGraph chinesePojoGraph = transformGraph2POJOGraph(entityGraph, graph2POJOGraphTransformer, Locale.CHINESE);
                preferencesDatabase.searchablePreferenceScreenGraphDAO().persist(chinesePojoGraph);

                // Then no exception was thrown
            });
        }
    }

    public static String addLocaleToId(final Locale locale, final String id) {
        return locale.getLanguage() + "-" + id;
    }

    private static SearchablePreferenceScreenGraph transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> entityGraph,
            final Graph2POJOGraphTransformer graph2POJOGraphTransformer,
            final Locale locale) {
        return new SearchablePreferenceScreenGraph(
                removeMapFromPojoNodes(
                        graph2POJOGraphTransformer.transformGraph2POJOGraph(
                                entityGraph,
                                locale)),
                locale,
                PersistableBundleTestFactory.createSomePersistableBundle());
    }

    private static BiConsumer<PreferenceScreen, Context> getAddPreferences2Screen() {
        return new BiConsumer<>() {

            @Override
            public void accept(final PreferenceScreen screen, final Context context) {
                {
                    final PreferenceCategory preference = createParent(context);
                    screen.addPreference(preference);
                    preference.addPreference(createChild(context, "some child key 1"));
                    preference.addPreference(createChild(context, "some child key 2"));
                }
                screen.addPreference(createConnectionToFragment(PreferenceFragmentWithSinglePreference.class, context));
            }

            private static PreferenceCategory createParent(final Context context) {
                final PreferenceCategory preferenceCategory = new PreferenceCategory(context);
                preferenceCategory.setKey("parentKey");
                preferenceCategory.setLayoutResource(15);
                return preferenceCategory;
            }

            private static Preference createChild(final Context context, final String key) {
                final Preference child = new Preference(context);
                child.setKey(key);
                child.setLayoutResource(16);
                return child;
            }

            private static Preference createConnectionToFragment(final Class<? extends Fragment> fragment,
                                                                 final Context context) {
                final Preference preference = new Preference(context);
                preference.setFragment(fragment.getName());
                preference.setTitle("preference connected to " + fragment.getSimpleName());
                preference.setKey("some key");
                return preference;
            }
        };
    }

    public static class PreferenceFragmentWithSinglePreference extends PreferenceFragmentTemplate {

        public PreferenceFragmentWithSinglePreference() {
            super(context -> {
                final Preference preference = new Preference(context);
                preference.setKey("some dst key");
                preference.setTitle("some title for dst key");
                return List.of(preference);
            });
        }
    }

    private PreferenceAndExpectedPredecessorOfPreference getPreferenceAndExpectedPredecessorOfPreference(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraphExpected,
            final SearchablePreferenceScreenGraphTestFactory.Data data) {
        final Set<SearchablePreference> searchablePreferences = getPreferences(pojoGraphExpected.vertexSet());
        return new PreferenceAndExpectedPredecessorOfPreference(
                getDstPreference(searchablePreferences, data.DST_PREFERENCE_ID()),
                getPreferenceConnectingSrc2Dst(searchablePreferences, data.PREFERENCE_CONNECTING_SRC_2_DST_ID()));
    }

    private record PreferenceAndExpectedPredecessorOfPreference(
            SearchablePreference preference,
            SearchablePreference expectedPredecessorOfPreference) {
    }

    private static SearchablePreference getPreferenceConnectingSrc2Dst(final Set<SearchablePreference> searchablePreferences,
                                                                       final String PREFERENCE_CONNECTING_SRC_2_DST_ID) {
        return getPreferenceById(searchablePreferences, PREFERENCE_CONNECTING_SRC_2_DST_ID);
    }

    private static SearchablePreference getDstPreference(final Set<SearchablePreference> searchablePreferences,
                                                         final String DST_PREFERENCE_ID) {
        return getPreferenceById(searchablePreferences, DST_PREFERENCE_ID);
    }

    private static SearchablePreference getPreferenceById(final Set<SearchablePreference> searchablePreferences,
                                                          final String id) {
        return SearchablePreferences
                .findPreferenceById(searchablePreferences, id)
                .orElseThrow();
    }

    private static Graph<PreferenceScreenWithHost, PreferenceEdge> createSomeEntityGraph(final FragmentActivity activity) {
        final PreferenceFragmentCompat preferenceFragment = new PreferenceFragmentTemplate(getAddPreferences2Screen());
        return PojoGraphTestFactory.createSomeEntityPreferenceScreenGraph(
                preferenceFragment,
                getInstantiateAndInitializeFragment(preferenceFragment, activity),
                activity);
    }

    private static Graph2POJOGraphTransformer createGraph2POJOGraphTransformer(final PreferenceFragmentIdProvider preferenceFragmentIdProvider) {
        return new Graph2POJOGraphTransformer(
                new PreferenceScreen2SearchablePreferenceScreenConverter(
                        new Preference2SearchablePreferenceConverter(
                                (preference, hostOfPreference) -> Optional.empty(),
                                new SearchableInfoAndDialogInfoProvider(
                                        preference -> Optional.empty(),
                                        (preference, hostOfPreference) -> Optional.empty()))),
                preferenceFragmentIdProvider);
    }
}