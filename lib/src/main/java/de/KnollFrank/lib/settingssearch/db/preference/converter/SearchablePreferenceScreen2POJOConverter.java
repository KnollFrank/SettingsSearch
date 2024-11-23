package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreference2POJOConverter.SearchablePreferencePOJOsWithMap;

import androidx.preference.PreferenceScreen;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

class SearchablePreferenceScreen2POJOConverter {

    public static SearchablePreferenceScreenPOJOWithMap convert2POJO(final PreferenceScreen preferenceScreen,
                                                                     final IdGenerator idGenerator) {
        final SearchablePreferencePOJOsWithMap searchablePreferencePOJOsWithMap =
                SearchablePreference2POJOConverter.convert2POJOs(
                        SearchablePreferenceCaster.cast(Preferences.getImmediateChildren(preferenceScreen)),
                        idGenerator);
        return new SearchablePreferenceScreenPOJOWithMap(
                new SearchablePreferenceScreenPOJO(
                        toStringOrNull(Optional.ofNullable(preferenceScreen.getTitle())),
                        toStringOrNull(Optional.ofNullable(preferenceScreen.getSummary())),
                        searchablePreferencePOJOsWithMap.searchablePreferencePOJOs()),
                searchablePreferencePOJOsWithMap.pojoEntityMap());
    }

    private static String toStringOrNull(final Optional<CharSequence> preferenceScreen) {
        return preferenceScreen
                .map(CharSequence::toString)
                .orElse(null);
    }
}
