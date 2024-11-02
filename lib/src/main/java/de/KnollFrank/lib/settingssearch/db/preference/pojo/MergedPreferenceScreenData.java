package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.PreferencePath;

public record MergedPreferenceScreenData(
        // FK-TODO: rename allPreferencesForSearch to preferences
        Set<SearchablePreferencePOJO> allPreferencesForSearch,
        Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference,
        Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
}
