package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Map;
import java.util.Set;

// FK-TODO: move to package de.KnollFrank.lib.settingssearch.db.preference.db.file and make package private
public record MergedPreferenceScreenDataWithIds(
        Set<SearchablePreference> preferences,
        Map<Integer, List<Integer>> preferencePathIdsByPreferenceId,
        Map<Integer, Class<? extends PreferenceFragmentCompat>> hostByPreferenceId) {
}
