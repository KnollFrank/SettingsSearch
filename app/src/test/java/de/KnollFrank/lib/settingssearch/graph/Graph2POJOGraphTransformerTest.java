package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassPOJOWithMap;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.getFragments;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAOTest.createSomePreferenceScreenGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAOTest.getAddPreferences2Screen;
import static de.KnollFrank.lib.settingssearch.graph.MapFromPojoNodesRemover.removeMapFromPojoNodes;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAOTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class Graph2POJOGraphTransformerTest {

    @Test
    public void shouldTransformGraph2POJOGraph() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceFragmentCompat preferenceFragment = new PreferenceFragmentTemplate(getAddPreferences2Screen());
                final Graph<PreferenceScreenWithHostClass, PreferenceEdge> entityGraph =
                        createSomePreferenceScreenGraph(
                                preferenceFragment,
                                getFragments(preferenceFragment, activity));

                // When
                final Graph<PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> pojoGraph =
                        Graph2POJOGraphTransformer.transformGraph2POJOGraph(entityGraph);

                // Then
                assertThat(removeMapFromPojoNodes(pojoGraph), is(createPojoGraph(preferenceFragment.getClass())));
            });
        }
    }

    private static Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> createPojoGraph(final Class<? extends PreferenceFragmentCompat> host) {
        final SearchablePreferencePOJO preferenceConnectingSrc2Dst =
                SearchablePreferencePOJO.of(
                        4,
                        Optional.empty(),
                        Optional.empty(),
                        2131427444,
                        Optional.empty(),
                        Optional.of("preference connected to TestPreferenceFragment"),
                        0,
                        Optional.of(SearchablePreferenceScreenGraphDAOTest.TestPreferenceFragment.class.getName()),
                        true,
                        Optional.empty(),
                        new Bundle(),
                        List.of());
        return DefaultDirectedGraph
                .<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge>createBuilder(SearchablePreferencePOJOEdge.class)
                .addEdge(
                        createSrc(preferenceConnectingSrc2Dst, host),
                        createDst(),
                        new SearchablePreferencePOJOEdge(preferenceConnectingSrc2Dst))
                .build();
    }

    private static PreferenceScreenWithHostClassPOJO createSrc(final SearchablePreferencePOJO preferenceConnectingSrc2Dst,
                                                               final Class<? extends PreferenceFragmentCompat> host) {
        return new PreferenceScreenWithHostClassPOJO(
                1,
                new SearchablePreferenceScreenPOJO(
                        "screen title",
                        "screen summary",
                        List.of(
                                SearchablePreferencePOJO.of(
                                        1,
                                        Optional.of("parentKey"),
                                        Optional.empty(),
                                        15,
                                        Optional.empty(),
                                        Optional.empty(),
                                        0,
                                        Optional.empty(),
                                        true,
                                        Optional.empty(),
                                        new Bundle(),
                                        List.of(
                                                SearchablePreferencePOJO.of(
                                                        2,
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        16,
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        0,
                                                        Optional.empty(),
                                                        true,
                                                        Optional.empty(),
                                                        new Bundle(),
                                                        List.of()),
                                                SearchablePreferencePOJO.of(
                                                        3,
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        16,
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        0,
                                                        Optional.empty(),
                                                        true,
                                                        Optional.empty(),
                                                        new Bundle(),
                                                        List.of()))),
                                preferenceConnectingSrc2Dst)),
                host);
    }

    private static PreferenceScreenWithHostClassPOJO createDst() {
        return new PreferenceScreenWithHostClassPOJO(
                2,
                new SearchablePreferenceScreenPOJO(null, null, List.of()),
                SearchablePreferenceScreenGraphDAOTest.TestPreferenceFragment.class);
    }
}