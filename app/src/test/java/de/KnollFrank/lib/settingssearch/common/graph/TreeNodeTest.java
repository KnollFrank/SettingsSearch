package de.KnollFrank.lib.settingssearch.common.graph;

import static org.junit.Assert.assertThrows;

import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeNodeTest {

    @Test
    public void treeNodeConstructor_shouldThrowIllegalArgumentException_ifNodeIsNotInGraph() {
        // Given
        // Graph structure: [ v1 ]
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> graph =
                new Tree<>(
                        StringGraphs
                        .newStringGraphBuilder()
                        .addNode(new StringNode("v1"))
                        .build());
        final StringNode nodeNotInGraph = new StringNode("not-in-graph");

        // Then
        assertThrows(
                IllegalArgumentException.class,
                () -> new TreeNode<>(nodeNotInGraph, graph));
    }
}
