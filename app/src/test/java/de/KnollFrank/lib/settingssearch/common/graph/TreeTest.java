package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;

import org.jgrapht.Graph;
import org.junit.Test;

public class TreeTest {

    private final StringVertex vR = new StringVertex("R");
    private final StringVertex vA = new StringVertex("A");
    private final StringVertex vB = new StringVertex("B");

    @Test
    public void shouldCreateValidTree() {
        // Given
        // [R] -> [A]
        //  |
        //  v
        // [B]
        final Graph<StringVertex, StringEdge> graph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vR, vA, vB)
                        .addEdge(vR, vA, new StringEdge("R->A"))
                        .addEdge(vR, vB, new StringEdge("R->B"))
                        .build();

        // When
        final Tree<StringVertex, StringEdge> tree = new Tree<>(graph);

        // Then
        assertThat(tree.getRootNode(), is(vR));
        assertThat(tree.graph(), is(graph));
    }

    @Test
    public void shouldThrowExceptionIfGraphHasCycle() {
        // Given
        // [A] -> [B] -> [A]
        final Graph<StringVertex, StringEdge> graph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vA, vB)
                        .addEdge(vA, vB, new StringEdge("A->B"))
                        .addEdge(vB, vA, new StringEdge("B->A"))
                        .build();

        // Then
        assertThrows(
                IllegalArgumentException.class,
                () -> new Tree<>(graph));
    }

    @Test
    public void shouldThrowExceptionIfNodeHasMultipleParents() {
        // Given
        // [R] -> [A]
        // [B] -> [A]
        final Graph<StringVertex, StringEdge> graph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vR, vB, vA)
                        .addEdge(vR, vA, new StringEdge("R->A"))
                        .addEdge(vB, vA, new StringEdge("B->A"))
                        .build();

        // Then
        assertThrows(
                IllegalArgumentException.class,
                () -> new Tree<>(graph));
    }

    @Test
    public void shouldThrowExceptionIfGraphHasMultipleRoots() {
        // Given
        // [R]  [B] (two isolated nodes)
        final Graph<StringVertex, StringEdge> graph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vR, vB)
                        .build();

        // Then
        assertThrows(
                IllegalArgumentException.class,
                () -> new Tree<>(graph));
    }

    @Test
    public void shouldThrowExceptionIfGraphIsEmpty() {
        // Given
        final Graph<StringVertex, StringEdge> emptyGraph =
                StringGraphs
                        .newGraphBuilder()
                        .build();

        // Then
        assertThrows(
                IllegalArgumentException.class,
                () -> new Tree<>(emptyGraph));
    }
}
