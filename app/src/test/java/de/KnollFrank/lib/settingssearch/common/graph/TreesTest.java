package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import com.google.common.graph.ValueGraphBuilder;

import org.junit.Test;

import de.KnollFrank.lib.settingssearch.test.Matchers;

@SuppressWarnings({"UnstableApiUsage"})
public class TreesTest {

    /**
     * Root
     * ├── Child1
     * │   └── GrandChild1
     * └── Child2
     */
    private final Tree<String, String> testTree =
            new Tree<>(
                    ValueGraphBuilder
                            .directed()
                            .<String, String>immutable()
                            .putEdgeValue("Root", "Child1", "edge1")
                            .putEdgeValue("Root", "Child2", "edge2")
                            .putEdgeValue("Child1", "GrandChild1", "edge3")
                            .build());

    @Test
    public void shouldReturnCorrectPathToLeaf() {
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
    public void shouldReturnSingleNodePathWhenTargetIsRootNode() {
        // When
        final TreePath<String, String> path =
                Trees.getPathFromRootNodeToTarget(
                        testTree,
                        "Root");

        // Then
        assertThat(path.nodes(), contains("Root"));
    }

    @Test
    public void shouldReturnPathToIntermediateNode() {
        // When
        final TreePath<String, String> path =
                Trees.getPathFromRootNodeToTarget(
                        testTree,
                        "Child1");

        // Assert
        assertThat(path.nodes(), contains("Root", "Child1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNodeIsNotFound() {
        // When
        Trees.getPathFromRootNodeToTarget(testTree, "UnknownNode");
    }
}