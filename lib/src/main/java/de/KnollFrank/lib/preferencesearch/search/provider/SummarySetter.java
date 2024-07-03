package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.Map;

public class SummarySetter {

    private final Map<Class<? extends Preference>, ISummarySetter> summarySetterByPreferenceClass;

    public SummarySetter(Map<Class<? extends Preference>, ISummarySetter> summarySetterByPreferenceClass) {
        this.summarySetterByPreferenceClass = summarySetterByPreferenceClass;
    }

    public void setSummary(final Preference preference, final CharSequence summary) {
        final ISummarySetter summarySetter = getSummarySetter(preference.getClass());
        summarySetter.setSummary(preference, summary);
    }

    private ISummarySetter getSummarySetter(final Class<? extends Preference> preferenceClass) {
        return summarySetterByPreferenceClass.getOrDefault(preferenceClass, new DefaultSummarySetter());
    }
}
