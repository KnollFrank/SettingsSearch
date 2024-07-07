package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.DropDownPreference;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Strings;

class DropDownPreferenceSearchableInfoProvider implements SearchableInfoProvider<DropDownPreference> {

    @Override
    public String getSearchableInfo(final DropDownPreference preference) {
        return Strings.join(", ", Optional.ofNullable(preference.getEntries()));
    }
}
