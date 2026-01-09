package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.junit.Test;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class ImmutableValueTreeTest {

    private final StringVertex vA = new StringVertex("A");
    private final StringVertex vB = new StringVertex("B");
    private final StringVertex vC = new StringVertex("C");

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
        final ImmutableValueTree<StringVertex, String> tree = ImmutableValueTree.of(validGraph);

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
        ImmutableValueTree.of(undirectedGraph);
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
        ImmutableValueTree.of(emptyGraph);
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
        ImmutableValueTree.of(graphWithCycle);
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
        ImmutableValueTree.of(graphWithCycle);
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
        ImmutableValueTree.of(graphWithMultipleParents);
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
        ImmutableValueTree.of(graphWithMultipleRoots);
    }

    @Test
    public void shouldReturnRootNode() {
        // Given: A valid tree with root A
        final ImmutableValueTree<StringVertex, String> tree =
                ImmutableValueTree.of(
                        ValueGraphBuilder
                                .directed()
                                .<StringVertex, String>immutable()
                                .putEdgeValue(vA, vB, "val")
                                .build());

        // When
        final StringVertex rootNode = tree.getRootNode();

        // Then
        assertThat(rootNode, is(vA));
    }
}
