package de.KnollFrank.lib.preferencesearch.client;

import androidx.preference.Preference;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoAttribute;

public class DefaultSearchableInfoAttribute implements SearchableInfoAttribute {

    private final Map<Preference, CharSequence> searchableInfoByPreference = new HashMap<>();

    @Override
    public Optional<CharSequence> getSearchableInfo(final Preference preference) {
        return Optional.ofNullable(searchableInfoByPreference.get(preference));
    }

    @Override
    public void setSearchableInfo(final Preference preference, final CharSequence searchableInfo) {
        searchableInfoByPreference.put(preference, searchableInfo);
    }
}
