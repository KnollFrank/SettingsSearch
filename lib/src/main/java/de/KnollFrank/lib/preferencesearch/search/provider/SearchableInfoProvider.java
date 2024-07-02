package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

@FunctionalInterface
public interface SearchableInfoProvider<T extends Preference> {

    String getSearchableInfo(T preference);
}
