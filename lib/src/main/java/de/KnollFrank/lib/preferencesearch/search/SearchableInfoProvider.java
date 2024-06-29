package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;

import java.util.Optional;

@FunctionalInterface
public interface SearchableInfoProvider {

    Optional<String> getSearchableInfo(Preference preference);

    default SearchableInfoProvider orElse(final SearchableInfoProvider other) {
        return preference -> {
            final Optional<String> searchableInfo = getSearchableInfo(preference);
            return searchableInfo.isPresent() ?
                    searchableInfo :
                    other.getSearchableInfo(preference);
        };
    }
}
