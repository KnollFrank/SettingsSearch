package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter.SearchablePreferencesWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

class SearchablePreferenceScreen2POJOConverter {

    public static SearchablePreferenceScreenWithMap convert2POJO(final int id,
                                                                 final PreferenceScreen preferenceScreen,
                                                                 final PreferenceFragmentCompat hostOfPreferenceScreen,
                                                                 final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter,
                                                                 final Optional<SearchablePreference> predecessorOfPreferenceScreen) {
        final SearchablePreferencesWithMap searchablePreferencesWithMap =
                preference2SearchablePreferenceConverter.convert2POJOs(
                        Preferences.getImmediateChildren(preferenceScreen),
                        hostOfPreferenceScreen,
                        predecessorOfPreferenceScreen);
        return new SearchablePreferenceScreenWithMap(
                new SearchablePreferenceScreen(
                        id,
                        toStringOrNull(Optional.ofNullable(preferenceScreen.getTitle())),
                        toStringOrNull(Optional.ofNullable(preferenceScreen.getSummary())),
                        searchablePreferencesWithMap.searchablePreferences()),
                searchablePreferencesWithMap.pojoEntityMap());
    }

    private static String toStringOrNull(final Optional<CharSequence> preferenceScreen) {
        return preferenceScreen
                .map(CharSequence::toString)
                .orElse(null);
    }
}
