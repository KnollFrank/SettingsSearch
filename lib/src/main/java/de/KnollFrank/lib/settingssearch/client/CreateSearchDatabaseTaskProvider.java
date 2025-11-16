package de.KnollFrank.lib.settingssearch.client;

import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.common.EspressoIdlingResource;
import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogsFactory;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataRepositoryProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class CreateSearchDatabaseTaskProvider {

    public static final @IdRes int FRAGMENT_CONTAINER_VIEW_ID = View.generateViewId();

    public static AsyncTaskWithProgressUpdateListeners<Void, DAOProvider> getCreateSearchDatabaseTask(
            final MergedPreferenceScreenDataRepositoryProvider mergedPreferenceScreenDataRepositoryProvider,
            final FragmentActivity activity,
            final DAOProvider daoProvider,
            final PersistableBundle configuration,
            final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                activity.findViewById(android.R.id.content),
                FRAGMENT_CONTAINER_VIEW_ID);
        // FK-TODO: Abhängigkeit zu espresso doch besser aus dem Produktivcode entfernen?
        EspressoIdlingResource.increment();
        // FK-FIXME: koordiniere diesen Task (1.) mit dem Task (2.) in SearchPreferenceFragment und mit (3.) SearchPreferenceFragments.rebuildSearchDatabase()
        return new AsyncTaskWithProgressUpdateListeners<>(
                (_void, progressUpdateListener) -> {
                    try {
                        fillSearchDatabaseWithPreferences(
                                mergedPreferenceScreenDataRepositoryProvider,
                                activity,
                                progressUpdateListener,
                                daoProvider,
                                configuration,
                                preferenceSearchablePredicate);
                        return daoProvider;
                    } finally {
                        EspressoIdlingResource.decrement();
                    }
                },
                _daoProvider -> {
                });
    }

    private static void fillSearchDatabaseWithPreferences(
            final MergedPreferenceScreenDataRepositoryProvider mergedPreferenceScreenDataRepositoryProvider,
            final FragmentActivity activity,
            final ProgressUpdateListener progressUpdateListener,
            final DAOProvider daoProvider,
            final PersistableBundle configuration,
            final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        mergedPreferenceScreenDataRepositoryProvider
                .createMergedPreferenceScreenDataRepository(
                        FragmentInitializerFactory.createFragmentInitializer(activity, FRAGMENT_CONTAINER_VIEW_ID, preferenceSearchablePredicate),
                        PreferenceDialogsFactory.createPreferenceDialogs(activity, FRAGMENT_CONTAINER_VIEW_ID, preferenceSearchablePredicate),
                        activity,
                        daoProvider,
                        progressUpdateListener)
                .fillSearchDatabaseWithPreferences(
                        Utils.getCurrentLanguageLocale(activity.getResources()),
                        configuration);
    }
}
