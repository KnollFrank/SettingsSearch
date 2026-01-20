package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.StringNode;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeBuilderTest {

    private final StringNode nA = new StringNode("A");
    private final StringNode nB = new StringNode("B");
    private final StringNode nC = new StringNode("C");

    @Test
    public void shouldBuildTreeCorrectly() {
        /*
         * A
         * | A->B
         * v
         * B
         * | B->C
         * v
         * C
         */
        final ChildNodeByEdgeValueProvider<StringNode, String> childNodeByEdgeValueProvider =
                node -> {
                    if (node.equals(nA)) {
                        return Map.of("A->B", nB);
                    }
                    if (node.equals(nB)) {
                        return Map.of("B->C", nC);
                    }
                    return Collections.emptyMap();
                };
        final TreeBuilder<StringNode, String> treeBuilder =
                new TreeBuilder<>(
                        TreeBuilderListeners.createNoOpTreeBuilderListener(),
                        childNodeByEdgeValueProvider);

        // When
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree = treeBuilder.buildTreeWithRoot(nA);

        // Then
        assertThat(
                tree,
                is(new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, nB, "A->B")
                                .putEdgeValue(nB, nC, "B->C")
                                .build())));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionOnCycle() {
        /*
         * A
         * ^
         * |
         * v
         * B
         */
        final ChildNodeByEdgeValueProvider<StringNode, String> childNodeByEdgeValueProvider = node -> {
            if (node.equals(nA)) {
                return Map.of("A->B", nB);
            }
            if (node.equals(nB)) {
                return Map.of("B->A", nA);
            }
            return Collections.emptyMap();
        };
        final TreeBuilder<StringNode, String> treeBuilder =
                new TreeBuilder<>(
                        TreeBuilderListeners.createNoOpTreeBuilderListener(),
                        childNodeByEdgeValueProvider);

        // When
        treeBuilder.buildTreeWithRoot(nA);
    }

    @Test
    public void shouldNotifyListenerInCorrectOrder() {
        /*
         * A
         * |
         * v
         * B
         */
        final ChildNodeByEdgeValueProvider<StringNode, String> childNodeByEdgeValueProvider = node -> {
            if (node.equals(nA)) {
                return Map.of("A->B", nB);
            }
            return Collections.emptyMap();
        };
        final TreeBuilderListener<StringNode> listener = Mockito.mock(TreeBuilderListener.class);
        final TreeBuilder<StringNode, String> treeBuilder =
                new TreeBuilder<>(
                        listener,
                        childNodeByEdgeValueProvider);

        // When
        treeBuilder.buildTreeWithRoot(nA);

        // Then
        final InOrder inOrder = Mockito.inOrder(listener);
        inOrder.verify(listener).onBuildSubtreeStarted(nA);
        inOrder.verify(listener).onBuildSubtreeStarted(nB);
        inOrder.verify(listener).onBuildSubtreeFinished(nB);
        inOrder.verify(listener).onBuildSubtreeFinished(nA);
    }
}
