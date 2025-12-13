package de.KnollFrank.lib.settingssearch.common.graph;

import static de.KnollFrank.lib.settingssearch.common.graph.StringGraphEquality.assertActualEqualsExpected;
import static de.KnollFrank.lib.settingssearch.common.graph.StringGraphs.cloneEdge;

import org.jgrapht.Graph;
import org.junit.Test;

public class NodeReplacerTest {

    private final StringVertex vR = new StringVertex("R");
    private final StringVertex vA = new StringVertex("A");
    private final StringVertex vB = new StringVertex("B");
    private final StringVertex vX = new StringVertex("X");
    private final StringVertex vP = new StringVertex("P");

    private final StringEdge ePR = new StringEdge("P->R");
    private final StringEdge eRA = new StringEdge("R->A");
    private final StringEdge eRB = new StringEdge("R->B");

    @Test
    public void shouldReplaceNodeAndRedirectEdges() {
        // Given
        final NodeReplacer<StringVertex, StringEdge> nodeReplacer = createNodeReplacer();    // Given an original graph:
        //   P --ePR--> R --eRA--> A
        //              |
        //              --eRB--> B
        final Graph<StringVertex, StringEdge> originalGraph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vP, vR, vA, vB)
                        .addEdge(vP, vR, ePR)
                        .addEdge(vR, vA, eRA)
                        .addEdge(vR, vB, eRB)
                        .build();

        // When replacing node R with X
        final Graph<StringVertex, StringEdge> returnedGraph =
                nodeReplacer.replaceNode(originalGraph, vR, vX);

        // Then the expected graph should have X connected to R's old neighbors:
        //   P --(ePR)--> X --(eRA)--> A
        //                |
        //                --(eRB)--> B
        final Graph<StringVertex, StringEdge> expectedGraph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vP, vX, vA, vB)
                        .addEdge(vP, vX, cloneEdge(ePR)) // old incoming edge
                        .addEdge(vX, vA, cloneEdge(eRA)) // old outgoing edge
                        .addEdge(vX, vB, cloneEdge(eRB)) // old outgoing edge
                        .build();
        assertActualEqualsExpected(returnedGraph, expectedGraph);
    }

    @Test
    public void shouldReplaceRootNode() {
        // Given
        final NodeReplacer<StringVertex, StringEdge> nodeReplacer = createNodeReplacer();    // Given an original graph:
        // Given a graph where the node to replace is the root:
        //   R --eRA--> A
        //   |
        //   --eRB--> B
        final Graph<StringVertex, StringEdge> originalGraph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vR, vA, vB)
                        .addEdge(vR, vA, eRA)
                        .addEdge(vR, vB, eRB)
                        .build();

        // When replacing root R with X
        final Graph<StringVertex, StringEdge> returnedGraph =
                nodeReplacer.replaceNode(originalGraph, vR, vX);

        // Then X should become the new root:
        //   X --(eRA)--> A
        //   |
        //   --(eRB)--> B
        final Graph<StringVertex, StringEdge> expectedGraph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vX, vA, vB)
                        .addEdge(vX, vA, cloneEdge(eRA))
                        .addEdge(vX, vB, cloneEdge(eRB))
                        .build();
        assertActualEqualsExpected(returnedGraph, expectedGraph);
    }

    @Test
    public void shouldReplaceLeafNode() {
        // Given
        final NodeReplacer<StringVertex, StringEdge> nodeReplacer = createNodeReplacer();    // Given an original graph:
        // Given a graph where the node to replace is a leaf:
        //   P --ePR--> R
        final Graph<StringVertex, StringEdge> originalGraph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vP, vR)
                        .addEdge(vP, vR, ePR)
                        .build();

        // When replacing leaf R with X
        final Graph<StringVertex, StringEdge> returnedGraph =
                nodeReplacer.replaceNode(originalGraph, vR, vX);

        // Then X should become the new leaf:
        //   P --(ePR)--> X
        final Graph<StringVertex, StringEdge> expectedGraph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vP, vX)
                        .addEdge(vP, vX, cloneEdge(ePR))
                        .build();
        assertActualEqualsExpected(returnedGraph, expectedGraph);
    }

    @Test
    public void shouldReturnOriginalGraphIfNodeToReplaceIsNotFound() {
        // Given
        final NodeReplacer<StringVertex, StringEdge> nodeReplacer = createNodeReplacer();    // Given an original graph:
        // Given a graph
        final Graph<StringVertex, StringEdge> originalGraph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertices(vP, vA)
                        .addEdge(vP, vA, ePR) // just some edge
                        .build();

        // When trying to replace a non-existent node R
        final Graph<StringVertex, StringEdge> returnedGraph =
                nodeReplacer.replaceNode(originalGraph, vR, vX);

        // Then the returned graph should be an identical copy of the original
        assertActualEqualsExpected(returnedGraph, originalGraph);
    }

    private NodeReplacer<StringVertex, StringEdge> createNodeReplacer() {
        return new NodeReplacer<>(
                StringEdge.class,
                StringGraphs::cloneEdge);
    }
}
