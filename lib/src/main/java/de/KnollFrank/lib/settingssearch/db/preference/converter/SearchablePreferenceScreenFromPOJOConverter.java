package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.BiMap;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

public class SearchablePreferenceScreenFromPOJOConverter {

    public record PreferenceScreenWithMap(PreferenceScreen preferenceScreen,
                                          BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
    }

    public static PreferenceScreenWithMap convertFromPOJO(final SearchablePreferenceScreenPOJO searchablePreferenceScreenPOJO,
                                                          final PreferenceManager preferenceManager) {
        final PreferenceScreen preferenceScreen = preferenceManager.createPreferenceScreen(preferenceManager.getContext());
        preferenceScreen.setTitle(searchablePreferenceScreenPOJO.title());
        preferenceScreen.setSummary(searchablePreferenceScreenPOJO.summary());
        return new PreferenceScreenWithMap(
                preferenceScreen,
                SearchablePreferenceFromPOJOConverter.addConvertedPOJOs2Parent(
                        searchablePreferenceScreenPOJO.children(),
                        preferenceScreen));
    }
}
