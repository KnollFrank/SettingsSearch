package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class SubtreeReplacerTest {

    private final StringVertex vR = new StringVertex("R");
    private final StringVertex vA = new StringVertex("A");
    private final StringVertex vB = new StringVertex("B");
    private final StringVertex vX = new StringVertex("X");
    private final StringVertex vY = new StringVertex("Y");
    private final StringVertex vZ = new StringVertex("Z");
    private final StringVertex vP = new StringVertex("P");

    private final String eRA = "R->A";
    private final String eRB = "R->B";
    private final String eXY = "X->Y";
    private final String eXZ = "X->Z";
    private final String ePR = "P->R";

    @Test
    public void replaceSubtree_nodeToReplaceIsRoot_replacesEntireGraph() {
        // Given
        // Original Tree: R -> A (eRA)
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> originalTree =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vR, vA, eRA)
                                .build());
        // Replacement Tree: X -> Y (eXY)
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> replacementTree =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vX, vY, eXY)
                                .build());
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> expectedReturnedGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vX, vY, eXY)
                                .build());

        // When
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> returnedGraph =
                SubtreeReplacer.replaceSubtreeWithTree(
                        new Subtree<>(originalTree, vR),
                        replacementTree);

        // Then
        assertThat(returnedGraph.graph(), is(expectedReturnedGraph.graph()));
    }

    @Test
    public void replaceSubtree_nodeToReplaceIsChild() {
        // Given
        // Original Tree:
        //   P --ePR--> R --eRA--> A
        //              |
        //              --eRB--> B
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> originalGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vP, vR, ePR)
                                .putEdgeValue(vR, vA, eRA)
                                .putEdgeValue(vR, vB, eRB)
                                .build());
        // Replacement Tree (to replace R and its subtree [A,B]):
        //   X --eXY--> Y
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> replacementGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vX, vY, eXY)
                                .build());
        // Expected Returned Tree:
        //   P --(label from ePR)--> X --> Y
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> expectedReturnedGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vP, vX, ePR)
                                .putEdgeValue(vX, vY, eXY)
                                .build());

        // When
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> returnedGraph =
                SubtreeReplacer.replaceSubtreeWithTree(
                        new Subtree<>(originalGraph, vR),
                        replacementGraph);

        // Then
        assertThat(returnedGraph.graph(), is(expectedReturnedGraph.graph()));
    }

    @Test
    public void replaceSubtree_nodeToReplaceIsLeaf() {
        // Given
        // Original Tree: P --ePR--> R
        // Node to replace: R (a leaf)
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> originalGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vP, vR, ePR)
                                .build());
        // Replacement Tree: X (single node, root X)
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> replacementGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .addNode(vX)
                                .build());
        // Expected Returned Tree: P --(label from ePR)--> X
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> expectedReturnedGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vP, vX, ePR)
                                .build());

        // When
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> returnedGraph =
                SubtreeReplacer.replaceSubtreeWithTree(
                        new Subtree<>(originalGraph, vR),
                        replacementGraph);

        // Then
        assertThat(returnedGraph.graph(), is(expectedReturnedGraph.graph()));
    }

    @Test
    public void replaceSubtree_complexScenarioWithDeeperSubtree() {
        // Given
        // Original Tree:
        //      P
        //      | (ePR P->R)
        //      R
        //     /  \ (eRA R->A, eRB R->B)
        //    A    B
        //   / (eAC_local A->C_local)
        //  C_local
        // Node to replace: R
        final StringVertex vC_local = new StringVertex("C_local");
        final String eAC_local = "A->C_local";
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> originalGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vP, vR, ePR)
                                .putEdgeValue(vR, vA, eRA)
                                .putEdgeValue(vR, vB, eRB)
                                .putEdgeValue(vA, vC_local, eAC_local)
                                .build());
        // Replacement Tree:
        //   X --eXY--> Y
        //   |
        //   --eXZ--> Z
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> replacementGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vX, vY, eXY)
                                .putEdgeValue(vX, vZ, eXZ)
                                .build());
        // Expected Returned Tree:
        //      P
        //      | (label from ePR, P->X)
        //      X
        //     /  \ (labels from eXY X->Y, eXZ X->Z)
        //    Y    Z
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> expectedReturnedGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vP, vX, ePR)
                                .putEdgeValue(vX, vY, eXY)
                                .putEdgeValue(vX, vZ, eXZ)
                                .build());

        // When
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> returnedGraph =
                SubtreeReplacer.replaceSubtreeWithTree(
                        new Subtree<>(originalGraph, vR),
                        replacementGraph);

        // Then
        assertThat(returnedGraph.graph(), is(expectedReturnedGraph.graph()));
    }

    @Test
    public void replaceSubtree_withOverlappingReplacementTree() {
        // Given
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> originalTree =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vR, vA, eRA)
                                .putEdgeValue(vR, vB, eRB)
                                .build());

        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> replacementTreeWithOverlap =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vX, vB, "X->B")
                                .build());

        // When
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> returnedGraph =
                SubtreeReplacer.replaceSubtreeWithTree(
                        new Subtree<>(originalTree, vA),
                        replacementTreeWithOverlap);

        // Then
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> expectedGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vR, vX, eRA)
                                .putEdgeValue(vR, vB, eRB)
                                .build());

        assertThat(returnedGraph.graph(), is(expectedGraph.graph()));
    }
}