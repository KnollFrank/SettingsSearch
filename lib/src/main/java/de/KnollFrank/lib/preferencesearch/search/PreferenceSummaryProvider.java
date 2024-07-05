package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import com.google.common.collect.ImmutableMap;

import java.util.function.Function;

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
                        .put(
                                SwitchPreference.class,
                                new SwitchPreferenceSummarySetter())
                        .build());
    }

    // FK-TODO: hier noch andere Preferences behandeln: SwitchPreference, ...
    // FK-TODO: use CustomPreferenceDescription<SwitchPreference>
    public static SummaryResetterFactories createBuiltinSummaryResetterFactories() {
        return new SummaryResetterFactories(
                ImmutableMap
                        .<Class<? extends Preference>, Function<Preference, ? extends ISummaryResetter>>builder()
                        .put(
                                SwitchPreference.class,
                                preference -> new SwitchPreferenceSummaryResetter((SwitchPreference) preference))
                        .build());
    }
}
