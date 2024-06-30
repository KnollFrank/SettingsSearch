package de.KnollFrank.preferencesearch;

import androidx.preference.Preference;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.search.SearchableInfoProvider;

class ReversedListPreferenceSearchableInfoProvider implements SearchableInfoProvider {

    @Override
    public Optional<String> getSearchableInfo(final Preference preference) {
        return preference instanceof final ReversedListPreference reversedListPreference ?
                Optional.of(enumerateNullableEntries(reversedListPreference.getEntries())) :
                Optional.empty();
    }

    private static String enumerateNullableEntries(final CharSequence[] entries) {
        return entries == null ? "" : enumerate(entries);
    }

    private static String enumerate(final CharSequence[] entries) {
        return String.join(", ", entries);
    }
}
