package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class POJOGraph2JSONConverterTest {

    @Test
    public void shouldTransformGraph2POJOGraph_FragmentWith2Connections() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> graph = createPojoGraph();
                final Writer writer = new StringWriter();

                // When
                POJOGraph2JSONConverter.pojoGraph2JSON(graph, writer);
                final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> graphActual =
                        JSON2POJOGraphConverter.json2PojoGraph(writer2Reader(writer));

                // Then
                assertThat(graphActual.vertexSet().size(), is(3));
                assertThat(graphActual.edgeSet().size(), is(2));
            });
        }
    }

    private static Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> createPojoGraph() {
        final SearchablePreferencePOJO preferenceConnectingSrc2Dst = getSearchablePreferencePOJO();
        final PreferenceScreenWithHostClassPOJO node = createNode(1);
        return DefaultDirectedGraph
                .<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge>createBuilder(SearchablePreferencePOJOEdge.class)
                .addEdge(node, createNode(2), new SearchablePreferencePOJOEdge(preferenceConnectingSrc2Dst))
                .addEdge(node, createNode(3), new SearchablePreferencePOJOEdge(preferenceConnectingSrc2Dst))
                .build();
    }

    private static SearchablePreferencePOJO getSearchablePreferencePOJO() {
        return new SearchablePreferencePOJO(
                1,
                "someKey",
                null,
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
    }

    private static PreferenceScreenWithHostClassPOJO createNode(final int id) {
        return new PreferenceScreenWithHostClassPOJO(
                id,
                new SearchablePreferenceScreenPOJO(null, null, List.of()),
                PreferenceFragmentCompat.class);
    }

    private static Reader writer2Reader(final Writer writer) {
        return new StringReader(writer.toString());
    }
}