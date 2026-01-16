package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

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
        // Given: A valid tree A -> B -> C
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, nB, "A->B")
                                .putEdgeValue(nB, nC, "B->C")
                                .build());
        final Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> subtree =
                new Subtree<>(tree, tree.rootNode());

        // When
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> actualTree = subtree.asTree();

        // Then
        assertThat(actualTree, is(tree));
    }

    @Test
    public void test_asTree_properSubtree() {
        // Given: A valid tree A -> B -> C, and A -> D
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, nB, "A->B")
                                .putEdgeValue(nB, nC, "B->C")
                                .putEdgeValue(nA, nD, "A->D")
                                .build());
        final Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> subtree =
                new Subtree<>(tree, nB);

        // When
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> actualTree = subtree.asTree();

        // Then
        // FK-TODO: erzeuge und vergleiche mit dem gewünschten Subtree B -> C
        assertThat(actualTree, is(not(tree))); // It's a new tree object
        assertThat(actualTree.rootNode(), is(nB)); // Root is B
        assertThat(actualTree.graph().nodes().size(), is(2)); // Contains B and C
        assertThat(actualTree.graph().hasEdgeConnecting(nB, nC), is(true));
        assertThat(actualTree.graph().edgeValueOrDefault(nB, nC, null), is("B->C"));
        assertThat(actualTree.graph().nodes().contains(nA), is(false)); // Should not contain A
        assertThat(actualTree.graph().nodes().contains(nD), is(false)); // Should not contain D
    }

    @Test
    public void test_asTree_shouldReturnSubtreeWithSingleNodeForLeaf() {
        // Given: A valid tree A -> B
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, nB, "A->B")
                                .build());
        // And a subtree starting from the leaf node B
        final Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> subtree =
                new Subtree<>(tree, nB);

        // When
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> resultTree = subtree.asTree();

        // Then: The result should be a new tree with only node B
        // FK-TODO: erzeuge und vergleiche mit dem gewünschten Subtree B
        assertThat(resultTree, is(not(tree)));
        assertThat(resultTree.rootNode(), is(nB));
        assertThat(resultTree.graph().nodes().size(), is(1));
        assertThat(resultTree.graph().nodes().contains(nB), is(true));
        assertThat(resultTree.graph().nodes().contains(nA), is(false));
    }
}
