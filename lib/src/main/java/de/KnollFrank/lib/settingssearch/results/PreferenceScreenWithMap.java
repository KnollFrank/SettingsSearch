package de.KnollFrank.lib.settingssearch.results;

import androidx.preference.PreferenceScreen;

import com.google.common.collect.BiMap;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public record PreferenceScreenWithMap(PreferenceScreen preferenceScreen,
                                      BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
}
