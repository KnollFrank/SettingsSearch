package de.KnollFrank.lib.settingssearch.common.graph;

import static de.KnollFrank.lib.settingssearch.common.graph.StringGraphEquality.assertActualEqualsExpected;
import static de.KnollFrank.lib.settingssearch.common.graph.StringGraphs.cloneEdge;

import org.junit.Test;

public class SubtreeReplacerTest {

    private final StringVertex vR = new StringVertex("R");
    private final StringVertex vA = new StringVertex("A");
    private final StringVertex vB = new StringVertex("B");
    private final StringVertex vX = new StringVertex("X");
    private final StringVertex vY = new StringVertex("Y");
    private final StringVertex vZ = new StringVertex("Z");
    private final StringVertex vP = new StringVertex("P");

    private final StringEdge eRA = new StringEdge("R->A");
    private final StringEdge eRB = new StringEdge("R->B");
    private final StringEdge eXY = new StringEdge("X->Y");
    private final StringEdge eXZ = new StringEdge("X->Z");
    private final StringEdge ePR = new StringEdge("P->R");

    @Test
    public void replaceSubtree_nodeToReplaceIsRoot_replacesEntireGraph() {
        // Given
        final SubtreeReplacer<StringVertex, StringEdge> subtreeReplacer = createSubtreeReplacer();
        // Original Tree: R -> A (eRA)
        final UnmodifiableTree<StringVertex, StringEdge> originalGraph =
                UnmodifiableTree.of(
                        StringGraphs
                                .newGraphBuilder()
                                .addVertices(vR, vA)
                                .addEdge(vR, vA, eRA)
                                .build());
        // Replacement Tree: X -> Y (eXY)
        final UnmodifiableTree<StringVertex, StringEdge> replacementTree =
                UnmodifiableTree.of(
                        StringGraphs
                                .newGraphBuilder()
                                .addVertices(vX, vY)
                                .addEdge(vX, vY, eXY)
                                .build());
        final UnmodifiableTree<StringVertex, StringEdge> expectedReturnedGraph =
                UnmodifiableTree.of(
                        StringGraphs
                                .newGraphBuilder()
                                .addVertices(vX, vY)
                                .addEdge(vX, vY, cloneEdge(eXY))
                                .build());

        // When
        final UnmodifiableTree<StringVertex, StringEdge> returnedGraph =
                subtreeReplacer.replaceSubtreeWithTree(
                        new Subtree<>(originalGraph, vR),
                        replacementTree);

        // Then
        assertActualEqualsExpected(returnedGraph.graph(), expectedReturnedGraph.graph());
    }

    @Test
    public void replaceSubtree_nodeToReplaceIsChild() {
        // Given
        final SubtreeReplacer<StringVertex, StringEdge> subtreeReplacer = createSubtreeReplacer();
        // Original Tree:
        //   P --ePR--> R --eRA--> A
        //              |
        //              --eRB--> B
        final UnmodifiableTree<StringVertex, StringEdge> originalGraph =
                UnmodifiableTree.of(
                        StringGraphs
                                .newGraphBuilder()
                                .addVertices(vP, vR, vA, vB)
                                .addEdge(vP, vR, ePR)
                                .addEdge(vR, vA, eRA)
                                .addEdge(vR, vB, eRB)
                                .build());
        // Replacement Tree (to replace R and its subtree [A,B]):
        //   X --eXY--> Y
        final UnmodifiableTree<StringVertex, StringEdge> replacementGraph =
                UnmodifiableTree.of(
                        StringGraphs
                                .newGraphBuilder()
                                .addVertices(vX, vY)
                                .addEdge(vX, vY, eXY)
                                .build());
        // Expected Returned Tree:
        //   P --(label from ePR)--> X --> Y
        final UnmodifiableTree<StringVertex, StringEdge> expectedReturnedGraph =
                UnmodifiableTree.of(
                        StringGraphs
                                .newGraphBuilder()
                                .addVertices(vP, vX, vY)
                                .addEdge(vP, vX, cloneEdge(ePR))
                                .addEdge(vX, vY, cloneEdge(eXY))
                                .build());

        // When
        final UnmodifiableTree<StringVertex, StringEdge> returnedGraph =
                subtreeReplacer.replaceSubtreeWithTree(
                        new Subtree<>(originalGraph, vR),
                        replacementGraph);

        // Then
        assertActualEqualsExpected(returnedGraph.graph(), expectedReturnedGraph.graph());
    }

