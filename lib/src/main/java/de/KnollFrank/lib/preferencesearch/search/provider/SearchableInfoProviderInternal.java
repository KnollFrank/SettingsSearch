package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.Map;
import java.util.Optional;

public class SearchableInfoProviderInternal implements ISearchableInfoProviderInternal {

    private final Map<Class<? extends Preference>, SearchableInfoProvider<?>> searchableInfoProviderByPreferenceClass;

    public SearchableInfoProviderInternal(final Map<Class<? extends Preference>, SearchableInfoProvider<?>> searchableInfoProviderByPreferenceClass) {
        this.searchableInfoProviderByPreferenceClass = searchableInfoProviderByPreferenceClass;
    }

    @Override
    public Optional<String> getSearchableInfo(final Preference preference) {
        final Class<? extends Preference> preferenceClass = preference.getClass();
        if (!searchableInfoProviderByPreferenceClass.containsKey(preferenceClass)) {
            return Optional.empty();
        }
        final SearchableInfoProvider searchableInfoProvider = searchableInfoProviderByPreferenceClass.get(preferenceClass);
        final String searchableInfo = searchableInfoProvider.getSearchableInfo(preference);
        return Optional.of(searchableInfo);
    }
}
