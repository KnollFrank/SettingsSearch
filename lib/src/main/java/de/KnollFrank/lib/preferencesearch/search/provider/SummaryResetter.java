package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.Map;

public class SummaryResetter {

    private final Map<Preference, ISummaryResetter> summaryResetterByPreference;

    public SummaryResetter(final Map<Preference, ISummaryResetter> summaryResetterByPreference) {
        this.summaryResetterByPreference = summaryResetterByPreference;
    }

    public void resetSummary(final Preference preference) {
        summaryResetterByPreference.get(preference).resetSummary();
    }
}
