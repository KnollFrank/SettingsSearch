package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferencePOJOConverter.SearchablePreferencePOJOsWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

class SearchablePreferenceScreen2POJOConverter {

    public static SearchablePreferenceScreenPOJOWithMap convert2POJO(final PreferenceScreen preferenceScreen,
                                                                     final PreferenceFragmentCompat hostOfPreferenceScreen,
                                                                     final Preference2SearchablePreferencePOJOConverter preference2SearchablePreferencePOJOConverter) {
        final SearchablePreferencePOJOsWithMap searchablePreferencePOJOsWithMap =
                preference2SearchablePreferencePOJOConverter.convert2POJOs(
                        Preferences.getImmediateChildren(preferenceScreen),
                        hostOfPreferenceScreen);
        return new SearchablePreferenceScreenPOJOWithMap(
                new SearchablePreferenceScreen(
                        toStringOrNull(Optional.ofNullable(preferenceScreen.getTitle())),
                        toStringOrNull(Optional.ofNullable(preferenceScreen.getSummary())),
                        searchablePreferencePOJOsWithMap.searchablePreferences()),
                searchablePreferencePOJOsWithMap.pojoEntityMap());
    }

    private static String toStringOrNull(final Optional<CharSequence> preferenceScreen) {
        return preferenceScreen
                .map(CharSequence::toString)
                .orElse(null);
    }
}
