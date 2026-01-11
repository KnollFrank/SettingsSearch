package de.KnollFrank.lib.settingssearch.common.graph;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

@SuppressWarnings({"UnstableApiUsage"})
public class SubtreeAndTreeAtNodeTest {

    @Test
    public void subtreeConstructor_shouldThrowIllegalArgumentException_ifRootNodeIsNotInTree() {
        // Given
        // Graph structure: [ v1 ]
        final Tree<StringVertex, String> tree =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .addNode(new StringVertex("v1"))
                                .build());
        final StringVertex vNotInGraph = new StringVertex("not-in-graph");

        // Then
        assertThrows(
                IllegalArgumentException.class,
                () -> new Subtree<>(tree, vNotInGraph));
    }

    @Test
    public void graphAtNodeConstructor_shouldThrowIllegalArgumentException_ifNodeIsNotInGraph() {
        // Given
        // Graph structure: [ v1 ]
        final Tree<StringVertex, String> graph =
                new Tree<>(
                        StringGraphs
                        .newStringGraphBuilder()
                        .addNode(new StringVertex("v1"))
                        .build());
        final StringVertex vNotInGraph = new StringVertex("not-in-graph");

        // Then
        assertThrows(
                IllegalArgumentException.class,
                () -> new TreeAtNode<>(graph, vNotInGraph));
    }
}
