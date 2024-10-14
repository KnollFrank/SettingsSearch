package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.getFragments;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAOTest.createSomePreferenceScreenGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAOTest.getAddPreferences2Screen;

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

    // FK-TODO: DRY with SearchablePreferenceScreenGraphDAOTest
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
                final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph = Graph2POJOGraphTransformer.transformGraph2POJOGraph(entityGraph);

                // Then
                assertThat(pojoGraph, is(createPojoGraph(preferenceFragment.getClass())));
            });
        }
    }

    private static Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> createPojoGraph(final Class<? extends PreferenceFragmentCompat> host) {
        final SearchablePreferencePOJO preferenceConnectingSrc2Dst =
                new SearchablePreferencePOJO(
                        null,
                        0,
                        2131427444,
                        null,
                        "preference connected to TestPreferenceFragment",
                        0,
                        SearchablePreferenceScreenGraphDAOTest.TestPreferenceFragment.class.getName(),
                        true,
                        null,
                        new Bundle(),
                        List.of(),
                        Optional.empty());
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
                                new SearchablePreferencePOJO(
                                        "parentKey",
                                        0,
                                        15,
                                        null,
                                        null,
                                        0,
                                        null,
                                        true,
                                        null,
                                        new Bundle(),
                                        List.of(
                                                new SearchablePreferencePOJO(
                                                        null,
                                                        0,
                                                        16,
                                                        null,
                                                        null,
                                                        0,
                                                        null,
                                                        true,
                                                        null,
                                                        new Bundle(),
                                                        List.of(),
                                                        Optional.empty()),
                                                new SearchablePreferencePOJO(
                                                        null,
                                                        0,
                                                        16,
                                                        null,
                                                        null,
                                                        0,
                                                        null,
                                                        true,
                                                        null,
                                                        new Bundle(),
                                                        List.of(),
                                                        Optional.empty())),
                                        Optional.empty()),
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