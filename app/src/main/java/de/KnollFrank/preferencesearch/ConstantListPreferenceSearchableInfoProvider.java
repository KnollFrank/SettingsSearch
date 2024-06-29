package de.KnollFrank.preferencesearch;

import androidx.preference.Preference;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.search.SearchableInfoProvider;

class ConstantListPreferenceSearchableInfoProvider implements SearchableInfoProvider {

    @Override
    public Optional<String> getSearchableInfo(final Preference preference) {
        return preference instanceof final ConstantListPreference constantListPreference ?
                Optional.of(enumerateNullableEntries(constantListPreference.getEntries())) :
                Optional.empty();
    }

    private static String enumerateNullableEntries(final CharSequence[] entries) {
        return entries == null ? "" : enumerate(entries);
    }

    private static String enumerate(final CharSequence[] entries) {
        return String.join(", ", entries);
    }
}
