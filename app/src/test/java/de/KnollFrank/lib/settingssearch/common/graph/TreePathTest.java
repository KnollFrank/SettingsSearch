package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import java.util.List;

import de.KnollFrank.lib.settingssearch.common.graph.tree.TreeTestFactory;

public class TreePathTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateTreePathHavingNoNodes() {
        // Given
        final List<String> emptyNodes = List.of();

        // When
        new TreePath<>(TreeTestFactory.testTree, emptyNodes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateTreePathHavingNodesNotBelongingToTree() {
        // Given
        final var tree = TreeTestFactory.testTree;
        final String nonExistingNode = "non existing node";
        assertThat(tree.graph().nodes(), not(hasItem(nonExistingNode)));

        // When
        new TreePath<>(tree, List.of(nonExistingNode));
    }

    @Test
    public void shouldCreateTreePath() {
        new TreePath<>(
                TreeTestFactory.testTree,
                List.of(TreeTestFactory.testTree.rootNode()));
    }
}
