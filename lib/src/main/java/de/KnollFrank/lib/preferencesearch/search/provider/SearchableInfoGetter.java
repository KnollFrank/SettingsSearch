package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

@FunctionalInterface
public interface SearchableInfoGetter {

    CharSequence getSearchableInfo(Preference preference);
}
