package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;

import java.util.Optional;

public class PreferenceAttributes {

    public static Optional<String> getTitleAsString(final Preference preference) {
        return asString(preference.getTitle());
    }

    public static void setTitle(final Preference preference, final CharSequence title) {
        preference.setTitle(null);
        preference.setTitle(title);
    }

    public static Optional<String> getSummaryAsString(final Preference preference) {
        return asString(preference.getSummary());
    }

    public static void setSummary(final Preference preference, final CharSequence summary) {
        preference.setSummary(null);
        preference.setSummary(summary);
    }

    private static Optional<String> asString(final CharSequence charSequence) {
        return Optional
                .ofNullable(charSequence)
                .map(CharSequence::toString);
    }
}
