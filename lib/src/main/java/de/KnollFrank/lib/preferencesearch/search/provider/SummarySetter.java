package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

public class SummarySetter {

    private final SummarySetters summarySetters;
    private final SearchableInfoSetter searchableInfoSetter;

    public SummarySetter(final SummarySetters summarySetters,
                         final SearchableInfoSetter searchableInfoSetter) {
        this.summarySetters = summarySetters;
        this.searchableInfoSetter = searchableInfoSetter;
    }

    public void setSummary(final Preference preference, final CharSequence summary) {
        final ISummarySetter summarySetter = getSummarySetter(preference.getClass());
        summarySetter.setSummary(preference, summary);
        searchableInfoSetter.setSearchableInfo(preference, summary);
    }

    private ISummarySetter getSummarySetter(final Class<? extends Preference> preferenceClass) {
        return summarySetters.summarySetterByPreferenceClass.getOrDefault(
                preferenceClass,
                new DefaultSummarySetter());
    }
}
