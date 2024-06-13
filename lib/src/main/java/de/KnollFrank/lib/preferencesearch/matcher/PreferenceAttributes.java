package de.KnollFrank.lib.preferencesearch.matcher;

import androidx.preference.ListPreference;
import androidx.preference.Preference;

import java.util.Arrays;
import java.util.Optional;

class PreferenceAttributes {

    public static Optional<String> getTitle(final Preference preference) {
        return asString(preference.getTitle());
    }

    public static Optional<String> getSummary(final Preference preference) {
        return asString(preference.getSummary());
    }

    public static Optional<String> getEntries(final Preference preference) {
        if (!(preference instanceof ListPreference)) {
            return Optional.empty();
        }
        return Optional
                .ofNullable(((ListPreference) preference).getEntries())
                .map(Arrays::toString);
    }

    private static Optional<String> asString(final CharSequence charSequence) {
        return Optional
                .ofNullable(charSequence)
                .map(CharSequence::toString);
    }
}
