package de.KnollFrank.lib.settingssearch.client;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataRepositoryFactory;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class CreateSearchDatabaseTaskProvider {

    private static final @IdRes int FRAGMENT_CONTAINER_VIEW_ID = View.generateViewId();

    // FK-TODO: die Information MergedPreferenceScreenData im Rückgabetyp entfernen
    public static AsyncTaskWithProgressUpdateListeners<MergedPreferenceScreenData> getCreateSearchDatabaseTask(
            final MergedPreferenceScreenDataRepositoryFactory mergedPreferenceScreenDataRepositoryFactory,
            final FragmentActivity activity) {
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                activity.findViewById(android.R.id.content),
                FRAGMENT_CONTAINER_VIEW_ID);
        // FK-FIXME: koordiniere diesen Task (1.) mit dem Task (2.) in SearchPreferenceFragment und mit (3.) SearchPreferenceFragments.rebuildSearchDatabase()
        return new AsyncTaskWithProgressUpdateListeners<>(
                getMergedPreferenceScreenData(mergedPreferenceScreenDataRepositoryFactory, activity),
                mergedPreferenceScreenData -> {
                });
    }

    private static Function<ProgressUpdateListener, MergedPreferenceScreenData> getMergedPreferenceScreenData(
            final MergedPreferenceScreenDataRepositoryFactory mergedPreferenceScreenDataRepositoryFactory,
            final FragmentActivity activity) {
        final DefaultFragmentInitializer preferenceDialogs =
                new DefaultFragmentInitializer(
                        activity.getSupportFragmentManager(),
                        FRAGMENT_CONTAINER_VIEW_ID,
                        OnUiThreadRunnerFactory.fromActivity(activity));
        return progressDisplayer ->
                mergedPreferenceScreenDataRepositoryFactory
                        .createMergedPreferenceScreenDataRepository(
                                preferenceDialogs,
                                activity,
                                progressDisplayer)
                        .getMergedPreferenceScreenData(Utils.geCurrentLocale(activity.getResources()));
    }
}
