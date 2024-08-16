package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.preference.Preference;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Maps;

public class SearchableInfoAttribute implements SearchableInfoSetter, SearchableInfoGetter {

    private final Map<Preference, CharSequence> searchableInfoByPreference = new HashMap<>();

    @Override
    public void setSearchableInfo(final Preference preference, final CharSequence searchableInfo) {
        searchableInfoByPreference.put(preference, searchableInfo);
    }

    @Override
    public Optional<CharSequence> getSearchableInfo(final Preference preference) {
        return Maps.get(searchableInfoByPreference, preference);
    }
}
