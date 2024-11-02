package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public record MergedPreferenceScreenData(
        Set<SearchablePreferencePOJO> allPreferencesForSearch,
        Map<Integer, List<Integer>> preferencePathByPreferenceId,
        Map<Integer, Class<? extends PreferenceFragmentCompat>> hostByPreferenceId) {
}
