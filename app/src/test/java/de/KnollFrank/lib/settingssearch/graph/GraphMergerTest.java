package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment;
import static de.KnollFrank.lib.settingssearch.graph.Graph2POJOGraphTransformerTest.PreferenceFragmentWithSinglePreference;
import static de.KnollFrank.lib.settingssearch.graph.MapFromPojoNodesRemover.removeMapFromPojoNodes;
import static de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst.markExtrasOfPreferenceConnectingSrcWithDst;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.DefaultPreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class GraphMergerTest {

    /*
    dot -Tpdf entityGraph.dot -o entityGraph.pdf
    dot -Tpdf partialEntityGraph.dot -o partialEntityGraph.pdf
    dot -Tpdf mergedGraphExpected.dot -o mergedGraphExpected.pdf
    pdfunite entityGraph.pdf partialEntityGraph.pdf mergedGraphExpected.pdf allGraphs.pdf
     */
    @Test
    public void shouldMergeGraphs() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final GraphMerger graphMerger = new GraphMerger();
                final Graph<PreferenceScreenWithHost, PreferenceEdge> entityGraph = createEntityGraph(List.of(), activity);
                // System.out.println("entityGraph: " + PreferenceScreenGraph2DOTConverter.graph2DOT(entityGraph));

                final Graph<PreferenceScreenWithHost, PreferenceEdge> partialEntityGraph = createEntityGraph(List.of("key1"), activity);
                partialEntityGraph.removeVertex(
                        partialEntityGraph.getEdgeTarget(
                                getPreferenceEdgeHavingKey(
                                        partialEntityGraph,
                                        "some preference key")));
                // System.out.println("partialEntityGraph: " + PreferenceScreenGraph2DOTConverter.graph2DOT(partialEntityGraph));

                // When
                final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergedGraph =
                        graphMerger.mergePartialGraphIntoGraph(
                                transformToPojoGraph(partialEntityGraph),
                                transformToPojoGraph(entityGraph));

                // Then
                final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergedGraphExpected = transformToPojoGraph(createEntityGraph(List.of("key1"), activity));
                // System.out.println("mergedGraphExpected: " + PreferenceScreenGraph2DOTConverter.graph2DOT(_mergedGraphExpected));
                PojoGraphEquality.assertActualEqualsExpected(mergedGraph, mergedGraphExpected);
            });
        }
    }

    private static PreferenceEdge getPreferenceEdgeHavingKey(final Graph<PreferenceScreenWithHost, PreferenceEdge> graph,
                                                             final String keyOfPreferenceOnEdge) {
        return graph
                .edgeSet()
                .stream()
                .filter(edge -> keyOfPreferenceOnEdge.equals(edge.preference.getKey()))
                .findFirst()
                .orElseThrow();
    }

    private static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> transformToPojoGraph(final Graph<PreferenceScreenWithHost, PreferenceEdge> entityGraph) {
        return removeMapFromPojoNodes(
                createGraph2POJOGraphTransformer().transformGraph2POJOGraph(
                        entityGraph,
                        Locale.GERMAN));
    }

    private static Graph2POJOGraphTransformer createGraph2POJOGraphTransformer() {
        return new Graph2POJOGraphTransformer(
                new PreferenceScreen2SearchablePreferenceScreenConverter(
                        new Preference2SearchablePreferenceConverter(
                                (preference, hostOfPreference) -> Optional.empty(),
                                new SearchableInfoAndDialogInfoProvider(
                                        preference -> Optional.empty(),
                                        (preference, hostOfPreference) -> Optional.empty()))),
                new DefaultPreferenceFragmentIdProvider());
    }

    private static Graph<PreferenceScreenWithHost, PreferenceEdge> createEntityGraph(final List<String> preferenceKeys, final TestActivity activity) {
        final InstantiateAndInitializeFragment instantiateAndInitializeFragment = createInstantiateAndInitializeFragment(activity);
        PreferenceFragmentWithPreferenceCategory.setPreferenceKeys(preferenceKeys);
        return PojoGraphTestFactory.createSomeEntityPreferenceScreenGraph(
                PreferenceFragmentWithPreferenceCategory.class,
                instantiateAndInitializeFragment,
                activity);
    }

    public static class PreferenceFragmentWithPreferenceCategory extends PreferenceFragmentCompat {

        private static List<String> preferenceKeys = List.of();

        public static void setPreferenceKeys(final List<String> preferenceKeys) {
            PreferenceFragmentWithPreferenceCategory.preferenceKeys = preferenceKeys;
        }

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(requireContext());
            preferenceScreen.setTitle("screen title");
            preferenceScreen.setSummary("screen summary");
            addPreferencesToPreferenceScreen(preferenceScreen);
            setPreferenceScreen(preferenceScreen);
        }

        private void addPreferencesToPreferenceScreen(final PreferenceScreen preferenceScreen) {
            preferenceScreen.addPreference(createPreference("some preference key"));
            final PreferenceCategory preferenceCategory = createPreferenceCategory();
            preferenceScreen.addPreference(preferenceCategory);
            this
                    .createPreferences(preferenceKeys)
                    .forEach(preferenceCategory::addPreference);
        }

        private PreferenceCategory createPreferenceCategory() {
            final PreferenceCategory preferenceCategory = new PreferenceCategory(requireContext());
            preferenceCategory.setTitle("some category");
            preferenceCategory.setKey("preferenceCategoryKey");
            return preferenceCategory;
        }

        private List<Preference> createPreferences(final List<String> keys) {
            return keys
                    .stream()
                    .map(this::createPreference)
                    .collect(Collectors.toList());
        }

        private Preference createPreference(final String key) {
            return createPreferenceConnectedToFragment(PreferenceFragmentWithSinglePreference.class, key);
        }

        private Preference createPreferenceConnectedToFragment(final Class<? extends Fragment> fragment,
                                                               final String keyOfPreference) {
            final Preference preference = new Preference(requireContext());
            preference.setFragment(fragment.getName());
            preference.setTitle("preference " + keyOfPreference + " connected to " + fragment.getSimpleName());
            preference.setKey(keyOfPreference);
            markExtrasOfPreferenceConnectingSrcWithDst(preference, this, fragment);
            return preference;
        }
    }
}
