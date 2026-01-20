package de.KnollFrank.lib.settingssearch.graph;

public interface TreeBuilderListener<N> {

    void onBuildSubtreeStarted(N subtreeRoot);

    void onBuildSubtreeFinished(N subtreeRoot);
}
