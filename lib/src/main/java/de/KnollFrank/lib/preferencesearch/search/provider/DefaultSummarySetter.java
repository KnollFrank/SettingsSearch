package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

public class DefaultSummarySetter implements ISummarySetter<Preference> {

    @Override
    public void setSummary(final Preference preference, final CharSequence summary) {
        preference.setSummary(null);
        preference.setSummary(summary);
    }
}
