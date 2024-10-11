package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public interface PreferenceScreenProvider {

    PreferenceScreen getPreferenceScreen(PreferenceFragmentCompat preferenceFragment);
}
