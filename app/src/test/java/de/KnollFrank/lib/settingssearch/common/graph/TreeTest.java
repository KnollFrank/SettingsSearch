package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.junit.Test;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeTest {

    private final StringVertex vA = new StringVertex("A");
    private final StringVertex vB = new StringVertex("B");
    private final StringVertex vC = new StringVertex("C");

    @Test
    public void shouldCreateTreeFromValidGraph() {
        // Given: A valid graph: [A] --("val")--> [B]
        final ValueGraph<StringVertex, String> validGraph =
                ValueGraphBuilder
                        .directed()
                        .<StringVertex, String>immutable()
                        .putEdgeValue(vA, vB, "val")
                        .build();

        // When
        final Tree<StringVertex, String> tree = new Tree<>(validGraph);

        // Then
        assertThat(tree.graph(), is(validGraph));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForUndirectedGraph() {
        // Given
        final ValueGraph<StringVertex, String> undirectedGraph =
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
        final ValueGraph<StringVertex, String> emptyGraph =
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
        final ValueGraph<StringVertex, String> graphWithCycle =
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
        final ValueGraph<StringVertex, String> graphWithCycle =
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
        final ValueGraph<StringVertex, String> graphWithMultipleParents =
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
        final ValueGraph<StringVertex, String> graphWithMultipleRoots =
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
        final Tree<StringVertex, String> tree =
                new Tree<>(ValueGraphBuilder
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
