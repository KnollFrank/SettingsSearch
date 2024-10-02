package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.db.SearchablePreferenceScreenWithMap;

record SearchablePreferenceScreenWithMapAndHost(
        SearchablePreferenceScreenWithMap searchablePreferenceScreenWithMap,
        PreferenceFragmentCompat host) {
}
