package de.KnollFrank.lib.preferencesearch.search.matcher;

import androidx.preference.Preference;

import java.util.Optional;

class PreferenceAttributes {

    public static Optional<String> getTitle(final Preference preference) {
        return asString(preference.getTitle());
    }

    public static Optional<String> getSummary(final Preference preference) {
        return asString(preference.getSummary());
    }

    private static Optional<String> asString(final CharSequence charSequence) {
        return Optional
                .ofNullable(charSequence)
                .map(CharSequence::toString);
    }
}
