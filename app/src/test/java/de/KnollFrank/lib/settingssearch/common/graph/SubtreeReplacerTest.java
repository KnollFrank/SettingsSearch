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

    private static final StringVertex vR = new StringVertex("R");
    private static final StringVertex vA = new StringVertex("A");
    private static final StringVertex vB = new StringVertex("B");
    private static final StringVertex vX = new StringVertex("X");
    private static final StringVertex vY = new StringVertex("Y");
    private static final StringVertex vZ = new StringVertex("Z");
    private static final StringVertex vP = new StringVertex("P");
    private static final StringVertex vQ = new StringVertex("Q");

    private static final String eRA = "R->A";
    private static final String eRB = "R->B";
    private static final String eXY = "X->Y";
    private static final String eXZ = "X->Z";
    private static final String ePR = "P->R";
    private static final String ePQ = "P->Q";

    private interface ISubtreeReplacerTest {

        Subtree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getSubtreeToReplace();

        Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getReplacementTree();

        Either<Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>>, Class<? extends Exception>> getExpectedOutcome();
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
                                    public Subtree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getSubtreeToReplace() {
                                        return new Subtree<>(
                                                new Tree<>(
                                                        StringGraphs
                                                                .newStringGraphBuilder()
                                                                .putEdgeValue(vR, vA, eRA)
                                                                .build()),
                                                vR);
                                    }

                                    @Override
                                    public Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getReplacementTree() {
                                        return new Tree<>(
                                                StringGraphs
                                                        .newStringGraphBuilder()
                                                        .putEdgeValue(vX, vY, eXY)
                                                        .build());
                                    }

                                    @Override
                                    public Either<Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>>, Class<? extends Exception>> getExpectedOutcome() {
                                        return Either.ofLeft(
                                                new Tree<>(
                                                        StringGraphs
                                                                .newStringGraphBuilder()
                                                                .putEdgeValue(vX, vY, eXY)
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
                                     *       >A< --------------------> X                   \ X (now has two parents: P and X)
                                     *                                 | (X->R)
                                     *                                 R (overlapping)
                                     * */
                                    @Override
                                    public Subtree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getSubtreeToReplace() {
                                        return new Subtree<>(
                                                new Tree<>(
                                                        StringGraphs.newStringGraphBuilder()
                                                                .putEdgeValue(vP, vR, ePR)
                                                                .putEdgeValue(vR, vA, eRA)
                                                                .build()),
                                                vA);
                                    }

                                    @Override
                                    public Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getReplacementTree() {
                                        return new Tree<>(
                                                StringGraphs.newStringGraphBuilder()
                                                        .putEdgeValue(vX, vR, "X->R")
                                                        .build());
                                    }

                                    @Override
                                    public Either<Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>>, Class<? extends Exception>> getExpectedOutcome() {
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
                                    public Subtree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getSubtreeToReplace() {
                                        return new Subtree<>(
                                                new Tree<>(
                                                        StringGraphs
                                                                .newStringGraphBuilder()
                                                                .putEdgeValue(vP, vR, ePR)
                                                                .putEdgeValue(vR, vA, eRA)
                                                                .putEdgeValue(vR, vB, eRB)
                                                                .build()),
                                                vR);
                                    }

                                    @Override
                                    public Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getReplacementTree() {
                                        return new Tree<>(
                                                StringGraphs
                                                        .newStringGraphBuilder()
                                                        .putEdgeValue(vX, vY, eXY)
                                                        .build());
                                    }

                                    @Override
                                    public Either<Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>>, Class<? extends Exception>> getExpectedOutcome() {
                                        return Either.ofLeft(
                                                new Tree<>(
                                                        StringGraphs
                                                                .newStringGraphBuilder()
                                                                .putEdgeValue(vP, vX, ePR)
                                                                .putEdgeValue(vX, vY, eXY)
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
                                    public Subtree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getSubtreeToReplace() {
                                        return new Subtree<>(
                                                new Tree<>(
                                                        StringGraphs.newStringGraphBuilder()
                                                                .putEdgeValue(vP, vQ, ePQ)
                                                                .putEdgeValue(vP, vR, ePR)
                                                                .putEdgeValue(vR, vA, eRA)
                                                                .build()),
                                                vR);
                                    }

                                    @Override
                                    public Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getReplacementTree() {
                                        return new Tree<>(
                                                StringGraphs.newStringGraphBuilder()
                                                        .putEdgeValue(vX, vY, eXY)
                                                        .build());
                                    }

                                    @Override
                                    public Either<Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>>, Class<? extends Exception>> getExpectedOutcome() {
                                        return Either.ofLeft(
                                                new Tree<>(
                                                        StringGraphs.newStringGraphBuilder()
                                                                .putEdgeValue(vP, vQ, ePQ)
                                                                .putEdgeValue(vP, vX, ePR)
                                                                .putEdgeValue(vX, vY, eXY)
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
                                    public Subtree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getSubtreeToReplace() {
                                        return new Subtree<>(
                                                new Tree<>(
                                                        StringGraphs
                                                                .newStringGraphBuilder()
                                                                .putEdgeValue(vP, vR, ePR)
                                                                .build()),
                                                vR);
                                    }

                                    @Override
                                    public Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getReplacementTree() {
                                        return new Tree<>(
                                                StringGraphs
                                                        .newStringGraphBuilder()
                                                        .addNode(vX)
                                                        .build());
                                    }

                                    @Override
                                    public Either<Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>>, Class<? extends Exception>> getExpectedOutcome() {
                                        return Either.ofLeft(
                                                new Tree<>(
                                                        StringGraphs
                                                                .newStringGraphBuilder()
                                                                .putEdgeValue(vP, vX, ePR)
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
                                    public Subtree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getSubtreeToReplace() {
                                        final StringVertex vC_local = new StringVertex("C_local");
                                        final String eAC_local = "A->C_local";
                                        return new Subtree<>(
                                                new Tree<>(
                                                        StringGraphs
                                                                .newStringGraphBuilder()
                                                                .putEdgeValue(vP, vR, ePR)
                                                                .putEdgeValue(vR, vA, eRA)
                                                                .putEdgeValue(vR, vB, eRB)
                                                                .putEdgeValue(vA, vC_local, eAC_local)
                                                                .build()),
                                                vR);
                                    }

                                    @Override
                                    public Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getReplacementTree() {
                                        return new Tree<>(
                                                StringGraphs
                                                        .newStringGraphBuilder()
                                                        .putEdgeValue(vX, vY, eXY)
                                                        .putEdgeValue(vX, vZ, eXZ)
                                                        .build());
                                    }

                                    @Override
                                    public Either<Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>>, Class<? extends Exception>> getExpectedOutcome() {
                                        return Either.ofLeft(
                                                new Tree<>(
                                                        StringGraphs
                                                                .newStringGraphBuilder()
                                                                .putEdgeValue(vP, vX, ePR)
                                                                .putEdgeValue(vX, vY, eXY)
                                                                .putEdgeValue(vX, vZ, eXZ)
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
                                    public Subtree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getSubtreeToReplace() {
                                        return new Subtree<>(
                                                new Tree<>(
                                                        StringGraphs
                                                                .newStringGraphBuilder()
                                                                .putEdgeValue(vR, vA, eRA)
                                                                .putEdgeValue(vR, vB, eRB)
                                                                .build()),
                                                vA);
                                    }

                                    @Override
                                    public Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> getReplacementTree() {
                                        return new Tree<>(
                                                StringGraphs
                                                        .newStringGraphBuilder()
                                                        .putEdgeValue(vX, vB, "X->B")
                                                        .build());
                                    }

                                    @Override
                                    public Either<Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>>, Class<? extends Exception>> getExpectedOutcome() {
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
            final Subtree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> subtreeToReplace,
            final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> replacementTree,
            final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> expectedTree) {
        MatcherAssert.assertThat(
                SubtreeReplacer.replaceSubtreeWithTree(subtreeToReplace, replacementTree),
                is(expectedTree));
    }

    private void test_replaceSubtreeWithTree_fail(
            final Subtree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> subtreeToReplace,
            final Tree<StringVertex, String, ImmutableValueGraph<StringVertex, String>> replacementTree,
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
