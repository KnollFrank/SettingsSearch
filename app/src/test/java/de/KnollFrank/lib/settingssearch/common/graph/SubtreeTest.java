package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.junit.Test;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class SubtreeTest {

    private final StringVertex vA = new StringVertex("A");
    private final StringVertex vB = new StringVertex("B");
    private final StringVertex vE = new StringVertex("E");

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfSubtreeNodeIsNotInTree() {
        // Given
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> tree =
                new Tree<>(
                        ValueGraphBuilder
                                .directed()
                                .<StringVertex, String>immutable()
                                .putEdgeValue(vA, vB, "val")
                                .build());
        final StringVertex nodeNotInTree = vE;

        // When
        new Subtree<>(tree, nodeNotInTree);
    }

    @Test
    public void shouldCreateSubtreeNodeIsInTree() {
        // Given
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> tree =
                new Tree<>(
                        ValueGraphBuilder
                                .directed()
                                .<StringVertex, String>immutable()
                                .putEdgeValue(vA, vB, "val")
                                .build());
        final StringVertex nodeOfTree = tree.graph().nodes().stream().findAny().orElseThrow();

        // When
        new Subtree<>(tree, nodeOfTree);

        // Then no exception is thrown
    }
}