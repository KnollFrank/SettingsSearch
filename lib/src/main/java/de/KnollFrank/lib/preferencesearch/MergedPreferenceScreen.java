package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Maps;
import de.KnollFrank.lib.preferencesearch.search.PreferenceScreenResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoAttribute;

public class MergedPreferenceScreen {

    public final PreferenceScreen preferenceScreen;
    private final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference;
    private final PreferenceScreenResetter preferenceScreenResetter;

    public MergedPreferenceScreen(final PreferenceScreen preferenceScreen,
                                  final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference,
                                  final SearchableInfoAttribute searchableInfoAttribute) {
        this.preferenceScreen = preferenceScreen;
        this.hostByPreference = hostByPreference;
        this.preferenceScreenResetter = new PreferenceScreenResetter(preferenceScreen, searchableInfoAttribute);
    }

    public Optional<? extends Class<? extends PreferenceFragmentCompat>> findHostByPreference(final Preference preference) {
        return Maps.get(hostByPreference, preference);
    }

    public void resetPreferenceScreen() {
        preferenceScreenResetter.reset();
    }
}
