package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public interface MergedPreferenceScreenDataRepositoryProvider {

    MergedPreferenceScreenDataRepository createMergedPreferenceScreenDataRepository(
            FragmentInitializer fragmentInitializer,
            PreferenceDialogs preferenceDialogs,
            Context context,
            ProgressUpdateListener progressUpdateListener);

    MergedPreferenceScreenDataRepository createMergedPreferenceScreenDataRepository(
            PreferenceDialogs preferenceDialogs,
            Context context,
            ProgressUpdateListener progressUpdateListener,
            InstantiateAndInitializeFragment instantiateAndInitializeFragment);
}
