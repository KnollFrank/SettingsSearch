package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class SubtreeTest {

    private final StringNode nA = new StringNode("A");
    private final StringNode nB = new StringNode("B");
    private final StringNode nE = new StringNode("E");

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfSubtreeNodeIsNotInTree() {
        // Given
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, nB, "val")
                                .build());
        final StringNode nodeNotInTree = nE;

        // When
        new Subtree<>(tree, nodeNotInTree);
    }

    @Test
    public void shouldCreateSubtreeNodeIsInTree() {
        // Given
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, nB, "val")
                                .build());
        final StringNode nodeOfTree = tree.graph().nodes().stream().findAny().orElseThrow();

        // When
        new Subtree<>(tree, nodeOfTree);

        // Then no exception is thrown
    }
}