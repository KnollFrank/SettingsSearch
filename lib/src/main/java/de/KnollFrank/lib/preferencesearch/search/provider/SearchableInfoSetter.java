package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

@FunctionalInterface
public interface SearchableInfoSetter {

    void setSearchableInfo(Preference preference, CharSequence searchableInfo);
}
