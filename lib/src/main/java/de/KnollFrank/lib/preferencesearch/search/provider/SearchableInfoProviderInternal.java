package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.Optional;

public class SearchableInfoProviderInternal implements ISearchableInfoProviderInternal {

    private final SearchableInfoProviders searchableInfoProviders;

    public SearchableInfoProviderInternal(final SearchableInfoProviders searchableInfoProviders) {
        this.searchableInfoProviders = searchableInfoProviders;
    }

    @Override
    public Optional<String> getSearchableInfo(final Preference preference) {
        return this
                .getSearchableInfoProvider(preference.getClass())
                .map(searchableInfoProvider -> searchableInfoProvider.getSearchableInfo(preference));
    }

    private Optional<SearchableInfoProvider> getSearchableInfoProvider(final Class<? extends Preference> preferenceClass) {
        if (!searchableInfoProviders.searchableInfoProvidersByPreferenceClass.containsKey(preferenceClass)) {
            return Optional.empty();
        }
        return Optional.of(searchableInfoProviders.searchableInfoProvidersByPreferenceClass.get(preferenceClass));
    }
}
