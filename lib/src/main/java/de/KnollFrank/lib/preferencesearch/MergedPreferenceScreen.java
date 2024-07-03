package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.search.provider.ISummaryResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.ISummarySetter;

public class MergedPreferenceScreen {

    public final PreferenceScreen preferenceScreen;
    private final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference;
    public final Map<Preference, ISummarySetter> summarySetterByPreference;
    public final Map<Preference, ISummaryResetter> summaryResetterByPreference;

    public MergedPreferenceScreen(final PreferenceScreen preferenceScreen,
                                  final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference,
                                  final Map<Preference, ISummarySetter> summarySetterByPreference,
                                  final Map<Preference, ISummaryResetter> summaryResetterByPreference) {
        this.preferenceScreen = preferenceScreen;
        this.hostByPreference = hostByPreference;
        this.summarySetterByPreference = summarySetterByPreference;
        this.summaryResetterByPreference = summaryResetterByPreference;
    }

    public Optional<? extends Class<? extends PreferenceFragmentCompat>> findHostByPreference(final Preference preference) {
        return hostByPreference.containsKey(preference) ?
                Optional.of(hostByPreference.get(preference)) :
                Optional.empty();
    }
}
