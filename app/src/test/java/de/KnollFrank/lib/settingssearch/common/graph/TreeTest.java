package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.junit.Test;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeTest {

    private final StringVertex vA = new StringVertex("A");
    private final StringVertex vB = new StringVertex("B");
    private final StringVertex vC = new StringVertex("C");
    private final StringVertex vD = new StringVertex("D");

    // FK-TODO: male schönere ASCII-Bäume
    @Test
    public void shouldCreateTreeFromValidGraph() {
        // Given: A valid graph: [A] --("val")--> [B]
        final ImmutableValueGraph<StringVertex, String> validGraph =
                ValueGraphBuilder
                        .directed()
                        .<StringVertex, String>immutable()
                        .putEdgeValue(vA, vB, "val")
                        .build();

        // When
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> tree = new Tree<>(validGraph);

        // Then
        assertThat(tree.graph(), is(validGraph));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForUndirectedGraph() {
        // Given
        final ImmutableValueGraph<StringVertex, String> undirectedGraph =
                ValueGraphBuilder
                        .undirected()
                        .<StringVertex, String>immutable()
                        .putEdgeValue(vA, vB, "val")
                        .build();

        // When & Then
        new Tree<>(undirectedGraph);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForEmptyGraph() {
        // Given
        final ImmutableValueGraph<StringVertex, String> emptyGraph =
                ValueGraphBuilder
                        .directed()
                        .<StringVertex, String>immutable()
                        .build();

        // When & Then
        new Tree<>(emptyGraph);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForGraphWithCycle1() {
        // Given: [A] <--> [B]
        final ImmutableValueGraph<StringVertex, String> graphWithCycle =
                ValueGraphBuilder
                        .directed()
                        .<StringVertex, String>immutable()
                        .putEdgeValue(vA, vB, "val1")
                        .putEdgeValue(vB, vA, "val2")
                        .build();

        // When & Then
        new Tree<>(graphWithCycle);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForGraphWithCycle2() {
        // Given: [C] --> [A] <--> [B]
        final ImmutableValueGraph<StringVertex, String> graphWithCycle =
                ValueGraphBuilder
                        .directed()
                        .<StringVertex, String>immutable()
                        .putEdgeValue(vC, vA, "C->A")
                        .putEdgeValue(vA, vB, "A->B")
                        .putEdgeValue(vB, vA, "B->A")
                        .build();

        // When & Then
        new Tree<>(graphWithCycle);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForNodeWithMultipleParents() {
        // Given: [A] --> [C] <-- [B]
        final ImmutableValueGraph<StringVertex, String> graphWithMultipleParents =
                ValueGraphBuilder
                        .directed()
                        .<StringVertex, String>immutable()
                        .putEdgeValue(vA, vC, "val1")
                        .putEdgeValue(vB, vC, "val2")
                        .build();

        // When & Then
        new Tree<>(graphWithMultipleParents);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForGraphWithMultipleRoots() {
        // Given: [A], [B] (disconnected)
        final ImmutableValueGraph<StringVertex, String> graphWithMultipleRoots =
                ValueGraphBuilder
                        .directed()
                        .<StringVertex, String>immutable()
                        .addNode(vA)
                        .addNode(vB)
                        .build();

        // When & Then
        new Tree<>(graphWithMultipleRoots);
    }

    @Test
    public void shouldReturnRootNode() {
        // Given: A valid tree with root A
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> tree =
                new Tree<>(
                        ValueGraphBuilder
                                .directed()
                                .<StringVertex, String>immutable()
                                .putEdgeValue(vA, vB, "val")
                                .build());

        // When
        final StringVertex rootNode = tree.rootNode();

        // Then
        assertThat(rootNode, is(vA));
    }

    @Test
    public void test_asTree_wholeTree() {
        // Given: A valid tree A -> B -> C
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> tree =
                new Tree<>(
                        ValueGraphBuilder
                                .directed()
                                .<StringVertex, String>immutable()
                                .putEdgeValue(vA, vB, "A->B")
                                .putEdgeValue(vB, vC, "B->C")
                                .build());
        final Subtree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> subtree =
                new Subtree<>(tree, tree.rootNode());

        // When
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> actualTree = subtree.asTree();

        // Then
        assertThat(actualTree, is(tree));
    }

    @Test
    public void test_asTree_properSubtree() {
        // Given: A valid tree A -> B -> C, and A -> D
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> tree =
                new Tree<>(
                        ValueGraphBuilder
                                .directed()
                                .<StringVertex, String>immutable()
                                .putEdgeValue(vA, vB, "A->B")
                                .putEdgeValue(vB, vC, "B->C")
                                .putEdgeValue(vA, vD, "A->D")
                                .build());
        final Subtree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> subtree =
                new Subtree<>(tree, vB);

        // When
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> actualTree = subtree.asTree();

        // Then
        // FK-TODO: erzeuge und vergleiche mit dem gewünschten Subtree B -> C
        assertThat(actualTree, is(not(tree))); // It's a new tree object
        assertThat(actualTree.rootNode(), is(vB)); // Root is B
        assertThat(actualTree.graph().nodes().size(), is(2)); // Contains B and C
        assertThat(actualTree.graph().hasEdgeConnecting(vB, vC), is(true));
        assertThat(actualTree.graph().edgeValueOrDefault(vB, vC, null), is("B->C"));
        assertThat(actualTree.graph().nodes().contains(vA), is(false)); // Should not contain A
        assertThat(actualTree.graph().nodes().contains(vD), is(false)); // Should not contain D
    }

    @Test
    public void test_asTree_shouldReturnSubtreeWithSingleNodeForLeaf() {
        // Given: A valid tree A -> B
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> tree =
                new Tree<>(
                        ValueGraphBuilder
                                .directed()
                                .<StringVertex, String>immutable()
                                .putEdgeValue(vA, vB, "A->B")
                                .build());
        // And a subtree starting from the leaf node B
        final Subtree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> subtree =
                new Subtree<>(tree, vB);

        // When
        final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> resultTree = subtree.asTree();

        // Then: The result should be a new tree with only node B
        // FK-TODO: erzeuge und vergleiche mit dem gewünschten Subtree B
        assertThat(resultTree, is(not(tree)));
        assertThat(resultTree.rootNode(), is(vB));
        assertThat(resultTree.graph().nodes().size(), is(1));
        assertThat(resultTree.graph().nodes().contains(vB), is(true));
        assertThat(resultTree.graph().nodes().contains(vA), is(false));
    }
}
