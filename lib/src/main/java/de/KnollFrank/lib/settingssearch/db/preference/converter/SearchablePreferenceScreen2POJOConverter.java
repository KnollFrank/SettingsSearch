package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

class SearchablePreferenceScreen2POJOConverter {

    public static SearchablePreferenceScreenPOJO convert2POJO(final PreferenceScreen preferenceScreen) {
        return new SearchablePreferenceScreenPOJO(
                SearchablePreference2POJOConverter.convert2POJOs(
                        SearchablePreferenceCaster.cast(Preferences.getDirectChildren(preferenceScreen))));
    }
}
