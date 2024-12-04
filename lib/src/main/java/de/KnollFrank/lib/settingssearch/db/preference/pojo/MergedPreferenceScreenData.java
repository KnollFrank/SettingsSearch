package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.PreferencePOJOs;

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

    // FK-TODO: refactor code until this method is not used any more
    public Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference() {
        return PreferencePOJOs
                .getPreferencesRecursively(preferences)
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                SearchablePreferencePOJO::getPreferencePath));
    }

    // FK-TODO: refactor code until this method is not used any more
    public Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference() {
        return PreferencePOJOs
                .getPreferencesRecursively(preferences)
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                SearchablePreferencePOJO::getHost));
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
