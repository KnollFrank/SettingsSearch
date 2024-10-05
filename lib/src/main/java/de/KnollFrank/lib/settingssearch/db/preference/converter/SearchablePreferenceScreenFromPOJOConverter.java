package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

class SearchablePreferenceScreenFromPOJOConverter {

    public static PreferenceScreen convertFromPOJO(final SearchablePreferenceScreenPOJO searchablePreferenceScreenPOJO,
                                                   final PreferenceManager preferenceManager) {
        final PreferenceScreen preferenceScreen = preferenceManager.createPreferenceScreen(preferenceManager.getContext());
        SearchablePreferenceFromPOJOConverter
                .convertFromPOJOs(searchablePreferenceScreenPOJO.children(), preferenceManager.getContext())
                .forEach(preferenceScreen::addPreference);
        return preferenceScreen;
    }
}
