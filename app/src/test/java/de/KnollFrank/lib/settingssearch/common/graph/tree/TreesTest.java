package de.KnollFrank.lib.settingssearch.common.graph.tree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.common.graph.tree.TreeTestFactory.CHILD_1;
import static de.KnollFrank.lib.settingssearch.common.graph.tree.TreeTestFactory.EDGE_1;
import static de.KnollFrank.lib.settingssearch.common.graph.tree.TreeTestFactory.EDGE_3;
import static de.KnollFrank.lib.settingssearch.common.graph.tree.TreeTestFactory.GRAND_CHILD_1;
import static de.KnollFrank.lib.settingssearch.common.graph.tree.TreeTestFactory.ROOT;
import static de.KnollFrank.lib.settingssearch.common.graph.tree.TreeTestFactory.testTree;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.graph.Edge;
import de.KnollFrank.lib.settingssearch.common.graph.TreePath;
import de.KnollFrank.lib.settingssearch.common.graph.Trees;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreesTest {

    @Test
    public void getEdgesOnPath_shouldReturnCorrectEdges() {
        // Given
        final TreePath<String, String, ImmutableValueGraph<String, String>> path =
                testTree.getPathFromRootNodeToTarget(GRAND_CHILD_1);

        // When
        final List<Edge<String, String>> edgesOnPath = Trees.getEdgesOnPath(path);

        // Then
        final List<String> edgeValues =
                edgesOnPath
                        .stream()
                        .map(Edge::value)
                        .collect(Collectors.toList());
        assertThat(edgesOnPath.size(), is(2));
        assertThat(edgeValues, contains(EDGE_1, EDGE_3));
        assertThat(edgesOnPath.get(0).endpointPair(), is(EndpointPair.ordered(ROOT, CHILD_1)));
        assertThat(edgesOnPath.get(1).endpointPair(), is(EndpointPair.ordered(CHILD_1, GRAND_CHILD_1)));
    }

    @Test
    public void getEdgesOnPath_whenPathIsOnlyRoot_shouldReturnEmptyList() {
        // Given
        final TreePath<String, String, ImmutableValueGraph<String, String>> path =
                testTree.getPathFromRootNodeToTarget(ROOT);

        // When
        final List<Edge<String, String>> edgesOnPath = Trees.getEdgesOnPath(path);

        // Then
        assertThat(edgesOnPath, is(empty()));
    }
}
