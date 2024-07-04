package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.Map;

import de.KnollFrank.lib.preferencesearch.common.Maps;

public class SummarySetters {

    public final Map<Class<? extends Preference>, ISummarySetter> summarySetterByPreferenceClass;

    public SummarySetters(final Map<Class<? extends Preference>, ISummarySetter> summarySetterByPreferenceClass) {
        this.summarySetterByPreferenceClass = summarySetterByPreferenceClass;
    }

    public SummarySetters combineWith(final SummarySetters other) {
        return new SummarySetters(
                Maps.merge(
                        this.summarySetterByPreferenceClass,
                        other.summarySetterByPreferenceClass));
    }
}
