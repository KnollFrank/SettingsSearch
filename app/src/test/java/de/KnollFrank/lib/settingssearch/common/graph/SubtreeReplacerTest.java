package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import com.codepoetics.ambivalence.Either;
import com.google.common.graph.ImmutableValueGraph;

import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
@RunWith(Parameterized.class)
public class SubtreeReplacerTest {

    private static final StringNode nR = new StringNode("R");
    private static final StringNode nA = new StringNode("A");
    private static final StringNode nB = new StringNode("B");
    private static final StringNode nX = new StringNode("X");
    private static final StringNode nY = new StringNode("Y");
    private static final StringNode nZ = new StringNode("Z");
    private static final StringNode nP = new StringNode("P");
    private static final StringNode nQ = new StringNode("Q");

    private static final String eRA = "R->A";
    private static final String eRB = "R->B";
    private static final String eXY = "X->Y";
    private static final String eXZ = "X->Z";
    private static final String ePR = "P->R";
    private static final String ePQ = "P->Q";

    private interface ISubtreeReplacerTest {

        Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> getSubtreeToReplace();

        Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> getReplacementTree();

        Either<Tree<StringNode, String, ImmutableValueGraph<StringNode, String>>, Class<? extends Exception>> getExpectedOutcome();
    }

    @Parameterized.Parameter(value = 0)
    public String description;

