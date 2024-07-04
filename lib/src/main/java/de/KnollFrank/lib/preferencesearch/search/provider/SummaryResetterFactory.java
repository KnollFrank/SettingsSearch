package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.Preferences;

public class SummaryResetterFactory {

    public static SummaryResetter createSummaryResetter(final PreferenceScreen preferenceScreen,
                                                        final SummaryResetterFactories summaryResetterFactories) {
        return new SummaryResetter(getSummaryResetters(preferenceScreen, summaryResetterFactories));
    }

    private static Map<Preference, ISummaryResetter> getSummaryResetters(
            final PreferenceScreen preferenceScreen,
            final SummaryResetterFactories summaryResetterFactories) {
        return Preferences
                .getAllPreferences(preferenceScreen)
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                preference -> getSummaryResetter(preference, summaryResetterFactories)));
    }

    private static ISummaryResetter getSummaryResetter(
            final Preference preference,
            final SummaryResetterFactories summaryResetterFactories) {
        return summaryResetterFactories
                .summaryResetterFactoryByPreferenceClass
                .getOrDefault(preference.getClass(), DefaultSummaryResetter::new)
                .apply(preference);
    }
}
