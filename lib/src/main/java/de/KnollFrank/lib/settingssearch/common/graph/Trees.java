package de.KnollFrank.lib.settingssearch.common.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"UnstableApiUsage"})
public class Trees {

    public static <N, V> TreePath<N, V> getPathFromRootToTarget(final Tree<N, V> tree, final N target) {
        if (!tree.graph().nodes().contains(target)) {
            throw new IllegalArgumentException("Target node " + target + " is not part of the tree.");
        }
        final LinkedList<N> path = new LinkedList<>();
        for (Optional<N> current = Optional.of(target); current.isPresent(); current = tree.parentNodeOf(current.get())) {
            path.addFirst(current.get());
        }
        return new TreePath<>(tree, List.copyOf(path));
    }
}
