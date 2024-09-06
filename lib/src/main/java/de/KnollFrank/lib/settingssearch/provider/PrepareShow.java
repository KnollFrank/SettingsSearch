package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.PreferenceFragmentCompat;

@FunctionalInterface
public interface PrepareShow {

    void prepareShow(PreferenceFragmentCompat preferenceFragment);
}
