package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenGraphProvider1Test.makeGetPreferencePathWorkOnGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHost2POJOConverterTest.getInstantiateAndInitializeFragment;
import static de.KnollFrank.lib.settingssearch.graph.MapFromPojoNodesRemover.removeMapFromPojoNodes;
import static de.KnollFrank.lib.settingssearch.graph.PojoGraphs.getPreferences;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGeneratorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.lib.settingssearch.db.preference.dao.TestPreferenceFragment;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.HostWithArguments;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class Graph2POJOGraphTransformerTest extends AppDatabaseTest {

    private static final int DST_PREFERENCE_ID = 5;
    private static final int PREFERENCE_CONNECTING_SRC_2_DST_ID = 4;

    @Test
    public void shouldTransformGraph2POJOGraph() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceFragmentCompat preferenceFragment = new PreferenceFragmentTemplate(getAddPreferences2Screen());
                final InstantiateAndInitializeFragment instantiateAndInitializeFragment = getInstantiateAndInitializeFragment(preferenceFragment, activity);
                final Graph<PreferenceScreenWithHost, PreferenceEdge> entityGraph =
                        PojoGraphTestFactory.createSomeEntityPreferenceScreenGraph(preferenceFragment, instantiateAndInitializeFragment, activity);
                final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter =
                        new Preference2SearchablePreferenceConverter(
                                (preference, hostOfPreference) -> Optional.empty(),
                                new SearchableInfoAndDialogInfoProvider(
                                        preference -> Optional.empty(),
                                        (preference, hostOfPreference) -> Optional.empty()),
                                IdGeneratorFactory.createIdGeneratorStartingAt(1));

                // When
                final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph =
                        removeMapFromPojoNodes(
                                Graph2POJOGraphTransformer.transformGraph2POJOGraph(
                                        entityGraph,
                                        preference2SearchablePreferenceConverter));

                // Then
                makeGetPreferencePathWorkOnGraph(pojoGraph, appDatabase);
                // check graph:
                assertThat(pojoGraph, is(createPojoGraph(preferenceFragment.getClass())));
                {
                    final var data = getPreferenceAndExpectedPredecessorOfPreference(pojoGraph);
                    final SearchablePreference preference = data.preference();
                    final SearchablePreference expectedPredecessorOfPreference = data.expectedPredecessorOfPreference();
                    // check predecessor:
                    assertThat("predecessor of " + preference, preference.getPredecessor(), is(Optional.of(expectedPredecessorOfPreference)));
                }
            });
        }
    }

    private static BiConsumer<PreferenceScreen, Context> getAddPreferences2Screen() {
        return new BiConsumer<>() {

            @Override
            public void accept(final PreferenceScreen screen, final Context context) {
                {
                    final PreferenceCategory preference = createParent(context);
                    screen.addPreference(preference);
                    preference.addPreference(createChild(context));
                    preference.addPreference(createChild(context));
                }
                screen.addPreference(createConnectionToFragment(PreferenceFragmentWithSinglePreference.class, context));
            }

            private static PreferenceCategory createParent(final Context context) {
                final PreferenceCategory preferenceCategory = new PreferenceCategory(context);
                preferenceCategory.setKey("parentKey");
                preferenceCategory.setLayoutResource(15);
                return preferenceCategory;
            }

            private static Preference createChild(final Context context) {
                final Preference child = new Preference(context);
                child.setKey("some child key");
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

    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> createPojoGraph(final Class<? extends PreferenceFragmentCompat> host) {
        final int screenId = 1;
        final SearchablePreference preferenceConnectingSrc2Dst =
                new SearchablePreference(
                        PREFERENCE_CONNECTING_SRC_2_DST_ID,
                        "some key",
                        Optional.empty(),
                        2131427444,
                        Optional.empty(),
                        Optional.of("preference connected to TestPreferenceFragment"),
                        0,
                        Optional.of(PreferenceFragmentWithSinglePreference.class.getName()),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        new Bundle(),
                        host,
                        Optional.empty(),
                        Optional.empty(),
                        screenId);
        return DefaultDirectedGraph
                .<SearchablePreferenceScreen, SearchablePreferenceEdge>createBuilder(SearchablePreferenceEdge.class)
                .addEdge(
                        createSrc(screenId, preferenceConnectingSrc2Dst, host),
                        createDst(Optional.of(screenId), preferenceConnectingSrc2Dst),
                        new SearchablePreferenceEdge(preferenceConnectingSrc2Dst))
                .build();
    }

    private static SearchablePreferenceScreen createSrc(final int screenId,
                                                        final SearchablePreference preferenceConnectingSrc2Dst,
                                                        final Class<? extends PreferenceFragmentCompat> host) {
        final SearchablePreference parent =
                new SearchablePreference(
                        1,
                        "parentKey",
                        Optional.empty(),
                        15,
                        Optional.empty(),
                        Optional.empty(),
                        0,
                        Optional.empty(),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        new Bundle(),
                        host,
                        Optional.empty(),
                        Optional.empty(),
                        screenId);
        return new SearchablePreferenceScreen(
                screenId,
                new HostWithArguments(host, Optional.empty()),
                "screen title",
                "screen summary",
                Set.of(
                        parent,
                        new SearchablePreference(
                                2,
                                "some child key 1",
                                Optional.empty(),
                                16,
                                Optional.empty(),
                                Optional.empty(),
                                0,
                                Optional.empty(),
                                Optional.empty(),
                                true,
                                Optional.empty(),
                                new Bundle(),
                                host,
                                Optional.of(1),
                                Optional.empty(),
                                screenId),
                        new SearchablePreference(
                                3,
                                "some child key 2",
                                Optional.empty(),
                                16,
                                Optional.empty(),
                                Optional.empty(),
                                0,
                                Optional.empty(),
                                Optional.empty(),
                                true,
                                Optional.empty(),
                                new Bundle(),
                                host,
                                Optional.of(1),
                                Optional.empty(),
                                screenId),
                        preferenceConnectingSrc2Dst),
                Optional.empty());
    }

    private static SearchablePreferenceScreen createDst(final Optional<Integer> parentScreenId,
                                                        final SearchablePreference predecessor) {
        final int screenId = 2;
        final SearchablePreference e1 =
                new SearchablePreference(
                        DST_PREFERENCE_ID,
                        "some dst key",
                        Optional.empty(),
                        16,
                        Optional.empty(),
                        Optional.of("some title for dst key"),
                        0,
                        Optional.empty(),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        new Bundle(),
                        TestPreferenceFragment.class,
                        Optional.empty(),
                        Optional.of(predecessor.getId()),
                        screenId);
        return new SearchablePreferenceScreen(
                screenId,
                new HostWithArguments(TestPreferenceFragment.class, Optional.empty()),
                null,
                null,
                Set.of(e1),
                parentScreenId);
    }

    private static PreferenceAndExpectedPredecessorOfPreference getPreferenceAndExpectedPredecessorOfPreference(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraphExpected) {
        final Set<SearchablePreference> searchablePreferences = getPreferences(pojoGraphExpected.vertexSet());
        return new PreferenceAndExpectedPredecessorOfPreference(
                getDstPreference(searchablePreferences),
                getPreferenceConnectingSrc2Dst(searchablePreferences));
    }

    private record PreferenceAndExpectedPredecessorOfPreference(SearchablePreference preference,
                                                                SearchablePreference expectedPredecessorOfPreference) {
    }

    private static SearchablePreference getPreferenceConnectingSrc2Dst(final Set<SearchablePreference> searchablePreferences) {
        return getPreferenceById(searchablePreferences, PREFERENCE_CONNECTING_SRC_2_DST_ID);
    }

    private static SearchablePreference getDstPreference(final Set<SearchablePreference> searchablePreferences) {
        return getPreferenceById(searchablePreferences, DST_PREFERENCE_ID);
    }

    private static SearchablePreference getPreferenceById(final Set<SearchablePreference> searchablePreferences,
                                                          final int id) {
        return SearchablePreferences
                .findPreferenceRecursivelyByPredicate(
                        searchablePreferences,
                        searchablePreference -> searchablePreference.getId() == id)
                .orElseThrow();
    }
}