package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.getTitleAsString;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.setSummary;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.setTitle;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Preferences;

class PreferenceScreenResetter {

    private final Map<Preference, Optional<CharSequence>> summaryByPreference;

    public PreferenceScreenResetter(final Map<Preference, Optional<CharSequence>> summaryByPreference) {
        this.summaryByPreference = summaryByPreference;
    }

    public void reset(final PreferenceScreen preferenceScreen) {
        Preferences
                .getAllPreferences(preferenceScreen)
                .forEach(this::reset);
    }

    private void reset(final Preference preference) {
        setTitle(preference, getTitleAsString(preference).orElse(null));
        setSummary(preference, summaryByPreference.get(preference).orElse(null));
    }
}
