package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.getSummaryAsString;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.getTitleAsString;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.setSummary;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.setTitle;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.preferencesearch.common.Preferences;

class PreferenceScreenUnhighlighter {

    public static void unhighlight(final PreferenceScreen preferenceScreen) {
        Preferences
                .getAllPreferences(preferenceScreen)
                .forEach(PreferenceScreenUnhighlighter::unhighlight);
    }

    private static void unhighlight(final Preference preference) {
        setTitle(preference, getTitleAsString(preference).orElse(null));
        setSummary(preference, getSummaryAsString(preference).orElse(null));
    }
}
