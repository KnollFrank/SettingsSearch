package de.KnollFrank.lib.preferencesearch;

import static de.KnollFrank.lib.preferencesearch.search.PreferenceScreenResetter.createPreferenceScreenResetter;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.search.PreferenceScreenResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.SummaryResetterFactories;

public class MergedPreferenceScreen {

    public final PreferenceScreen preferenceScreen;
    private final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference;
    private final PreferenceScreenResetter preferenceScreenResetter;

    public MergedPreferenceScreen(final PreferenceScreen preferenceScreen,
                                  final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference,
                                  final SummaryResetterFactories summaryResetterFactories) {
        this.preferenceScreen = preferenceScreen;
        this.hostByPreference = hostByPreference;
        this.preferenceScreenResetter = createPreferenceScreenResetter(preferenceScreen, summaryResetterFactories);
    }

    public Optional<? extends Class<? extends PreferenceFragmentCompat>> findHostByPreference(final Preference preference) {
        return hostByPreference.containsKey(preference) ?
                Optional.of(hostByPreference.get(preference)) :
                Optional.empty();
    }

    public void resetPreferenceScreen() {
        preferenceScreenResetter.reset();
    }
}
