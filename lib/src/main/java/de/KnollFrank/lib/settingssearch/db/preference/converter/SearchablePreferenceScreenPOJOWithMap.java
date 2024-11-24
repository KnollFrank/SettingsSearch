package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;

import com.google.common.collect.BiMap;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

public record SearchablePreferenceScreenPOJOWithMap(
        SearchablePreferenceScreenPOJO searchablePreferenceScreen,
        BiMap<SearchablePreferencePOJO, Preference> pojoEntityMap) {
}
