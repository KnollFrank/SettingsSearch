package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.search.PreferenceScreenResetter;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class MergedPreferenceScreen {

    public final PreferenceScreen searchablePreferenceScreen;
    public final Set<PreferenceCategory> isNonClickable;
    private final Map<Preference, PreferenceFragmentCompat> hostByPreference;
    public final Map<Preference, PreferencePath> preferencePathByPreference;
    private final PreferenceScreenResetter preferenceScreenResetter;

    public MergedPreferenceScreen(final PreferenceScreen searchablePreferenceScreen,
                                  final Set<PreferenceCategory> isNonClickable,
                                  final Map<Preference, PreferenceFragmentCompat> hostByPreference,
                                  final Map<Preference, PreferencePath> preferencePathByPreference,
                                  final SearchableInfoAttribute searchableInfoAttribute) {
        this.searchablePreferenceScreen = searchablePreferenceScreen;
        this.isNonClickable = isNonClickable;
        this.hostByPreference = hostByPreference;
        this.preferencePathByPreference = preferencePathByPreference;
        this.preferenceScreenResetter = new PreferenceScreenResetter(searchablePreferenceScreen, searchableInfoAttribute);
    }

    public Optional<? extends PreferenceFragmentCompat> findHost(final Preference preference) {
        return Maps.get(hostByPreference, preference);
    }

    public void resetPreferenceScreen() {
        preferenceScreenResetter.reset();
    }
}
