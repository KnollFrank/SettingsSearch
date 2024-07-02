package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.ListPreference;

import de.KnollFrank.lib.preferencesearch.common.Strings;

class ListPreferenceSearchableInfoProvider implements SearchableInfoProvider<ListPreference> {

    @Override
    public String getSearchableInfo(final ListPreference preference) {
        return Strings.join(preference.getEntries());
    }
}
