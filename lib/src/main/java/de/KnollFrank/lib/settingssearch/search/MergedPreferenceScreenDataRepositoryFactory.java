package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.search.progress.IProgressDisplayer;

public interface MergedPreferenceScreenDataRepositoryFactory {

    MergedPreferenceScreenDataRepository createMergedPreferenceScreenDataRepository(
            DefaultFragmentInitializer preferenceDialogs,
            Context context,
            IProgressDisplayer progressDisplayer);

    MergedPreferenceScreenDataRepository createMergedPreferenceScreenDataRepository(
            DefaultFragmentInitializer preferenceDialogs,
            Context context,
            IProgressDisplayer progressDisplayer,
            Fragments fragments);
}
