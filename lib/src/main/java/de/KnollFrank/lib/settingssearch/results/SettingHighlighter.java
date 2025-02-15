package de.KnollFrank.lib.settingssearch.results;

import androidx.fragment.app.Fragment;

public interface SettingHighlighter {

    // FK-TODO: add parameter "final Duration highlightDuration"?
    void highlightSetting(final Fragment settingsFragment, final Setting setting);
}
