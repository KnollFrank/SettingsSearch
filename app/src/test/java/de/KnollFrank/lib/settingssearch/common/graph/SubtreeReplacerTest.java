package de.KnollFrank.lib.settingssearch.common.graph;

import static de.KnollFrank.lib.settingssearch.common.graph.GraphEquality.assertActualEqualsExpected;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;
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
        final Graph<StringVertex, StringEdge> originalGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vR, vA)
                        .addEdge(vR, vA, eRA)
                        .build();
        // Replacement Tree: X -> Y (eXY)
        final Graph<StringVertex, StringEdge> replacementGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vX, vY)
                        .addEdge(vX, vY, eXY)
                        .build();
        final Graph<StringVertex, StringEdge> expectedReturnedGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vX, vY)
                        .addEdge(vX, vY, cloneEdge(eXY))
                        .build();

        // When
        final Graph<StringVertex, StringEdge> returnedGraph =
                subtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        vR,
                        replacementGraph);

        // Then
        assertActualEqualsExpected(returnedGraph, expectedReturnedGraph);
    }

    @Test
    public void replaceSubtree_nodeToReplaceIsChild() {
        // Given
        final SubtreeReplacer<StringVertex, StringEdge> subtreeReplacer = createSubtreeReplacer();
        // Original Tree:
        //   P --ePR--> R --eRA--> A
        //              |
        //              --eRB--> B
        final Graph<StringVertex, StringEdge> originalGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vP, vR, vA, vB)
                        .addEdge(vP, vR, ePR)
                        .addEdge(vR, vA, eRA)
                        .addEdge(vR, vB, eRB)
                        .build();
        // Replacement Tree (to replace R and its subtree [A,B]):
        //   X --eXY--> Y
        final Graph<StringVertex, StringEdge> replacementGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vX, vY)
                        .addEdge(vX, vY, eXY)
                        .build();
        // Expected Returned Tree:
        //   P --(label from ePR)--> X --> Y
        final Graph<StringVertex, StringEdge> expectedReturnedGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vP, vX, vY)
                        .addEdge(vP, vX, cloneEdge(ePR))
                        .addEdge(vX, vY, cloneEdge(eXY))
                        .build();

        // When
        final Graph<StringVertex, StringEdge> returnedGraph =
                subtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        vR,
                        replacementGraph);

        // Then
        assertActualEqualsExpected(returnedGraph, expectedReturnedGraph);
    }

    @Test
    public void replaceSubtree_emptyReplacement_removesSubtree() {
        // Given
        final SubtreeReplacer<StringVertex, StringEdge> subtreeReplacer = createSubtreeReplacer();
        // Original Tree:
        //   P --ePR--> R --eRA--> A
        // Node to replace: R (and its subtree [A])
        final Graph<StringVertex, StringEdge> originalGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vP, vR, vA)
                        .addEdge(vP, vR, ePR)
                        .addEdge(vR, vA, eRA)
                        .build();
        // Replacement Tree: (empty)
        final Graph<StringVertex, StringEdge> emptyReplacementGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .build();
        // Expected Returned Tree:
        //   P
        // (R and A and edges ePR, eRA are removed; P remains, R's subtree is pruned)
        final Graph<StringVertex, StringEdge> expectedReturnedGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertex(vP)
                        .build();

        // When
        final Graph<StringVertex, StringEdge> returnedGraph =
                subtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        vR,
                        emptyReplacementGraph);

        // Then
        assertActualEqualsExpected(returnedGraph, expectedReturnedGraph);
    }

    @Test
    public void replaceSubtree_nodeToReplaceIsLeaf() {
        // Given
        final SubtreeReplacer<StringVertex, StringEdge> subtreeReplacer = createSubtreeReplacer();
        // Original Tree: P --ePR--> R
        // Node to replace: R (a leaf)
        final Graph<StringVertex, StringEdge> originalGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vP, vR)
                        .addEdge(vP, vR, ePR)
                        .build();
        // Replacement Tree: X (single node, root X)
        final Graph<StringVertex, StringEdge> replacementGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertex(vX)
                        .build();
        // Expected Returned Tree: P --(label from ePR)--> X
        final Graph<StringVertex, StringEdge> expectedReturnedGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vP, vX)
                        .addEdge(vP, vX, cloneEdge(ePR))
                        .build();

        // When
        final Graph<StringVertex, StringEdge> returnedGraph =
                subtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        vR,
                        replacementGraph);

        // Then
        assertActualEqualsExpected(returnedGraph, expectedReturnedGraph);
    }

    @Test
    public void replaceSubtree_nodeToReplaceNotInGraph_returnsOriginalGraphInstance() {
        // Given
        final SubtreeReplacer<StringVertex, StringEdge> subtreeReplacer = createSubtreeReplacer();
        // Original Tree: R -> A (eRA)
        final Graph<StringVertex, StringEdge> originalGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vR, vA)
                        .addEdge(vR, vA, eRA)
                        .build();
        // Replacement Tree: X
        final Graph<StringVertex, StringEdge> replacementGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertex(vX)
                        .build();
        final Graph<StringVertex, StringEdge> expectedReturnedGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vR, vA)
                        .addEdge(vR, vA, eRA)
                        .build();
        final StringVertex nonExistentNode = new StringVertex("NonExistent");

        // When
        final Graph<StringVertex, StringEdge> returnedGraph =
                subtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        nonExistentNode,
                        replacementGraph);

        // Then
        assertActualEqualsExpected(returnedGraph, expectedReturnedGraph);
    }

    @Test
    public void replaceSubtree_replacementGraphIsEmptyAndNodeToReplaceIsRoot_resultsInEmptyGraph() {
        // Given
        final SubtreeReplacer<StringVertex, StringEdge> subtreeReplacer = createSubtreeReplacer();
        // Original Tree: R -> A (eRA)
        // Node to replace: R (the root)
        final Graph<StringVertex, StringEdge> originalGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vR, vA)
                        .addEdge(vR, vA, eRA)
                        .build();
        // Replacement Tree: (Empty Graph)
        final Graph<StringVertex, StringEdge> emptyReplacementGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .build();
        // Expected Returned Tree: (Empty Graph)
        final Graph<StringVertex, StringEdge> expectedReturnedGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .build();

        // When
        final Graph<StringVertex, StringEdge> returnedGraph =
                subtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        vR,
                        emptyReplacementGraph);

        // Then
        assertActualEqualsExpected(returnedGraph, expectedReturnedGraph);
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
        final Graph<StringVertex, StringEdge> originalGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vP, vR, vA, vB, vC_local)
                        .addEdge(vP, vR, ePR)
                        .addEdge(vR, vA, eRA)
                        .addEdge(vR, vB, eRB)
                        .addEdge(vA, vC_local, eAC_local)
                        .build();
        // Replacement Tree:
        //   X --eXY--> Y
        //   |
        //   --eXZ--> Z
        final Graph<StringVertex, StringEdge> replacementGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vX, vY, vZ)
                        .addEdge(vX, vY, eXY)
                        .addEdge(vX, vZ, eXZ)
                        .build();
        // Expected Returned Tree:
        //      P
        //      | (label from ePR, P->X)
        //      X
        //     /  \ (labels from eXY X->Y, eXZ X->Z)
        //    Y    Z
        final Graph<StringVertex, StringEdge> expectedReturnedGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vP, vX, vY, vZ)
                        .addEdge(vP, vX, cloneEdge(ePR)) // Edge factory reuses ePR label
                        .addEdge(vX, vY, cloneEdge(eXY)) // Edges from replacement
                        .addEdge(vX, vZ, cloneEdge(eXZ)) // Edges from replacement
                        .build();

        // When
        final Graph<StringVertex, StringEdge> returnedGraph =
                subtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        vR,
                        replacementGraph);

        // Then
        assertActualEqualsExpected(returnedGraph, expectedReturnedGraph);
    }

    @Test
    public void replaceSubtree_originalGraphIsEmpty_nodeToReplaceNotFound() {
        // Given
        final SubtreeReplacer<StringVertex, StringEdge> subtreeReplacer = createSubtreeReplacer();
        // Original Tree: (empty)
        final Graph<StringVertex, StringEdge> originalGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .build();
        final Graph<StringVertex, StringEdge> originalGraphSnapshotForExpected =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .build();
        // Node to replace: vR (not in the empty graph)
        // Replacement Tree: X
        final Graph<StringVertex, StringEdge> replacementGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertex(vX)
                        .build();

        // When
        final Graph<StringVertex, StringEdge> returnedGraph =
                subtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        vR,
                        replacementGraph);

        // Then
        assertActualEqualsExpected(returnedGraph, originalGraphSnapshotForExpected);
    }

    private SubtreeReplacer<StringVertex, StringEdge> createSubtreeReplacer() {
        return new SubtreeReplacer<>(
                () -> new DefaultDirectedGraph<>(StringEdge.class),
                SubtreeReplacerTest::cloneEdge);
    }

    private static GraphBuilder<StringVertex, StringEdge, ?> newGraphBuilder() {
        return DefaultDirectedGraph.createBuilder(StringEdge.class);
    }

    private static StringEdge cloneEdge(final StringEdge edge) {
        return new StringEdge(edge.getLabel());
    }
}