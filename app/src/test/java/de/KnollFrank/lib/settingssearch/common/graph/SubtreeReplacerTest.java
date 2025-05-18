package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;
import org.junit.Test;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

    private final Supplier<Graph<StringVertex, StringEdge>> graphSupplier = () -> new DefaultDirectedGraph<>(StringEdge.class);
    private final SubtreeReplacer.EdgeFactory<StringVertex, StringEdge> edgeFactory =
            new SubtreeReplacer.EdgeFactory<>() {

                @Override
                public StringEdge createEdge(final StringVertex source, final StringVertex target, final StringEdge originalEdge) {
                    return new StringEdge(originalEdge != null ? originalEdge.getLabel() : source.getLabel() + "->" + target.getLabel() + "_newFromFactory");
                }
            };


    @Test
    public void replaceSubtree_nonMutating_nodeToReplaceIsRoot_replacesEntireGraph() {
        // Given
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

        // Expected Returned Tree: X -> Y (new edge, label from eXY, as originalEdge to factory is null)
        // For root replacement, the edge factory might create a new label if it can't reuse an incoming edge label.
        // Let's assume the replacement tree's own edges are used directly (or their labels).
        final Graph<StringVertex, StringEdge> expectedReturnedGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vX, vY)
                        .addEdge(vX, vY, new StringEdge(eXY.getLabel()))
                        .build();

        // When
        final Graph<StringVertex, StringEdge> returnedGraph =
                SubtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        vR,
                        replacementGraph,
                        graphSupplier,
                        edgeFactory);

        // Then
        assertActualEqualsExpected(returnedGraph, expectedReturnedGraph);
        assertThat("Returned graph should be a new instance.", returnedGraph, is(not(sameInstance(originalGraph))));
    }

    @Test
    public void replaceSubtree_nonMutating_nodeToReplaceIsChild() {
        // Given
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
        //   P --(label from ePR)--> X --(label from eXY)--> Y
        final Graph<StringVertex, StringEdge> expectedReturnedGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vP, vX, vY)
                        .addEdge(vP, vX, new StringEdge(ePR.getLabel())) // Edge factory reuses ePR's label
                        .addEdge(vX, vY, new StringEdge(eXY.getLabel())) // Edges from replacement tree
                        .build();

        // When
        final Graph<StringVertex, StringEdge> returnedGraph =
                SubtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        vR,
                        replacementGraph,
                        graphSupplier,
                        edgeFactory);

        // Then
        assertActualEqualsExpected(returnedGraph, expectedReturnedGraph);
        final StringEdge edgeParentToNewRoot = returnedGraph.getEdge(vP, vX);
        assertThat("Edge P->X should exist in returned graph", edgeParentToNewRoot, is(notNullValue()));
        assertThat("Label of P->X should be from original P->R edge", edgeParentToNewRoot.getLabel(), is(equalTo(ePR.getLabel())));
        assertThat(returnedGraph, is(not(sameInstance(originalGraph))));
    }

    @Test
    public void replaceSubtree_nonMutating_emptyReplacement_removesSubtree() {
        // Given
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
                SubtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        vR,
                        emptyReplacementGraph,
                        graphSupplier,
                        edgeFactory);

        // Then
        assertActualEqualsExpected(returnedGraph, expectedReturnedGraph);
        assertThat(
                "Returned graph with empty replacement should only contain parent P.",
                returnedGraph.vertexSet(),
                contains(vP));
        assertThat(
                "Returned graph with empty replacement should have no edges.",
                returnedGraph.edgeSet(),
                is(empty()));
        assertThat(returnedGraph, is(not(sameInstance(originalGraph))));
    }

    @Test
    public void replaceSubtree_nonMutating_nodeToReplaceIsLeaf() {
        // Given
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
                        .addEdge(vP, vX, new StringEdge(ePR.getLabel()))
                        .build();

        // When
        final Graph<StringVertex, StringEdge> returnedGraph =
                SubtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        vR,
                        replacementGraph,
                        graphSupplier,
                        edgeFactory);

        // Then
        assertActualEqualsExpected(returnedGraph, expectedReturnedGraph);
        assertThat(returnedGraph, is(not(sameInstance(originalGraph))));
    }

    @Test
    public void replaceSubtree_nonMutating_nodeToReplaceNotInGraph_returnsOriginalGraphInstance() {
        // Given
        // Original Tree: R -> A (eRA)
        final Graph<StringVertex, StringEdge> originalGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertices(vR, vA)
                        .addEdge(vR, vA, eRA)
                        .build();
        final Graph<StringVertex, StringEdge> originalGraphSnapshotForExpected = deepCopyGraph(originalGraph);

        final StringVertex nonExistentNode = new StringVertex("NonExistent");
        // Replacement Tree: X
        final Graph<StringVertex, StringEdge> replacementGraph =
                SubtreeReplacerTest
                        .newGraphBuilder()
                        .addVertex(vX)
                        .build();

        // When
        final Graph<StringVertex, StringEdge> returnedGraph =
                SubtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        nonExistentNode,
                        replacementGraph,
                        graphSupplier,
                        edgeFactory);

        // Then
        assertThat(
                "Returned graph should be the same instance as original when node not found.",
                returnedGraph,
                is(sameInstance(originalGraph)));
        assertActualEqualsExpected(returnedGraph, originalGraphSnapshotForExpected);
    }

    @Test
    public void replaceSubtree_nonMutating_replacementGraphIsEmptyAndNodeToReplaceIsRoot_resultsInEmptyGraph() {
        // Given
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
                SubtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        vR,
                        emptyReplacementGraph,
                        graphSupplier,
                        edgeFactory);

        // Then
        assertActualEqualsExpected(returnedGraph, expectedReturnedGraph);
        assertThat("Returned graph should be empty.", returnedGraph.vertexSet(), is(empty()));
        assertThat("Returned graph should have no edges.", returnedGraph.edgeSet(), is(empty()));
        assertThat(returnedGraph, is(not(sameInstance(originalGraph))));
    }

    @Test
    public void replaceSubtree_nonMutating_complexScenarioWithDeeperSubtree() {
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
                        .addEdge(vP, vX, new StringEdge(ePR.getLabel())) // Edge factory reuses ePR label
                        .addEdge(vX, vY, new StringEdge(eXY.getLabel())) // Edges from replacement
                        .addEdge(vX, vZ, new StringEdge(eXZ.getLabel())) // Edges from replacement
                        .build();

        // When
        final Graph<StringVertex, StringEdge> returnedGraph =
                SubtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        vR,
                        replacementGraph,
                        graphSupplier,
                        edgeFactory);

        // Then
        assertActualEqualsExpected(returnedGraph, expectedReturnedGraph);
        final StringEdge edgeParentToNewRoot = returnedGraph.getEdge(vP, vX);
        assertThat(edgeParentToNewRoot, is(notNullValue()));
        assertThat(edgeParentToNewRoot.getLabel(), is(equalTo(ePR.getLabel())));
        assertThat(returnedGraph, is(not(sameInstance(originalGraph))));
    }

    @Test
    public void replaceSubtree_nonMutating_originalGraphIsEmpty_nodeToReplaceNotFound() {
        // Given
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
                SubtreeReplacer.replaceSubtreeWithTree(
                        originalGraph,
                        vR,
                        replacementGraph,
                        graphSupplier,
                        edgeFactory);

        // Then
        assertThat(returnedGraph, is(sameInstance(originalGraph)));
        assertActualEqualsExpected(returnedGraph, originalGraphSnapshotForExpected);
    }

    private static GraphBuilder<StringVertex, StringEdge, ?> newGraphBuilder() {
        return DefaultDirectedGraph.createBuilder(StringEdge.class);
    }

    private Graph<StringVertex, StringEdge> deepCopyGraph(final Graph<StringVertex, StringEdge> graph) {
        final Graph<StringVertex, StringEdge> copy = graphSupplier.get();
        graph.vertexSet().forEach(copy::addVertex);
        graph.edgeSet().forEach(e -> copy.addEdge(graph.getEdgeSource(e), graph.getEdgeTarget(e), new StringEdge(e.getLabel())));
        return copy;
    }

    private void assertActualEqualsExpected(final Graph<StringVertex, StringEdge> actual,
                                            final Graph<StringVertex, StringEdge> expected) {
        assertActualEqualsExpected(actual.vertexSet(), expected.vertexSet());
        assertActualEdgesEqualsExpectedEdges(actual, expected);
    }

    private static void assertActualEqualsExpected(final Set<StringVertex> nodesActual, final Set<StringVertex> nodesExpected) {
        assertThat(
                "Vertex sets should be equal. Expected: [" + nodes2String(nodesExpected) + "], Actual: [" + nodes2String(nodesActual) + "]",
                nodesActual,
                is(equalTo(nodesExpected)));
    }

    private static void assertActualEdgesEqualsExpectedEdges(final Graph<StringVertex, StringEdge> actual, final Graph<StringVertex, StringEdge> expected) {
        final Set<String> expectedEdgesRepr = edgesAsStrings(expected);
        final Set<String> actualEdgesRepr = edgesAsStrings(actual);
        assertThat(
                "Edge representations should be equal. Expected: " + expectedEdgesRepr + ", Actual: " + actualEdgesRepr,
                actualEdgesRepr,
                is(equalTo(expectedEdgesRepr)));
    }

    private static String nodes2String(final Set<StringVertex> nodes) {
        return nodes
                .stream()
                .map(StringVertex::getLabel)
                .sorted()
                .collect(Collectors.joining(", "));
    }

    private static Set<String> edgesAsStrings(final Graph<StringVertex, StringEdge> graph) {
        return graph
                .edgeSet()
                .stream()
                .map(edge -> graph.getEdgeSource(edge).getLabel() + "->" + graph.getEdgeTarget(edge).getLabel() + ":" + edge.getLabel())
                .collect(Collectors.toSet());
    }
}