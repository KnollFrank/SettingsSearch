package de.KnollFrank.lib.settingssearch.results;

import androidx.fragment.app.Fragment;

public interface SettingHighlighter {

    // FK-TODO: introduce Setting class which has a key
    void highlightSetting(final Fragment settingsFragment, final String keyOfSetting2Highlight);
}
