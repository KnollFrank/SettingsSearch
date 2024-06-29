package de.KnollFrank.lib.preferencesearch.search;

import java.util.Optional;

public class SearchableInfoProviders {

    // FK-TODO: replace with default method named orElse() in SearchableInfoProvider
    public static SearchableInfoProvider merge(final SearchableInfoProvider first,
                                               final SearchableInfoProvider second) {
        return preference -> {
            final Optional<String> searchableInfo = first.getSearchableInfo(preference);
            return searchableInfo.isPresent() ?
                    searchableInfo :
                    second.getSearchableInfo(preference);
        };
    }
}
