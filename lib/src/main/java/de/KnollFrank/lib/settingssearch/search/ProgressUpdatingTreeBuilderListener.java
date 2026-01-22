package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.Preference;

import com.google.common.graph.ImmutableValueGraph;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.graph.TreeBuilderListener;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

class ProgressUpdatingTreeBuilderListener implements TreeBuilderListener<PreferenceScreenWithHost, Preference> {

    private final TreeBuilderListener<PreferenceScreenWithHost, Preference> delegate;
    private final ProgressUpdateListener progressUpdateListener;

    public ProgressUpdatingTreeBuilderListener(final TreeBuilderListener<PreferenceScreenWithHost, Preference> delegate,
                                               final ProgressUpdateListener progressUpdateListener) {
        this.delegate = delegate;
        this.progressUpdateListener = progressUpdateListener;
    }

    @Override
    public void onStartBuildTree(final PreferenceScreenWithHost treeRoot) {
        delegate.onStartBuildTree(treeRoot);
    }

    @Override
    public void onStartBuildSubtree(final PreferenceScreenWithHost subtreeRoot) {
        delegate.onStartBuildSubtree(subtreeRoot);
        progressUpdateListener.onProgressUpdate(ProgressProvider.getProgress(subtreeRoot));
    }

    @Override
    public void onFinishBuildSubtree(final PreferenceScreenWithHost subtreeRoot) {
        delegate.onFinishBuildSubtree(subtreeRoot);
    }

    @Override
    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public void onFinishBuildTree(final Tree<PreferenceScreenWithHost, Preference, ImmutableValueGraph<PreferenceScreenWithHost, Preference>> tree) {
        delegate.onFinishBuildTree(tree);
    }
}
