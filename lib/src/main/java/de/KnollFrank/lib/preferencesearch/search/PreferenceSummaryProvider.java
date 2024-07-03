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

    public static Map<Class<? extends Preference>, ISummarySetter> getSummarySetterByPreferenceClass() {
        // FK-TODO: hier noch andere Preferences behandeln: SwitchPreference, ...
        return ImmutableMap
                .<Class<? extends Preference>, ISummarySetter>builder()
                .put(SwitchPreference.class, new SwitchPreferenceSummarySetter())
                .build();
    }

    public static Map<Preference, ISummaryResetter> getSummaryResetterByPreference(final PreferenceScreen preferenceScreen) {
        return Preferences
                .getAllPreferences(preferenceScreen)
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                PreferenceSummaryProvider::getSummaryResetter));
    }

    private static ISummaryResetter getSummaryResetter(final Preference preference) {
        // FK-TODO: hier noch andere Preferences behandeln: SwitchPreference, ...
        if (SwitchPreference.class.equals(preference.getClass())) {
            return new SwitchPreferenceSummaryResetter((SwitchPreference) preference);
        }
        return new DefaultSummaryResetter(preference);
    }
}
