package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.Preference;

import java.util.Optional;

public class PreferenceSummary {

    public static void setSummary(final Preference preference, final CharSequence summary) {
        preference.setSummary(null);
        preference.setSummary(summary);
    }

    public static Optional<CharSequence> getOptionalSummary(final Preference preference) {
        return Optional.ofNullable(preference.getSummary());
    }
}
