package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        final ChildNodeTransitionsProvider<StringNode, String> childNodeTransitionsProvider =
                node -> {
                    if (node.equals(nA)) {
                        return List.of(new ChildNodeTransition<>("A->B", () -> Optional.of(nB)));
                    }
                    if (node.equals(nB)) {
                        return List.of(new ChildNodeTransition<>("B->C", () -> Optional.of(nC)));
                    }
                    return Collections.emptyList();
                };
        final TreeBuilder<StringNode, String> treeBuilder =
                new TreeBuilder<>(
                        TreeBuilderListeners.emptyTreeBuilderListener(),
                        childNodeTransitionsProvider);

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
        final ChildNodeTransitionsProvider<StringNode, String> childNodeTransitionsProvider =
                node -> {
                    if (node.equals(nA)) {
                        return List.of(new ChildNodeTransition<>("A->B", () -> Optional.of(nB)));
                    }
                    if (node.equals(nB)) {
                        return List.of(new ChildNodeTransition<>("B->A", () -> Optional.of(nA)));
                    }
                    return Collections.emptyList();
                };
        final TreeBuilder<StringNode, String> treeBuilder =
                new TreeBuilder<>(
                        TreeBuilderListeners.emptyTreeBuilderListener(),
                        childNodeTransitionsProvider);

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
        final ChildNodeTransitionsProvider<StringNode, String> childNodeTransitionsProvider =
                node ->
                        node.equals(nA) ?
                                List.of(
                                        new ChildNodeTransition<>(
                                                "A->B",
                                                () -> Optional.of(nB))) :
                                Collections.emptyList();
        @SuppressWarnings("unchecked") final TreeBuilderListener<StringNode, String> listener = Mockito.mock(TreeBuilderListener.class);
        final TreeBuilder<StringNode, String> treeBuilder =
                new TreeBuilder<>(
                        listener,
                        childNodeTransitionsProvider);

        // When
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree = treeBuilder.buildTreeWithRoot(nA);

        // Then
        final InOrder inOrder = Mockito.inOrder(listener);
        inOrder.verify(listener).onStartBuildTree(nA);
        inOrder.verify(listener).onStartBuildSubtree(nA);
        inOrder.verify(listener).onStartBuildSubtree(nB);
        inOrder.verify(listener).onFinishBuildSubtree(nB);
        inOrder.verify(listener).onFinishBuildSubtree(nA);
        inOrder.verify(listener).onFinishBuildTree(tree);
    }
}
