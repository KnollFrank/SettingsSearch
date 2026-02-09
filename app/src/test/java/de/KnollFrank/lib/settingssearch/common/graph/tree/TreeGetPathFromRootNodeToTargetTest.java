package de.KnollFrank.lib.settingssearch.common.graph.tree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.common.graph.tree.TreeTestFactory.CHILD_1;
import static de.KnollFrank.lib.settingssearch.common.graph.tree.TreeTestFactory.GRAND_CHILD_1;
import static de.KnollFrank.lib.settingssearch.common.graph.tree.TreeTestFactory.ROOT;
import static de.KnollFrank.lib.settingssearch.common.graph.tree.TreeTestFactory.testTree;

import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;

import de.KnollFrank.lib.settingssearch.common.graph.TreePath;
import de.KnollFrank.lib.settingssearch.matcher.ViewMatchers;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreeGetPathFromRootNodeToTargetTest {

    @Test
    public void getPathFromRootNodeToTarget_shouldReturnCorrectPathToLeaf() {
        // When
        final TreePath<String, String, ImmutableValueGraph<String, String>> path =
                testTree.getPathFromRootNodeToTarget(GRAND_CHILD_1);

        // Then
        assertThat(path.nodes(), contains(ROOT, CHILD_1, GRAND_CHILD_1));
        assertThat(path.startNode(), is(ROOT));
        assertThat(path.endNode(), is(GRAND_CHILD_1));
        assertThat(path.tree(), is(testTree));
        ViewMatchers.assertIsSubset(path.nodes(), path.tree().graph().nodes());
    }

    @Test
    public void getPathFromRootNodeToTarget_shouldReturnSingleNodePathWhenTargetIsRootNode() {
        // When
        final TreePath<String, String, ImmutableValueGraph<String, String>> path =
                testTree.getPathFromRootNodeToTarget(ROOT);

        // Then
        assertThat(path.nodes(), contains(ROOT));
    }

    @Test
    public void getPathFromRootNodeToTarget_shouldReturnPathToIntermediateNode() {
        // When
        final TreePath<String, String, ImmutableValueGraph<String, String>> path =
                testTree.getPathFromRootNodeToTarget(CHILD_1);

        // Assert
        assertThat(path.nodes(), contains(ROOT, CHILD_1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPathFromRootNodeToTarget_shouldThrowExceptionWhenNodeIsNotFound() {
        // When
        testTree.getPathFromRootNodeToTarget("UnknownNode");
    }
}
