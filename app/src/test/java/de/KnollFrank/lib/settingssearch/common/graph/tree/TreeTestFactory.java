package de.KnollFrank.lib.settingssearch.common.graph.tree;

import com.google.common.graph.ImmutableValueGraph;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;

public class TreeTestFactory {

    private TreeTestFactory() {
    }

    public static final String ROOT = "Root";
    public static final String CHILD_1 = "Child1";
    public static final String CHILD_2 = "Child2";
    public static final String GRAND_CHILD_1 = "GrandChild1";
    public static final String EDGE_1 = "edge1";
    public static final String EDGE_2 = "edge2";
    public static final String EDGE_3 = "edge3";
    /*
     * Root
     *   |
     *   |--(edge1)--> Child1
     *   |             |
     *   |             '--(edge3)--> GrandChild1
     *   |
     *   '--(edge2)--> Child2
     */
    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public static final Tree<String, String, ImmutableValueGraph<String, String>> testTree =
            new Tree<>(
                    Graphs
                            .<String, String>directedImmutableValueGraphBuilder()
                            .putEdgeValue(ROOT, CHILD_1, EDGE_1)
                            .putEdgeValue(ROOT, CHILD_2, EDGE_2)
                            .putEdgeValue(CHILD_1, GRAND_CHILD_1, EDGE_3)
                            .build());
}
