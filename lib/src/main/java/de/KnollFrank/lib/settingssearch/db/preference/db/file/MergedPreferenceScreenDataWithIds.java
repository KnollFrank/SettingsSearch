package de.KnollFrank.lib.settingssearch.db.preference.db.file;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

record MergedPreferenceScreenDataWithIds(
        Set<SearchablePreference> preferences,
        Map<Integer, Optional<Integer>> predecessorIdByPreferenceId) {
}
