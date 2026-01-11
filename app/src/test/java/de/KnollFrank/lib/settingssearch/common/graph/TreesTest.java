package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraphBuilder;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.test.Matchers;

@SuppressWarnings({"UnstableApiUsage"})
public class TreesTest {

    /*
     * Root
     *   |
     *   |--(edge1)--> Child1
     *   |             |
     *   |             '--(edge3)--> GrandChild1
     *   |
     *   '--(edge2)--> Child2
     */
    private final Tree<String, String> testTree =
            new Tree<>(
                    ValueGraphBuilder
                            .directed()
                            .<String, String>immutable()
                            // FK-TODO: extrahiere Konstanten für die Nodes und Values
                            .putEdgeValue("Root", "Child1", "edge1")
                            .putEdgeValue("Root", "Child2", "edge2")
                            .putEdgeValue("Child1", "GrandChild1", "edge3")
                            .build());

    @Test
    public void getPathFromRootNodeToTarget_shouldReturnCorrectPathToLeaf() {
        // When
        final TreePath<String, String> path =
                Trees.getPathFromRootNodeToTarget(
                        testTree,
                        "GrandChild1");

        // Then
        assertThat(path.nodes(), contains("Root", "Child1", "GrandChild1"));
        assertThat(path.startNode(), is("Root"));
        assertThat(path.endNode(), is("GrandChild1"));
        assertThat(path.tree(), is(testTree));
        Matchers.assertIsSubset(path.nodes(), path.tree().graph().nodes());
    }

    @Test
    public void getEdgesOnPath_shouldReturnCorrectEdges() {
        // Given
        final TreePath<String, String> path =
                Trees.getPathFromRootNodeToTarget(
                        testTree,
                        "GrandChild1");

        // When
        final List<Edge<String, String>> edgesOnPath = Trees.getEdgesOnPath(path);

        // Then
        final List<String> edgeValues =
                edgesOnPath
                        .stream()
                        .map(Edge::edgeValue)
                        .collect(Collectors.toList());
        assertThat(edgesOnPath.size(), is(2));
        assertThat(edgeValues, contains("edge1", "edge3"));
        assertThat(edgesOnPath.get(0).edge(), is(EndpointPair.ordered("Root", "Child1")));
        assertThat(edgesOnPath.get(1).edge(), is(EndpointPair.ordered("Child1", "GrandChild1")));
    }

    @Test
    public void getEdgesOnPath_whenPathIsOnlyRoot_shouldReturnEmptyList() {
        // Given
        final TreePath<String, String> path =
                Trees.getPathFromRootNodeToTarget(
                        testTree,
                        "Root");

        // When
        final List<Edge<String, String>> edgesOnPath = Trees.getEdgesOnPath(path);

        // Then
        assertThat(edgesOnPath, is(empty()));
    }


    @Test
    public void getPathFromRootNodeToTarget_shouldReturnSingleNodePathWhenTargetIsRootNode() {
        // When
        final TreePath<String, String> path =
                Trees.getPathFromRootNodeToTarget(
                        testTree,
                        "Root");

        // Then
        assertThat(path.nodes(), contains("Root"));
    }

    @Test
    public void getPathFromRootNodeToTarget_shouldReturnPathToIntermediateNode() {
        // When
        final TreePath<String, String> path =
                Trees.getPathFromRootNodeToTarget(
                        testTree,
                        "Child1");

        // Assert
        assertThat(path.nodes(), contains("Root", "Child1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPathFromRootNodeToTarget_shouldThrowExceptionWhenNodeIsNotFound() {
        // When
        Trees.getPathFromRootNodeToTarget(testTree, "UnknownNode");
    }
}
