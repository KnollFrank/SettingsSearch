package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;

import java.util.Optional;

public class DefaultSearchableInfoProvider implements SearchableInfoProvider {

    @Override
    public Optional<String> getSearchableInfo(final Preference preference) {
        return DefaultSearchableInfoProvider
                .getEntries(preference)
                .map(DefaultSearchableInfoProvider::enumerate);
    }

    private static Optional<CharSequence[]> getEntries(final Preference preference) {
        if (preference instanceof final ListPreference listPreference) {
            return Optional.ofNullable(listPreference.getEntries());
        }
        if (preference instanceof final MultiSelectListPreference multiSelectListPreference) {
            return Optional.ofNullable(multiSelectListPreference.getEntries());
        }
        return Optional.empty();
    }

    private static String enumerate(final CharSequence[] entries) {
        return String.join(", ", entries);
    }
}
