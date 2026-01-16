package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.junit.Test;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeTest {

    private final StringNode nA = new StringNode("A");
    private final StringNode nB = new StringNode("B");
    private final StringNode nC = new StringNode("C");
    private final StringNode nD = new StringNode("D");

    // FK-TODO: male schönere ASCII-Bäume
    @Test
    public void shouldCreateTreeFromValidGraph() {
        // Given: A valid graph: [A] --("val")--> [B]
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
        final ImmutableValueGraph<StringNode, String> emptyGraph =
                Graphs
                        .<StringNode, String>directedImmutableValueGraphBuilder()
                        .build();

        // When & Then
        new Tree<>(emptyGraph);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForGraphWithCycle1() {
        // Given: [A] <--> [B]
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
        // Given: [C] --> [A] <--> [B]
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
        // Given: [A] --> [C] <-- [B]
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
        // Given: [A], [B] (disconnected)
        final ImmutableValueGraph<StringNode, String> graphWithMultipleRoots =
                Graphs
                        .<StringNode, String>directedImmutableValueGraphBuilder()
                        .addNode(nA)
                        .addNode(nB)
                        .build();

        // When & Then
        new Tree<>(graphWithMultipleRoots);
    }

    @Test
    public void shouldReturnRootNode() {
        // Given: A valid tree with root A
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, nB, "val")
                                .build());

        // When
        final StringNode rootNode = tree.rootNode();

        // Then
        assertThat(rootNode, is(nA));
    }

    @Test
    public void test_asTree_wholeTree() {
        // Given
        final Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> subtree =
                new Subtree<>(
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nA, nB, "A->B")
                                        .putEdgeValue(nB, nC, "B->C")
                                        .build()),
                        nA);

        // When
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> actualTree = subtree.asTree();

        // Then
        assertThat(actualTree, is(subtree.tree()));
    }

    @Test
    public void test_asTree_properSubtree() {
        // Given
        final Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> subtree =
                new Subtree<>(
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nA, nB, "A->B")
                                        .putEdgeValue(nB, nC, "B->C")
                                        .putEdgeValue(nA, nD, "A->D")
                                        .build()),
                        nB);

        // When
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> actualTree = subtree.asTree();

        // Then
        assertThat(
                actualTree,
                is(
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nB, nC, "B->C")
                                        .build())));
    }

    @Test
    public void test_asTree_shouldReturnSubtreeWithSingleNodeForLeaf() {
        // Given
        final Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> subtree =
                new Subtree<>(
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nA, nB, "A->B")
                                        .build()),
                        nB);

        // When
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> resultTree = subtree.asTree();

        // Then
        assertThat(
                resultTree,
                is(
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .addNode(nB)
                                        .build())));
    }
}
