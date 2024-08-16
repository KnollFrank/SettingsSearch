package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.Preference;

import java.util.Optional;

// FK-TODO: move this and similar classes to another package?
class PreferenceTitle {

    public static void setTitle(final Preference preference, final CharSequence title) {
        preference.setTitle(null);
        preference.setTitle(title);
    }

    public static Optional<CharSequence> getOptionalTitle(final Preference preference) {
        return Optional.ofNullable(preference.getTitle());
    }
}
