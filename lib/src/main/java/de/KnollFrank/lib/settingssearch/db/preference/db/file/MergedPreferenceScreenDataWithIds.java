package de.KnollFrank.lib.settingssearch.db.preference.db.file;

import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

record MergedPreferenceScreenDataWithIds(
        Set<SearchablePreference> preferences,
        Map<Integer, List<Integer>> preferencePathIdsByPreferenceId,
        Map<Integer, Class<? extends PreferenceFragmentCompat>> hostByPreferenceId) {
}
