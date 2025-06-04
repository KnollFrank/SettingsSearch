package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;

import com.google.common.collect.BiMap;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public record SearchablePreferenceScreenWithMap(
        SearchablePreferenceScreenEntity searchablePreferenceScreen,
        BiMap<SearchablePreferenceEntity, Preference> pojoEntityMap) {
}
