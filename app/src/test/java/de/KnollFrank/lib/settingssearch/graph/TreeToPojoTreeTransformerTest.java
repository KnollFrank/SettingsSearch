package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment;
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

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceToSearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceWithinTrees;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleTestFactory;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeToPojoTreeTransformerTest extends PreferencesRoomDatabaseTest {

    @Test
    public void shouldTransformTreeToPojoTree() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceFragmentCompat preferenceFragment = new PreferenceFragmentTemplate(getAddPreferencesToScreen());
                final Tree<PreferenceScreenWithHost, Preference, ImmutableValueGraph<PreferenceScreenWithHost, Preference>> entityGraph =
                        PojoGraphTestFactory.createEntityPreferenceScreenGraphRootedAt(
                                preferenceFragment.getClass(),
                                createInstantiateAndInitializeFragment(preferenceFragment, activity),
                                activity);
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
                final TreeToPojoTreeTransformer treeToPojoTreeTransformer =
                        createGraphToPojoGraphTransformer(
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
                final Tree<SearchablePreferenceScreen, SearchablePreference, ? extends ValueGraph<SearchablePreferenceScreen, SearchablePreference>> pojoGraph =
                        removeMapFromPojoNodes(
                                treeToPojoTreeTransformer.transformTreeToPojoTree(
                                        entityGraph,
                                        locale));

                // Then
                // check graph:
                assertThat(
                        pojoGraph,
                        is(
                                SearchablePreferenceScreenGraphTestFactory
                                        .createGraph(
                                                preferenceFragment.getClass(),
                                                locale,
                                                _data)
                                        .pojoTree()));
                {
                    final var data = getPreferenceAndExpectedPredecessorOfPreference(pojoGraph, _data);
                    final SearchablePreferenceOfHostWithinTree preference = data.preference();
                    final SearchablePreferenceOfHostWithinTree expectedPredecessorOfPreference = data.expectedPredecessorOfPreference();
                    // check predecessor:
                    final List<SearchablePreferenceOfHostWithinTree> preferences = preference.getPreferencePath().preferences();
                    assertThat("predecessor of " + preference, preferences.get(preferences.size() - 2), is(expectedPredecessorOfPreference));
                }
            });
        }
    }

    @Test(expected = IllegalStateException.class)
    public void test_transformTreeToPojoTree_nonUniqueId_fail() {
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
                final TreeToPojoTreeTransformer treeToPojoTreeTransformer =
                        createGraphToPojoGraphTransformer(preferenceFragmentIdProviderCreatingNonUniqueId);

                // When
                treeToPojoTreeTransformer.transformTreeToPojoTree(
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
                final PreferenceFragmentCompat preferenceFragment = new PreferenceFragmentTemplate(getAddPreferencesToScreen());
                final Tree<PreferenceScreenWithHost, Preference, ImmutableValueGraph<PreferenceScreenWithHost, Preference>> entityGraph =
                        PojoGraphTestFactory.createEntityPreferenceScreenGraphRootedAt(
                                preferenceFragment.getClass(),
                                createInstantiateAndInitializeFragment(preferenceFragment, activity),
                                activity);
                final TreeToPojoTreeTransformer treeToPojoTreeTransformer =
                        createGraphToPojoGraphTransformer(
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
                final SearchablePreferenceScreenGraph germanPojoGraph = transformTreeToPojoTree(entityGraph, treeToPojoTreeTransformer, Locale.GERMAN);
                preferencesRoomDatabase.searchablePreferenceScreenGraphDAO().persistOrReplace(germanPojoGraph);

                // And
                final SearchablePreferenceScreenGraph chinesePojoGraph = transformTreeToPojoTree(entityGraph, treeToPojoTreeTransformer, Locale.CHINESE);
                preferencesRoomDatabase.searchablePreferenceScreenGraphDAO().persistOrReplace(chinesePojoGraph);

                // Then no exception was thrown
            });
        }
    }

    public static String addLocaleToId(final Locale locale, final String id) {
        return locale.getLanguage() + "-" + id;
    }

    private static SearchablePreferenceScreenGraph transformTreeToPojoTree(
            final Tree<PreferenceScreenWithHost, Preference, ? extends ValueGraph<PreferenceScreenWithHost, Preference>> entityGraph,
            final TreeToPojoTreeTransformer treeToPojoTreeTransformer,
            final Locale locale) {
        return new SearchablePreferenceScreenGraph(
                MapFromPojoNodesRemover.removeMapFromPojoNodes(
                        treeToPojoTreeTransformer.transformTreeToPojoTree(
                                entityGraph,
                                locale)),
                locale,
                PersistableBundleTestFactory.createSomePersistableBundle());
    }

    private static BiConsumer<PreferenceScreen, Context> getAddPreferencesToScreen() {
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
            final Tree<SearchablePreferenceScreen, SearchablePreference, ? extends ValueGraph<SearchablePreferenceScreen, SearchablePreference>> pojoGraphExpected,
            final SearchablePreferenceScreenGraphTestFactory.Data data) {
        final Set<SearchablePreferenceOfHostWithinTree> searchablePreferences = getPreferences(pojoGraphExpected);
        return new PreferenceAndExpectedPredecessorOfPreference(
                getDstPreference(searchablePreferences, data.DST_PREFERENCE_ID()),
                getPreferenceConnectingSrc2Dst(searchablePreferences, data.PREFERENCE_CONNECTING_SRC_TO_DST_ID()));
    }

    private record PreferenceAndExpectedPredecessorOfPreference(
            SearchablePreferenceOfHostWithinTree preference,
            SearchablePreferenceOfHostWithinTree expectedPredecessorOfPreference) {
    }

    private static SearchablePreferenceOfHostWithinTree getPreferenceConnectingSrc2Dst(final Set<SearchablePreferenceOfHostWithinTree> searchablePreferences,
                                                                                       final String PREFERENCE_CONNECTING_SRC_2_DST_ID) {
        return getPreferenceById(searchablePreferences, PREFERENCE_CONNECTING_SRC_2_DST_ID);
    }

    private static SearchablePreferenceOfHostWithinTree getDstPreference(final Set<SearchablePreferenceOfHostWithinTree> searchablePreferences,
                                                                         final String DST_PREFERENCE_ID) {
        return getPreferenceById(searchablePreferences, DST_PREFERENCE_ID);
    }

    private static SearchablePreferenceOfHostWithinTree getPreferenceById(final Set<SearchablePreferenceOfHostWithinTree> searchablePreferences,
                                                                          final String id) {
        return SearchablePreferenceWithinTrees
                .findPreferenceById(searchablePreferences, id)
                .orElseThrow();
    }

    private static Tree<PreferenceScreenWithHost, Preference, ImmutableValueGraph<PreferenceScreenWithHost, Preference>> createSomeEntityGraph(final FragmentActivity activity) {
        final PreferenceFragmentCompat preferenceFragment = new PreferenceFragmentTemplate(getAddPreferencesToScreen());
        return PojoGraphTestFactory.createEntityPreferenceScreenGraphRootedAt(
                preferenceFragment.getClass(),
                createInstantiateAndInitializeFragment(preferenceFragment, activity),
                activity);
    }

    private static TreeToPojoTreeTransformer createGraphToPojoGraphTransformer(final PreferenceFragmentIdProvider preferenceFragmentIdProvider) {
        return new TreeToPojoTreeTransformer(
                new PreferenceScreenToSearchablePreferenceScreenConverter(
                        new PreferenceToSearchablePreferenceConverter(
                                (preference, hostOfPreference) -> Optional.empty(),
                                new SearchableInfoAndDialogInfoProvider(
                                        preference -> Optional.empty(),
                                        (preference, hostOfPreference) -> Optional.empty()))),
                preferenceFragmentIdProvider);
    }
}