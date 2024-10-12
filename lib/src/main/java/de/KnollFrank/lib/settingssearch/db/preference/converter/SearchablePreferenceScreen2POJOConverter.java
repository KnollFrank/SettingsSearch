package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.PreferenceScreen;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;
import de.KnollFrank.lib.settingssearch.search.PreferenceSummary;
import de.KnollFrank.lib.settingssearch.search.PreferenceTitle;

class SearchablePreferenceScreen2POJOConverter {

    public static SearchablePreferenceScreenPOJO convert2POJO(final PreferenceScreen preferenceScreen) {
        return new SearchablePreferenceScreenPOJO(
                toStringOrNull(PreferenceTitle.getOptionalTitle(preferenceScreen)),
                toStringOrNull(PreferenceSummary.getOptionalSummary(preferenceScreen)),
                SearchablePreference2POJOConverter.convert2POJOs(
                        SearchablePreferenceCaster.cast(Preferences.getImmediateChildren(preferenceScreen))));
    }

    private static String toStringOrNull(final Optional<CharSequence> preferenceScreen) {
        return preferenceScreen
                .map(CharSequence::toString)
                .orElse(null);
    }
}
