package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.SwitchPreference;

public class SwitchPreferenceSummaryResetter implements ISummaryResetter {

    private final SwitchPreference switchPreference;
    private final CharSequence summary;
    private final CharSequence summaryOn;
    private final CharSequence summaryOff;

    public SwitchPreferenceSummaryResetter(final SwitchPreference switchPreference) {
        this.switchPreference = switchPreference;
        this.summary = switchPreference.getSummary();
        this.summaryOn = switchPreference.getSummaryOn();
        this.summaryOff = switchPreference.getSummaryOff();
    }

    @Override
    public void resetSummary() {
        switchPreference.setSummaryOn(summaryOn);
        switchPreference.setSummaryOff(summaryOff);
        new DefaultSummarySetter().setSummary(switchPreference, summary);
    }
}
