package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

@SuppressWarnings({"UnstableApiUsage"})
public class SubtreeReplacerTest {

    private final StringVertex vR = new StringVertex("R");
    private final StringVertex vA = new StringVertex("A");
    private final StringVertex vB = new StringVertex("B");
    private final StringVertex vX = new StringVertex("X");
    private final StringVertex vY = new StringVertex("Y");
    private final StringVertex vZ = new StringVertex("Z");
    private final StringVertex vP = new StringVertex("P");

    private final String eRA = "R->A";
    private final String eRB = "R->B";
    private final String eXY = "X->Y";
    private final String eXZ = "X->Z";
    private final String ePR = "P->R";

    @Test
    public void replaceSubtree_nodeToReplaceIsRoot_replacesEntireGraph() {
        //   subtreeToReplace   replacementTree  =>   expectedTree
        //      >R< ----------->       X                   X
        //       | (eRA)               | (eXY)   =>        | (eXY)
        //       A                     Y                   Y
        // Given
        final var subtreeToReplace =
                new Subtree<>(
                        new Tree<>(
                                StringGraphs
                                        .newStringGraphBuilder()
                                        .putEdgeValue(vR, vA, eRA)
                                        .build()),
                        vR);
        final var replacementTree =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vX, vY, eXY)
                                .build());
        final var expectedTree =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vX, vY, eXY)
                                .build());

        // When
        final var actualTree = SubtreeReplacer.replaceSubtreeWithTree(subtreeToReplace, replacementTree);

        // Then
        assertThat(actualTree, is(expectedTree));
    }

    @Test
    public void replaceSubtree_nodeToReplaceIsChild() {
        //   subtreeToReplace replacementTree => expectedTree
        //       P                                   P
        //       | (ePR)                             | (label from ePR)
        //      >R< ------------> X           =>     X
        //      / \ (eRA, eRB)    | (eXY)            | (eXY)
        //     A   B              Y                  Y
        // Given
        final var subtreeToReplace =
                new Subtree<>(
                        new Tree<>(
                                StringGraphs
                                        .newStringGraphBuilder()
                                        .putEdgeValue(vP, vR, ePR)
                                        .putEdgeValue(vR, vA, eRA)
                                        .putEdgeValue(vR, vB, eRB)
                                        .build()),
                        vR);
        final var replacementTree =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vX, vY, eXY)
                                .build());
        final var expectedTree =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vP, vX, ePR)
                                .putEdgeValue(vX, vY, eXY)
                                .build());

        // When
        final var actualTree = SubtreeReplacer.replaceSubtreeWithTree(subtreeToReplace, replacementTree);

        // Then
        assertThat(actualTree, is(expectedTree));
    }

    @Test
    public void replaceSubtree_nodeToReplaceIsLeaf() {
        //   subtreeToReplace   replacementTree =>     expectedTree
        //       P                                         P
        //       | (ePR)                                   | (label from ePR)
        //      >R< ------------> X             =>         X
        // Given
        final var subtreeToReplace =
                new Subtree<>(
                        new Tree<>(
                                StringGraphs
                                        .newStringGraphBuilder()
                                        .putEdgeValue(vP, vR, ePR)
                                        .build()),
                        vR);
        final var replacementTree =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .addNode(vX)
                                .build());
        final var expectedTree =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vP, vX, ePR)
                                .build());

        // When
        final var actualTree = SubtreeReplacer.replaceSubtreeWithTree(subtreeToReplace, replacementTree);

        // Then
        assertThat(actualTree, is(expectedTree));
    }

    @Test
    public void replaceSubtree_complexScenarioWithDeeperSubtree() {
        //   subtreeToReplace       replacementTree      =>   expectedTree
        //         P                                           P
        //         | (ePR)                                     | (label from ePR)
        //        >R< ------------------> X               =>   X
        //       / \ (eRA, eRB)          / \ (eXY, eXZ)       / \ (eXY, eXZ)
        //      A   B                   Y   Z                Y   Z
        //     / (eAC_local)
        //    C_local
        // Given
        final StringVertex vC_local = new StringVertex("C_local");
        final String eAC_local = "A->C_local";
        final var subtreeToReplace =
                new Subtree<>(
                        new Tree<>(
                                StringGraphs
                                        .newStringGraphBuilder()
                                        .putEdgeValue(vP, vR, ePR)
                                        .putEdgeValue(vR, vA, eRA)
                                        .putEdgeValue(vR, vB, eRB)
                                        .putEdgeValue(vA, vC_local, eAC_local)
                                        .build()),
                        vR);
        final var replacementTree =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vX, vY, eXY)
                                .putEdgeValue(vX, vZ, eXZ)
                                .build());
        final var expectedTree =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vP, vX, ePR)
                                .putEdgeValue(vX, vY, eXY)
                                .putEdgeValue(vX, vZ, eXZ)
                                .build());

        // When
        final var actualTree = SubtreeReplacer.replaceSubtreeWithTree(subtreeToReplace, replacementTree);

        // Then
        assertThat(actualTree, is(expectedTree));
    }

    @Test
    public void replaceSubtree_withOverlappingReplacementTree() {
        //   subtreeToReplace       replacementTree   => expectedTree
        //      R                                             R
        //     / \ (eRA, eRB)                                / \ (eRA, eRB)
        //    >A<  B  -------------> X                =>    X   B
        //                           |
        //                           B (overlapping)
        // Given
        final var subtreeToReplace =
                new Subtree<>(
                        new Tree<>(
                                StringGraphs
                                        .newStringGraphBuilder()
                                        .putEdgeValue(vR, vA, eRA)
                                        .putEdgeValue(vR, vB, eRB)
                                        .build()),
                        vA);
        final var replacementTreeWithOverlap =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vX, vB, "X->B")
                                .build());
        final var expectedTree =
                new Tree<>(
                        StringGraphs
                                .newStringGraphBuilder()
                                .putEdgeValue(vR, vX, eRA)
                                .putEdgeValue(vR, vB, eRB)
                                .build());

        // When
        final var actualTree = SubtreeReplacer.replaceSubtreeWithTree(subtreeToReplace, replacementTreeWithOverlap);

        // Then
        assertThat(actualTree, is(expectedTree));
    }
}
