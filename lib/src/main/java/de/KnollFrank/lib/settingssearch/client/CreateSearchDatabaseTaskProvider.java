package de.KnollFrank.lib.settingssearch.client;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseConfig;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogsFactory;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataRepositoryProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class CreateSearchDatabaseTaskProvider {

    public static final @IdRes int FRAGMENT_CONTAINER_VIEW_ID = View.generateViewId();

    public static AsyncTaskWithProgressUpdateListeners<Void, DAOProvider> getCreateSearchDatabaseTask(
            final MergedPreferenceScreenDataRepositoryProvider mergedPreferenceScreenDataRepositoryProvider,
            final FragmentActivity activity,
            final Consumer<DAOProvider> appDatabaseConsumer,
            final AppDatabaseConfig appDatabaseConfig,
            final DAOProvider daoProvider) {
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                activity.findViewById(android.R.id.content),
                FRAGMENT_CONTAINER_VIEW_ID);
        // FK-FIXME: koordiniere diesen Task (1.) mit dem Task (2.) in SearchPreferenceFragment und mit (3.) SearchPreferenceFragments.rebuildSearchDatabase()
        return new AsyncTaskWithProgressUpdateListeners<>(
                (_void, progressUpdateListener) ->
                        createSearchDatabase(
                                mergedPreferenceScreenDataRepositoryProvider,
                                activity,
                                progressUpdateListener,
                                appDatabaseConfig,
                                daoProvider),
                appDatabaseConsumer);
    }

    private static DAOProvider createSearchDatabase(
            final MergedPreferenceScreenDataRepositoryProvider mergedPreferenceScreenDataRepositoryProvider,
            final FragmentActivity activity,
            final ProgressUpdateListener progressUpdateListener,
            final AppDatabaseConfig appDatabaseConfig,
            final DAOProvider daoProvider) {
        return mergedPreferenceScreenDataRepositoryProvider
                .createMergedPreferenceScreenDataRepository(
                        FragmentInitializerFactory.createFragmentInitializer(activity, FRAGMENT_CONTAINER_VIEW_ID),
                        PreferenceDialogsFactory.createPreferenceDialogs(activity, FRAGMENT_CONTAINER_VIEW_ID),
                        activity,
                        daoProvider,
                        progressUpdateListener)
                .getSearchDatabaseFilledWithPreferences(
                        Utils.geCurrentLocale(activity.getResources()),
                        appDatabaseConfig);
    }
}
