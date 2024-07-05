package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PreferenceDescriptions {

    public static SearchableInfoProviders getSearchableInfoProviders(final List<PreferenceDescription> preferenceDescriptions) {
        return new SearchableInfoProviders(
                collect(
                        preferenceDescriptions,
                        preferenceDescription -> preferenceDescription.searchableInfoProvider));
    }

    public static SummarySetters getSummarySetters(final List<PreferenceDescription> preferenceDescriptions) {
        return new SummarySetters(
                collect(
                        preferenceDescriptions,
                        preferenceDescription -> preferenceDescription.summarySetter));
    }

    public static SummaryResetterFactories getSummaryResetterFactories(final List<PreferenceDescription> preferenceDescriptions) {
        return new SummaryResetterFactories(
                collect(
                        preferenceDescriptions,
                        preferenceDescription -> preferenceDescription.summaryResetterFactory));
    }

    private static <T> Map<Class<? extends Preference>, T> collect(final List<PreferenceDescription> preferenceDescriptions,
                                                                   final Function<PreferenceDescription, T> valueMapper) {
        return preferenceDescriptions
                .stream()
                .collect(
                        Collectors.toMap(
                                preferenceDescription -> preferenceDescription.preferenceClass,
                                valueMapper));
    }
}
