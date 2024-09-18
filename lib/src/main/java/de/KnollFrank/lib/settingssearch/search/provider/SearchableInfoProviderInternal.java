package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.preference.Preference;

import java.util.Optional;

// FK-TODO: inline this class using searchableInfoProvider
public class SearchableInfoProviderInternal {

    private final SearchableInfoProvider searchableInfoProvider;

    public SearchableInfoProviderInternal(final SearchableInfoProvider searchableInfoProvider) {
        this.searchableInfoProvider = searchableInfoProvider;
    }

    public Optional<String> getSearchableInfo(final Preference preference) {
        return searchableInfoProvider.getSearchableInfo(preference);
    }
}
