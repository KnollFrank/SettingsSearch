package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public interface MergedPreferenceScreenDataRepositoryFactory {

    MergedPreferenceScreenDataRepository createMergedPreferenceScreenDataRepository(
            DefaultFragmentInitializer preferenceDialogs,
            Context context,
            ProgressUpdateListener progressUpdateListener);

    MergedPreferenceScreenDataRepository createMergedPreferenceScreenDataRepository(
            DefaultFragmentInitializer preferenceDialogs,
            Context context,
            ProgressUpdateListener progressUpdateListener,
            InstantiateAndInitializeFragment instantiateAndInitializeFragment);
}
