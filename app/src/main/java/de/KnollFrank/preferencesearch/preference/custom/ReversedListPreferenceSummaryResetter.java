package de.KnollFrank.preferencesearch.preference.custom;

import de.KnollFrank.lib.preferencesearch.search.provider.ISummaryResetter;

public class ReversedListPreferenceSummaryResetter implements ISummaryResetter {

    private final ReversedListPreference reversedListPreference;
    private final CharSequence summary;

    public ReversedListPreferenceSummaryResetter(final ReversedListPreference reversedListPreference) {
        this.reversedListPreference = reversedListPreference;
        this.summary = reversedListPreference.getSummary();
    }

    @Override
    public void resetSummary() {
        new ReversedListPreferenceSummarySetter().setSummary(reversedListPreference, summary);
    }
}
