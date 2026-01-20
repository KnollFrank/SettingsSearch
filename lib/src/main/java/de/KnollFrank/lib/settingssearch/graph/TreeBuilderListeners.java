package de.KnollFrank.lib.settingssearch.graph;

public class TreeBuilderListeners {

    public static <N> TreeBuilderListener<N> createNoOpTreeBuilderListener() {
        return new TreeBuilderListener<>() {

            @Override
            public void onBuildSubtreeStarted(final N subtreeRoot) {
            }

            @Override
            public void onBuildSubtreeFinished(final N subtreeRoot) {
            }
        };
    }
}
