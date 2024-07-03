package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.search.provider.DefaultSummaryResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.ISummaryResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.ISummarySetter;
import de.KnollFrank.lib.preferencesearch.search.provider.SwitchPreferenceSummaryResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.SwitchPreferenceSummarySetter;

// FK-TODO: rename class
public class PreferenceSummaryProvider {

    // FK-TODO: refactor
    public static Map<Class<? extends Preference>, ISummarySetter> getSummarySetters(
            final Map<Class<? extends Preference>, ISummarySetter> summarySetterByPreferenceClass) {
        return ImmutableMap.
                <Class<? extends Preference>, ISummarySetter>builder()
                .putAll(getBuiltinSummarySetters())
                .putAll(summarySetterByPreferenceClass)
                .build();
    }

    public static Map<Preference, ISummaryResetter> getSummaryResetters(
            final PreferenceScreen preferenceScreen,
            final Map<Class<? extends Preference>, Function<Preference, ? extends ISummaryResetter>> summaryResetterFactoryByPreferenceClass) {
        return Preferences
                .getAllPreferences(preferenceScreen)
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                preference -> getSummaryResetter(preference, summaryResetterFactoryByPreferenceClass)));
    }

    // FK-TODO: hier noch andere Preferences behandeln: SwitchPreference, ...
    private static Map<Class<? extends Preference>, ISummarySetter> getBuiltinSummarySetters() {
        return ImmutableMap
                .<Class<? extends Preference>, ISummarySetter>builder()
                .put(SwitchPreference.class, new SwitchPreferenceSummarySetter())
                .build();
    }

    private static ISummaryResetter getSummaryResetter(
            final Preference preference,
            final Map<Class<? extends Preference>, Function<Preference, ? extends ISummaryResetter>> summaryResetterFactoryByPreferenceClass) {
        // FK-TODO: hier noch andere Preferences behandeln: SwitchPreference, ...
        if (SwitchPreference.class.equals(preference.getClass())) {
            return new SwitchPreferenceSummaryResetter((SwitchPreference) preference);
        }
        // FK-TODO: refactor
        if (summaryResetterFactoryByPreferenceClass.containsKey(preference.getClass())) {
            final Function<Preference, ? extends ISummaryResetter> summaryResetterFactory = summaryResetterFactoryByPreferenceClass.get(preference.getClass());
            return summaryResetterFactory.apply(preference);
        }
        return new DefaultSummaryResetter(preference);
    }
}
