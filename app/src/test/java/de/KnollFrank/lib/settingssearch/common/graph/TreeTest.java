package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.junit.Test;

import java.util.Optional;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeTest {

    private final StringNode nA = new StringNode("A");
    private final StringNode nB = new StringNode("B");
    private final StringNode nC = new StringNode("C");
    private final StringNode nD = new StringNode("D");

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

    @Test
    public void test_rootNode() {
        // Given
        /*
         * A
         * |
         * v
         * B
         */
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
    public void test_incomingEdgeOfRootNode() {
        // Given
        /*
         * A
         * |
         * v
         * B
         */
        final StringNode rootNode = nA;
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(rootNode, nB, "val")
                                .build());

        // When
        final Optional<Edge<StringNode, String>> incomingEdgeOfRootNode = tree.incomingEdgeOf(rootNode);

        // Then
        assertThat(incomingEdgeOfRootNode, is(Optional.empty()));
    }

    @Test
    public void test_incomingEdgeOfInnerNode() {
        // Given
        /*
         * A
         * | "val"
         * v
         * B
         */
        final String edgeValueOfnAnB = "val";
        final StringNode innerNode = nB;
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, innerNode, edgeValueOfnAnB)
                                .build());

        // When
        final Optional<Edge<StringNode, String>> incomingEdgeOfInnerNode = tree.incomingEdgeOf(innerNode);

        // Then
        assertThat(
                incomingEdgeOfInnerNode,
                is(
                        Optional.of(
                                new Edge<>(
                                        EndpointPair.ordered(nA, innerNode),
                                        edgeValueOfnAnB))));
    }

    @Test
    public void test_parentNodeOfRootNode() {
        // Given
        /*
         * A
         * |
         * v
         * B
         */
        final StringNode rootNode = nA;
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(rootNode, nB, "val")
                                .build());

        // When
        final Optional<StringNode> parentNodeOfRootNode = tree.parentNodeOf(rootNode);

        // Then
        assertThat(parentNodeOfRootNode, is(Optional.empty()));
    }

    @Test
    public void test_parentNodeOfInnerNode() {
        // Given
        /*
         * A
         * |
         * v
         * B
         */
        final StringNode innerNode = nB;
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, innerNode, "val")
                                .build());

        // When
        final Optional<StringNode> parentNodeOfInnerNode = tree.parentNodeOf(innerNode);

        // Then
        assertThat(parentNodeOfInnerNode, is(Optional.of(nA)));
    }

    @Test
    public void test_asTree_wholeTree() {
        /*
         * >A<  =>   A
         *  |        |
         *  v        v
         *  B        B
         *  |        |
         *  v        v
         *  C        C
         */
        assert_subtree_asTree_is_expectedTree(
                new Subtree<>(
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nA, nB, "A->B")
                                        .putEdgeValue(nB, nC, "B->C")
                                        .build()),
                        nA),
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, nB, "A->B")
                                .putEdgeValue(nB, nC, "B->C")
                                .build()));
    }

    @Test
    public void test_asTree_properSubtree() {
        /*
         *    A
         *   / \
         *  v   v
         * >B<  D   =>   B
         *  |            |
         *  v            v
         *  C            C
         */
        assert_subtree_asTree_is_expectedTree(
                new Subtree<>(
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nA, nB, "A->B")
                                        .putEdgeValue(nB, nC, "B->C")
                                        .putEdgeValue(nA, nD, "A->D")
                                        .build()),
                        nB),
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nB, nC, "B->C")
                                .build()));
    }

    @Test
    public void test_asTree_shouldReturnSubtreeWithSingleNodeForLeaf() {
        /*
         *  A
         *  |
         *  v
         * >B<  =>  B
         */
        assert_subtree_asTree_is_expectedTree(
                new Subtree<>(
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nA, nB, "A->B")
                                        .build()),
                        nB),
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .addNode(nB)
                                .build()));
    }

    private static void assert_subtree_asTree_is_expectedTree(
            final Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> subtree,
            final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> expectedTree) {
        // When
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> actualTree = subtree.asTree();

        // Then
        assertThat(actualTree, is(expectedTree));
    }
}
