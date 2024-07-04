package de.KnollFrank.preferencesearch.preference.custom;

import de.KnollFrank.lib.preferencesearch.search.provider.DefaultSummarySetter;
import de.KnollFrank.lib.preferencesearch.search.provider.ISummarySetter;

public class ReversedListPreferenceSummarySetter implements ISummarySetter<ReversedListPreference> {

    @Override
    public void setSummary(final ReversedListPreference reversedListPreference, final CharSequence summary) {
        doWithSummarySetterEnabled(
                reversedListPreference,
                () -> new DefaultSummarySetter().setSummary(reversedListPreference, summary));
    }

    private void doWithSummarySetterEnabled(final ReversedListPreference reversedListPreference,
                                            final Runnable runnable) {
        final boolean memo = reversedListPreference.isSummarySetterEnabled();
        reversedListPreference.setSummarySetterEnabled(true);
        runnable.run();
        reversedListPreference.setSummarySetterEnabled(memo);
    }
}
