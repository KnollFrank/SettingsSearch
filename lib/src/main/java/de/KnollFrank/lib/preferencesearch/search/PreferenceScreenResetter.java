package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.getOptionalTitle;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.setTitle;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.search.provider.ISummaryResetter;

class PreferenceScreenResetter {

    private final Map<Preference, ISummaryResetter> summaryResetterByPreference;

    public PreferenceScreenResetter(final Map<Preference, ISummaryResetter> summaryResetterByPreference) {
        this.summaryResetterByPreference = summaryResetterByPreference;
    }

    public void reset(final PreferenceScreen preferenceScreen) {
        Preferences
                .getAllPreferences(preferenceScreen)
                .forEach(this::reset);
    }

    private void reset(final Preference preference) {
        unhighlightTitle(preference);
        resetSummary(preference);
    }

    private static void unhighlightTitle(final Preference preference) {
        setTitle(
                preference,
                unhighlight(getOptionalTitle(preference)));
    }

    private void resetSummary(final Preference preference) {
        // FK-TODO: hier braucht man keine Datenstruktur (Map), sondern einen Consumer<Preference>
        summaryResetterByPreference.get(preference).resetSummary();
    }

    private static String unhighlight(final Optional<CharSequence> optionalCharSequence) {
        return optionalCharSequence
                .map(CharSequence::toString)
                .orElse(null);
    }
}
