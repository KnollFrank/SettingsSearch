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
import de.KnollFrank.lib.preferencesearch.search.provider.SummaryResetterFactories;
import de.KnollFrank.lib.preferencesearch.search.provider.SummarySetters;
import de.KnollFrank.lib.preferencesearch.search.provider.SwitchPreferenceSummaryResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.SwitchPreferenceSummarySetter;

// FK-TODO: rename class
public class PreferenceSummaryProvider {

    // FK-TODO: hier noch andere Preferences behandeln: SwitchPreference, ...
    public static SummarySetters createBuiltinSummarySetters() {
        return new SummarySetters(
                ImmutableMap
                        .<Class<? extends Preference>, ISummarySetter>builder()
                        .put(SwitchPreference.class, new SwitchPreferenceSummarySetter())
                        .build());
    }

    // FK-TODO: hier noch andere Preferences behandeln: SwitchPreference, ...
    public static SummaryResetterFactories createBuiltinSummaryResetterFactories() {
        return new SummaryResetterFactories(
                ImmutableMap
                        .<Class<? extends Preference>, Function<Preference, ? extends ISummaryResetter>>builder()
                        .put(SwitchPreference.class, preference -> new SwitchPreferenceSummaryResetter((SwitchPreference) preference))
                        .build());
    }

    // FK-TODO: move to another class?
    public static Map<Preference, ISummaryResetter> getSummaryResetters(
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
