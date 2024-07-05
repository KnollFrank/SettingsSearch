package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.Map;

public class SummarySetters {

    public final Map<Class<? extends Preference>, ISummarySetter> summarySetterByPreferenceClass;

    public SummarySetters(final Map<Class<? extends Preference>, ISummarySetter> summarySetterByPreferenceClass) {
        this.summarySetterByPreferenceClass = summarySetterByPreferenceClass;
    }
}
