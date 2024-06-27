package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.getTitleAsString;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.setSummary;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.setTitle;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Preferences;

class PreferenceScreenUnhighlighter {

    public static void unhighlight(final PreferenceScreen preferenceScreen,
                                   final Map<Preference, Optional<CharSequence>> summaryByPreference) {
        Preferences
                .getAllPreferences(preferenceScreen)
                .forEach(preference -> unhighlight(preference, summaryByPreference.get(preference)));
    }

    private static void unhighlight(final Preference preference, final Optional<CharSequence> summary) {
        setTitle(preference, getTitleAsString(preference).orElse(null));
        setSummary(preference, summary.orElse(null));
    }
}
