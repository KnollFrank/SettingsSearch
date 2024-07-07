package de.KnollFrank.lib.preferencesearch.search.provider;


import static de.KnollFrank.lib.preferencesearch.common.Strings.joinNonNullElements;

import androidx.preference.SwitchPreference;

import java.util.Arrays;

class SwitchPreferenceSearchableInfoProvider implements SearchableInfoProvider<SwitchPreference> {

    @Override
    public String getSearchableInfo(final SwitchPreference preference) {
        return joinNonNullElements(
                ", ",
                Arrays.asList(
                        preference.getSummaryOff(),
                        preference.getSummaryOn()));
    }
}
