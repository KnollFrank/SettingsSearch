package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.preference.Preference;

@FunctionalInterface
public interface SearchableInfoProvider {

    String getSearchableInfo(Preference preference);

    default SearchableInfoProvider mergeWith(final SearchableInfoProvider searchableInfoProvider) {
        return preference ->
                String.format(
                        "%s\n%s",
                        getSearchableInfo(preference),
                        searchableInfoProvider.getSearchableInfo(preference));
    }
}
