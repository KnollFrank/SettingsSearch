package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;

import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.StringNode;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreeNode;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeMergerStringTest {

    private final StringNode nP = new StringNode("P");
    private final StringNode nA = new StringNode("A");
    private final StringNode nB = new StringNode("B");
    private final StringNode nC = new StringNode("C");
    private final StringNode nD = new StringNode("D");

    @Test
    public void shouldMergeSubtreeWithSingleNode() {
        /*
         *                        P       P
         *                        |       |
         *                        v       v
         *   A  --merge into-->  >A<  =>  A
         */
        final var treeNode =
                new TreeNode<>(
                        nA,
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nP, nA, "P->A")
                                        .build()));
        assert_mergeTreeIntoTreeNode_is_expectedTree(
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .addNode(nA)
                                .build()),
                treeNode,
                treeNode.tree());
    }

    @Test
    public void test_mergeTreeIntoTreeNode_nodeAFromActualTreeIsSameAsRootNodeOfTree() {
        /*
         *   A  --merge into-->  >A<  =>  A
         */
        final var tree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .addNode(createNodeA())
                                .build());
        final var treeNode =
                new TreeNode<>(
                        createNodeA(),
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .addNode(createNodeA())
                                        .build()));
        // When
        final var actualTree = TreeMerger.mergeTreeIntoTreeNode(tree, treeNode);

        // Then
        final StringNode nodeAFromActualTree =
                Sets
                        .findElementByPredicate(
                                actualTree.graph().nodes(),
                                node -> node.equals(createNodeA()))
                        .orElseThrow();
        assertThat(nodeAFromActualTree == tree.rootNode(), is(true));
    }

    private static StringNode createNodeA() {
        return new StringNode("A");
    }

    @Test
    public void shouldMergeNodeWithNewChildren() {
        /*
         *                       P         P
         *                       |         |
         *                       v         v
         *    A --merge into--> >A< =>     A
         *   / \                          / \
         *  v   v                        v   v
         *  B   C                        B   C
         */
        assert_mergeTreeIntoTreeNode_is_expectedTree(
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, nB, "A->B")
                                .putEdgeValue(nA, nC, "A->C")
                                .build()),
                new TreeNode<>(
                        nA,
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nP, nA, "P->A")
                                        .build())),
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nP, nA, "P->A")
                                .putEdgeValue(nA, nB, "A->B")
                                .putEdgeValue(nA, nC, "A->C")
                                .build()));
    }

    @Test
    public void shouldMergeNodeAndAddChildren() {
        /*
         *                      P       P
         *                      |       |
         *                      v       v
         *   A --merge into--> >A<  =>  A
         *   |                  |      / \
         *   v                  v     v   v
         *   C                  B     B   C
         */
        assert_mergeTreeIntoTreeNode_is_expectedTree(
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, nC, "A->C")
                                .build()),
                new TreeNode<>(
                        nA,
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nP, nA, "P->A")
                                        .putEdgeValue(nA, nB, "A->B")
                                        .build())),
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nP, nA, "P->A")
                                .putEdgeValue(nA, nB, "A->B")
                                .putEdgeValue(nA, nC, "A->C")
                                .build()));
    }

    @Test
    public void shouldMergeAtRoot() {
        /*
         *   P --merge into--> >P<   =>    P
         *   |                 / \        /|\
         *   v                v   v      v v v
         *   C                A   B      A B C
         */
        assert_mergeTreeIntoTreeNode_is_expectedTree(
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nP, nC, "P->C")
                                .build()),
                new TreeNode<>(
                        nP,
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nP, nA, "P->A")
                                        .putEdgeValue(nP, nB, "P->B")
                                        .build())),
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nP, nA, "P->A")
                                .putEdgeValue(nP, nB, "P->B")
                                .putEdgeValue(nP, nC, "P->C")
                                .build()));
    }

    @Test
    public void shouldMergeAtLeaf() {
        /*
         *                      P     P
         *                      |     |
         *                      v     v
         *                      A     A
         *                      |     |
         *                      v     v
         *   C --merge into--> >C< => C
         *   |                        |
         *   v                        v
         *   D                        D
         *
         */
        assert_mergeTreeIntoTreeNode_is_expectedTree(
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nC, nD, "C->D")
                                .build()),
                new TreeNode<>(
                        nC,
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nP, nA, "P->A")
                                        .putEdgeValue(nA, nC, "A->C")
                                        .build())),
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nP, nA, "P->A")
                                .putEdgeValue(nA, nC, "A->C")
                                .putEdgeValue(nC, nD, "C->D")
                                .build()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenRootNodesDoNotMatch() {
        /*
         *                     P
         *                     |
         *                     v
         *  B --merge into--> >A< => IllegalStateException (node B does not match node A)
         *  |
         *  v
         *  C
         */
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> treeToMerge =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nB, nC, "B->C")
                                .build());

        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> targetTree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nP, nA, "P->A")
                                .build());
        final TreeNode<StringNode, String, ImmutableValueGraph<StringNode, String>> mergePoint = new TreeNode<>(nA, targetTree);
        TreeMerger.mergeTreeIntoTreeNode(treeToMerge, mergePoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenChildNodeExistsInTargetTree() {
        /*
         *                       P
         *                      / \
         *                     v   v
         *  A --merge into--> >A<  B  => IllegalArgumentException (node 'B' overlaps)
         *  |
         *  v
         *  B
         */
        TreeMerger.mergeTreeIntoTreeNode(
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, nB, "A->B")
                                .build()),
                new TreeNode<>(
                        nA,
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nP, nA, "P->A")
                                        .putEdgeValue(nP, nB, "P->B")
                                        .build())));
    }

    private static void assert_mergeTreeIntoTreeNode_is_expectedTree(
            final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree,
            final TreeNode<StringNode, String, ImmutableValueGraph<StringNode, String>> treeNode,
            final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> expectedTree) {
        // When
        final var actualTree = TreeMerger.mergeTreeIntoTreeNode(tree, treeNode);

        // Then
        assertThat(actualTree, is(expectedTree));
    }
}
