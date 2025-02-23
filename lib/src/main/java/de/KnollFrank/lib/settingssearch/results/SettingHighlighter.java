package de.KnollFrank.lib.settingssearch.results;

import androidx.fragment.app.Fragment;

public interface SettingHighlighter {

    void highlightSetting(final Fragment settingsFragment, final Setting setting);
}
