package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

class SearchablePreferenceScreenPOJOConverter {

    public static SearchablePreferenceScreenPOJO convert2POJO(final PreferenceScreen preferenceScreen) {
        return new SearchablePreferenceScreenPOJO(
                SearchablePreferencePOJOConverter.convert2POJOs(
                        SearchablePreferenceCaster.cast(Preferences.getDirectChildren(preferenceScreen))));
    }
}
