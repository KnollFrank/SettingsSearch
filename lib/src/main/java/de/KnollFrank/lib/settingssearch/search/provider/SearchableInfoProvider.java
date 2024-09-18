package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.preference.Preference;

import java.util.Optional;

@FunctionalInterface
public interface SearchableInfoProvider {

    Optional<String> getSearchableInfo(Preference preference);

    default SearchableInfoProvider orElse(final SearchableInfoProvider other) {
        return preference ->
                this
                        .getSearchableInfo(preference)
                        .or(() -> other.getSearchableInfo(preference));
    }
}
