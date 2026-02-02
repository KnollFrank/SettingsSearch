package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.Preference;

import com.google.common.graph.ImmutableValueGraph;

import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.graph.TreeBuilderListener;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

class ProgressUpdatingTreeBuilderListener implements TreeBuilderListener<PreferenceScreenOfHostOfActivity, Preference> {

    private final TreeBuilderListener<PreferenceScreenOfHostOfActivity, Preference> delegate;
    private final ProgressUpdateListener progressUpdateListener;

    public ProgressUpdatingTreeBuilderListener(final TreeBuilderListener<PreferenceScreenOfHostOfActivity, Preference> delegate,
                                               final ProgressUpdateListener progressUpdateListener) {
        this.delegate = delegate;
        this.progressUpdateListener = progressUpdateListener;
    }

    @Override
    public void onStartBuildTree(final PreferenceScreenOfHostOfActivity treeRoot) {
        delegate.onStartBuildTree(treeRoot);
    }

    @Override
    public void onStartBuildSubtree(final PreferenceScreenOfHostOfActivity subtreeRoot) {
        delegate.onStartBuildSubtree(subtreeRoot);
        progressUpdateListener.onProgressUpdate(ProgressProvider.getProgress(subtreeRoot));
    }

    @Override
    public void onFinishBuildSubtree(final PreferenceScreenOfHostOfActivity subtreeRoot) {
        delegate.onFinishBuildSubtree(subtreeRoot);
    }

    @Override
    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public void onFinishBuildTree(final Tree<PreferenceScreenOfHostOfActivity, Preference, ImmutableValueGraph<PreferenceScreenOfHostOfActivity, Preference>> tree) {
        delegate.onFinishBuildTree(tree);
    }
}
