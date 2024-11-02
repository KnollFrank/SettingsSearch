package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.PreferencePath;

public record MergedPreferenceScreenData(
        Set<SearchablePreferencePOJO> preferences,
        Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference,
        Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
}
