package de.KnollFrank.lib.settingssearch.common.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"UnstableApiUsage"})
public class Trees {

    public static <N, V> TreePath<N, V> getPathFromRootToTarget(final Tree<N, V> tree, final N target) {
        if (!tree.graph().nodes().contains(target)) {
            throw new IllegalArgumentException("Target node " + target + " is not part of the tree.");
        }

        final List<N> path = new ArrayList<>();
        N current = target;

        // Wir hangeln uns von dem Zielknoten hoch zur Wurzel
        while (current != null) {
            path.add(current);

            // In einem validen Tree hat jeder Knoten außer der Wurzel genau einen Parent
            // parentNodeOf liefert ein Optional<N>
            current = tree.parentNodeOf(current).orElse(null);
        }

        // Da wir von Target zu Root gelaufen sind, müssen wir die Liste umdrehen
        Collections.reverse(path);

        // Sicherheitshalber prüfen, ob der Pfad tatsächlich bei der Wurzel beginnt
        if (!path.get(0).equals(tree.rootNode())) {
            throw new IllegalStateException("The path found does not start at the root node. The tree might be inconsistent.");
        }

        return new TreePath<>(tree, List.copyOf(path));
    }
}