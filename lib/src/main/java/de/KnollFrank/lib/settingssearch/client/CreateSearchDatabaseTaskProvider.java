package de.KnollFrank.lib.settingssearch.client;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceDAO;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataRepositoryFactory;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class CreateSearchDatabaseTaskProvider {

    public static final @IdRes int FRAGMENT_CONTAINER_VIEW_ID = View.generateViewId();

    public static AsyncTaskWithProgressUpdateListeners<Void, SearchablePreferenceDAO> getCreateSearchDatabaseTask(
            final MergedPreferenceScreenDataRepositoryFactory mergedPreferenceScreenDataRepositoryFactory,
            final FragmentActivity activity,
            final Consumer<SearchablePreferenceDAO> searchablePreferenceDAOConsumer) {
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                activity.findViewById(android.R.id.content),
                FRAGMENT_CONTAINER_VIEW_ID);
        // FK-FIXME: koordiniere diesen Task (1.) mit dem Task (2.) in SearchPreferenceFragment und mit (3.) SearchPreferenceFragments.rebuildSearchDatabase()
        return new AsyncTaskWithProgressUpdateListeners<>(
                (_void, progressUpdateListener) ->
                        createSearchDatabase(
                                mergedPreferenceScreenDataRepositoryFactory,
                                activity,
                                progressUpdateListener),
                searchablePreferenceDAOConsumer);
    }

    private static SearchablePreferenceDAO createSearchDatabase(
            final MergedPreferenceScreenDataRepositoryFactory mergedPreferenceScreenDataRepositoryFactory,
            final FragmentActivity activity,
            final ProgressUpdateListener progressUpdateListener) {
        return mergedPreferenceScreenDataRepositoryFactory
                .createMergedPreferenceScreenDataRepository(
                        new DefaultFragmentInitializer(
                                activity.getSupportFragmentManager(),
                                FRAGMENT_CONTAINER_VIEW_ID,
                                OnUiThreadRunnerFactory.fromActivity(activity)),
                        activity,
                        progressUpdateListener)
                .getSearchDatabaseFilledWithPreferences(Utils.geCurrentLocale(activity.getResources()));
    }
}
