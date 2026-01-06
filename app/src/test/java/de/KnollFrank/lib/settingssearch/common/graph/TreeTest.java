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
    private final StringVertex vC = new StringVertex("C");

    private final StringEdge eRA = new StringEdge("R->A");
    private final StringEdge eRB = new StringEdge("R->B");
    private final StringEdge eAB = new StringEdge("A->B");
    private final StringEdge eBC = new StringEdge("B->C");
    private final StringEdge eCA = new StringEdge("C->A");

    @Test
    public void shouldCreateValidTree() {
        // Given
        // Graph structure:
        //      R
        //     / \
        //    A   B
        final Graph<StringVertex, StringEdge> validTreeGraph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vR, vA, vB)
                        .addEdge(vR, vA, eRA)
                        .addEdge(vR, vB, eRB)
                        .build();

        // When
        final Tree<StringVertex, StringEdge> tree = new Tree<>(validTreeGraph);

        // Then
        assertThat(tree.getRoot(), is(vR));
        assertThat(tree.graph(), is(validTreeGraph));
    }

    @Test
    public void shouldThrowExceptionIfGraphHasCycle() {
        // Given
        // Graph structure (Cycle):
        //    A -> B -> C -> A
        final Graph<StringVertex, StringEdge> cyclicGraph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vA, vB, vC)
                        .addEdge(vA, vB, eAB)
                        .addEdge(vB, vC, eBC)
                        .addEdge(vC, vA, eCA)
                        .build();

        // Then
        // Should throw IllegalArgumentException due to cycle detection
        assertThrows(
                IllegalArgumentException.class,
                () -> new Tree<>(cyclicGraph));
    }

    @Test
    public void shouldThrowExceptionIfNodeHasTwoParents() {
        // Given
        // Graph structure (Diamond/DAG, not a tree):
        //      R
        //     / \
        //    A   B
        //     \ /
        //      C
        final StringVertex vTarget = new StringVertex("C");
        final Graph<StringVertex, StringEdge> dagGraph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vR, vA, vB, vTarget)
                        .addEdge(vR, vA, eRA)
                        .addEdge(vR, vB, eRB)
                        .addEdge(vA, vTarget, new StringEdge("A->C"))
                        .addEdge(vB, vTarget, new StringEdge("B->C"))
                        .build();

        // Then
        // Should throw IllegalArgumentException because C has in-degree 2
        assertThrows(
                IllegalArgumentException.class,
                () -> new Tree<>(dagGraph));
    }

    @Test
    public void shouldThrowExceptionIfGraphHasMultipleRoots() {
        // Given
        // Graph structure (Forest/Disconnected):
        //    R1 -> A      R2 -> B
        final StringVertex vR1 = new StringVertex("R1");
        final StringVertex vR2 = new StringVertex("R2");
        final Graph<StringVertex, StringEdge> forestGraph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vR1, vA, vR2, vB)
                        .addEdge(vR1, vA, new StringEdge("R1->A"))
                        .addEdge(vR2, vB, new StringEdge("R2->B"))
                        .build();

        // Then
        // Should throw IllegalArgumentException because there are 2 roots
        assertThrows(
                IllegalArgumentException.class,
                () -> new Tree<>(forestGraph));
    }

    @Test
    public void shouldThrowExceptionIfGraphIsEmpty() {
        // Given
        // Empty graph
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
