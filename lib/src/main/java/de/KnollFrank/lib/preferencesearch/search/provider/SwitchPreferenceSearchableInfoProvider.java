package de.KnollFrank.lib.preferencesearch.search.provider;

import static de.KnollFrank.lib.preferencesearch.common.Strings.joinNonNullCharSequences;

import androidx.preference.SwitchPreference;

class SwitchPreferenceSearchableInfoProvider implements SearchableInfoProvider<SwitchPreference> {

    @Override
    public String getSearchableInfo(final SwitchPreference preference) {
        return joinNonNullCharSequences(
                ", ",
                preference.getSummaryOff(),
                preference.getSummaryOn());
    }
}
