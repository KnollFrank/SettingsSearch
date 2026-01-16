package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.StringNode;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreeNode;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
// FK-TODO: refactor
public class TreeMergerStringTest {

    private final StringNode nP = new StringNode("P");
    private final StringNode nA = new StringNode("A");
    private final StringNode nB = new StringNode("B");
    private final StringNode nC = new StringNode("C");
    private final StringNode nD = new StringNode("D");
    private final StringNode nX = new StringNode("X");
    private final StringNode nY = new StringNode("Y");

    @Test
    public void shouldMergeSubtreeWithSingleNode() {
        // Test merging a single node X, replacing A
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> treeToMerge =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .addNode(nX)
                                .build());
        assert_mergeTreeIntoTreeNode_is_expectedTree(
                /*
                 * merge      into      =>      P
                 *                           /
                 *  X          P            X
                 *             |           / \
                 *             v          v   v
                 *            >A<         B   C
                 *            / \
                 *           v   v
                 *           B   C
                 */
                treeToMerge,
                new TreeNode<>(
                        nA,
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nP, nA, "P->A")
                                        .putEdgeValue(nA, nB, "A->B")
                                        .putEdgeValue(nA, nC, "A->C")
                                        .build())),
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nP, nX, "P->A")
                                .putEdgeValue(nX, nB, "A->B")
                                .putEdgeValue(nX, nC, "A->C")
                                .build()));
    }

    @Test
    public void shouldMergeSubtreeWithChildren() {
        // Test merging a subtree (X -> Y), replacing A
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> treeToMerge =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nX, nY, "X->Y")
                                .build());
        assert_mergeTreeIntoTreeNode_is_expectedTree(
                /*
                 * merge      into      =>      P
                 *                               /
                 *  X          P               X
                 *  |          |              /|\
                 *  v          v             v v v
                 *  Y         >A<            B C Y
                 *            / \
                 *           v   v
                 *           B   C
                 */
                treeToMerge,
                new TreeNode<>(
                        nA,
                        new Tree<>(
                                Graphs
                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                        .putEdgeValue(nP, nA, "P->A")
                                        .putEdgeValue(nA, nB, "A->B")
                                        .putEdgeValue(nA, nC, "A->C")
                                        .build())),
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nP, nX, "P->A")
                                .putEdgeValue(nX, nB, "A->B")
                                .putEdgeValue(nX, nC, "A->C")
                                .putEdgeValue(nX, nY, "X->Y")
                                .build()));
    }

    @Test
    public void shouldMergeAtRoot() {
        // Test merging at the root node P
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> treeToMerge =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nX, nY, "X->Y")
                                .build());
        assert_mergeTreeIntoTreeNode_is_expectedTree(
                /*
                 * merge      into      =>      X
                 *                               /|\
                 *  X          >P<             v v v
                 *  |          / \             A B Y
                 *  v         v   v
                 *  Y         A   B
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
                                .putEdgeValue(nX, nA, "P->A")
                                .putEdgeValue(nX, nB, "P->B")
                                .putEdgeValue(nX, nY, "X->Y")
                                .build()));
    }

    @Test
    public void shouldMergeAtLeaf() {
        // Test merging at a leaf node C
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> treeToMerge =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nX, nY, "X->Y")
                                .build());
        assert_mergeTreeIntoTreeNode_is_expectedTree(
                /*
                 * merge      into      =>      P
                 *                               |
                 *  X          P               v
                 *  |          |               A
                 *  v          v               |
                 *  Y          A               v
                 *             |               X
                 *             v               |
                 *            >C<              v
                 *                             Y
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
                                .putEdgeValue(nA, nX, "A->C")
                                .putEdgeValue(nX, nY, "X->Y")
                                .build()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenMergedTreeIsInvalid() {
        // Merging X->B into P->A->B should fail, because B would have two parents (A and X).
        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> tree =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nP, nA, "P->A")
                                .putEdgeValue(nA, nB, "A->B")
                                .build());

        final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> treeToMerge =
                new Tree<>(
                        Graphs
                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                .putEdgeValue(nX, nB, "X->B") // B is also in the main tree
                                .build());
        /*
         * merge      into      =>
         *
         *  X          P
         *  |          |         INVALID TREE
         *  v          v         (B would have two parents: A and X)
         *  B         >A<
         *             |
         *             v
         *             B
         */
        final TreeNode<StringNode, String, ImmutableValueGraph<StringNode, String>> mergePoint = new TreeNode<>(nA, tree);
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
