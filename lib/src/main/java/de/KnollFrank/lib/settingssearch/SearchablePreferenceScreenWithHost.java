package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.db.SearchablePreferenceScreen;

public record SearchablePreferenceScreenWithHost(
        SearchablePreferenceScreen searchablePreferenceScreen,
        PreferenceFragmentCompat host) {
}
