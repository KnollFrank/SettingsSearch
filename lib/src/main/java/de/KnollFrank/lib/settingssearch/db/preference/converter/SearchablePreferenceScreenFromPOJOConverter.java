package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

class SearchablePreferenceScreenFromPOJOConverter {

    public static PreferenceScreen convertFromPOJO(final SearchablePreferenceScreenPOJO searchablePreferenceScreenPOJO,
                                                   final PreferenceManager preferenceManager) {
        final PreferenceScreen preferenceScreen = preferenceManager.createPreferenceScreen(preferenceManager.getContext());
        preferenceScreen.setTitle(searchablePreferenceScreenPOJO.title());
        preferenceScreen.setSummary(searchablePreferenceScreenPOJO.summary());
        SearchablePreferenceFromPOJOConverter
                .convertFromPOJOs(searchablePreferenceScreenPOJO.children(), preferenceManager.getContext())
                .forEach(preferenceScreen::addPreference);
        return preferenceScreen;
    }
}
