package de.KnollFrank.preferencesearch.preference.custom;

import androidx.preference.Preference;

import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoProvider;

public class ReversedListPreferenceSearchableInfoProvider implements SearchableInfoProvider {

    @Override
    public String getSearchableInfo(final Preference reversedListPreference) {
        return join(((ReversedListPreference) reversedListPreference).getEntries());
    }

    private static String join(final CharSequence[] charSequences) {
        return charSequences == null ?
                "" :
                String.join(", ", charSequences);
    }
}
