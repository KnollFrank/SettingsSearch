package de.KnollFrank.lib.settingssearch.results;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

@FunctionalInterface
public interface ShowSettingsFragmentAndHighlightSetting {

    void showSettingsFragmentAndHighlightSetting(final FragmentActivity activity, final Fragment settingsFragment, final SearchablePreference setting2Highlight);
}
