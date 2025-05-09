package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.graph.Graph2POJOGraphTransformerTest.createPojoGraph;

import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.Iterables;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import de.KnollFrank.lib.settingssearch.common.graph.GraphUtils;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenDAOTest extends AppDatabaseTest {

    @Test
    public void shouldGetChildrenOfPersistedSearchablePreferenceScreen() {
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
    }
}
