package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.SwitchPreference;

public class SwitchPreferenceSummarySetter implements ISummarySetter<SwitchPreference> {

    @Override
    public void setSummary(final SwitchPreference switchPreference, final CharSequence summary) {
        switchPreference.setSummaryOn(null);
        switchPreference.setSummaryOff(null);
        new DefaultSummarySetter().setSummary(switchPreference, summary);
    }
}
