package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.StringNode;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreeNode;

// FK-TODO: refactor
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
        final var tree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .addNode(nA)
                                .build());
        final var treeNode =
                new TreeNode<>(
                        nA,
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nP, nA, "P->A")
                                        .build()));
        assert_mergeTreeIntoTreeNode_is_expectedTree(
                tree,
                treeNode,
                treeNode.tree());
    }

    @Test
    public void shouldMergeNodeWithNewChildren() {
        // Test merging a tree with root A and new children (B, C) into a tree where A is a leaf.
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> treeToMerge =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, nB, "A->B")
                                .putEdgeValue(nA, nC, "A->C")
                                .build());

        assert_mergeTreeIntoTreeNode_is_expectedTree(
                /*
                 * merge         into      =>      P
                 *                                 |
                 *    A          P                 v
                 *   / \         |                 A
                 *  v   v        v                / \
                 *  B   C       >A<              v   v
                 *                               B   C
                 */
                treeToMerge,
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
        // Test merging a tree (A -> C) adding to the old structure (A -> B)
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> treeToMerge =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, nC, "A->C")
                                .build());

        assert_mergeTreeIntoTreeNode_is_expectedTree(
                /*
                 * merge      into      =>      P
                 *                              |
                 *   A          P               v
                 *   |          |               A
                 *   v          v              / \
                 *   C         >A<            v   v
                 *              |             B   C
                 *              v
                 *              B
                 */
                treeToMerge,
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
        // Test merging new children into the root node P
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> treeToMerge =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nP, nC, "P->C")
                                .build());

        assert_mergeTreeIntoTreeNode_is_expectedTree(
                /*
                 * merge      into      =>      P
                 *                              /|\
                 *   P          >P<            v v v
                 *   |          / \            A B C
                 *   v         v   v
                 *   C         A   B
                 */
                treeToMerge,
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
        // Test merging new children into a leaf node C
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> treeToMerge =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nC, nD, "C->D")
                                .build());

        assert_mergeTreeIntoTreeNode_is_expectedTree(
                /*
                 * merge      into      =>      P
                 *                              |
                 *   C          P               v
                 *   |          |               A
                 *   v          v               |
                 *   D          A               v
                 *              |               C
                 *              v               |
                 *             >C<              v
                 *                              D
                 */
                treeToMerge,
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
        // This test must fail because the precondition (tree.rootNode() == treeNode.node()) is not met.
        // We are trying to merge a tree rooted at 'B' into a merge point 'A'.
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
        /*
         * merge      into
         *
         *  B          P
         *  |          |
         *  v          v
         *  C         >A<      => IllegalStateException
         */
        final TreeNode<StringNode, String, ImmutableValueGraph<StringNode, String>> mergePoint = new TreeNode<>(nA, targetTree);
        TreeMerger.mergeTreeIntoTreeNode(treeToMerge, mergePoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenChildNodeExistsInTargetTree() {
        // Merging A->B into P->B should fail, because child 'B' of the merged tree already exists in the target tree.
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> treeToMerge =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nA, nB, "A->B")
                                .build());

        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> targetTree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nP, nB, "P->B") // Target tree already contains B
                                .putEdgeValue(nP, nA, "P->A")
                                .build());
        /*
         * merge      into
         *
         *  A          P
         *  |         / \
         *  v        v   v
         *  B       B   >A<      => IllegalArgumentException (node 'B' overlaps)
         */
        final TreeNode<StringNode, String, ImmutableValueGraph<StringNode, String>> mergePoint = new TreeNode<>(nA, targetTree);
        TreeMerger.mergeTreeIntoTreeNode(treeToMerge, mergePoint);
    }

    private static void assert_mergeTreeIntoTreeNode_is_expectedTree(
            final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree,
            final TreeNode<StringNode, String, ImmutableValueGraph<StringNode, String>> treeNode,
            final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> expectedTree) {
        // When
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> actualTree =
                TreeMerger.mergeTreeIntoTreeNode(tree, treeNode);

        // Then
        assertThat(actualTree, is(expectedTree));
    }
}
