package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import de.KnollFrank.lib.preferencesearch.search.PreferenceScreenResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.ISummaryResetter;

public class MergedPreferenceScreen {

    public final PreferenceScreen preferenceScreen;
    private final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference;
    private final PreferenceScreenResetter preferenceScreenResetter;

    public MergedPreferenceScreen(final PreferenceScreen preferenceScreen,
                                  final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference,
                                  final Map<Class<? extends Preference>, Function<Preference, ? extends ISummaryResetter>> summaryResetterFactoryByPreferenceClass) {
        this.preferenceScreen = preferenceScreen;
        this.hostByPreference = hostByPreference;
        this.preferenceScreenResetter = PreferenceScreenResetter.createPreferenceScreenResetter(preferenceScreen, summaryResetterFactoryByPreferenceClass);
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
