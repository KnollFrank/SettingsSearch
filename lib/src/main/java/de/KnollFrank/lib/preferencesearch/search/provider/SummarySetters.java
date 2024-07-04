package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class SummarySetters {

    // FK-TODO: encapsulate Map<Class<? extends Preference>, ISummarySetter> in a new class named SummarySetters
    public static Map<Class<? extends Preference>, ISummarySetter> combineSummarySetters(
            final Map<Class<? extends Preference>, ISummarySetter> summarySetters1,
            final Map<Class<? extends Preference>, ISummarySetter> summarySetters2) {
        return ImmutableMap.
                <Class<? extends Preference>, ISummarySetter>builder()
                .putAll(summarySetters1)
                .putAll(summarySetters2)
                .build();
    }
}
