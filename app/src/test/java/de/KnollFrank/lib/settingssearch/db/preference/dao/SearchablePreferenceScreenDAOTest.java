package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenDAOTest extends AppDatabaseTest {

    @Test
    public void shouldFindSearchablePreferenceScreenById() {
        fail("not yet implemented");
/*
        // Given
        final SearchablePreferenceScreenDAO dao = appDatabase.searchablePreferenceScreenDAO();
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph = createPojoGraph(PreferenceFragmentCompat.class);
        final SearchablePreferenceScreen parent = GraphUtils.getRootNode(graph);
        final SearchablePreferenceScreen child = graph.getEdgeTarget(Iterables.getOnlyElement(graph.outgoingEdgesOf(parent)));
        dao.persist(List.of(parent, child));

        // When
        final List<SearchablePreferenceScreen> childrenOfParent = parent.getChildren();

        // Then
        assertThat(childrenOfParent, contains(child));

        // When
        final List<SearchablePreferenceScreen> childrenOfChild = child.getChildren();

        // Then
        assertThat(childrenOfChild, is(empty()));
*/
    }
}
