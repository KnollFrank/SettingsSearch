package de.KnollFrank.lib.settingssearch.client;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataRepositoryFactory;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class CreateSearchDatabaseTaskProvider {

    private static final @IdRes int FRAGMENT_CONTAINER_VIEW_ID = View.generateViewId();

    public static AsyncTaskWithProgressUpdateListeners<?> getCreateSearchDatabaseTask(
            final MergedPreferenceScreenDataRepositoryFactory mergedPreferenceScreenDataRepositoryFactory,
            final FragmentActivity activity) {
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                activity.findViewById(android.R.id.content),
                FRAGMENT_CONTAINER_VIEW_ID);
        // FK-FIXME: koordiniere diesen Task (1.) mit dem Task (2.) in SearchPreferenceFragment und mit (3.) SearchPreferenceFragments.rebuildSearchDatabase()
        return new AsyncTaskWithProgressUpdateListeners<>(
                progressUpdateListener -> {
                    createSearchDatabase(
                            mergedPreferenceScreenDataRepositoryFactory,
                            activity,
                            progressUpdateListener);
                    return null;
                },
                _void -> {
                });
    }

    private static void createSearchDatabase(
            final MergedPreferenceScreenDataRepositoryFactory mergedPreferenceScreenDataRepositoryFactory,
            final FragmentActivity activity,
            final ProgressUpdateListener progressUpdateListener) {
        mergedPreferenceScreenDataRepositoryFactory
                .createMergedPreferenceScreenDataRepository(
                        new DefaultFragmentInitializer(
                                activity.getSupportFragmentManager(),
                                FRAGMENT_CONTAINER_VIEW_ID,
                                OnUiThreadRunnerFactory.fromActivity(activity)),
                        activity,
                        progressUpdateListener)
                .persistOrLoadPreferences(Utils.geCurrentLocale(activity.getResources()));
    }
}
