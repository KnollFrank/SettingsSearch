package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.getOptionalTitle;
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
        unhighlightTitle(preference);
        unhighlightAndResetSummary(preference);
    }

    private static void unhighlightTitle(final Preference preference) {
        setTitle(
                preference,
                unhighlight(getOptionalTitle(preference)));
    }

    private void unhighlightAndResetSummary(final Preference preference) {
        setSummary(
                preference,
                unhighlight(summaryByPreference.get(preference)));
    }

    private static String unhighlight(final Optional<CharSequence> optionalCharSequence) {
        return optionalCharSequence
                .map(CharSequence::toString)
                .orElse(null);
    }
}
