package de.KnollFrank.lib.settingssearch.search;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabase;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public interface MergedPreferenceScreenDataRepositoryProvider<C> {

    MergedPreferenceScreenDataRepository<C> createMergedPreferenceScreenDataRepository(
            FragmentInitializer fragmentInitializer,
            PreferenceDialogs preferenceDialogs,
            FragmentActivity activityContext,
            PreferencesDatabase<C> preferencesDatabase,
            ProgressUpdateListener progressUpdateListener);

    MergedPreferenceScreenDataRepository<C> createMergedPreferenceScreenDataRepository(
            PreferenceDialogs preferenceDialogs,
            FragmentActivity activityContext,
            PreferencesDatabase<C> preferencesDatabase,
            ProgressUpdateListener progressUpdateListener,
            InstantiateAndInitializeFragment instantiateAndInitializeFragment);
}
