package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTestFactory.createScreen;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory.createPreference;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabaseTest;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceTest extends PreferencesRoomDatabaseTest {

    @Test
    public void shouldGetPreferencePath() {
        // Given
        final SearchablePreference predecessor = createPreference("predecessor");
        final SearchablePreference parent = createPreference("parent");
        final DefaultDirectedGraph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph =
                SearchablePreferenceScreenGraphTestFactory
                        .createGraphBuilder()
                        .addEdge(createScreen(predecessor), createScreen(parent), new SearchablePreferenceEdge(predecessor))
                        .build();
        final SearchablePreferenceWithinGraph _predecessor = new SearchablePreferenceWithinGraph(predecessor, graph);
        final SearchablePreferenceWithinGraph _parent = new SearchablePreferenceWithinGraph(parent, graph);

        // When
        final PreferencePath preferencePath = _parent.getPreferencePath();

        // Then
        assertThat(preferencePath, is(new PreferencePath(List.of(_predecessor, _parent))));
    }
}
