package de.KnollFrank.lib.settingssearch.common.graph.tree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.common.graph.tree.NodeTestFactory.nA;
import static de.KnollFrank.lib.settingssearch.common.graph.tree.NodeTestFactory.nB;
import static de.KnollFrank.lib.settingssearch.common.graph.tree.NodeTestFactory.nC;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.junit.Test;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.StringNode;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeCreationTest {

    @Test
    public void shouldCreateTreeFromValidGraph() {
        // Given
        /*
         * A
         * |
         * v
         * B
         */
        final ImmutableValueGraph<StringNode, String> validGraph =
                Graphs
                        .<StringNode, String>directedImmutableValueGraphBuilder()
                        .putEdgeValue(nA, nB, "val")
                        .build();

        // When
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree = new Tree<>(validGraph);

        // Then
        assertThat(tree.graph(), is(validGraph));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForUndirectedGraph() {
        // Given
        /*
         * A -- B
         */
        final ImmutableValueGraph<StringNode, String> undirectedGraph =
                ValueGraphBuilder
                        .undirected()
                        .<StringNode, String>immutable()
                        .putEdgeValue(nA, nB, "val")
                        .build();

        // When & Then
        new Tree<>(undirectedGraph);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForEmptyGraph() {
        // Given
        // (empty graph)
        final ImmutableValueGraph<StringNode, String> emptyGraph =
                Graphs
                        .<StringNode, String>directedImmutableValueGraphBuilder()
                        .build();

        // When & Then
        new Tree<>(emptyGraph);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForGraphWithCycle1() {
        // Given
        /*
         * A
         * ^
         * |
         * v
         * B
         */
        final ImmutableValueGraph<StringNode, String> graphWithCycle =
                Graphs
                        .<StringNode, String>directedImmutableValueGraphBuilder()
                        .putEdgeValue(nA, nB, "val1")
                        .putEdgeValue(nB, nA, "val2")
                        .build();

        // When & Then
        new Tree<>(graphWithCycle);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForGraphWithCycle2() {
        // Given
        /*
         *   C
         *   |
         *   v
         *   A
         *   ^
         *   |
         *   v
         *   B
         */
        final ImmutableValueGraph<StringNode, String> graphWithCycle =
                Graphs
                        .<StringNode, String>directedImmutableValueGraphBuilder()
                        .putEdgeValue(nC, nA, "C->A")
                        .putEdgeValue(nA, nB, "A->B")
                        .putEdgeValue(nB, nA, "B->A")
                        .build();

        // When & Then
        new Tree<>(graphWithCycle);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForNodeWithMultipleParents() {
        // Given
        /*
         * A   B
         *  \ /
         *   v
         *   C
         */
        final ImmutableValueGraph<StringNode, String> graphWithMultipleParents =
                Graphs
                        .<StringNode, String>directedImmutableValueGraphBuilder()
                        .putEdgeValue(nA, nC, "val1")
                        .putEdgeValue(nB, nC, "val2")
                        .build();

        // When & Then
        new Tree<>(graphWithMultipleParents);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForGraphWithMultipleRoots() {
        // Given
        /*
         * A   B
         */
        final ImmutableValueGraph<StringNode, String> graphWithMultipleRoots =
                Graphs
                        .<StringNode, String>directedImmutableValueGraphBuilder()
                        .addNode(nA)
                        .addNode(nB)
                        .build();

        // When & Then
        new Tree<>(graphWithMultipleRoots);
    }
}
