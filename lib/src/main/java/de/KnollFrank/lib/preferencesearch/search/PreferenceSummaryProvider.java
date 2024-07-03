package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.search.provider.DefaultSummaryResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.ListPreferenceSummaryResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.SummaryResetter;

public class PreferenceSummaryProvider {

    public static Map<Preference, SummaryResetter> getSummaryResetterByPreference(final PreferenceScreen preferenceScreen) {
        return Preferences
                .getAllPreferences(preferenceScreen)
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                PreferenceSummaryProvider::getSummary));
    }

    private static SummaryResetter getSummary(final Preference preference) {
        // FK-TODO: hier noch andere Preferences behandeln: SwitchPreference, ...
        if (ListPreference.class.equals(preference.getClass())) {
            return new ListPreferenceSummaryResetter((ListPreference) preference);
        }
        return new DefaultSummaryResetter(preference);
    }
}
