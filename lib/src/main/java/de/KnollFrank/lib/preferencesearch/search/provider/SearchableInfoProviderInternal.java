package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Maps;

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
        final Optional<SearchableInfoProvider<?>> searchableInfoProvider =
                Maps.get(
                        searchableInfoProviders.searchableInfoProvidersByPreferenceClass,
                        preferenceClass);
        return Optional.ofNullable(searchableInfoProvider.orElse(null));
    }
}
