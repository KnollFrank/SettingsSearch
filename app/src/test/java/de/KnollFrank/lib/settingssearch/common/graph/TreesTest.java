package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.matcher.ViewMatchers;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreesTest {

    private static final String ROOT = "Root";
    private static final String CHILD_1 = "Child1";
    private static final String CHILD_2 = "Child2";
    private static final String GRAND_CHILD_1 = "GrandChild1";
    private static final String EDGE_1 = "edge1";
    private static final String EDGE_2 = "edge2";
    private static final String EDGE_3 = "edge3";
    /*
     * Root
     *   |
     *   |--(edge1)--> Child1
     *   |             |
     *   |             '--(edge3)--> GrandChild1
     *   |
     *   '--(edge2)--> Child2
     */
    private static final Tree<String, String, ImmutableValueGraph<String, String>> testTree =
            new Tree<>(
                    Graphs
                            .<String, String>directedImmutableValueGraphBuilder()
                            .putEdgeValue(ROOT, CHILD_1, EDGE_1)
                            .putEdgeValue(ROOT, CHILD_2, EDGE_2)
                            .putEdgeValue(CHILD_1, GRAND_CHILD_1, EDGE_3)
                            .build());

    // FK-TODO: move all getPathFromRootNodeToTarget-Testmethods to TreeTest
    @Test
    public void getPathFromRootNodeToTarget_shouldReturnCorrectPathToLeaf() {
        // When
        final TreePath<String, String, ImmutableValueGraph<String, String>> path =
                testTree.getPathFromRootNodeToTarget(GRAND_CHILD_1);

        // Then
        assertThat(path.nodes(), contains(ROOT, CHILD_1, GRAND_CHILD_1));
        assertThat(path.startNode(), is(ROOT));
        assertThat(path.endNode(), is(GRAND_CHILD_1));
        assertThat(path.tree(), is(testTree));
        ViewMatchers.assertIsSubset(path.nodes(), path.tree().graph().nodes());
    }

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

    @Test
    public void getPathFromRootNodeToTarget_shouldReturnSingleNodePathWhenTargetIsRootNode() {
        // When
        final TreePath<String, String, ImmutableValueGraph<String, String>> path =
                testTree.getPathFromRootNodeToTarget(ROOT);

        // Then
        assertThat(path.nodes(), contains(ROOT));
    }

    @Test
    public void getPathFromRootNodeToTarget_shouldReturnPathToIntermediateNode() {
        // When
        final TreePath<String, String, ImmutableValueGraph<String, String>> path =
                testTree.getPathFromRootNodeToTarget(CHILD_1);

        // Assert
        assertThat(path.nodes(), contains(ROOT, CHILD_1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPathFromRootNodeToTarget_shouldThrowExceptionWhenNodeIsNotFound() {
        // When
        testTree.getPathFromRootNodeToTarget("UnknownNode");
    }
}
