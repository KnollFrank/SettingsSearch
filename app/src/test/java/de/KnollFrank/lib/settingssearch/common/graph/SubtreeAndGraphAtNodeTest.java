package de.KnollFrank.lib.settingssearch.common.graph;

import static org.junit.Assert.assertThrows;

import org.jgrapht.Graph;
import org.junit.Test;

public class SubtreeAndGraphAtNodeTest {

    @Test
    public void subtreeConstructor_shouldThrowIllegalArgumentException_ifRootNodeIsNotInTree() {
        // Given
        // Graph structure: [ v1 ]
        final UnmodifiableTree<StringVertex, StringEdge> tree =
                UnmodifiableTree.of(
                        StringGraphs
                                .newGraphBuilder()
                                .addVertex(new StringVertex("v1"))
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
        final Graph<StringVertex, StringEdge> graph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertex(new StringVertex("v1"))
                        .build();
        final StringVertex vNotInGraph = new StringVertex("not-in-graph");

        // Then
        assertThrows(
                IllegalArgumentException.class,
                () -> new GraphAtNode<>(graph, vNotInGraph));
    }
}
