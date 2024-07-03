package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.search.provider.DefaultSummaryResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.ISummaryResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.ListPreferenceSummaryResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.SwitchPreferenceSummaryResetter;

// FK-TODO: rename class
public class PreferenceSummaryProvider {

    public static Map<Preference, ISummaryResetter> getSummaryResetterByPreference(final PreferenceScreen preferenceScreen) {
        return Preferences
                .getAllPreferences(preferenceScreen)
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                PreferenceSummaryProvider::getSummary));
    }

    private static ISummaryResetter getSummary(final Preference preference) {
        // FK-TODO: hier noch andere Preferences behandeln: SwitchPreference, ...
        if (ListPreference.class.equals(preference.getClass())) {
            return new ListPreferenceSummaryResetter((ListPreference) preference);
        }
        if (SwitchPreference.class.equals(preference.getClass())) {
            return new SwitchPreferenceSummaryResetter((SwitchPreference) preference);
        }
        return new DefaultSummaryResetter(preference);
    }
}
