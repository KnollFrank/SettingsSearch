package de.KnollFrank.lib.settingssearch.graph;

import java.util.Optional;

@FunctionalInterface
public interface ChildNodeProvider<N> {

    Optional<N> traverse();
}
