package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Maps;

public class SearchableInfoProviderInternal {

    private final Map<Class<? extends Preference>, SearchableInfoProvider> searchableInfoProviderByPreferenceClass;

    public SearchableInfoProviderInternal(final Map<Class<? extends Preference>, SearchableInfoProvider> searchableInfoProviderByPreferenceClass) {
        this.searchableInfoProviderByPreferenceClass = searchableInfoProviderByPreferenceClass;
    }

    public Optional<String> getSearchableInfo(final Preference preference) {
        return this
                .getSearchableInfoProvider(preference.getClass())
                .map(searchableInfoProvider -> searchableInfoProvider.getSearchableInfo(preference));
    }

    private Optional<SearchableInfoProvider> getSearchableInfoProvider(final Class<? extends Preference> preferenceClass) {
        return Maps.get(
                searchableInfoProviderByPreferenceClass,
                preferenceClass);
    }
}
