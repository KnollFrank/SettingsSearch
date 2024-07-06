package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;

import java.util.Optional;

class PreferenceAttributes {

    // FK-TODO: DRY with setSummary
    public static void setTitle(final Preference preference, final CharSequence title) {
        preference.setTitle(null);
        preference.setTitle(title);
    }

    // FK-TODO: DRY with getOptionalSummary
    public static Optional<CharSequence> getOptionalTitle(final Preference preference) {
        return Optional.ofNullable(preference.getTitle());
    }

    public static void setSummary(final Preference preference, final CharSequence summary) {
        preference.setSummary(null);
        preference.setSummary(summary);
    }

    public static Optional<CharSequence> getOptionalSummary(final Preference preference) {
        return Optional.ofNullable(preference.getSummary());
    }
}
