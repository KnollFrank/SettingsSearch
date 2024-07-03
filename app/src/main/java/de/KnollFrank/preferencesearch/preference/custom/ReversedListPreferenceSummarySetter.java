package de.KnollFrank.preferencesearch.preference.custom;

import de.KnollFrank.lib.preferencesearch.search.provider.DefaultSummarySetter;
import de.KnollFrank.lib.preferencesearch.search.provider.ISummarySetter;

public class ReversedListPreferenceSummarySetter implements ISummarySetter<ReversedListPreference> {

    @Override
    public void setSummary(final ReversedListPreference reversedListPreference, final CharSequence summary) {
        new DefaultSummarySetter().setSummary(reversedListPreference, summary);
    }
}
