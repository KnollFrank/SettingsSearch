package de.KnollFrank.lib.settingssearch.db.preference.converter;

import com.google.common.collect.BiMap;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

public record SearchablePreferenceScreenPOJOWithMap(
        SearchablePreferenceScreenPOJO searchablePreferenceScreen,
        BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
}
