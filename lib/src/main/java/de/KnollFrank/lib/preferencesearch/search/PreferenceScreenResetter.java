package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.getOptionalTitle;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.setTitle;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.search.provider.SummaryResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.SummaryResetterFactories;

public class PreferenceScreenResetter {

    private final PreferenceScreen preferenceScreen;
    private final SummaryResetter summaryResetter;

    public PreferenceScreenResetter(final PreferenceScreen preferenceScreen,
                                    final SummaryResetter summaryResetter) {
        this.preferenceScreen = preferenceScreen;
        this.summaryResetter = summaryResetter;
    }

    public static PreferenceScreenResetter createPreferenceScreenResetter(
            final PreferenceScreen preferenceScreen,
            final SummaryResetterFactories summaryResetterFactories) {
        return new PreferenceScreenResetter(
                preferenceScreen,
                new SummaryResetter(
                        PreferenceSummaryProvider.getSummaryResetters(
                                preferenceScreen,
                                summaryResetterFactories)));
    }

    public void reset() {
        Preferences
                .getAllPreferences(preferenceScreen)
                .forEach(this::reset);
    }

    private void reset(final Preference preference) {
        unhighlightTitle(preference);
        summaryResetter.resetSummary(preference);
    }

    private static void unhighlightTitle(final Preference preference) {
        setTitle(
                preference,
                unhighlight(getOptionalTitle(preference)));
    }

    private static String unhighlight(final Optional<CharSequence> optionalCharSequence) {
        return optionalCharSequence
                .map(CharSequence::toString)
                .orElse(null);
    }
}
