package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.preference.Preference;

@FunctionalInterface
public interface SearchableInfoProvider<T extends Preference> {

    String getSearchableInfo(T preference);

    default SearchableInfoProvider<T> mergeWith(final SearchableInfoProvider<T> searchableInfoProvider) {
        return preference ->
                String.format(
                        "%s\n%s",
                        getSearchableInfo(preference),
                        searchableInfoProvider.getSearchableInfo(preference));
    }
}
