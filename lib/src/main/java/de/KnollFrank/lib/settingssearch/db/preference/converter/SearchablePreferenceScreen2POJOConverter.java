package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreference2POJOConverter.SearchablePreferencePOJOsWithMap;

import androidx.preference.PreferenceScreen;

import com.google.common.collect.BiMap;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;
import de.KnollFrank.lib.settingssearch.search.PreferenceSummary;
import de.KnollFrank.lib.settingssearch.search.PreferenceTitle;

class SearchablePreferenceScreen2POJOConverter {

    public record SearchablePreferenceScreenPOJOWithMap(
            SearchablePreferenceScreenPOJO searchablePreferenceScreen,
            BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
    }

    public static SearchablePreferenceScreenPOJOWithMap convert2POJO(final PreferenceScreen preferenceScreen,
                                                                     final IdGenerator idGenerator) {
        final SearchablePreferencePOJOsWithMap searchablePreferencePOJOsWithMap =
                SearchablePreference2POJOConverter.convert2POJOs(
                        SearchablePreferenceCaster.cast(Preferences.getImmediateChildren(preferenceScreen)),
                        idGenerator);
        return new SearchablePreferenceScreenPOJOWithMap(
                new SearchablePreferenceScreenPOJO(
                        toStringOrNull(PreferenceTitle.getOptionalTitle(preferenceScreen)),
                        toStringOrNull(PreferenceSummary.getOptionalSummary(preferenceScreen)),
                        searchablePreferencePOJOsWithMap.searchablePreferencePOJOs()),
                searchablePreferencePOJOsWithMap.pojoEntityMap());
    }

    private static String toStringOrNull(final Optional<CharSequence> preferenceScreen) {
        return preferenceScreen
                .map(CharSequence::toString)
                .orElse(null);
    }
}
