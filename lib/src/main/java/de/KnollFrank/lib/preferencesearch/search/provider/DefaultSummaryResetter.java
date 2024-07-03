package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

public class DefaultSummaryResetter implements ISummaryResetter {

    private final Preference preference;
    private final CharSequence summary;

    public DefaultSummaryResetter(final Preference preference) {
        this.preference = preference;
        this.summary = preference.getSummary();
    }

    @Override
    public void resetSummary() {
        new DefaultSummarySetter().setSummary(preference, summary);
    }
}
