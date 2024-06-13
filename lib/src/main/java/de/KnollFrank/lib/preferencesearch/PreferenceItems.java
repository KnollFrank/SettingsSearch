package de.KnollFrank.lib.preferencesearch;

import androidx.preference.ListPreference;
import androidx.preference.Preference;

import java.util.Arrays;
import java.util.Optional;

public class PreferenceItems {

    public static PreferenceItem getPreferenceItem(final Preference preference) {
        class _PreferenceItems {

            public static PreferenceItem getPreferenceItem(final Preference preference) {
                return new PreferenceItem(
                        asString(preference.getTitle()),
                        asString(preference.getSummary()),
                        Optional.ofNullable(preference.getKey()),
                        Optional.empty(),
                        getEntries(preference));
            }

            private static Optional<String> asString(final CharSequence charSequence) {
                return Optional
                        .ofNullable(charSequence)
                        .map(CharSequence::toString);
            }

            private static Optional<String> getEntries(final Preference preference) {
                if (!(preference instanceof ListPreference)) {
                    return Optional.empty();
                }
                return Optional
                        .ofNullable(((ListPreference) preference).getEntries())
                        .map(Arrays::toString);
            }
        }

        return _PreferenceItems.getPreferenceItem(preference);
    }
}
