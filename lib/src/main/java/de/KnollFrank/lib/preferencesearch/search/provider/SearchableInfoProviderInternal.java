package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Maps;

public class SearchableInfoProviderInternal {

    private final SearchableInfoProviders searchableInfoProviders;

    public SearchableInfoProviderInternal(final SearchableInfoProviders searchableInfoProviders) {
        this.searchableInfoProviders = searchableInfoProviders;
    }

    public Optional<String> getSearchableInfo(final Preference preference) {
        return this
                .getSearchableInfoProvider(preference.getClass())
                .map(searchableInfoProvider -> searchableInfoProvider.getSearchableInfo(preference));
    }

    private Optional<SearchableInfoProvider> getSearchableInfoProvider(final Class<? extends Preference> preferenceClass) {
        return Maps.get(
                searchableInfoProviders.searchableInfoProvidersByPreferenceClass,
                preferenceClass);
    }
}
