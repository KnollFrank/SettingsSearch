package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

@SuppressWarnings({"UnstableApiUsage"})
public class NodeReplacerTest {

    private final StringVertex vR = new StringVertex("R");
    private final StringVertex vA = new StringVertex("A");
    private final StringVertex vB = new StringVertex("B");
    private final StringVertex vX = new StringVertex("X");
    private final StringVertex vP = new StringVertex("P");

    private final String ePR = "P->R";
    private final String eRA = "R->A";
    private final String eRB = "R->B";

    @Test
    public void shouldReplaceNodeAndRedirectEdges() {
        // Given
        // Given an original graph:
        //   P --ePR--> R --eRA--> A
        //              |
        //              --eRB--> B
        final Tree<StringVertex, String> originalGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vP, vR, ePR)
                                .putEdgeValue(vR, vA, eRA)
                                .putEdgeValue(vR, vB, eRB)
                                .build());

        // When replacing node R with X
        final Tree<StringVertex, String> returnedGraph =
                NodeReplacer.replaceNode(
                        new TreeAtNode<>(originalGraph, vR),
                        vX);

        // Then the expected graph should have X connected to R's old neighbors:
        //   P --(ePR)--> X --(eRA)--> A
        //                |
        //                --(eRB)--> B
        final Tree<StringVertex, String> expectedGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vP, vX, ePR) // old incoming edge
                                .putEdgeValue(vX, vA, eRA) // old outgoing edge
                                .putEdgeValue(vX, vB, eRB) // old outgoing edge
                                .build());
        assertThat(returnedGraph, is(expectedGraph));
    }

    @Test
    public void shouldReplaceRootNode() {
        // Given
        // Given an original graph:
        // Given a graph where the node to replace is the root:
        //   R --eRA--> A
        //   |
        //   --eRB--> B
        final Tree<StringVertex, String> originalGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vR, vA, eRA)
                                .putEdgeValue(vR, vB, eRB)
                                .build());

        // When replacing root R with X
        final Tree<StringVertex, String> returnedGraph =
                NodeReplacer.replaceNode(
                        new TreeAtNode<>(originalGraph, vR),
                        vX);

        // Then X should become the new root:
        //   X --(eRA)--> A
        //   |
        //   --(eRB)--> B
        final Tree<StringVertex, String> expectedGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vX, vA, eRA)
                                .putEdgeValue(vX, vB, eRB)
                                .build());
        assertThat(returnedGraph, is(expectedGraph));
    }

    @Test
    public void shouldReplaceLeafNode() {
        // Given
        // Given an original graph:
        // Given a graph where the node to replace is a leaf:
        //   P --ePR--> R
        final Tree<StringVertex, String> originalGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vP, vR, ePR)
                                .build());

        // When replacing leaf R with X
        final Tree<StringVertex, String> returnedGraph =
                NodeReplacer.replaceNode(
                        new TreeAtNode<>(originalGraph, vR),
                        vX);

        // Then X should become the new leaf:
        //   P --(ePR)--> X
        final Tree<StringVertex, String> expectedGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vP, vX, ePR)
                                .build());
        assertThat(returnedGraph, is(expectedGraph));
    }
}
