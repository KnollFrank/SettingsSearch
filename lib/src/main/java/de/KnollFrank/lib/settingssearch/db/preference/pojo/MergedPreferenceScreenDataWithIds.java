package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record MergedPreferenceScreenDataWithIds(
        Set<SearchablePreferencePOJO> preferences,
        // FK-TODO: rename to preferencePathIdsByPreferenceId
        Map<Integer, List<Integer>> preferencePathIdByPreferenceId,
        Map<Integer, Class<? extends PreferenceFragmentCompat>> hostByPreferenceId) {
}
