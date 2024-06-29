package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;

import java.util.Optional;

@FunctionalInterface
public interface SearchableInfoProvider {

    Optional<String> getSearchableInfo(Preference preference);
}
