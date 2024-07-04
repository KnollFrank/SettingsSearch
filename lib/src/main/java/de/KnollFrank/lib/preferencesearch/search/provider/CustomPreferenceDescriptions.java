package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomPreferenceDescriptions {

    public static Map<Class<? extends Preference>, SearchableInfoProvider<?>> getSearchableInfoProviders(final List<CustomPreferenceDescription> customPreferenceDescriptions) {
        return collect(
                customPreferenceDescriptions,
                customPreferenceDescription -> customPreferenceDescription.searchableInfoProvider);
    }

    public static SummarySetters getSummarySetters(final List<CustomPreferenceDescription> customPreferenceDescriptions) {
        return new SummarySetters(
                collect(
                        customPreferenceDescriptions,
                        customPreferenceDescription -> customPreferenceDescription.summarySetter));
    }

    public static SummaryResetterFactories getSummaryResetterFactories(final List<CustomPreferenceDescription> customPreferenceDescriptions) {
        return new SummaryResetterFactories(
                collect(
                        customPreferenceDescriptions,
                        customPreferenceDescription -> customPreferenceDescription.summaryResetterFactory));
    }

    private static <T> Map<Class<? extends Preference>, T> collect(final List<CustomPreferenceDescription> customPreferenceDescriptions,
                                                                   final Function<CustomPreferenceDescription, T> valueMapper) {
        return customPreferenceDescriptions
                .stream()
                .collect(
                        Collectors.toMap(
                                customPreferenceDescription -> customPreferenceDescription.preferenceClass,
                                valueMapper));
    }
}
