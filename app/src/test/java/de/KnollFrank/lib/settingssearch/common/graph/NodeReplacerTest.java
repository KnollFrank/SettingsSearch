package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class NodeReplacerTest {

    private final StringNode nR = new StringNode("R");
    private final StringNode nA = new StringNode("A");
    private final StringNode nB = new StringNode("B");
    private final StringNode nX = new StringNode("X");
    private final StringNode nP = new StringNode("P");

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
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> originalGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(nP, nR, ePR)
                                .putEdgeValue(nR, nA, eRA)
                                .putEdgeValue(nR, nB, eRB)
                                .build());

        // When replacing node R with X
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> returnedGraph =
                NodeReplacer.replaceNode(
                        new TreeNode<>(nR, originalGraph),
                        nX);

        // Then the expected graph should have X connected to R's old neighbors:
        //   P --(ePR)--> X --(eRA)--> A
        //                |
        //                --(eRB)--> B
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> expectedGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(nP, nX, ePR) // old incoming edge
                                .putEdgeValue(nX, nA, eRA) // old outgoing edge
                                .putEdgeValue(nX, nB, eRB) // old outgoing edge
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
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> originalGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(nR, nA, eRA)
                                .putEdgeValue(nR, nB, eRB)
                                .build());

        // When replacing root R with X
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> returnedGraph =
                NodeReplacer.replaceNode(
                        new TreeNode<>(nR, originalGraph),
                        nX);

        // Then X should become the new root:
        //   X --(eRA)--> A
        //   |
        //   --(eRB)--> B
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> expectedGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(nX, nA, eRA)
                                .putEdgeValue(nX, nB, eRB)
                                .build());
        assertThat(returnedGraph, is(expectedGraph));
    }

    @Test
    public void shouldReplaceLeafNode() {
        // Given
        // Given an original graph:
        // Given a graph where the node to replace is a leaf:
        //   P --ePR--> R
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> originalGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(nP, nR, ePR)
                                .build());

        // When replacing leaf R with X
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> returnedGraph =
                NodeReplacer.replaceNode(
                        new TreeNode<>(nR, originalGraph),
                        nX);

        // Then X should become the new leaf:
        //   P --(ePR)--> X
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> expectedGraph =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(nP, nX, ePR)
                                .build());
        assertThat(returnedGraph, is(expectedGraph));
    }
}
