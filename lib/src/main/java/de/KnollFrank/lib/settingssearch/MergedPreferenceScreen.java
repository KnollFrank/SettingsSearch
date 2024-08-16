package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.search.PreferenceScreenResetter;
import de.KnollFrank.lib.settingssearch.search.provider.PreferenceDescription;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class MergedPreferenceScreen {

    public final PreferenceScreen preferenceScreen;
    public final Set<PreferenceCategory> isNonClickable;
    private final Map<Preference, PreferenceFragmentCompat> hostByPreference;
    private final Map<Preference, String> searchableInfoByPreference;
    public final Map<Preference, PreferencePath> preferencePathByPreference;
    private final PreferenceScreenResetter preferenceScreenResetter;

    public MergedPreferenceScreen(final PreferenceScreen preferenceScreen,
                                  final Set<PreferenceCategory> isNonClickable,
                                  final Map<Preference, PreferenceFragmentCompat> hostByPreference,
                                  final Map<Preference, String> searchableInfoByPreference,
                                  final Map<Preference, PreferencePath> preferencePathByPreference,
                                  final SearchableInfoAttribute searchableInfoAttribute) {
        this.preferenceScreen = preferenceScreen;
        this.isNonClickable = isNonClickable;
        this.hostByPreference = hostByPreference;
        this.searchableInfoByPreference = searchableInfoByPreference;
        this.preferencePathByPreference = preferencePathByPreference;
        this.preferenceScreenResetter = new PreferenceScreenResetter(preferenceScreen, searchableInfoAttribute);
    }

    public Optional<? extends PreferenceFragmentCompat> findHost(final Preference preference) {
        return Maps.get(hostByPreference, preference);
    }

    public List<PreferenceDescription> getPreferenceDescriptions() {
        return searchableInfoByPreference
                .keySet()
                .stream()
                .map(preference -> new PreferenceDescription<>(preference.getClass(), searchableInfoByPreference::get))
                .collect(Collectors.toList());
    }

    public void resetPreferenceScreen() {
        preferenceScreenResetter.reset();
    }
}
