package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;

import java.util.Optional;

class PreferenceTitle {

    public static void setTitle(final Preference preference, final CharSequence title) {
        preference.setTitle(null);
        preference.setTitle(title);
    }

    public static Optional<CharSequence> getOptionalTitle(final Preference preference) {
        return Optional.ofNullable(preference.getTitle());
    }
}
