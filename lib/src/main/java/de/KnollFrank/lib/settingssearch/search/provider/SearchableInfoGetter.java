package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.preference.Preference;

import java.util.Optional;

@FunctionalInterface
public interface SearchableInfoGetter {

    Optional<CharSequence> getSearchableInfo(Preference preference);
}
