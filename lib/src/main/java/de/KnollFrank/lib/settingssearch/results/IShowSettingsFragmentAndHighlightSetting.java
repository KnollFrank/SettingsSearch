package de.KnollFrank.lib.settingssearch.results;

import androidx.fragment.app.Fragment;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public interface IShowSettingsFragmentAndHighlightSetting {

    void showSettingsFragmentAndHighlightSetting(final Fragment settingsFragment, final SearchablePreference setting2Highlight);
}