    @Test
    public void replaceSubtree_nodeToReplaceIsLeaf() {
        // Given
        final SubtreeReplacer<StringVertex, StringEdge> subtreeReplacer = createSubtreeReplacer();
        // Original Tree: P --ePR--> R
        // Node to replace: R (a leaf)
        final UnmodifiableTree<StringVertex, StringEdge> originalGraph =
                UnmodifiableTree.of(
                        StringGraphs
                                .newGraphBuilder()
                                .addVertices(vP, vR)
                                .addEdge(vP, vR, ePR)
                                .build());
        // Replacement Tree: X (single node, root X)
        final UnmodifiableTree<StringVertex, StringEdge> replacementGraph =
                UnmodifiableTree.of(
                        StringGraphs
                                .newGraphBuilder()
                                .addVertex(vX)
                                .build());
        // Expected Returned Tree: P --(label from ePR)--> X
        final UnmodifiableTree<StringVertex, StringEdge> expectedReturnedGraph =
                UnmodifiableTree.of(
                        StringGraphs
                                .newGraphBuilder()
                                .addVertices(vP, vX)
                                .addEdge(vP, vX, cloneEdge(ePR))
                                .build());

        // When
        final UnmodifiableTree<StringVertex, StringEdge> returnedGraph =
                subtreeReplacer.replaceSubtreeWithTree(
                        new Subtree<>(originalGraph, vR),
                        replacementGraph);

        // Then
        assertActualEqualsExpected(returnedGraph.graph(), expectedReturnedGraph.graph());
    }

    @Test
    public void replaceSubtree_complexScenarioWithDeeperSubtree() {
        // Given
        final SubtreeReplacer<StringVertex, StringEdge> subtreeReplacer = createSubtreeReplacer();
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
        final StringEdge eAC_local = new StringEdge("A->C_local");
        final UnmodifiableTree<StringVertex, StringEdge> originalGraph =
                UnmodifiableTree.of(
                        StringGraphs
                                .newGraphBuilder()
                                .addVertices(vP, vR, vA, vB, vC_local)
                                .addEdge(vP, vR, ePR)
                                .addEdge(vR, vA, eRA)
                                .addEdge(vR, vB, eRB)
                                .addEdge(vA, vC_local, eAC_local)
                                .build());
        // Replacement Tree:
        //   X --eXY--> Y
        //   |
        //   --eXZ--> Z
        final UnmodifiableTree<StringVertex, StringEdge> replacementGraph =
                UnmodifiableTree.of(
                        StringGraphs
                                .newGraphBuilder()
                                .addVertices(vX, vY, vZ)
                                .addEdge(vX, vY, eXY)
                                .addEdge(vX, vZ, eXZ)
                                .build());
        // Expected Returned Tree:
        //      P
        //      | (label from ePR, P->X)
        //      X
        //     /  \ (labels from eXY X->Y, eXZ X->Z)
        //    Y    Z
        final UnmodifiableTree<StringVertex, StringEdge> expectedReturnedGraph =
                UnmodifiableTree.of(
                        StringGraphs
                                .newGraphBuilder()
                                .addVertices(vP, vX, vY, vZ)
                                .addEdge(vP, vX, cloneEdge(ePR)) // Edge factory reuses ePR label
                                .addEdge(vX, vY, cloneEdge(eXY)) // Edges from replacement
                                .addEdge(vX, vZ, cloneEdge(eXZ)) // Edges from replacement
                                .build());

        // When
        final UnmodifiableTree<StringVertex, StringEdge> returnedGraph =
                subtreeReplacer.replaceSubtreeWithTree(
                        new Subtree<>(originalGraph, vR),
                        replacementGraph);

        // Then
        assertActualEqualsExpected(returnedGraph.graph(), expectedReturnedGraph.graph());
    }

    private SubtreeReplacer<StringVertex, StringEdge> createSubtreeReplacer() {
        return new SubtreeReplacer<>(
                StringGraphs::createEmptyGraph,
                StringGraphs::cloneEdge);
    }
}