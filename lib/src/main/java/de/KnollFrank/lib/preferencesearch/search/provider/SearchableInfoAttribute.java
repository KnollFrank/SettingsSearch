package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SearchableInfoAttribute implements SearchableInfoSetter, SearchableInfoGetter {

    private final Map<Preference, CharSequence> searchableInfoByPreference = new HashMap<>();

    @Override
    public void setSearchableInfo(final Preference preference, final CharSequence searchableInfo) {
        searchableInfoByPreference.put(preference, searchableInfo);
    }

    @Override
    public Optional<CharSequence> getSearchableInfo(final Preference preference) {
        return Optional.ofNullable(searchableInfoByPreference.get(preference));
    }
}
