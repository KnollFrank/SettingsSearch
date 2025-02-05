package de.KnollFrank.lib.settingssearch.results;

import de.KnollFrank.lib.settingssearch.fragment.PreferencePathPointer;

@FunctionalInterface
public interface INavigatePreferencePathAndHighlightPreference {

    void navigatePreferencePathAndHighlightPreference(PreferencePathPointer preferencePathPointer);
}
