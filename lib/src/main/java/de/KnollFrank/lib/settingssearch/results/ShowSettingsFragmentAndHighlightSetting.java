package de.KnollFrank.lib.settingssearch.results;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

@FunctionalInterface
public interface ShowSettingsFragmentAndHighlightSetting {

    void showSettingsFragmentAndHighlightSetting(final Activity activity, final Fragment settingsFragment, final SearchablePreference setting2Highlight);
}
