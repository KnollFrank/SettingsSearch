package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.PreferencePath;

public final class MergedPreferenceScreenData {

    // FK-TODO: unwrap preferences, i.e. remove class MergedPreferenceScreenData
    private final Set<SearchablePreferencePOJO> preferences;

    public MergedPreferenceScreenData(
            final Set<SearchablePreferencePOJO> preferences,
            final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference,
            final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
        this.preferences = preferences;
        PreferencePathsAndHostsSetter.setPreferencePathsAndHosts(preferences, preferencePathByPreference, hostByPreference);
    }

    public Set<SearchablePreferencePOJO> preferences() {
        return preferences;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MergedPreferenceScreenData that = (MergedPreferenceScreenData) o;
        return Objects.equals(preferences, that.preferences);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(preferences);
    }

    @Override
    public String toString() {
        return "MergedPreferenceScreenData{" +
                "preferences=" + preferences +
                '}';
    }
}
