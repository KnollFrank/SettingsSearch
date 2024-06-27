package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;

import java.util.Optional;

class PreferenceAttributes {

    public static Optional<CharSequence> getOptionalTitle(final Preference preference) {
        return Optional.ofNullable(preference.getTitle());
    }

    public static void setTitle(final Preference preference, final CharSequence title) {
        preference.setTitle(null);
        preference.setTitle(title);
    }

    public static Optional<CharSequence> getOptionalSummary(final Preference preference) {
        return Optional.ofNullable(preference.getSummary());
    }

    public static void setSummary(final Preference preference, final CharSequence summary) {
        preference.setSummary(null);
        preference.setSummary(summary);
    }
}