    @Parameterized.Parameter(value = 1)
    public ISubtreeReplacerTest testCase;

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(
                new Object[][]{
                        {
                                "Node to replace is root, replaces entire graph",
                                new ISubtreeReplacerTest() {
                                    /*
                                     *   subtreeToReplace   replacementTree  =>   expectedTree
                                     *      >R< ----------->       X                   X
                                     *       | (eRA)               | (eXY)   =>        | (eXY)
                                     *       A                     Y                   Y
                                     */
                                    @Override
                                    public Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> getSubtreeToReplace() {
                                        return new Subtree<>(
                                                new Tree<>(
                                                        Graphs
                                                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                                                .putEdgeValue(nR, nA, eRA)
                                                                .build()),
                                                nR);
                                    }

                                    @Override
                                    public Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> getReplacementTree() {
                                        return new Tree<>(
                                                Graphs
                                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                                        .putEdgeValue(nX, nY, eXY)
                                                        .build());
                                    }

                                    @Override
                                    public Either<Tree<StringNode, String, ImmutableValueGraph<StringNode, String>>, Class<? extends Exception>> getExpectedOutcome() {
                                        return Either.ofLeft(
                                                new Tree<>(
                                                        Graphs
                                                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                                                .putEdgeValue(nX, nY, eXY)
                                                                .build()));
                                    }
                                }
                        },
                        {
                                "Replacement with overlapping predecessor node should throw exception",
                                new ISubtreeReplacerTest() {
                                    /*
                                     *   subtreeToReplace         replacementTree  =>   INVALID TREE
                                     *        P                                              P
                                     *        | (ePR)                                        | (ePR)
                                     *        R                                    =>        R
                                     *        |                                            / |
                                     *        | (eRA)                               (X->R) | | (label from eRA)
                                     *        |                                            \ |
                                     *       >A< --------------------> X                     X (now has two parents: P and X)
                                     *                                 | (X->R)
                                     *                                 R (overlapping)
                                     * */
                                    @Override
                                    public Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> getSubtreeToReplace() {
                                        return new Subtree<>(
                                                new Tree<>(
                                                        Graphs
                                                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                                                .putEdgeValue(nP, nR, ePR)
                                                                .putEdgeValue(nR, nA, eRA)
                                                                .build()),
                                                nA);
                                    }

                                    @Override
                                    public Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> getReplacementTree() {
                                        return new Tree<>(
                                                Graphs
                                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                                        .putEdgeValue(nX, nR, "X->R")
                                                        .build());
                                    }

                                    @Override
                                    public Either<Tree<StringNode, String, ImmutableValueGraph<StringNode, String>>, Class<? extends Exception>> getExpectedOutcome() {
                                        return Either.ofRight(IllegalArgumentException.class);
                                    }
                                }
                        },
                        {
                                "Node to replace is a child",
                                new ISubtreeReplacerTest() {
                                    /*
                                     *   subtreeToReplace replacementTree => expectedTree
                                     *       P                                   P
                                     *       | (ePR)                             | (label from ePR)
                                     *      >R< ------------> X           =>     X
                                     *      / \ (eRA, eRB)    | (eXY)            | (eXY)
                                     *     A   B              Y                  Y
                                     */
                                    @Override
                                    public Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> getSubtreeToReplace() {
                                        return new Subtree<>(
                                                new Tree<>(
                                                        Graphs
                                                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                                                .putEdgeValue(nP, nR, ePR)
                                                                .putEdgeValue(nR, nA, eRA)
                                                                .putEdgeValue(nR, nB, eRB)
                                                                .build()),
                                                nR);
                                    }

                                    @Override
                                    public Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> getReplacementTree() {
                                        return new Tree<>(
                                                Graphs
                                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                                        .putEdgeValue(nX, nY, eXY)
                                                        .build());
                                    }

                                    @Override
                                    public Either<Tree<StringNode, String, ImmutableValueGraph<StringNode, String>>, Class<? extends Exception>> getExpectedOutcome() {
                                        return Either.ofLeft(
                                                new Tree<>(
                                                        Graphs
                                                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                                                .putEdgeValue(nP, nX, ePR)
                                                                .putEdgeValue(nX, nY, eXY)
                                                                .build()));
                                    }
                                }
                        },
                        {
                                "Should keep sibling subtree",
                                new ISubtreeReplacerTest() {
                                    /*
                                     *      P                           P
                                     *     / \                         / \
                                     *    Q   >R< ---replace--> X  => Q   X
                                     *         |                |         |
                                     *         A                Y         Y
                                     */
                                    @Override
                                    public Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> getSubtreeToReplace() {
                                        return new Subtree<>(
                                                new Tree<>(
                                                        Graphs
                                                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                                                .putEdgeValue(nP, nQ, ePQ)
                                                                .putEdgeValue(nP, nR, ePR)
                                                                .putEdgeValue(nR, nA, eRA)
                                                                .build()),
                                                nR);
                                    }

                                    @Override
                                    public Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> getReplacementTree() {
                                        return new Tree<>(
                                                Graphs
                                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                                        .putEdgeValue(nX, nY, eXY)
                                                        .build());
                                    }

                                    @Override
                                    public Either<Tree<StringNode, String, ImmutableValueGraph<StringNode, String>>, Class<? extends Exception>> getExpectedOutcome() {
                                        return Either.ofLeft(
                                                new Tree<>(
                                                        Graphs
                                                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                                                .putEdgeValue(nP, nQ, ePQ)
                                                                .putEdgeValue(nP, nX, ePR)
                                                                .putEdgeValue(nX, nY, eXY)
                                                                .build()));
                                    }
                                }
                        },
                        {
                                "Node to replace is a leaf",
                                new ISubtreeReplacerTest() {
                                    /*
                                     *   subtreeToReplace   replacementTree =>     expectedTree
                                     *       P                                         P
                                     *       | (ePR)                                   | (label from ePR)
                                     *      >R< ------------> X             =>         X
                                     */
                                    @Override
                                    public Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> getSubtreeToReplace() {
                                        return new Subtree<>(
                                                new Tree<>(
                                                        Graphs
                                                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                                                .putEdgeValue(nP, nR, ePR)
                                                                .build()),
                                                nR);
                                    }

                                    @Override
                                    public Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> getReplacementTree() {
                                        return new Tree<>(
                                                Graphs
                                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                                        .addNode(nX)
                                                        .build());
                                    }

                                    @Override
                                    public Either<Tree<StringNode, String, ImmutableValueGraph<StringNode, String>>, Class<? extends Exception>> getExpectedOutcome() {
                                        return Either.ofLeft(
                                                new Tree<>(
                                                        Graphs
                                                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                                                .putEdgeValue(nP, nX, ePR)
                                                                .build()));
                                    }
                                }
                        },
                        {
                                "Complex scenario with a deeper subtree",
                                new ISubtreeReplacerTest() {
                                    /*
                                     *   subtreeToReplace       replacementTree      =>   expectedTree
                                     *         P                                           P
                                     *         | (ePR)                                     | (label from ePR)
                                     *        >R< ------------------> X              =>    X
                                     *       / \ (eRA, eRB)          / \ (eXY, eXZ)       / \ (eXY, eXZ)
                                     *      A   B                   Y   Z                Y   Z
                                     *     / (eAC_local)
                                     *    C_local
                                     */
                                    @Override
                                    public Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> getSubtreeToReplace() {
                                        final StringNode nC_local = new StringNode("C_local");
                                        final String eAC_local = "A->C_local";
                                        return new Subtree<>(
                                                new Tree<>(
                                                        Graphs
                                                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                                                .putEdgeValue(nP, nR, ePR)
                                                                .putEdgeValue(nR, nA, eRA)
                                                                .putEdgeValue(nR, nB, eRB)
                                                                .putEdgeValue(nA, nC_local, eAC_local)
                                                                .build()),
                                                nR);
                                    }

                                    @Override
                                    public Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> getReplacementTree() {
                                        return new Tree<>(
                                                Graphs
                                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                                        .putEdgeValue(nX, nY, eXY)
                                                        .putEdgeValue(nX, nZ, eXZ)
                                                        .build());
                                    }

                                    @Override
                                    public Either<Tree<StringNode, String, ImmutableValueGraph<StringNode, String>>, Class<? extends Exception>> getExpectedOutcome() {
                                        return Either.ofLeft(
                                                new Tree<>(
                                                        Graphs
                                                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                                                .putEdgeValue(nP, nX, ePR)
                                                                .putEdgeValue(nX, nY, eXY)
                                                                .putEdgeValue(nX, nZ, eXZ)
                                                                .build()));
                                    }
                                }
                        },
                        {
                                "Replacement with overlapping nodes should throw exception",
                                new ISubtreeReplacerTest() {
                                    /*
                                     *   subtreeToReplace       replacementTree   =>   INVALID TREE
                                     *      R                                             R
                                     *     / \ (eRA, eRB)                                /  \
                                     *    >A<  B  -------------> X                =>    X----B (now has two parents: R and X)
                                     *                           |
                                     *                           B (overlapping)
                                     */
                                    @Override
                                    public Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> getSubtreeToReplace() {
                                        return new Subtree<>(
                                                new Tree<>(
                                                        Graphs
                                                                .<StringNode, String>directedImmutableValueGraphBuilder()
                                                                .putEdgeValue(nR, nA, eRA)
                                                                .putEdgeValue(nR, nB, eRB)
                                                                .build()),
                                                nA);
                                    }

                                    @Override
                                    public Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> getReplacementTree() {
                                        return new Tree<>(
                                                Graphs
                                                        .<StringNode, String>directedImmutableValueGraphBuilder()
                                                        .putEdgeValue(nX, nB, "X->B")
                                                        .build());
                                    }

                                    @Override
                                    public Either<Tree<StringNode, String, ImmutableValueGraph<StringNode, String>>, Class<? extends Exception>> getExpectedOutcome() {
                                        return Either.ofRight(IllegalArgumentException.class);
                                    }
                                }
                        }
                });
    }

    @Test
    public void test_replaceSubtreeWithTree() {
        testCase
                .getExpectedOutcome()
                .forEither(
                        expectedTree ->
                                test_replaceSubtreeWithTree_succeed(
                                        testCase.getSubtreeToReplace(),
                                        testCase.getReplacementTree(),
                                        expectedTree),
                        expectedException ->
                                test_replaceSubtreeWithTree_fail(
                                        testCase.getSubtreeToReplace(),
                                        testCase.getReplacementTree(),
                                        expectedException));
    }

    private void test_replaceSubtreeWithTree_succeed(
            final Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> subtreeToReplace,
            final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> replacementTree,
            final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> expectedTree) {
        MatcherAssert.assertThat(
                SubtreeReplacer.replaceSubtreeWithTree(subtreeToReplace, replacementTree),
                is(expectedTree));
    }

    private void test_replaceSubtreeWithTree_fail(
            final Subtree<StringNode, String, ImmutableValueGraph<StringNode, String>> subtreeToReplace,
            final Tree<StringNode, String, ImmutableValueGraph<StringNode, String>> replacementTree,
            final Class<? extends Exception> expectedException) {
        try {
            SubtreeReplacer.replaceSubtreeWithTree(subtreeToReplace, replacementTree);
            fail("Expected an " + expectedException.getSimpleName() + " to be thrown, but nothing was thrown.");
        } catch (final Exception e) {
            assertThat(
                    "Thrown exception is not of the expected type or a subtype.",
                    expectedException.isAssignableFrom(e.getClass()),
                    is(true));
        }
    }
}
