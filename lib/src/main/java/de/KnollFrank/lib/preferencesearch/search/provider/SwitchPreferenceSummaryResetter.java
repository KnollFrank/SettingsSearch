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
    public void setSummary(final CharSequence summary) {
        switchPreference.setSummaryOn(null);
        switchPreference.setSummaryOff(null);
        // FK-TODO: verwende DefaultSummaryResetter?
        switchPreference.setSummary(null);
        switchPreference.setSummary(summary);
    }

    @Override
    public void resetSummary() {
        switchPreference.setSummaryOn(summaryOn);
        switchPreference.setSummaryOff(summaryOff);
        // FK-TODO: verwende DefaultSummaryResetter?
        switchPreference.setSummary(null);
        switchPreference.setSummary(summary);
    }
}
