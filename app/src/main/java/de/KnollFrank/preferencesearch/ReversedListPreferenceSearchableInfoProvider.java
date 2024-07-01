package de.KnollFrank.preferencesearch;

import androidx.preference.Preference;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.search.SearchableInfoProvider;

public class ReversedListPreferenceSearchableInfoProvider implements SearchableInfoProvider {

    @Override
    public Optional<String> getSearchableInfo(final Preference preference) {
        return preference instanceof final ReversedListPreference reversedListPreference ?
                Optional.of(join(reversedListPreference.getEntries())) :
                Optional.empty();
    }

    private static String join(final CharSequence[] charSequences) {
        return charSequences == null ?
                "" :
                String.join(", ", charSequences);
    }
}
