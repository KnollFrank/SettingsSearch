package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.ListPreference;

// FK-TODO: kann auch durch den DefaultSummaryResetter ersetzt werden
public class ListPreferenceSummaryResetter implements ISummarySetter, ISummaryResetter {

    private final ListPreference listPreference;
    private final CharSequence summary;

    public ListPreferenceSummaryResetter(final ListPreference listPreference) {
        this.listPreference = listPreference;
        this.summary = listPreference.getSummary();
    }

    @Override
    public void setSummary(final CharSequence summary) {
        listPreference.setSummary(summary);
    }

    @Override
    public void resetSummary() {
        setSummary(summary);
    }
}
